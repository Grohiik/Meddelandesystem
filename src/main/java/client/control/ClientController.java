package client.control;

import client.boundary.ClientUI;
import client.boundary.listener.IOnEvent;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import javax.swing.ImageIcon;
import shared.entity.IMessage;
import shared.entity.Message;
import shared.entity.User;
import shared.entity.UserListMessage;

/**
 * ClientController controls and show the whole graphical interface and its logic.
 * It connects to  the server using {@link MessageWorker}. The incoming message and other events are
 * received by callbacks.
 *
 * @author Pratchaya Khansomboon
 * @author Eric Lundin
 * @version 1.0
 */
final public class ClientController {
    private MessageWorker messageWorker;

    private ClientUI clientUI;

    // Remote server configurations
    private String serverAddress;
    private int serverPort;

    private User user;
    private ArrayList<User> contactList;
    private ArrayList<User> connectedUserList;
    private ArrayList<User> recipientList;
    private HashMap<User, ArrayList<Message>> userMessageMap;

    private User selectedUser;
    private ArrayList<Message> activeMessageList;
    private ArrayList<User> activeUserList;

    private IOnEvent onUpdateGUI;

    /**
     * Default constructor for the controller. This sets the server address to "localhost" and its
     * port to 3000.
     */
    public ClientController() {
        this("localhost", 3000);
    }

    /**
     * Create controller with specified server address and port.
     *
     * @param address The server address to use.
     * @param port    The server port.
     */
    public ClientController(String address, int port) {
        this.serverAddress = address;
        this.serverPort = port;
    }

    /**
     * Starts the graphical user interface.
     */
    public void startGUI() {
        clientUI = new ClientUI(this);

        // FIXME: Rewrite this for better test.

        User tmpUser = null;
        ArrayList<User> tmpContactList = null;

        try {
            tmpUser = readUser();
            tmpContactList = readContactList();
        } catch (IOException e) {
        }

        if (tmpUser == null) {
            contactList = new ArrayList<>();
            clientUI.showLogin(this::login);
        } else {
            user = tmpUser;
            contactList = tmpContactList;
            connect();
        }

        onUpdateGUI = this::updateGUI;
    }

    /**
     * Set the port that the server is listening on.
     *
     * @param port Server port
     */
    public void setServerPort(int port) {
        this.serverPort = port;
    }

    /**
     * Set the server address to connect to.
     *
     * @param address The server raw address
     */
    public void setServerAddress(String address) {
        this.serverAddress = address;
    }

    /**
     * Connect to the server
     */
    public void connect() {
        if (messageWorker == null) {
            messageWorker = new MessageWorker(serverAddress, serverPort);

            // Listen for incoming messages
            messageWorker.setOnMessage(this::onMessage);

            // Listen for connection status
            messageWorker.setOnFailedConnect(this::onFailedConnect);
            messageWorker.setOnConnect(this::onConnect);
            messageWorker.setOnDisconnect(this::onDisconnect);
            messageWorker.setOnFailToSent(this::onFailToSent);
        }

        messageWorker.connect();
    }

    /**
     * Disconnect from the server
     */
    public void disconnect() {
        if (messageWorker != null) messageWorker.disconnect();
    }

    /**
     * Get if the user is connected to the server
     *
     * @return {@code true} for connected, {@code false} for not connected
     */
    public boolean getIsConnected() {
        synchronized (this) {
            return messageWorker.getIsConnected();
        }
    }

    /**
     * Create a user and stores it on the disk.
     *
     * @param username The name of the user
     * @param filename The filepath to where the image lives.
     */
    public void initUser(String username, String filename) {
        final var image =
            new ImageIcon(filename).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        user = new User(username, new ImageIcon(image));

        try {
            createUser(user);
        } catch (IOException e) {
        }
    }

    /**
     * Add connected user in the list to contact list
     *
     * @param index Selected index in the user list
     */
    public void addContact(int index) {
        if (contactList == null) contactList = new ArrayList<>();

        boolean isDuplicate = false;
        int size = contactList.size();
        for (int i = 0; i < size; i++) {
            if (contactList.get(i) == connectedUserList.get(index)) {
                isDuplicate = true;
                break;
            }
        }

        if (!isDuplicate) contactList.add(connectedUserList.get(index));
    }

    /**
     * Show added contacts
     */
    public void showContactList() {
        if (contactList == null) contactList = new ArrayList<>();
        activeUserList = contactList;
        if (onUpdateGUI != null) onUpdateGUI.signal();
    }

    /**
     * Show connected user list
     */
    public void showOnlineList() {
        if (connectedUserList == null) connectedUserList = new ArrayList<>();
        activeUserList = connectedUserList;
        if (onUpdateGUI != null) onUpdateGUI.signal();
    }

    /**
     * Send the text message.
     *
     * @param msg Text message to be sent
     */
    public void sendTextMessage(String msg) {
        var message = new Message();
        message.setText(msg);
        message.setSender(user);
        sendMessage(message);
    }

    /**
     * Load and send the image.
     *
     * @param filename file path to load
     */
    public void sendImageMessage(String filename) {
        ImageIcon imageIcon = new ImageIcon(filename);
        var message = new Message();
        message.setImage(imageIcon);
        message.setSender(user);
        sendMessage(message);
    }

    /**
     * Add selected user into the recipient list.
     *
     * @param index The index in the connected user list.
     */
    public void addRecipient(int index) {
        if (recipientList == null) recipientList = new ArrayList<>();
        if (index < 0) return; // Return if the list is not selected.

        boolean isDuplicate = false;
        int size = recipientList.size();

        // Linearly search for the the correct object in the recipient list.
        for (int i = 0; i < size; i++) {
            if (recipientList.get(i) == connectedUserList.get(index)) {
                isDuplicate = true;
                break;
            }
        }

        if (isDuplicate) return;
        recipientList.add(connectedUserList.get(index));

        String names[] = new String[recipientList.size()];
        for (int i = 0; i < names.length; i++) names[i] = recipientList.get(i).getUsername();
        clientUI.setRecipient(names);
    }

    /**
     * Remove recipient from the list.
     *
     * @param index The index in the connected user list.
     */
    public void removeRecipient(int index) {
        if (recipientList == null) return;

        int size = recipientList.size();
        int listIndex = -1;
        // Linearly search for the the correct object in the recipient list.
        for (int i = 0; i < size; i++) {
            if (recipientList.get(i) == connectedUserList.get(index)) {
                listIndex = i;
                break;
            }
        }

        if (listIndex > -1) {
            recipientList.remove(listIndex);

            String names[] = new String[recipientList.size()];
            for (int i = 0; i < names.length; i++) names[i] = recipientList.get(i).getUsername();
            clientUI.setRecipient(names);
        }
    }

    /**
     * Show message from the selected user.
     *
     * @param index The index of the selected user in the user list.
     */
    public void showMessage(int index) {
        var selectedUser = connectedUserList.get(index);
        var messages = userMessageMap.get(selectedUser);

        clientUI.clearMessages();
        int size = messages.size();
        for (int i = 0; i < size; i++) {
            var message = messages.get(i);
            var time = message.getSentTime().toString();
            var username = message.getSender().getUsername();
            if (message.getImage() == null) {
                clientUI.addMessage(time, username, message.getText());
            } else {
                clientUI.addMessage(time, username, message.getImage());
            }
        }
    }

    /**
     * Update the whole GUI
     */
    private void updateGUI() {
        if (userMessageMap == null) userMessageMap = new HashMap<>();
        var messages = userMessageMap.get(selectedUser);

        if (messages != null) {
            if (messages == activeMessageList) {
                var msg = messages.get(messages.size() - 1);
                var textMsg = msg.getText();
                if (textMsg == null)
                    clientUI.addMessage(msg.getSentTime().toString(), selectedUser.getUsername(),
                                        msg.getImage());
                else
                    clientUI.addMessage(msg.getSentTime().toString(), selectedUser.getUsername(),
                                        textMsg);
            } else {
                clientUI.clearMessages();
                for (Message message : activeMessageList) {
                    var textMsg = message.getText();
                    if (textMsg == null)
                        clientUI.addMessage(message.getSentTime().toString(),
                                            message.getSender().getUsername(), message.getImage());
                    else
                        clientUI.addMessage(message.getSentTime().toString(),
                                            message.getSender().getUsername(), textMsg);
                }
            }
        }

        if (activeUserList == connectedUserList && activeUserList.size() > 1) {
            final int size = activeUserList.size();
            final int newSize = activeUserList.size() - 1;
            var usernames = new String[newSize];
            var images = new ImageIcon[newSize];

            int newListCounter = 0;
            for (int i = 0; i < size; i++) {
                final var newUser = activeUserList.get(i);
                if (!newUser.equals(user)) {
                    usernames[newListCounter] = newUser.getUsername();
                    images[newListCounter] = newUser.getImage();
                    newListCounter++;
                }
            }

            clientUI.setUserList(usernames, images);
        }
    }

    /**
     * Send the message using MessageWorker.
     *
     * @param message Message object to be sent.
     */
    private void sendMessage(Message message) {
        if (recipientList != null && !recipientList.isEmpty()) {
            message.setReceiverList(recipientList.toArray(new User[recipientList.size()]));
            messageWorker.sendMessage(message);
        } else {
            onFailToSent();
        }
    }

    /**
     * Callback event for when message is failed to be sent.
     */
    private void onFailToSent() {
        clientUI.addMessage(new Date().toString(), "Local Bot",
                            "Message failed to be sent (only you can see this message)");
    }

    /**
     * Callback event for login the user and connect to the server.
     *
     * @param username The name for the user.
     * @param filename The file path to the image.
     */
    private void login(String username, String filename) {
        initUser(username, filename);
        connect();
    }

    /**
     * Callback for incoming message.
     *
     * @param msg Message object from the server
     */
    private void onMessage(IMessage msg) {
        if (msg instanceof Message) {
            var message = (Message) msg;
            var sender = message.getSender();

            var senderMessages = userMessageMap.get(sender);
            if (senderMessages == null) {
                var userMessages = new ArrayList<Message>();
                userMessages.add(message);
                userMessageMap.put(sender, userMessages);
            } else {
                senderMessages.add(message);
            }
        } else if (msg instanceof UserListMessage) {
            var userList = (UserListMessage) msg;
            if (connectedUserList == null) connectedUserList = new ArrayList<>();
            var arrUser = userList.getUsers();
            connectedUserList.clear();
            for (var user : arrUser) connectedUserList.add(user);

            if (activeUserList == null) activeUserList = connectedUserList;
        }

        if (onUpdateGUI != null) onUpdateGUI.signal();
    }

    /**
     * Callback for when connection is established.
     */
    private void onConnect() {
        System.out.println("WE ARE CONNECTED!");

        messageWorker.sendUser(user);
        clientUI.showMain();
        clientUI.setUserTitle(user.getUsername() + " (you)");
    }

    /**
     * Callback for when connection is disconnected.
     */
    private void onDisconnect() {
        System.out.println("WE ARE DISCONNECTED!");
    }

    /**
     * Callback for when failed to connect
     */
    private void onFailedConnect() {
        System.out.println("FAILED TO CONNECT!");
    }

    /**
     * Read the stored user data from disk.
     *
     * @throws IOException Error in reading the file.
     */
    private User readUser() throws IOException {
        try (ObjectInputStream ois = new ObjectInputStream(
                 new BufferedInputStream(new FileInputStream("data/User.dat")))) {
            try {
                return (User) ois.readObject();
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }

    /**
     * Create User object file in the data directory.
     *
     * @param user The User object to be stored in the file.
     */
    private void createUser(User user) throws IOException {
        // Create "data" directory in the project root.
        File data = new File("data");
        if (!data.exists()) data.mkdir();

        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream("data/User.dat"))) {
            oos.writeObject((User) user);
            oos.flush();
        }
    }

    /**
     * Create contact list from list of added users.
     *
     * @param users Contact list of users in ArrayList
     * @throws IOException
     */
    private void saveContactList(ArrayList<User> users) throws IOException {
        // Create "data" directory in the project root.
        File data = new File("data");
        if (!data.exists()) data.mkdir();

        try (ObjectOutputStream oos =
                 new ObjectOutputStream(new FileOutputStream("data/ContactList.dat"))) {
            int size = users.size();
            oos.writeInt(size);
            for (int i = 0; i < size; i++) {
                oos.writeObject(users.get(i));
            }
            oos.flush();
        }
    }

    /**
     * Read stored contact list from disk.
     *
     * @return Contact list of users in ArrayList.
     * @throws IOException Error reading file.
     */
    private ArrayList<User> readContactList() throws IOException {
        ArrayList<User> users = new ArrayList<>();

        try (ObjectInputStream ois = new ObjectInputStream(
                 new BufferedInputStream(new FileInputStream("data/User.dat")))) {
            try {
                int size = ois.readInt();
                for (int i = 0; i < size; i++) {
                    users.add((User) ois.readObject());
                }
            } catch (ClassNotFoundException e) {
            }
        }
        return users;
    }
}

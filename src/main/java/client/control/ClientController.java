package client.control;

import client.boundary.ClientUI;
import java.awt.Image;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import shared.entity.Message;
import shared.entity.User;

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
    private ArrayList<User> connectedUser;
    private ArrayList<User> recipientList;

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

        // FIXME: Remove this can get the data from server instead.
        var imageA = new ImageIcon("/Users/k/Desktop/lmao.png")
                         .getImage()
                         .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        var imageB = new ImageIcon("/Users/k/Desktop/cat.png")
                         .getImage()
                         .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        var imageC = new ImageIcon("/Users/k/Desktop/penguin.png")
                         .getImage()
                         .getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        connectedUser = new ArrayList<>();

        connectedUser.add(new User("Kalle", new ImageIcon(imageA)));
        connectedUser.add(new User("Gustav", new ImageIcon(imageB)));
        connectedUser.add(new User("Filip", new ImageIcon(imageC)));

        int size = connectedUser.size();
        String names[] = new String[size];
        ImageIcon icons[] = new ImageIcon[size];
        for (int i = 0; i < size; i++) {
            names[i] = connectedUser.get(i).getUsername();
            icons[i] = connectedUser.get(i).getImage();
        }
        clientUI.setUserList(names, icons);
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
     * @return True for connected, False for not connected
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
        user = new User(username, new ImageIcon(filename));

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
    public void addContact(int index) {}

    /**
     * Send the text message
     *
     * @param msg Text message to be sent
     */
    public void sendTextMessage(String msg) {
        var message = new Message();
        message.setText(msg);
        message.setSender(user);

        messageWorker.sendMessage(message);
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
        messageWorker.sendMessage(message);
    }

    public void addRecipient(int i) {
        if (recipientList == null) recipientList = new ArrayList<>();
        recipientList.add(connectedUser.get(i));

        String names[] = new String[recipientList.size()];
        for (int j = 0; j < names.length; j++) {
            names[j] = recipientList.get(j).getUsername();
        }
        clientUI.setRecipient(names);
    }

    public void removeRecipient(int i) {}

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
    private void onMessage(Message msg) {
        System.out.print("Message:");
        System.out.println(msg.getText());

        var userMsg = msg.getSender();
        if (userMsg == null) userMsg = new User("null", new ImageIcon());

        var textMsg = msg.getText();
        var imageMsg = msg.getImage();

        if (textMsg == null) {
            clientUI.addMessage(msg.getSentTime().toString(), userMsg.getUsername(), imageMsg);
        } else {
            clientUI.addMessage(msg.getSentTime().toString(), userMsg.getUsername(), msg.getText());
        }
    }

    /**
     * Callback for when connection is established.
     */
    private void onConnect() {
        System.out.println("WE ARE CONNECTED!");

        messageWorker.sendUser(user);
        clientUI.showMain();
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
     * @param user User object to be stored in the file.
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
    private void createContactList(ArrayList<User> users) throws IOException {
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

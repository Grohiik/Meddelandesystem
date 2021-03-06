package client.control;

import client.boundary.IUserInterface;
import java.awt.Image;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.ImageIcon;
import shared.entity.Message;
import shared.entity.User;

/**
 * GUIController class convert the entity data for boundary side. It attach events onto the boundary
 * side and can call to show the different values.
 *
 * @author  Pratchaya Khansomboon
 * @author  Eric Lundin
 * @version 1.0
 */
final public class GUIController {
    // Reference to other controller and the boundary side
    private IUserInterface userInterface;
    private ClientController controller;

    // Property for saving and loading data
    private FileIO fileIO;
    private boolean isCached;

    /**
     * Register the events for callbacks and store the references.
     *
     * @param controller    The ClientController object reference for adding
     *                      and call the different method.
     * @param userInterface The user interface object reference for attaching events.
     */
    public void registerEvents(final ClientController controller,
                               final IUserInterface userInterface) {
        this.controller = controller;
        this.userInterface = userInterface;

        this.userInterface.setOnAddFriendAction(this::onAddFriendAction);
        this.userInterface.setOnSendTextAction(this::onSendAction);
        this.userInterface.setOnSendFileAction(this::onSendFileAction);
        this.userInterface.setOnTypingAction(this::onTypingAction);
        this.userInterface.setOnShowMessageAction(this::onShowMessages);
        this.userInterface.setOnShowFriendAction(this::onShowFriendAction);
        this.userInterface.setOnShowOnlineAction(this::onShowOnlineAction);

        this.userInterface.setOnConnectAction(controller::connect);
        this.userInterface.setOnDisconnectAction(controller::disconnect);

        this.userInterface.setOnAddRecipientAction(controller::addRecipient);
        this.userInterface.setOnRemoveRecipientAction(controller::removeRecipient);

        controller.setOnUpdate(this::update);
        controller.setOnConnect(this::onConnect);
        controller.setOnDisconnect(this::onDisconnect);

        fileIO = new FileIO();
    }

    /**
     * Starts the Graphical Interface.
     *
     * @param isCached Tells the GUI if it should load the cached data.
     */
    public void start(boolean isCached) {
        this.isCached = isCached;
        User user = null;
        if (isCached) user = fileIO.read("User.dat", User.class);
        if (user == null) {
            userInterface.showLogin(this::login);
        } else {
            controller.setUser(user);
            final var friendListRaw = fileIO.read("FriendList.dat", List.class);
            if (friendListRaw != null) {
                List<User> friendList = new ArrayList<>();

                for (var u : friendListRaw) {
                    if (u instanceof User) friendList.add((User) u);
                }
                controller.setFriendList(friendList);
            }
            controller.connect();
            userInterface.showMain();
            userInterface.setUserTitle(user.getUsername());
        }
    }

    /**
     * Callback event for add friend action. This is bounds to the boundary side. It'll also save
     * the list on the disk if {@code isCached} is {@code true}.
     *
     * @param index Selected index on the user list.
     */
    public void onAddFriendAction(int index) {
        controller.addFriend(index);
        if (isCached) fileIO.save("FriendList.dat", controller.getFriendList());
        update();
    }

    /**
     * Callback event for send text action. This is bounds to the boundary side.
     *
     * @param text The text to be send as the message.
     */
    public void onSendAction(String text) {
        controller.sendTextMessage(text);
    }

    /**
     * Callback event for send file action. This is bounds to the boundary side.
     *
     * @param filename The file path to the image.
     */
    public void onSendFileAction(String filename) {
        controller.sendImageMessage(filename);
    }

    /**
     * Callback event for when the text field is being type. This is bounds to the boundary side.
     */
    public void onTypingAction() {
        System.out.println("Typing...");
    }

    /**
     * Callback event for show friend action. This is bounds to the boundary side.
     */
    public void onShowFriendAction() {
        controller.showFriendList();
        update();
    }

    /**
     * Callback event for show online action. This is bounds to the boundary side.
     */
    public void onShowOnlineAction() {
        controller.showOnlineList();
        update();
    }

    /**
     * Callback for when there's a show message event. This bounds to the boundary side.
     *
     * @param index index of the message in the list
     */
    public void onShowMessages(int index) {
        if (index < 0) return;
        final var messages = controller.getMessages(index);
        if (messages != null) showMessages(messages);
        controller.setRecipient(index);
        update();
    }

    /**
     * Callback event for when the client connects to a server.
     * This bounds to the boundary side. Writes a message when
     * the client connects to the server successfully.
     */
    public void onConnect() {
        userInterface.addMessage(new Date().toString(), "System",
                                 "You're now connected to the server");
    }

    /**
     * Callback event for when the client disconnects from a server.
     * This bounds to the boundary side. Writes a message when the
     * client disconnects from a server successfully
     */
    public void onDisconnect() {
        userInterface.addMessage(new Date().toString(), "System",
                                 "You're disconnected from the server");
    }

    /**
     * Callback event for when the user logs in to the server
     * This bounds to the boundary side. Login the user with
     * name and the file path to an image.
     *
     * @param username The name of the user.
     * @param filename The file path to the image for the user.
     */
    private void login(String username, String filename) {
        final var image =
            new ImageIcon(filename).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        final var user = new User(username, new ImageIcon(image));
        System.out.println(user.getUsername());
        fileIO.save("User.dat", user);
        login(user);
    }

    /**
     * Connects the user to the server after the user is created or read from disk
     *
     * @param user The user that is created or read from disk
     */
    public void login(User user) {
        controller.setUser(user);

        controller.connect();
        userInterface.showMain();
        userInterface.setUserTitle(user.getUsername());
    }

    /**
     * Update the whole GUI with current data.
     */
    private void update() {
        final var recipients = controller.getRecipients();
        if (recipients != null) {
            final String names[] = new String[recipients.size()];
            for (int i = 0; i < names.length; i++) names[i] = recipients.get(i).getUsername();
            userInterface.showRecipients(names);
        }

        final var messages = controller.getMessages();
        if (messages != null) showMessages(messages);

        final var users = controller.getUsers();
        final int size = users.size();
        var usernames = new String[size];
        var images = new ImageIcon[size];

        for (int i = 0; i < size; i++) {
            final var newUser = users.get(i);
            usernames[i] = newUser.getUsername();
            images[i] = newUser.getImage();
        }

        userInterface.setUserList(usernames, images);
    }

    /**
     * Show the message to the user interface.
     *
     * @param messages The list of messages.
     */
    private void showMessages(final List<Message> messages) {
        final int size = messages.size();
        userInterface.clearMessages();
        for (int i = 0; i < size; i++) {
            var message = messages.get(i);
            var time = message.getSentTime().toString();
            var username = message.getSender().getUsername();
            if (message.getImage() == null) {
                userInterface.addMessage(time, username, message.getText());
            } else {
                userInterface.addMessage(time, username, message.getImage());
            }
        }
    }
}

package client.control;

import client.boundary.IUserInterface;
import java.awt.Image;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import shared.entity.Message;
import shared.entity.User;

/**
 * GUIController
 *
 * @author  Pratchaya Khansomboon
 * @author  Eric Lundin
 * @version 1.0
 */
final public class GUIController {
    private IUserInterface userInterface;
    private ClientController controller;

    private FileIO fileIO;

    // Events callbacks

    public void start(final ClientController controller, final IUserInterface userInterface) {
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

    public void loadCached() {
        final var user = fileIO.read("User.dat", User.class);
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

    // IN

    public void onAddFriendAction(int index) {
        controller.addFriend(index);
        fileIO.save("FriendList.dat", controller.getFriendList());
        update();
    }

    public void onSendAction(String text) {
        controller.sendTextMessage(text);
    }

    public void onSendFileAction(String filename) {
        controller.sendImageMessage(filename);
    }

    public void onTypingAction() {
        System.out.println("Typing...");
    }

    public void onShowFriendAction() {
        controller.showFriendList();
        update();
    }

    public void onShowOnlineAction() {
        controller.showOnlineList();
        update();
    }

    // OUT

    public void onShowMessages(int index) {
        final var messages = controller.getMessages(index);
        if (messages != null) showMessages(messages);
        controller.setRecipient(index);
        update();
    }

    public void onConnect() {
        // TODO: show we're connected
        System.out.println("GUIController::Connected");
    }

    public void onDisconnect() {
        // TODO show we're disconnected
        System.out.println("GUIController::Disconnected");
    }

    private void login(String username, String filename) {
        final var image =
            new ImageIcon(filename).getImage().getScaledInstance(32, 32, Image.SCALE_SMOOTH);
        final var user = new User(username, new ImageIcon(image));
        System.out.println(user.getUsername());
        fileIO.save("User.dat", user);
        login(user);
    }

    public void login(User user) {
        controller.setUser(user);

        controller.connect();
        userInterface.showMain();
        userInterface.setUserTitle(user.getUsername());
    }

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

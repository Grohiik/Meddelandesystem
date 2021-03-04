package client.control;

import client.boundary.ClientUI;
import java.awt.Image;
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
    private ClientUI clientUI;
    private ClientController controller;

    private FileIO fileIO;

    // Events callbacks

    public void start(final ClientController controller, final ClientUI clientUI) {
        this.controller = controller;
        this.clientUI = clientUI;

        this.clientUI.setOnSendTextAction(this::onSendAction);
        this.clientUI.setOnSendFileAction(this::onSendFileAction);
        this.clientUI.setOnTypingAction(this::onTypingAction);
        this.clientUI.setOnShowMessageAction(this::onShowMessages);
        this.clientUI.setOnShowFriendAction(this::onShowFriendAction);
        this.clientUI.setOnShowOnlineAction(this::onShowOnlineAction);

        this.clientUI.setOnConnectAction(controller::connect);
        this.clientUI.setOnDisconnectAction(controller::disconnect);

        this.clientUI.setOnAddRecipientAction(controller::addRecipient);
        this.clientUI.setOnRemoveRecipientAction(controller::removeRecipient);

        controller.setOnUpdate(this::update);
        controller.setOnConnect(this::onConnect);
        controller.setOnDisconnect(this::onDisconnect);

        fileIO = new FileIO();
    }

    public void loadCached() {
        final var user = fileIO.read("User.dat", User.class);
        if (user == null) {
            clientUI.showLogin(this::login);
        } else {
            controller.setUser(user);
            controller.connect();
            clientUI.showMain();
            clientUI.setUserTitle(user.getUsername());
        }
    }

    // IN

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
        clientUI.showMain();
        clientUI.setUserTitle(user.getUsername());
    }

    private void update() {
        final var recipients = controller.getRecipients();
        if (recipients != null) {
            final String names[] = new String[recipients.size()];
            for (int i = 0; i < names.length; i++) names[i] = recipients.get(i).getUsername();
            clientUI.showRecipients(names);
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

        clientUI.setUserList(usernames, images);
    }

    private void showMessages(final List<Message> messages) {
        final int size = messages.size();
        clientUI.clearMessages();
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
}

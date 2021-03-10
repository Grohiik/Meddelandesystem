package client.boundary.page;

import client.boundary.ClientUI;
import client.boundary.component.MessagePanel;
import client.boundary.event.IOnEvent;
import client.boundary.event.IOnEventParam;
import client.boundary.panel.ChatPanel;
import client.boundary.panel.UserListPanel;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * MainPage contains the ChatPanel and UserListPanel for displaying the chat and connected users or
 * friend list.
 *
 * @author  Pratchaya Khansomboon
 * @author  Eric Lundin
 * @version 1.0
 */
public class MainPage {
    private JPanel panel;
    private ChatPanel chatPanel;
    private UserListPanel userListPanel;

    private JMenuItem connectMI;
    private JMenuItem disconnectMI;

    private IOnEventParam<String> onSendText;
    private IOnEventParam<String> onSendFile;

    /**
     * Create MainPage with its default components.
     *
     * @param clientUI The reference object for attaching different events.
     */
    public MainPage(ClientUI clientUI) {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m2 = new JMenu("Tools");
        mb.add(m2);

        connectMI = new JMenuItem("Connect to server");
        disconnectMI = new JMenuItem("Disconnect from server");
        m2.add(connectMI);
        m2.add(disconnectMI);

        chatPanel = new ChatPanel(clientUI);
        chatPanel.addSendFileListener(this::sendFile);
        chatPanel.addSendTextListener(this::sendText);

        userListPanel = new UserListPanel();

        panel.add(chatPanel.getPanel(), BorderLayout.CENTER);
        panel.add(userListPanel.getPanel(), BorderLayout.EAST);

        panel.add(BorderLayout.NORTH, mb);
    }

    /**
     * @return returns parent
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Clear all the messages in the chat panel.
     */
    public void clearMessages() {
        SwingUtilities.invokeLater(chatPanel::clearMessages);
    }

    /**
     * Add the text message to the {@link client.boundary.panel.ChatPanel}.
     *
     * @param time The string formatted time.
     * @param name The name of the user for the message.
     * @param text The text content of the message.
     */
    public void addMessage(String time, String name, String text) {
        SwingUtilities.invokeLater(() -> chatPanel.addMessage(new MessagePanel(time, name, text)));
    }

    /**
     * Add the image message to the {@link client.boundary.panel.ChatPanel}.
     *
     * @param time  The string formatted time.
     * @param name  The name of the user for the message.
     * @param image The image content of the message.
     */
    public void addMessage(String time, String name, ImageIcon image) {
        SwingUtilities.invokeLater(() -> chatPanel.addMessage(new MessagePanel(time, name, image)));
    }

    /**
     * Set the user list in the {@link client.boundary.panel.UserListPanel}. The arrays needs to be
     * the same size.
     *
     * @param usernames The string array of usernames.
     * @param images    The image array.
     */
    public void setUserList(String[] usernames, ImageIcon[] images) {
        SwingUtilities.invokeLater(() -> userListPanel.setUserList(usernames, images));
    }

    /**
     * Set the event callback for when the add recipient button has been pressed.
     *
     * @param onAddRecipient The function callback with one int parameter.
     */
    public void setOnAddRecipient(IOnEventParam<Integer> onAddRecipient) {
        userListPanel.setOnAddRecipient(onAddRecipient);
    }

    /**
     * Set event callback for when the remove recipient button has been pressed.
     *
     * @param onRemoveRecipient The function callback with one int parameter.
     */
    public void setOnRemoveRecipient(IOnEventParam<Integer> onRemoveRecipient) {
        userListPanel.setOnRemoveRecipient(onRemoveRecipient);
    }

    /**
     * Set the event callback for connect action.
     *
     * @param onConnect The function callback with no parameter.
     */
    public void setOnConnect(IOnEvent onConnect) {
        connectMI.addActionListener(e -> onConnect.signal());
    }

    /**
     * Set the event callback for disconnect action.
     *
     * @param onDisconnect The function callback with no parameter.
     */
    public void setOnDisconnect(IOnEvent onDisconnect) {
        disconnectMI.addActionListener(e -> onDisconnect.signal());
    }

    /**
     * Set event callback for send text action.
     *
     * @param onSend The function callback with one string parameter.
     */
    public void setOnSendText(IOnEventParam<String> onSend) {
        onSendText = onSend;
    }

    /**
     * Set the event callback for send file action.
     *
     * @param onSend The function callback with one string parameter.
     */
    public void setOnSendFile(IOnEventParam<String> onSend) {
        onSendFile = onSend;
    }

    /**
     * Set the event callback for typing action.
     *
     * @param onTyping The function callback with no parameter.
     */
    public void setOnTyping(IOnEvent onTyping) {
        chatPanel.setOnTyping(onTyping);
    }

    /**
     * Set the event callback for show message action.
     *
     * @param onShowMessages The function callback with one int parameter.
     */
    public void setOnShowMessages(IOnEventParam<Integer> onShowMessages) {
        userListPanel.setOnShowMessage(onShowMessages);
    }

    /**
     * Sets the event callback for show friend action
     *
     * @param onShowFriend
     */
    public void setOnShowFriend(IOnEvent onShowFriend) {
        userListPanel.setOnShowFriend(onShowFriend);
    }

    /**
     * Sets the event callback for show online action
     *
     * @param onShowOnline The function Callback with no parameter.
     */
    public void setOnShowOnline(IOnEvent onShowOnline) {
        userListPanel.setOnShowOnline(onShowOnline);
    }

    /**
     * Set the recipients in the {@link client.boundary.panel.ChatPanel}.
     *
     * @param names The string array of names.
     */
    public void setRecipient(String[] names) {
        chatPanel.setRecipient(names);
    }

    /**
     * Set the event callback for when the add friend button has been pressed.
     *
     * @param onAddFriend The function callback with one int parameter.
     */
    public void setOnAddFriend(IOnEventParam<Integer> onAddFriend) {
        userListPanel.setOnAddFriend(onAddFriend);
    }

    /**
     * Send text message
     *
     * @param msg Message to send
     */
    public boolean sendText(String msg) {
        onSendText.signal(msg);
        // controller.sendTextMessage(msg);
        // return controller.getIsConnected();
        return true;
    }

    /**
     * Send file message
     *
     * @param filename File path on the system
     */
    public boolean sendFile(String filename) {
        onSendFile.signal(filename);
        // controller.sendImageMessage(filename);
        // return controller.getIsConnected();
        return true;
    }

    private void onConnect() {
        // controller.connect();
    }

    private void onDisconnect() {
        // controller.disconnect();
    }
}

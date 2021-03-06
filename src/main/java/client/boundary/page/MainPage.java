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
 * MainPage
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

    public JPanel getPanel() {
        return panel;
    }

    public void clearMessages() {
        SwingUtilities.invokeLater(chatPanel::clearMessages);
    }

    public void addMessage(String time, String name, String text) {
        SwingUtilities.invokeLater(() -> chatPanel.addMessage(new MessagePanel(time, name, text)));
    }

    public void addMessage(String time, String name, ImageIcon image) {
        SwingUtilities.invokeLater(() -> chatPanel.addMessage(new MessagePanel(time, name, image)));
    }

    public void setUserList(String[] usernames, ImageIcon[] images) {
        SwingUtilities.invokeLater(() -> userListPanel.setUserList(usernames, images));
    }

    public void setOnAddRecipient(IOnEventParam<Integer> onAddRecipient) {
        userListPanel.setOnAddRecipient(onAddRecipient);
    }

    public void setOnRemoveRecipient(IOnEventParam<Integer> onRemoveRecipient) {
        userListPanel.setOnRemoveRecipient(onRemoveRecipient);
    }

    public void setOnConnect(IOnEvent onConnect) {
        connectMI.addActionListener(e -> onConnect.signal());
    }

    public void setOnDisconnect(IOnEvent onDisconnect) {
        disconnectMI.addActionListener(e -> onDisconnect.signal());
    }

    public void setOnSendText(IOnEventParam<String> onSend) {
        onSendText = onSend;
    }

    public void setOnSendFile(IOnEventParam<String> onSend) {
        onSendFile = onSend;
    }

    public void setOnTyping(IOnEvent onTyping) {
        chatPanel.setOnTyping(onTyping);
    }

    public void setOnShowMessages(IOnEventParam<Integer> onShowMessages) {
        userListPanel.setOnShowMessage(onShowMessages);
    }

    public void setOnShowFriend(IOnEvent onShowFriend) {
        userListPanel.setOnShowFriend(onShowFriend);
    }

    public void setOnShowOnline(IOnEvent onShowOnline) {
        userListPanel.setOnShowOnline(onShowOnline);
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

    public void setRecipient(String[] names) {
        chatPanel.setRecipient(names);
    }

    public void setOnAddFriend(IOnEventParam<Integer> onAddFriend) {
        userListPanel.setOnAddFriend(onAddFriend);
    }
}

package client.boundary.page;

import client.boundary.ClientUI;
import client.boundary.component.MessagePanel;
import client.boundary.panel.ChatPanel;
import client.boundary.panel.UserListPanel;
import client.control.ClientController;
import java.awt.BorderLayout;
import javax.swing.ImageIcon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;

/**
 * MainView
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class MainPage {
    private JPanel panel;
    private ChatPanel chatPanel;
    private UserListPanel userListPanel;

    private ClientController controller;
    private ClientUI clientUI;

    public MainPage(ClientController controller, ClientUI clientUI) {
        this.controller = controller;
        this.clientUI = clientUI;

        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        // JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Tools");
        // mb.add(m1);
        mb.add(m2);

        // JMenuItem m11 = new JMenuItem("Hello");
        JMenuItem connectMI = new JMenuItem("Connect to server");
        JMenuItem disconnectMI = new JMenuItem("Disconnect from server");
        disconnectMI.addActionListener(e -> onDisconnect());
        connectMI.addActionListener(e -> onConnect());

        // m1.add(m11);
        m2.add(connectMI);
        m2.add(disconnectMI);

        chatPanel = new ChatPanel(clientUI);

        chatPanel.addSendTextListener(msg -> {
            controller.sendTextMessage(msg);
            return controller.getIsConnected();
        });
        chatPanel.addSendFileListener(filename -> {
            controller.sendImageMessage(filename);
            return controller.getIsConnected();
        });

        userListPanel = new UserListPanel();
        userListPanel.setOnAddRecipient(controller::addRecipient);
        userListPanel.setOnRemoveRecipient(controller::removeRecipient);
        userListPanel.setOnShowMessage(controller::showMessage);
        userListPanel.setOnAddFriend(controller::addContact);
        userListPanel.setOnShowFriend(controller::showContactList);
        userListPanel.setOnShowOnline(controller::showOnlineList);

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

    /**
     * Send text message
     *
     * @param msg Message to send
     */
    public boolean sendText(String msg) {
        controller.sendTextMessage(msg);
        return controller.getIsConnected();
    }

    /**
     * Send file message
     *
     * @param filename File path on the system
     */
    public boolean sendFile(String filename) {
        controller.sendImageMessage(filename);
        return controller.getIsConnected();
    }

    private void onConnect() {
        controller.connect();
    }

    private void onDisconnect() {
        controller.disconnect();
    }

    public void setRecipient(String[] names) {
        chatPanel.setRecipient(names);
    }
}

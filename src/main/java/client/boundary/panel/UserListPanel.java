package client.boundary.panel;

import client.boundary.component.ListPanel;
import client.boundary.component.UserPanel;
import client.boundary.listener.IOnEventParam;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * FriendPanel
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class UserListPanel {
    private JPanel panel;

    private ListPanel<UserPanel> userList;

    private JPanel buttonPanel;
    private JButton showFriendButton;
    private JButton showOnlineButton;
    private JButton addButton;

    private JPanel recipientButtonPanel;
    private JButton removeRecipientButton;
    private JButton addRecipientButton;

    public UserListPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        showFriendButton = new JButton("Friends");
        showFriendButton.addActionListener(e -> onShowFriend());
        showOnlineButton = new JButton("Online");
        showOnlineButton.addActionListener(e -> onShowOnline());
        addButton = new JButton("Add");
        buttonPanel.add(showFriendButton);
        buttonPanel.add(showOnlineButton);
        buttonPanel.add(addButton);

        userList = new ListPanel<>();
        userList.setEnabled(true);

        // FIXME: Add listener for add recipient
        removeRecipientButton = new JButton("Remove");
        addRecipientButton = new JButton("Add");

        recipientButtonPanel = new JPanel();
        recipientButtonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        recipientButtonPanel.add(removeRecipientButton);
        recipientButtonPanel.add(addRecipientButton);

        panel.add(recipientButtonPanel, BorderLayout.PAGE_START);
        panel.add(userList.getPane(), BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setUserList(String[] usernames, ImageIcon[] images) {
        userList.clear();
        for (int i = 0; i < images.length; i++) {
            var tmp = new UserPanel(images[i], usernames[i]);
            userList.add(tmp);
        }
    }

    public void setOnAddRecipient(IOnEventParam<Integer> listener) {
        addRecipientButton.addActionListener(e -> listener.signal(userList.getSelectedIndex()));
    }

    public void setOnRemoveRecipient(IOnEventParam<Integer> listener) {
        removeRecipientButton.addActionListener(e -> listener.signal(userList.getSelectedIndex()));
    }

    public int getSelected() {
        return userList.getSelectedIndex();
    }

    public JPanel getPanel() {
        return panel;
    }

    private void onShowOnline() {
        System.out.println("Show Online");
    }

    private void onShowFriend() {
        System.out.println("Show Friends");
    }
}

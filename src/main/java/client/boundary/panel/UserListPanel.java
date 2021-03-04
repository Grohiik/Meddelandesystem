package client.boundary.panel;

import client.boundary.component.ListPanel;
import client.boundary.component.UserPanel;
import client.boundary.listener.IOnEvent;
import client.boundary.listener.IOnEventParam;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * FriendPanel
 *
 * @author  Pratchaya Khansomboon
 * @author  Eric Lundin
 * @version 1.0
 */
public class UserListPanel {
    private JPanel panel;

    private ListPanel<UserPanel> userList;

    private JPanel buttonPanel;
    private JButton showFriendButton;
    private JButton showOnlineButton;
    private JButton addFriendButton;

    private JPanel recipientButtonPanel;
    private JButton removeRecipientButton;
    private JButton addRecipientButton;
    private JButton showMessageButton;

    public UserListPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());

        buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        showFriendButton = new JButton("Friends");
        showOnlineButton = new JButton("Online");
        addFriendButton = new JButton("Add Friend");
        buttonPanel.add(showFriendButton);
        buttonPanel.add(showOnlineButton);
        buttonPanel.add(addFriendButton);

        userList = new ListPanel<>();
        userList.setEnabled(true);

        removeRecipientButton = new JButton("Remove");
        removeRecipientButton.setToolTipText("Remove from recipient list");
        addRecipientButton = new JButton("Add");
        addRecipientButton.setToolTipText("Add to recipient list");
        showMessageButton = new JButton("Show");
        showMessageButton.setToolTipText("Show message");

        recipientButtonPanel = new JPanel();
        recipientButtonPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        recipientButtonPanel.add(removeRecipientButton);
        recipientButtonPanel.add(addRecipientButton);
        recipientButtonPanel.add(showMessageButton);

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

    public void setOnShowMessage(IOnEventParam<Integer> listener) {
        showMessageButton.addActionListener(e -> listener.signal(userList.getSelectedIndex()));
    }

    public void setOnAddFriend(IOnEventParam<Integer> listener) {
        addFriendButton.addActionListener(e -> listener.signal(userList.getSelectedIndex()));
    }

    public void setOnShowFriend(IOnEvent listener) {
        showFriendButton.addActionListener(e -> listener.signal());
    }

    public void setOnShowOnline(IOnEvent listener) {
        showOnlineButton.addActionListener(e -> listener.signal());
    }

    public int getSelected() {
        return userList.getSelectedIndex();
    }

    public JPanel getPanel() {
        return panel;
    }
}

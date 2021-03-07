package client.boundary.panel;

import client.boundary.component.ListPanel;
import client.boundary.component.UserPanel;
import client.boundary.event.IOnEvent;
import client.boundary.event.IOnEventParam;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * FriendPanel is custom panel for rendering the friend list and showing the buttons for doing
 * different actions.
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
        showOnlineButton.setEnabled(false);
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

    /**
     * Displays a list of usernames and profile pictures
     *
     *  @param usernames array of users to be shown
     * @param images array of images to b shown
     */
    public void setUserList(String[] usernames, ImageIcon[] images) {
        userList.clear();
        for (int i = 0; i < images.length; i++) {
            var tmp = new UserPanel(images[i], usernames[i]);
            userList.add(tmp);
        }
    }

    /**
     * Set event callback for when the AddRecipient button is pressed.
     *
     * @param listener The function callback listener
     */
    public void setOnAddRecipient(IOnEventParam<Integer> listener) {
        addRecipientButton.addActionListener(e -> listener.signal(userList.getSelectedIndex()));
    }

    /**
     * Set event callback for when the removeRecipient button is pressed.
     *
     * @param listener The function callback listener
     */
    public void setOnRemoveRecipient(IOnEventParam<Integer> listener) {
        removeRecipientButton.addActionListener(e -> listener.signal(userList.getSelectedIndex()));
    }

    /**
     * Set event callback for when the showMessage button is pressed.
     *
     * @param listener The function callback listener
     */
    public void setOnShowMessage(IOnEventParam<Integer> listener) {
        showMessageButton.addActionListener(e -> listener.signal(userList.getSelectedIndex()));
    }

    /**
     * Set event callback for when the addFriend button is pressed.
     *
     * @param listener The function callback listener
     */
    public void setOnAddFriend(IOnEventParam<Integer> listener) {
        addFriendButton.addActionListener(e -> listener.signal(userList.getSelectedIndex()));
    }

    /**
     * Set event callback for when the showFriend button is pressed.
     *
     * @param listener The function callback listener
     */
    public void setOnShowFriend(IOnEvent listener) {
        showFriendButton.addActionListener(e -> {
            showFriendButton.setEnabled(false);
            showOnlineButton.setEnabled(true);
            listener.signal();
        });
    }

    /**
     * Set event callback for when the showOnline button is pressed.
     *
     * @param listener The function callback listener
     */
    public void setOnShowOnline(IOnEvent listener) {
        showOnlineButton.addActionListener(e -> {
            listener.signal();
            showOnlineButton.setEnabled(false);
            showFriendButton.setEnabled(true);
        });
    }

    /**
     * @return returns the index of the selected user
     */
    public int getSelected() {
        return userList.getSelectedIndex();
    }

    public JPanel getPanel() {
        return panel;
    }
}

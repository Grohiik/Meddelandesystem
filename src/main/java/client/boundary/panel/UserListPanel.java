package client.boundary.panel;

import client.boundary.component.ListPanel;
import client.boundary.component.UserPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Image;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ScrollPaneConstants;

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

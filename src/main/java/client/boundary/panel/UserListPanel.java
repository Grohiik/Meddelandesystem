package client.boundary.panel;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JButton;
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

    private JList<String> userList;

    private JPanel buttonPanel;
    private JButton showFriendButton;
    private JButton showOnlineButton;
    private JButton addButton;

    private ArrayList<String> usernameList;
    private ArrayList<ImageIcon> userIconList;

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

        userList = new JList<>();

        var jsp = new JScrollPane(userList);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(jsp, BorderLayout.CENTER);
        panel.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void setUserList(String[] users) {
        userList.setListData(users);
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

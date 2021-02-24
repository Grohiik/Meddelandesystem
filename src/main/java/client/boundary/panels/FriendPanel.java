package client.boundary.panels;

import java.awt.BorderLayout;
import java.awt.Dimension;
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
public class FriendPanel {
    private JPanel panel;

    private JList userList;

    private JPanel buttonPanel;
    private JButton showFriendButton;
    private JButton showOnlineButton;
    private JButton addButton;

    private ArrayList<String> usernameList;
    private ArrayList<ImageIcon> userIconList;

    public FriendPanel() {
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

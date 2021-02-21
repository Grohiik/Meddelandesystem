package client.boundary;

import client.control.ClientController;

import javax.swing.*;
import java.awt.*;

public class FriendPanel extends JPanel {
    JList friendList;
    JButton addFriend;

    int width;
    int height;

    ClientController controller;

    public FriendPanel(ClientController controller, int width, int height) {
        this.controller = controller;
        this.width = width;
        this.height = height;

        createComponents();

    }

    private void createComponents() {
        friendList = new JList();
        JScrollPane s = new JScrollPane(friendList);
        //extra saker
        s.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        s.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        s.setPreferredSize(new Dimension(width, height-300));

        friendList.setSelectionMode(DefaultListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        add(s, BorderLayout.PAGE_START);

        addFriend = new JButton("add a friend");
        add(addFriend, BorderLayout.PAGE_END);
    }
}

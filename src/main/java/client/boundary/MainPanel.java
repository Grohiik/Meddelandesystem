package client.boundary;

import client.control.ClientController;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import java.awt.*;

public class MainPanel extends JPanel {
    private int width;
    private int height;
    private MsgPanel msgPanel;
    private FriendPanel friendPanel;
    private InputPanel inputPanel;

    private ClientController controller;

    public MainPanel(ClientController controller, int width, int height) {
        this.controller = controller;
        this.width = width;
        this.height = height;

        setupPanels();
    }

    private void setupPanels() {
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        Border border = this.getBorder();

        Border margin = BorderFactory.createEmptyBorder(10, 10, 10, 10);
        setBorder(new CompoundBorder(border, margin));

        msgPanel = new MsgPanel(controller, width, height, 3);
        add(msgPanel, BorderLayout.CENTER);

        inputPanel = new InputPanel(controller, width, height/10);
        add(inputPanel, BorderLayout.SOUTH);

        friendPanel = new FriendPanel(controller, width/5, height);
        add(friendPanel, BorderLayout.EAST);
    }
}

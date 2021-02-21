package client.boundary;

import client.control.ClientController;

import javax.swing.*;
import java.awt.*;

public class MsgPanel extends JPanel {
    private ClientController controller;
    private JList msgList;

    public MsgPanel(ClientController controller, int width, int height, int margin) {
        this.controller = controller;

        createComponents(width, height, margin);
    }


    private void createComponents(int width, int height, int margin) {
        msgList = new JList();

        Font font = new Font("Courier New", Font.PLAIN, 11);
        msgList.setFont(font);

        Component table;
        JScrollPane scrollPane = new JScrollPane(msgList);

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.setPreferredSize(new Dimension(width-margin, height-margin));

//        msgList.setSelectionMode(DefaultListSelectionModel.SINGLE_SELECTION);

        add(scrollPane);
    }
}

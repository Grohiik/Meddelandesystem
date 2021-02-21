package client.boundary;

import client.control.ClientController;

import javax.swing.*;
import java.awt.*;

public class InputPanel extends JPanel {
    JTextField inputField;
    JButton send;
    JButton file;
    int width;
    int height;

    ClientController controller;

    public InputPanel(ClientController controller, int width, int height) {
        this.controller = controller;
        this.width = width;
        this.height = height;

        setPreferredSize(new Dimension(width, height));


        createComponents();
    }

    private void createComponents() {
        inputField = new JTextField();
        inputField.setPreferredSize(new Dimension(width-150,20));
        add(inputField, BorderLayout.CENTER);

        send = new JButton("send");
        add(send, BorderLayout.EAST);

        file = new JButton("file");
        add(file, BorderLayout.EAST);
    }
}

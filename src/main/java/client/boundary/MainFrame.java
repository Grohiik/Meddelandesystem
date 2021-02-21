package client.boundary;

import client.control.ClientController;

import javax.swing.*;

public class MainFrame {
    private JFrame frame;
    private MainPanel panel;

    private int width = 800;
    private int height = 500;


    public MainFrame(ClientController controller) {
        frame = new JFrame("PINM is not MSN");//PINM
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setSize(width, height);
        frame.setResizable(true);

        panel = new MainPanel(controller, width, height);
        frame.add(panel);
        frame.pack();
        frame.setVisible(true);
    }


}

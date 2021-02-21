package client;

import client.boundary.MainFrame;
import client.control.ClientController;

/**
 * StartClient
 *
 * @version 1.0
 */
public class StartClient {
    public static void main(String[] args) {
        System.out.println("Hello, Client!");
        MainFrame mainFrame = new MainFrame(new ClientController());
    }
}

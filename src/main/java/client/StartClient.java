package client;

import client.control.ClientController;

/**
 * StartClient
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class StartClient {
    public static void main(String[] args) {
        var controller = new ClientController();
        controller.startGUI();
    }
}

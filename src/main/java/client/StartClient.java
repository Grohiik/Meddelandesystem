package client;

import client.control.ClientController;

/**
 * StartClient
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
final public class StartClient {
    public static void main(String[] args) {
        var controller = new ClientController();
        controller.setServerAddress("167.99.42.19");
        controller.startGUI();
    }
}

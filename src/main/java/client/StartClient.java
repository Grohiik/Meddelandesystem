package client;

import client.control.ClientController;
import java.net.UnknownHostException;

/**
 * StartClient
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class StartClient {
    public static void main(String[] args) throws UnknownHostException {
        var controller = new ClientController();
        controller.setServerAddress("167.99.42.19");
        controller.startGUI();
    }
}

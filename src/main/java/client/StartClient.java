package client;

import client.control.ClientController;

/**
 * StartClient class configures how the client should be started. This include setting which server
 * to connect to.
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
final public class StartClient {
    public static void main(String[] args) {
        var controller = new ClientController();
        // FIXME: Use environment variables.
        if (args.length > 1 && args[0].equals("-ip")) controller.setServerAddress(args[1]);
        controller.setServerAddress("localhost");
        controller.startGUI();
    }
}

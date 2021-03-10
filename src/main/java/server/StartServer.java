package server;

import server.control.ServerController;

/**
 * StartServer is used to start the server
 *
 * @author Marcus Linné
 * @version 1.0
 */
public class StartServer {
    public static void main(String[] args) {
        ServerController serverController = new ServerController(3000);
    }
}
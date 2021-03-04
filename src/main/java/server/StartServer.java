package server;

import server.control.ServerController;

/**
 * StartServer
 *
 * @version 1.0
 */
public class StartServer {
    public static void main(String[] args) {
        System.out.println("Hello, Server!");
        ServerController serverController = new ServerController(3000);
    }
}

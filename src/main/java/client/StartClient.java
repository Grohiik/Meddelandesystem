package client;

import client.boundary.ClientUI;
import client.control.ClientController;
import client.control.GUIController;
import java.util.HashMap;

/**
 * StartClient class configures how the client should be started. This include setting which server
 * to connect to.
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
final public class StartClient {
    public static void main(String[] args) {
        final var mapArgs = parseArguments(args);
        var isLoadCache = true;

        var port = 3000;
        var server = "localhost";

        if (mapArgs != null) {
            final var ip = mapArgs.get("-ip");
            final var portRaw = mapArgs.get("-p");
            final var isLoad = mapArgs.get("-c");
            if (ip != null) server = ip;
            if (portRaw != null) port = Integer.parseInt(portRaw);
            if (isLoad != null && (isLoad.equals("false") || isLoad.equals("f")))
                isLoadCache = false;
        }
        final var controller = new ClientController(server, port);
        final var ui = new ClientUI();
        final var gui = new GUIController();
        gui.start(controller, ui);

        if (isLoadCache) gui.loadCached();
    }

    private static HashMap<String, String> parseArguments(String[] args) {
        if (args.length % 2 != 0) return null;

        var map = new HashMap<String, String>();
        for (int i = 0; i < args.length; i += 2) {
            final var key = args[i];
            final var val = args[i + 1];
            map.put(key, val);
        }
        return map;
    }
}

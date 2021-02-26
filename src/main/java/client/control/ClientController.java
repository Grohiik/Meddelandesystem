package client.control;

import client.boundary.ClientUI;
import javax.swing.ImageIcon;
import shared.entity.Message;
import shared.entity.User;

/**
 * ClientController
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
final public class ClientController {
    private MessageWorker messageWorker;

    private ClientUI clientUI;

    // Remote server configurations
    private String serverAddress;
    private int serverPort;

    private User user;

    public ClientController() {
        this("localhost", 3000);
    }

    public ClientController(String address, int port) {
        this.serverAddress = address;
        this.serverPort = port;
    }

    public void startGUI() {
        clientUI = new ClientUI(this);

        // FIXME: READ FROM DISK FIRST BEFORE LOGIN
        clientUI.showLogin((username, filename) -> login(username, filename));
    }

    public void login(String username, String filename) {
        createUser(username, filename);
        clientUI.showMain();
        connect();
    }

    /**
     * Set the port that the server is listening on.
     *
     * @param port Server port
     */
    public void setServerPort(int port) {
        this.serverPort = port;
    }

    /**
     * Set the server address to connect to.
     *
     * @param address The server raw address
     */
    public void setServerAddress(String address) {
        this.serverAddress = address;
    }

    /**
     * Connect to the server
     */
    public void connect() {
        if (messageWorker == null) {
            messageWorker = new MessageWorker(serverAddress, serverPort);

            // Listen for incoming messages
            messageWorker.setOnMessage(msg -> onMessage(msg));

            // Listen for connection status
            messageWorker.setOnFailedConnect(() -> onFailedConnect());
            messageWorker.setOnConnect(() -> onConnect());
            messageWorker.setOnDisconnect(() -> onDisconnect());
        }

        messageWorker.connect();
    }

    /**
     * Disconnect from the server
     */
    public void disconnect() {
        if (messageWorker != null) messageWorker.disconnect();
    }

    /**
     * Get if the user is connected to the server
     *
     * @return True for connected, False for not connected
     */
    public boolean getIsConnected() {
        synchronized (this) {
            return messageWorker.getIsConnected();
        }
    }

    /**
     * Create a user.
     *
     * @param username The name of the user
     * @param filename The filepath to where the image lives.
     */
    public void createUser(String username, String filename) {
        user = new User(username, new ImageIcon(filename));
    }

    /**
     * Add connected user in the list to contact list
     *
     * @param index Selected index in the user list
     */
    public void addContact(int index) {}

    /**
     * Send the text message
     *
     * @param msg Text message to be sent
     */
    public void sendTextMessage(String msg) {}

    /**
     * Load and send the image.
     *
     * @param filename File path to load
     */
    public void sendImageMessage(String filename) {}

    /**
     * Callback for incoming message.
     *
     * @param msg Message object from the server
     */
    private void onMessage(Message msg) {
        System.out.println("Hello");
    }

    /**
     * Callback for when connection is established.
     */
    private void onConnect() {
        System.out.println("WE ARE CONNECTED!");
    }

    /**
     * Callback for when connection is disconnected.
     */
    private void onDisconnect() {
        System.out.println("WE ARE DISCONNECTED!");
    }

    /**
     * Callback for when failed to connect
     */
    private void onFailedConnect() {
        System.out.println("FAILED TO CONNECT!");
    }
}

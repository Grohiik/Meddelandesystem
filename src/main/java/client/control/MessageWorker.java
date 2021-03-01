package client.control;

import client.control.listeners.IOnConnection;
import client.control.listeners.IOnMessage;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import shared.entity.Message;
import shared.entity.User;

/**
 * MessageWorker connect to the server and keeps the connection alive and listen for incoming
 * messages.
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class MessageWorker implements Runnable {
    private enum ConnectionState {
        CONNECTING,
        CONNECTED,
        DISCONNECTED,
    }

    // The state of the connection
    private ConnectionState connectionState = ConnectionState.DISCONNECTED;

    // Socket connection
    private Socket socket;
    private ObjectInputStream oInputStream;
    private ObjectOutputStream oOutputStream;

    // Server address and port
    private String address;
    private int port;

    // Event callbacks
    private IOnMessage<Message> onMessage;
    private IOnConnection onConnect;
    private IOnConnection onDisconnect;
    private IOnConnection onFailedConnect;

    private Thread connectionThread;

    /**
     * Construct a worker for listening and sending message to server.
     *
     * @param address The server ip address.
     * @param port    The server opened port.
     */
    public MessageWorker(String address, int port) {
        this.address = address;
        this.port = port;
    }

    /**
     * Disconnect from the server.
     */
    public void disconnect() {
        if (connectionState == ConnectionState.DISCONNECTED) return;

        try {
            socket.close();
            socket = null;

            connectionThread.interrupt();
            connectionThread = null;

            connectionState = ConnectionState.DISCONNECTED;

            if (onDisconnect != null) onDisconnect.signal();
        } catch (IOException e) {
            System.err.println("ERROR: Failed to disconnect from server.");
        }
    }

    /**
     * Get if there's a connection to the server is alive.
     *
     * @return {@code true} for connected, {@code false} for when disconnected.
     */
    public synchronized boolean getIsConnected() {
        return connectionState == ConnectionState.CONNECTED;
    }

    /**
     * Send user object.
     *
     * @param user The user object to be sent.
     */
    public void sendUser(User user) {
        try {
            oOutputStream.writeObject(user);
        } catch (IOException e) {
            System.err.println("ERROR WRITING USER: " + e);
        }
    }

    /**
     * Send the message.
     *
     * @param message Message object to be sent.
     */
    public void sendMessage(Message message) {
        try {
            oOutputStream.writeObject(message);
        } catch (IOException e) {
            System.err.println("ERROR WRITING MESSAGE: " + e);
        }
    }

    /**
     * Set callback for incoming message.
     *
     * @param onMessage On message callback.
     */
    public void setOnMessage(IOnMessage<Message> onMessage) {
        this.onMessage = onMessage;
    }

    /**
     * Set callback for when connecting.
     *
     * @param onConnect Callback for connection successful.
     */
    public void setOnConnect(IOnConnection onConnect) {
        this.onConnect = onConnect;
    }

    /**
     * Set callback for disconnecting.
     *
     * @param onDisconnect Callback for disconnect.
     */
    public void setOnDisconnect(IOnConnection onDisconnect) {
        this.onDisconnect = onDisconnect;
    }

    /**
     * Set callback for failed to connect.
     *
     * @param onFailedConnect Callback for failed to connect
     */
    public void setOnFailedConnect(IOnConnection onFailedConnect) {
        this.onFailedConnect = onFailedConnect;
    }

    /**
     * Connect to the server.
     */
    public void connect() {
        if (connectionState == ConnectionState.CONNECTED
            || connectionState == ConnectionState.CONNECTING)
            return;

        connectionState = ConnectionState.CONNECTING;

        connectionThread = new Thread(this);
        connectionThread.start();
    }

    @Override
    public void run() {
        if (!connecting()) return; // Premature return if failed to connect

        listen();
    }

    /**
     * Connect to the server, used internally when {@link#connect()} is called.
     *
     * @return {@code true} for success, {@code false} for not successful.
     */
    private boolean connecting() {
        try {
            socket = new Socket(address, port);
            oOutputStream = new ObjectOutputStream(socket.getOutputStream());
            oInputStream = new ObjectInputStream(socket.getInputStream());

            return true;
        } catch (IOException e) {
            socket = null;
            connectionState = ConnectionState.DISCONNECTED;

            if (onFailedConnect != null) onFailedConnect.signal();
            return false;
        }
    }

    /**
     * Enter a loop that listens for incoming message. It calls the onMessage callback. The {@link
     * #setOnMessage(IOnMessage)} needs to be set for it to work.
     */
    private void listen() {
        connectionState = ConnectionState.CONNECTED;
        if (onConnect != null) onConnect.signal();

        while (!socket.isClosed() && !Thread.interrupted()) {
            try {
                var msg = (Message) oInputStream.readObject();
                onMessage.message(msg);
            } catch (ClassNotFoundException e) {
                System.err.println("ERROR FORMAT");
            } catch (IOException e) {
                disconnect();
                break;
            }
        }
    }
}

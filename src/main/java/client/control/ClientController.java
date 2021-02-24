package client.control;

import client.boundary.ClientUI;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

/**
 * ClientController
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class ClientController {
    private ClientUI ui;
    private ArrayList<String> toBeSend = new ArrayList<>();
    private boolean isConnected = false;

    private Socket socket;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;

    private InetAddress serverAddress;
    private int serverPort;

    private Listener listener;

    // FIXME: Remove this
    private String username;
    private ArrayList<String> users = new ArrayList<>();
    private ArrayList<String> messages = new ArrayList<>();

    public ClientController() {
        this("localhost", 3000);
    }

    public ClientController(String address, int port) {
        try {
            this.setServerAddress(address);
            this.setServerPort(port);
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }

    /**
     * Open up the GUI
     */
    public void startGUI() {
        ui = new ClientUI(this);
        username = ui.getName();
    }

    public void updateGUI() {
        ui.addMessage(LocalTime.now().toString(), users.get(users.size() - 1),
                      messages.get(messages.size() - 1));
    }

    /**
     *
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
     * @throws UnknownHostException
     */
    public void setServerAddress(String address) throws UnknownHostException {
        setServerAddress(InetAddress.getByName(address));
    }

    /**
     * Set the server address
     *
     * @param address The server address
     */
    public void setServerAddress(InetAddress address) {
        this.serverAddress = address;
    }

    /**
     * Connect to the server
     */
    public void connect() {
        if (isConnected) return;

        System.out.println("Connecting...");
        try {
            socket = new Socket(serverAddress.getHostName(), serverPort);
            oos = new ObjectOutputStream(socket.getOutputStream());
            ois = new ObjectInputStream(socket.getInputStream());

            listener = new Listener();
            listener.start();

            isConnected = true;
        } catch (IOException ioe) {
            socket = null;
            isConnected = false;
        }
    }

    public void disconnect() {
        if (!isConnected) return;

        System.out.println("Disconnecting...");

        try {
            listener.interrupt();
            socket.close();
            socket = null;
            listener = null;
            isConnected = false;
        } catch (IOException e) {
        }
    }

    /**
     * Get if the user is connected to the server
     *
     * @return True for connected, False for not connected
     */
    public boolean getIsConnected() {
        return isConnected;
    }

    public void addContact(int selectedIndex) {}

    /**
     * Send the text message to the server
     *
     * @param msg Text message
     */
    public void sendTextMessage(String msg) {
        if (isConnected) try {
                oos.writeUTF(username);
                oos.writeUTF(msg);
                oos.flush();
            } catch (IOException e) {
                System.out.println("ERROR SENDING MESSAGE");
            }

        toBeSend.add(msg);
    }

    public void sendFileMessage(String filename) {}

    /**
     * Send typing heartbeat
     */
    public void sendTyping() {}

    public List<String> getTextMessages() {
        return toBeSend;
    }

    // FIXEME: Entity class for Message
    private void sendMessage() {}

    private class Listener extends Thread {
        @Override
        public void run() {
            System.out.println("Listening...");
            while (!socket.isClosed() && !Thread.interrupted()) {
                try {
                    // FIXME: Read Message object
                    var user = ois.readUTF();
                    var msg = ois.readUTF();
                    users.add(user);
                    messages.add(msg);
                    updateGUI();
                } catch (IOException e) {
                    e.printStackTrace();
                    disconnect();
                    break;
                }
            }
        }
    }
}

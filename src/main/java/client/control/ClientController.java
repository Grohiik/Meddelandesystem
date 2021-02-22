package client.control;

import client.boundary.ClientUI;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

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

    /**
     * Open up the GUI
     */
    public void startGUI() {
        ui = new ClientUI(this);
    }

    /**
     * Connect to the server
     */
    public void connectToServer() {
        isConnected = true;
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
    public void sendMessage(String msg) {
        toBeSend.add(msg);
    }

    /**
     * Send the image message
     * @param image
     */
    public void sendMessage(ImageIcon image) {
        System.out.print("Message: ");
        System.out.println("[IMAGE]");
    }

    /**
     * Send typing heartbeat
     */
    public void sendTyping() {}

    public List<String> getTextMessages() {
        return toBeSend;
    }
}

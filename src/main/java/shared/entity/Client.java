package shared.entity;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Client
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class Client {
    private boolean isOnline = false;
    private ObjectOutputStream oos;

    /**
     * Create client for sending message
     *
     * @param oos Connected client object output stream.
     */
    public Client(ObjectOutputStream oos) {
        this.oos = oos;
    }

    /**
     * Send Message Object
     *
     * @param message Message object to be send
     * @return True for success and False for unsuccessful
     */
    public boolean sendMessage(Message message) {
        try {
            oos.writeObject(message);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public void setIsOnline(boolean tof) {
        isOnline = tof;
    }

    public boolean getIsOnline() {
        return isOnline;
    }
}

package shared.entity;

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
    public Client() {}

    public void setIsOnline(boolean tof) {
        isOnline = tof;
    }

    public boolean getIsOnline() {
        return isOnline;
    }
}

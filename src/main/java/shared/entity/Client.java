package shared.entity;

/**
 * Client
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class Client {
    private boolean isOnline = false;

    /**
     * Create client for checking if online
     */
    public Client() {}

    public void setIsOnline(boolean tof) {
        isOnline = tof;
    }

    public boolean getIsOnline() {
        return isOnline;
    }
}

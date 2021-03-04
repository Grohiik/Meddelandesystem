package shared.entity;

/**
 * Client
 *
 * @author Pratchaya Khansomboon
 * @author Linnéa Mörk
 * @version 1.0
 */
public class Client {
    private boolean isOnline = false;

    public void setIsOnline(boolean tof) {
        isOnline = tof;
    }

    public boolean getIsOnline() {
        return isOnline;
    }
}

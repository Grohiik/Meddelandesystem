package server.entity;

import shared.entity.User;

/**
 * Client
 *
 * @author Pratchaya Khansomboon
 * @author Linnéa Mörk
 * @version 1.0
 */
public class Client {
    private boolean isOnline = false;
    private User user;

    public Client(User user) {
        this.user = user;
    }

    public void setIsOnline(boolean tof) {
        isOnline = tof;
    }

    public boolean getIsOnline() {
        return isOnline;
    }

    @Override
    public String toString()
    {
        return user.toString();
    }

}
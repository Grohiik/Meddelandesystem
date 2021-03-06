package server.entity;

import java.util.HashMap;
import java.util.Set;
import shared.entity.User;

/**
 * Class that handles all Clients.
 *
 * @author Marcus Linné
 * @author Linnéa Mörk
 * @version 1.0
 */
public class Clients {
    private HashMap<User, Client> clients = new HashMap<>();

    /**
     * Method used to add a clients to the HashMap.
     *
     * @param user which user it is
     * @param client which client it is
     */
    public synchronized void put(User user, Client client) {
        clients.put(user, client);
    }

    /**
     * Gets the connected clients user.
     *
     * @param user Which user account
     * @return the user it is
     */
    public synchronized Client get(User user) {
        if (!clients.containsKey(user)) {
            return null;
        }
        return clients.get(user);
    }

    @Override
    public String toString() {
        String out = "";
        Set<User> users = clients.keySet();

        for (User user : users) {
            out += user + " ";
        }
        return out;
    }
}
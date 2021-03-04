package shared.entity;

import java.util.HashMap;

/**
 * Class that handles all Clients, might be moved to another directory
 *
 * @author Marcus Linné
 * @version 1.0
 */
public class Clients {
    private HashMap<User, Client>
        clients = new HashMap<>(); // TODO complete this, might need more of the code layout to fully complete
    // missing some additions

    /**
     * says error due to sync but should be sync
     *
     * @param user which user it is
     * @param client which client it is
     */
    public synchronized void put(User user, Client client) {
        clients.put(user, client);
    }

    /**
     * getter
     *
     * @param user Which user account
     * @return the user it is
     */
    public synchronized Client get(User user) {
        return clients.get(user);
    }

    // TODO more sync methods
}

package server;

import java.util.ArrayList;
import java.util.HashMap;
import shared.entity.*;

/**
 * @author Eric Lundin
 * @version 1.0
 */
public class UnsentMessages {
    private HashMap<User, ArrayList<Message>> unsent;

    /**
     * adds new unsent messages
     * @param user
     * @param message
     */
    public synchronized void put(User user, Message message) {
        if (unsent.get(user) == null) {
            ArrayList<Message> messages = new ArrayList<Message>();
            messages.add(message);
            unsent.put(user, messages);
        } else {
            unsent.get(user).add(message);
        }
    }

    /**
     *
     * @param user
     * @return returns an ArrayList with Messages for the server to send
     */
    public synchronized ArrayList<Message> get(User user) {
        ArrayList<Message> returnList = unsent.get(user);
        unsent.remove(user);
        return returnList;
    }
}

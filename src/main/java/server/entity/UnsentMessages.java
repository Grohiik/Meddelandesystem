package server.entity;

import java.util.ArrayList;
import java.util.HashMap;
import shared.entity.Message;
import shared.entity.User;

/**
 * UnsentMessages stores messages that failed to be sent to be able to send them later
 *
 * @author Eric Lundin
 * @author Linnéa Mörk
 * @version 1.0
 */
public class UnsentMessages {
    private HashMap<User, ArrayList<Message>> unsent = new HashMap<>();

    /**
     * Adds new unsent messages.
     *
     * @param user The user which could not receive the message.
     * @param message The message which could not be sent.
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
     * The server sends a user to this method when a user connects to the server.
     *
     * @param user The user that connects to the server.
     * @return Returns an ArrayList with Messages for the server to send.
     */
    public synchronized ArrayList<Message> get(User user) {
        if (!unsent.containsKey(user)) {
            return null;
        }
        ArrayList<Message> returnList = unsent.get(user);
        unsent.remove(user);
        return returnList;
    }
}
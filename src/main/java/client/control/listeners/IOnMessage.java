package client.control.listeners;

/**
 * IOnMessage generic callback for messages.
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public interface IOnMessage<T> {
    /**
     * Event for new message.
     *
     * @param msg The received message.
     */
    void message(T msg);
}
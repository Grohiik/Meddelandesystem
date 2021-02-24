package client.boundary.listeners;

/**
 * SendListener
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public interface SendListener<T> {
    /**
     * Send
     *
     * @param message Generic message, can be anything in your codebase
     * @return Successfully send
     */
    boolean send(T message);
}

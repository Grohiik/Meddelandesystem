package client.boundary.event;

/**
 * IOnSend listen for send
 *
 * @author  Pratchaya Khansomboon
 * @version 1.0
 */
public interface IOnSend<T> {
    /**
     * Send message callback.
     *
     * @param message Generic message, can be anything in your codebase
     * @return {@code true} Successfully send, {@code false} failed to send.
     */
    boolean send(T message);
}

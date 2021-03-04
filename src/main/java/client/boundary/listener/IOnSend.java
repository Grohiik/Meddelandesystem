package client.boundary.listener;

/**
 * IOnSend listen for send
 *
 * @author  Pratchaya Khansomboon
 * @version 1.0
 */
public interface IOnSend<T> {
    /**
     * Send message.
     *
     * @param message Generic message, can be anything in your codebase
     * @return True Successfully send, False failed to send.
     */
    boolean send(T message);
}

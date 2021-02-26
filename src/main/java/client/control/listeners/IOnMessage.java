package client.control.listeners;

/**
 * IOnMessage generic callback for one parameter with void return.
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public interface IOnMessage<T> {
    void message(T msg);
}
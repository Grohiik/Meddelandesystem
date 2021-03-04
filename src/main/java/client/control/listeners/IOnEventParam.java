package client.control.listeners;

/**
 * IOnEventParam
 *
 * @author Pratchaya Khansomboon
 * @author Eric Lundin
 * @version 1.0
 */
public interface IOnEventParam<T> {
    void signal(T object);
}

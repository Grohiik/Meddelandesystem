package client.boundary.listener;

/**
 * IOEventParam is an interface for events with one parameter.
 *
 * @author  Pratchaya Khansomboon
 * @version 1.0
 */
public interface IOnEventParam<T> {
    void signal(T value);
}

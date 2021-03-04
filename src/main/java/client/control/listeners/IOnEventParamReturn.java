package client.control.listeners;

/**
 * IOnEventParamReturn
 *
 * @author Pratchaya Khansomboon
 * @author Eric Lundin
 * @version 1.0
 */
public interface IOnEventParamReturn<T, R> {
    R signal(T object);
}
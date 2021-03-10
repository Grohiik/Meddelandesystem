package client.boundary.event;

/**
 * IOnLogin is an interface for login events that takes in two string parameters.
 *
 * @author  Pratchaya Khansomboon
 * @version 1.0
 */
public interface IOnLogin {
    void login(String username, String filename);
}

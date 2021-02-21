package shared.entity;

import javax.swing.*;

/**
 * The user class is used to identify users and direct messages to the right client/clients
 */
public class User
{
    private String username;
    ImageIcon image;

    /**
     *
     * @param username
     * @param image
     */
    public User(String username, ImageIcon image){
        this.image = image;
        this.username = username;
    }
}

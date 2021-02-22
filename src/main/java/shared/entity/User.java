package shared.entity;

import java.io.Serializable;
import javax.swing.*;

/**
 * The user class is used to identify users and direct messages to the right client/clients
 * @author Eric Lundin al3214
 * @version 1.0
 */
public class User implements Serializable {
    private String username;
    ImageIcon image;

    /**
     * @param username
     * @param image
     */
    public User(String username, ImageIcon image) {
        this.image = image;
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     *
     * @return returns a hashcode of the username String
     */
    public int hashCode() {
        return username.hashCode();
    }

    /**
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof User) {
            return username.equals(((User) obj).getUsername());
        }
        return false;
    }
}

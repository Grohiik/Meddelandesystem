package shared.entity;

import java.io.Serializable;
import javax.swing.ImageIcon;

/**
 * The User class is used to identify users and direct messages to the right client/clients.
 *
 * @author Eric Lundin
 * @version 1.0
 */
public class User implements Serializable {
    private String username;
    private ImageIcon image;

    /**
     * Constructor of the User that contains the username
     * and the profile picture of the user.
     *
     * @param username The username of the user.
     * @param image    The image for the user profile.
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
     * @return Returns a hashcode of the username String.
     */
    @Override
    public int hashCode() {
        return username.hashCode();
    }

    /**
     * This method is used to compare usernames of other user objects.
     *
     * @param obj The user object to be compared.
     * @return {@code true} if the User object is the same or {@code false} if the User object is
     *         not the same.
     */
    @Override
    public boolean equals(Object obj) {
        if (obj != null && obj instanceof User) {
            return username.equals(((User) obj).getUsername());
        }
        return false;
    }

    @Override
    public String toString() {
        return username;
    }
}
package shared.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * UserListMessage class that lists all connected Users.
 *
 * @author Marcus Linné
 * @author Linnéa Mörk
 * @version 1.0
 */
public class UserListMessage implements IMessage, Serializable {
    private User[] users;
    private Date receiveTime;

    /**
     * Constructor creating a list of users.
     * @param users users in the list of all Users.
     */
    public UserListMessage(User[] users) {
        this.users = users;
    }

    /**
     * Copy constructor used to copy the UserListMessage.
     * @param userListMessage the userListMessage to copy.
     */
    public UserListMessage(UserListMessage userListMessage) {
        this.users = userListMessage.users;
    }

    public User[] getUsers() {
        return users;
    }

    @Override
    public Date getReceiveTime() {
        return receiveTime;
    }

    @Override
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public User[] getReceiverList() {
        return users;
    }

    @Override
    public String toString() {
        String out = "";
        for (User user : users) {
            out += user + " ";
        }
        return out;
    }
}
package shared.entity;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

/**
 * UserList that lists all connected Users
 *
 * @author Marcus Linné
 * @author Linnéa Mörk
 * @version 1.0
 */
public class UserListMessage implements IMessage, Serializable {
    private User[] users;
    private Date receiveTime;

    public UserListMessage(User[] users) {
        this.users = users;
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
        out += "at: " + receiveTime;
        return out;
    }
}
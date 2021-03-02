package shared.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * UserList that lists all connected Users
 *
 * @author Marcus Linné, Linnéa Mörk
 * @version 1.0
 */
public class UserListMessage implements IMessage, Serializable {
    private User[] users;
    private Date sentTime;
    private Date receiveTime;

    public UserListMessage(User[] users) {
        this.users = users;
    }

    @Override
    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public Date getReceiveTime() {
        return receiveTime;
    }

    @Override
    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    @Override
    public Date getSentTime() {
        return sentTime;
    }

    public User[] getUsers() {
        return users;
    }
}
package shared.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * ClientList that lists all connected Clients
 *
 * @author Marcus Linné, Linnéa Mörk
 * @version 1.0
 */
public class ClientListMessage implements IMessage, Serializable {
    User[] users;
    Date sentTime;
    Date receiveTime;

    public ClientListMessage(User[] users) {
        this.users = users;
    }

    @Override
    public void setReceiveTime(Date receiveTime) {
        this.sentTime = receiveTime;
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
}
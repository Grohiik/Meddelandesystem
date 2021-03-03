package shared.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * IMessage
 *
 * @author Christian Heisterkamp
 * @version 1.0
 */
public interface IMessage extends Serializable {
    void setReceiveTime(Date receiveTime);
    Date getReceiveTime();

    void setSentTime(Date date);
    Date getSentTime();

    User[] getReceiverList();
}
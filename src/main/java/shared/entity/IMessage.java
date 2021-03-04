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
    Date getReceiveTime();

    void setReceiveTime(Date receiveTime);

    User[] getReceiverList();
}
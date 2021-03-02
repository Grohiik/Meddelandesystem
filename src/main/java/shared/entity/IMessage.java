package shared.entity;

import java.io.Serializable;
import java.util.Date;

public interface IMessage extends Serializable
{
    void setReceiveTime(Date receiveTime);
    Date getReceiveTime();
    User getSender();
    void setSender(User sender);
}

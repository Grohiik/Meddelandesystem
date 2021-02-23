package shared.entity;

import java.io.Serializable;
import java.util.Date;

public class Message implements Serializable {
    private Date sentTime;
    private Date receivedTime;

    public Message() {}
}

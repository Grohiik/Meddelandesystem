package shared.entity;

import java.io.Serializable;
import java.util.Date;
import javax.swing.ImageIcon;

/**
 * Message
 *
 * @author Christian Heisterkamp
 * @version 1.0
 */
public class Message implements Serializable {
    private User sender;
    private User[] receiverList;
    private String text;
    private ImageIcon image;
    private Date sentTime;
    private Date receiveTime;

    public Message() {}
}

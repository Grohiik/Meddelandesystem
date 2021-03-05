package shared.entity;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.ImageIcon;

/**
 * Message
 *
 * @author Christian Heisterkamp
 * @author Marcus Linné
 * @version 1.0
 */
public class Message implements IMessage, Serializable {
    private User sender;
    private User[] receiverList;
    private String text;
    private ImageIcon image;
    private Date sentTime;
    private Date receiveTime;

    public Message() {}

    /**
     * Constructor for Message
     * @param sender sender is which user that has sent the message.
     * @param receiverList receiverList is which users to receive the message.
     * @param text text the message contains.
     * @param image image that the message contains.
     */
    public Message(User sender, User[] receiverList, String text, ImageIcon image) {
        this.sender = sender;
        this.receiverList = receiverList;
        this.text = text;
        this.image = image;
    }

    public Message(Message message) {
        this.sender = message.sender;
        this.receiverList = message.receiverList;
        this.text = message.text;
        this.image = message.image;
        this.sentTime = message.sentTime;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }

    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User[] getReceiverList() {
        return receiverList;
    }

    public void setReceiverList(User[] receiverList) {
        this.receiverList = receiverList;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public ImageIcon getImage() {
        return image;
    }

    public void setImage(ImageIcon image) {
        this.image = image;
    }

    public Date getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(Date receiveTime) {
        this.receiveTime = receiveTime;
    }

    @Override
    public String toString() {
        String receiverString = "";
        String imageString = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (User currentUser : receiverList) {
            receiverString += " " + currentUser;
        }
        if (image != null) {
            imageString = "and an image";
        }

        String receiveTimeString = format.format(receiveTime);
        String sentTimeString = "never";

        if (sentTime != null) {
            sentTimeString = format.format(sentTime);
        }

        String out = String.format(
            "%s sent a message to%s with the content %s %s| Sent: %s | Received: %s", sender,
            receiverString, text, imageString, sentTimeString, receiveTimeString);

        return out;
    }
}
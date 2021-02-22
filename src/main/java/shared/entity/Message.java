
package shared.entity;

import javax.swing.ImageIcon;
/**
 * Message
 * @author Christian Heisterkamp
 * @version 1.0
 */
public class Message
{
    private User sender;
    private User[] receiverList;
    private String text;
    private ImageIcon image;
    private String sentTime;
    private String receiveTime;

    public Message(){

    }
}

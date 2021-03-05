package client.boundary;

import client.boundary.event.IOnEvent;
import client.boundary.event.IOnEventParam;
import client.boundary.event.IOnLogin;
import javax.swing.ImageIcon;

/**
 * IUserInterface has all the method to be implemented for the controller to use for setting an
 * getting the events.
 *
 * @author  Pratchaya Khansomboon
 * @version 1.0
 */
public interface IUserInterface {
    void showLogin(IOnLogin onLogin);
    void showMain();

    void setOnAddRecipientAction(IOnEventParam<Integer> onAddRecipient);
    void setOnRemoveRecipientAction(IOnEventParam<Integer> onRemoveRecipient);
    void setOnSendTextAction(IOnEventParam<String> onSend);
    void setOnSendFileAction(IOnEventParam<String> onSend);
    void setOnTypingAction(IOnEvent onTyping);
    void setOnShowMessageAction(IOnEventParam<Integer> onShowMessages);
    void setOnShowFriendAction(IOnEvent onShowFriend);
    void setOnShowOnlineAction(IOnEvent onShowOnline);
    void setOnConnectAction(IOnEvent onConnect);
    void setOnDisconnectAction(IOnEvent onDisconnect);

    void showRecipients(String[] names);
    void setTitle(String title);
    void setUserTitle(String name);

    void setUserList(String[] usernames, ImageIcon[] images);
    void clearMessages();
    void addMessage(String time, String name, String text);
    void addMessage(String time, String name, ImageIcon image);
}

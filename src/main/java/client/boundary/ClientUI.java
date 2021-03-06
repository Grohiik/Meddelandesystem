package client.boundary;

import client.boundary.event.IOnEvent;
import client.boundary.event.IOnEventParam;
import client.boundary.event.IOnLogin;
import client.boundary.page.LoginPage;
import client.boundary.page.MainPage;
import java.awt.Dimension;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * ClientUI class is the main graphical interface for the user. We can register the events such as
 * button presses by using the setOn methods.
 *
 * @author  Pratchaya Khansomboon
 * @author  Eric Lundin
 * @version 1.0
 */
public class ClientUI implements IUserInterface {
    private final JFileChooser fileChooser;

    private final JFrame frame;
    private String windowTitle = "Message Cat";

    // Different pages
    private final MainPage mainPage;
    private final LoginPage loginPage;

    /**
     * Create the graphical user interface. This won't display the window.
     */
    public ClientUI() {
        changeLook();
        fileChooser = new JFileChooser();
        setupFileChooser();

        frame = new JFrame("Message Cat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // frame.setLocation(0, -800); // FIXME: For dual screen only

        mainPage = new MainPage(this);
        loginPage = new LoginPage(this);
    }

    /**
     * Show the Login page and register the confirmation event.
     *
     * @param onLogin Event callback when the user press confirm.
     */
    @Override
    public void showLogin(IOnLogin onLogin) {
        loginPage.setOnConfirm(onLogin);

        frame.setContentPane(loginPage.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Show the Main page of the application. It has the chat and the list of all the connected
     * added friends.
     */
    @Override
    public void showMain() {
        frame.setSize(850, 520);
        frame.setMinimumSize(new Dimension(850, 520));

        frame.setContentPane(mainPage.getPanel());
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * Clear the messages in the ChatPanel.
     */
    @Override
    public void clearMessages() {
        mainPage.clearMessages();
    }

    /**
     * Set the event callback for when the add friend button has been pressed.
     *
     * @param onAddRecipient The function callback with one int parameter.
     */
    @Override
    public void setOnAddFriendAction(IOnEventParam<Integer> onAddFriend) {
        mainPage.setOnAddFriend(onAddFriend);
    }

    /**
     * Set the event callback for when the add recipient button has been pressed.
     *
     * @param onAddRecipient The function callback with one int parameter.
     */
    @Override
    public void setOnAddRecipientAction(IOnEventParam<Integer> onAddRecipient) {
        mainPage.setOnAddRecipient(onAddRecipient);
    }

    /**
     * Set event callback for when the remove recipient button has been pressed.
     *
     * @param onRemoveRecipient The function callback with one int parameter.
     */
    @Override
    public void setOnRemoveRecipientAction(IOnEventParam<Integer> onRemoveRecipient) {
        mainPage.setOnRemoveRecipient(onRemoveRecipient);
    }

    /**
     * Set event callback for send text action.
     *
     * @param onSend The function callback with one string parameter.
     */
    @Override
    public void setOnSendTextAction(IOnEventParam<String> onSend) {
        mainPage.setOnSendText(onSend);
    }

    /**
     * Set the event callback for send file action.
     *
     * @param onSend The function callback with one string parameter.
     */
    @Override
    public void setOnSendFileAction(IOnEventParam<String> onSend) {
        mainPage.setOnSendFile(onSend);
    }

    /**
     * Set the event callback for typing action.
     *
     * @param onTyping The function callback with no parameter.
     */
    @Override
    public void setOnTypingAction(IOnEvent onTyping) {
        mainPage.setOnTyping(onTyping);
    }

    /**
     * Set the event callback for show message action.
     *
     * @param onShowMessages The function callback with one int parameter.
     */
    @Override
    public void setOnShowMessageAction(IOnEventParam<Integer> onShowMessages) {
        mainPage.setOnShowMessages(onShowMessages);
    }

    /**
     * Set the event callback for show friend action.
     *
     * @param onShowFriend The function callback with no parameter.
     */
    @Override
    public void setOnShowFriendAction(IOnEvent onShowFriend) {
        mainPage.setOnShowFriend(onShowFriend);
    }

    /**
     * Set the event callback for show online action.
     *
     * @param onShowOnline The function callback with no parameter.
     */
    @Override
    public void setOnShowOnlineAction(IOnEvent onShowOnline) {
        mainPage.setOnShowOnline(onShowOnline);
    }

    /**
     * Set the event callback for connect action.
     *
     * @param onConnect The function callback with no parameter.
     */
    @Override
    public void setOnConnectAction(IOnEvent onConnect) {
        mainPage.setOnConnect(onConnect);
    }

    /**
     * Set the event callback for disconnect action.
     *
     * @param onDisconnect The function callback with no parameter.
     */
    @Override
    public void setOnDisconnectAction(IOnEvent onDisconnect) {
        mainPage.setOnDisconnect(onDisconnect);
    }

    /**
     * Set the recipients in the {@link client.boundary.panel.ChatPanel}.
     *
     * @param names The string array of names.
     */
    @Override
    public void showRecipients(String[] names) {
        mainPage.setRecipient(names);
    }

    /**
     * Set the window title.
     *
     * @param title Text for the title.
     */
    @Override
    public void setTitle(String title) {
        frame.setTitle(title);
    }

    /**
     * Set the user on the title with default window title.
     *
     * @param name The username.
     */
    @Override
    public void setUserTitle(String name) {
        frame.setTitle(name + " - " + windowTitle);
    }

    /**
     * Set the user list in the {@link client.boundary.panel.UserListPanel}. The arrays needs to be
     * the same size.
     *
     * @param usernames The string array of usernames.
     * @param images    The image array.
     */
    @Override
    public void setUserList(String[] usernames, ImageIcon[] images) {
        mainPage.setUserList(usernames, images);
    }

    /**
     * Add the text message to the {@link client.boundary.panel.ChatPanel}.
     *
     * @param time The string formatted time.
     * @param name The name of the user for the message.
     * @param text The text content of the message.
     */
    @Override
    public void addMessage(String time, String name, String text) {
        mainPage.addMessage(time, name, text);
    }

    /**
     * Add the image message to the {@link client.boundary.panel.ChatPanel}.
     *
     * @param time  The string formatted time.
     * @param name  The name of the user for the message.
     * @param image The image content of the message.
     */
    @Override
    public void addMessage(String time, String name, ImageIcon image) {
        mainPage.addMessage(time, name, image);
    }

    /**
     * Default file chooser dialog
     *
     * @return Status if the there is a file selected
     * @see JFileChooser
     * @since 1.0
     */
    public int showFileDialog() {
        return showFileDialog("File");
    }

    /**
     * Show file chooser dialog
     *
     * @param msg The message to display
     * @return Status for file chooser
     * @see JFileChooser
     * @since 1.0
     */
    public int showFileDialog(String msg) {
        fileChooser.setDialogTitle(msg);
        return fileChooser.showOpenDialog(frame);
    }

    /**
     * Get the selected file from the file chooser
     *
     * @return The selected file
     */
    public File getSelectedFile() {
        return fileChooser.getSelectedFile();
    }

    /**
     * Setup how the FileChooser should behave.
     */
    private void setupFileChooser() {
        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
    }

    /**
     * Change the look and feel of the Swing UI.
     */
    private void changeLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException ignored) {
        }
    }
}

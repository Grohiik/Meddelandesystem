package client.boundary;

import client.boundary.listener.IOnEvent;
import client.boundary.listener.IOnEventParam;
import client.boundary.listener.IOnLogin;
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
 * ClientUI the main graphical interface for the user
 *
 * @author  Pratchaya Khansomboon
 * @author  Eric Lundin
 * @version 1.0
 */
public class ClientUI {
    private final JFileChooser FILE_CHOOSER = new JFileChooser();

    private JFrame frame;

    private MainPage mainPage;
    private LoginPage loginPage;

    /**
     * Create the main gui
     *
     * @param controller Reference to the ClientController
     */
    public ClientUI() {
        frame = new JFrame("Message Cat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // frame.setLocation(0, -800); // FIXME: For dual screen only

        changeLook();
        setupFileChooser();

        mainPage = new MainPage(this);
        loginPage = new LoginPage(this);

        frame.setVisible(true);
    }

    public void showLogin(IOnLogin onLogin) {
        loginPage.setOnConfirm(onLogin);

        frame.setContentPane(loginPage.getPanel());
        frame.pack();
    }

    public void showMain() {
        frame.setSize(850, 520);
        frame.setMinimumSize(new Dimension(850, 520));

        frame.setContentPane(mainPage.getPanel());
        frame.pack();
    }

    public void clearMessages() {
        mainPage.clearMessages();
    }

    public void setOnAddRecipientAction(IOnEventParam<Integer> onAddRecipient) {
        mainPage.setOnAddRecipient(onAddRecipient);
    }

    public void setOnRemoveRecipientAction(IOnEventParam<Integer> onRemoveRecipient) {
        mainPage.setOnRemoveRecipient(onRemoveRecipient);
    }

    public void setOnSendTextAction(IOnEventParam<String> onSend) {
        mainPage.setOnSendText(onSend);
    }

    public void setOnSendFileAction(IOnEventParam<String> onSend) {
        mainPage.setOnSendFile(onSend);
    }

    public void setOnTypingAction(IOnEvent onTyping) {
        mainPage.setOnTyping(onTyping);
    }

    public void setOnShowMessageAction(IOnEventParam<Integer> onShowMessages) {
        mainPage.setOnShowMessages(onShowMessages);
    }

    public void setOnShowFriendAction(IOnEvent onShowFriend) {
        mainPage.setOnShowFriend(onShowFriend);
    }

    public void setOnShowOnlineAction(IOnEvent onShowOnline) {
        mainPage.setOnShowOnline(onShowOnline);
    }

    public void setOnConnectAction(IOnEvent onConnect) {
        mainPage.setOnConnect(onConnect);
    }

    public void setOnDisconnectAction(IOnEvent onDisconnect) {
        mainPage.setOnDisconnect(onDisconnect);
    }

    public void showRecipients(String[] names) {
        mainPage.setRecipient(names);
    }

    public void setTitle(String title) {
        frame.setTitle(title);
    }

    public void setUserTitle(String name) {
        frame.setTitle(name + " - Message Cat");
    }

    public void setUserList(String[] usernames, ImageIcon[] images) {
        mainPage.setUserList(usernames, images);
    }

    public void addMessage(String time, String name, String text) {
        mainPage.addMessage(time, name, text);
    }

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
        FILE_CHOOSER.setDialogTitle(msg);
        return FILE_CHOOSER.showOpenDialog(frame);
    }

    /**
     * Get the selected file from the file chooser
     *
     * @return The selected file
     */
    public File getSelectedFile() {
        return FILE_CHOOSER.getSelectedFile();
    }

    private void setupFileChooser() {
        FILE_CHOOSER.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "png", "jpeg"));
    }

    private void changeLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException ignored) {
        }
    }
}

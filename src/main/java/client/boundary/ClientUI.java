package client.boundary;

import client.boundary.components.MessagePanel;
import client.boundary.panels.*;
import client.control.ClientController;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.time.format.DateTimeFormatter;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * ClientUI the main graphical interface for the user
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class ClientUI {
    private final JFileChooser FILE_CHOOSER = new JFileChooser();
    private ClientController controller;

    private JFrame frame;
    private JPanel mainPanel;
    private ChatPanel chatPanel;
    private FriendPanel friendPanel;

    /**
     * Create the main gui
     *
     * @param controller Reference to the ClientController
     */
    public ClientUI(ClientController controller) {
        this.controller = controller;

        frame = new JFrame("Message Cat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(850, 520);
        frame.setMinimumSize(new Dimension(850, 520));
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        changeLook();
        setupFileChooser();

        // Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        // JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Tools");
        // mb.add(m1);
        mb.add(m2);

        // JMenuItem m11 = new JMenuItem("Hello");
        JMenuItem connectMI = new JMenuItem("Connect to server");
        JMenuItem disconnectMI = new JMenuItem("Disconnect from server");
        disconnectMI.addActionListener(e -> onDisconnect());
        connectMI.addActionListener(e -> onConnect());

        // m1.add(m11);
        m2.add(connectMI);
        m2.add(disconnectMI);

        chatPanel = new ChatPanel();

        chatPanel.addSendTextListener(msg -> {
            controller.sendTextMessage(msg);
            return controller.getIsConnected();
        });
        chatPanel.addSendFileListener(filename -> {
            controller.sendFileMessage(filename);
            return controller.getIsConnected();
        });

        friendPanel = new FriendPanel();

        mainPanel.add(chatPanel.getPanel(), BorderLayout.CENTER);
        mainPanel.add(friendPanel.getPanel(), BorderLayout.EAST);

        mainPanel.add(BorderLayout.NORTH, mb);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    // FIXME: Remove this
    public String getName() {
        return JOptionPane.showInputDialog(frame, "Name: ", "Enter your name.",
                                           JOptionPane.QUESTION_MESSAGE);
    }

    public void clearMessages() {
        SwingUtilities.invokeLater(() -> chatPanel.clearMessages());
    }

    public void addMessage(String time, String name, String text) {
        SwingUtilities.invokeLater(() -> chatPanel.addMessage(new MessagePanel(time, name, text)));
    }

    public void addMessage(String time, String name, ImageIcon image) {
        SwingUtilities.invokeLater(() -> chatPanel.addMessage(new MessagePanel(time, name, image)));
    }

    /**
     * Send text message
     *
     * @param msg Message to send
     */
    public boolean sendText(String msg) {
        controller.sendTextMessage(msg);
        return controller.getIsConnected();
    }

    /**
     * Send file message
     *
     * @param filename File path on the system
     */
    public boolean sendFile(String filename) {
        controller.sendFileMessage(filename);
        return controller.getIsConnected();
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

    private void onConnect() {
        controller.connect();
    }

    private void onDisconnect() {
        controller.disconnect();
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

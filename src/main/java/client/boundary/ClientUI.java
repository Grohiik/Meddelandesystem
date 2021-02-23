package client.boundary;

import client.control.ClientController;
import java.awt.BorderLayout;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
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

    private JButton fileButton, sendButton;
    private JTextField messageTextField;

    private JFrame frame;
    private JPanel mainPanel;
    private ChatPanel chatPanel;
    /**
     * Create the main gui
     *
     * @param controller Reference to the ClientController
     */
    public ClientUI(ClientController controller) {
        this.controller = controller;

        frame = new JFrame("Message Cat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 480);
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        changeLook();
        setupFileChooser();

        // Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Tools");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m11 = new JMenuItem("Hello");
        JMenuItem connectMI = new JMenuItem("Connect to server");
        JMenuItem disconnectMI = new JMenuItem("Disconnect from server");
        disconnectMI.addActionListener(e -> onDisconnect());
        connectMI.addActionListener(e -> onConnect());
        m1.add(m11);
        m2.add(connectMI);
        m2.add(disconnectMI);

        chatPanel = new ChatPanel(this);
        mainPanel.add(chatPanel.getPanel(), BorderLayout.CENTER);
        mainPanel.add(BorderLayout.NORTH, mb);
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    public void setMessages(String[] messages) {
        chatPanel.setMessages(messages);
    }

    /**
     * Send text message
     *
     * @param msg Message to send
     */
    boolean sendText(String msg) {
        controller.sendTextMessage(msg);
        return controller.getIsConnected();
    }

    /**
     * Send file message
     *
     * @param filename File path on the system
     */
    boolean sendFile(String filename) {
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
    int showFileDialog() {
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
    int showFileDialog(String msg) {
        FILE_CHOOSER.setDialogTitle(msg);
        return FILE_CHOOSER.showOpenDialog(frame);
    }

    /**
     * Get the selected file from the file chooser
     *
     * @return The selected file
     */
    File getSelectedFile() {
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

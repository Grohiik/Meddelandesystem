package client.boundary;

import client.control.ClientController;
import java.awt.BorderLayout;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    private final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    private final JFileChooser FILE_CHOOSER = new JFileChooser();

    private JButton fileButton, sendButton;
    private JTextField messageTextField;

    private JFrame frame;

    private ClientController controller;

    /**
     * Create the main gui
     *
     * @param controller Reference to the ClientController
     */
    public ClientUI(ClientController controller) {
        this.controller = controller;

        frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);

        changeLook();
        setupFileChooser();

        // Creating the MenuBar and adding components
        JMenuBar mb = new JMenuBar();
        JMenu m1 = new JMenu("File");
        JMenu m2 = new JMenu("Help");
        mb.add(m1);
        mb.add(m2);
        JMenuItem m11 = new JMenuItem("Open");
        JMenuItem m22 = new JMenuItem("Save as");
        m1.add(m11);
        m1.add(m22);

        // Creating the panel at bottom and adding components
        JPanel panel = new JPanel(); // the panel is not visible in output
        JPanel btnPanel = new JPanel();
        panel.setLayout(new BorderLayout());
        messageTextField = new JTextField(); // accepts upto 10 characters
        messageTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        messageTextField.addActionListener(e -> onSend());
        messageTextField.addCaretListener(e -> onTyping());

        fileButton = new JButton("File");
        fileButton.addActionListener(e -> onFileButton());
        sendButton = new JButton("Send");
        sendButton.addActionListener(e -> onSend());
        panel.add(messageTextField, BorderLayout.CENTER); // Components Added using Flow Layout
        btnPanel.add(fileButton);
        btnPanel.add(sendButton);
        panel.add(btnPanel, BorderLayout.LINE_END);

        JTextPane txtPane = new JTextPane();
        JScrollPane jsp = new JScrollPane(txtPane);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        // Adding Components to the frame.
        frame.getContentPane().add(BorderLayout.SOUTH, panel);
        frame.getContentPane().add(BorderLayout.NORTH, mb);
        frame.getContentPane().add(BorderLayout.CENTER, jsp);
        frame.setVisible(true);
    }

    /**
     * File button pressed event
     */
    private void onFileButton() {
        int isApproved = showFileDialog("Choose an image");

        if (isApproved == JFileChooser.APPROVE_OPTION) {
            File f = getSelectedFile();
            System.out.println(f.getAbsolutePath());
        }
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

    /**
     * Send event from button
     */
    private void onSend() {
        controller.sendMessage(messageTextField.getText());
        messageTextField.setText("");
    }

    /**
     * Typing event from the input field
     */
    private void onTyping() {
        System.out.print(DATE_FORMAT.format(LocalDateTime.now()));
        System.out.println(": Typing...");
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

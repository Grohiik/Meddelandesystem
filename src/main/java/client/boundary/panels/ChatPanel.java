package client.boundary.panels;

import client.boundary.ClientUI;
import client.boundary.components.ListPanel;
import client.boundary.components.MessagePanel;
import client.boundary.listeners.SendListener;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JEditorPane;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ListCellRenderer;
import javax.swing.ScrollPaneConstants;

/**
 * ChatPanel
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class ChatPanel {
    private final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private JPanel panel;

    private JButton fileButton, sendButton;
    private JTextField messageTextField;

    private JPanel recipientPanel;
    private JLabel recipientName;
    private JLabel recipientIcon;

    private ListPanel<MessagePanel> listPanel;

    public ChatPanel() {
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        recipientPanel = new JPanel();
        recipientPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        recipientName = new JLabel("Obi-Wan Kenobi");
        try {
            var icon = new ImageIcon(
                new ImageIcon(new URL("https://engineering.kthexiii.com/assets/general.png"))
                    .getImage()
                    .getScaledInstance(32, 32, Image.SCALE_SMOOTH));
            recipientIcon = new JLabel(icon);
            recipientPanel.add(recipientIcon);
        } catch (MalformedURLException e1) {
        }

        recipientPanel.add(recipientName);

        JPanel inputPanel = new JPanel();
        JPanel btnPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        messageTextField = new JTextField(); // accepts upto 10 characters
        messageTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        messageTextField.addCaretListener(e -> onTyping());

        fileButton = new JButton("File");
        fileButton.setEnabled(false); // FIXME: Enable this when there's a message object
        sendButton = new JButton("Send");
        inputPanel.add(messageTextField, BorderLayout.CENTER); // Components Added using Flow Layout
        btnPanel.add(fileButton);
        btnPanel.add(sendButton);
        inputPanel.add(btnPanel, BorderLayout.LINE_END);

        listPanel = new ListPanel<MessagePanel>();

        panel.add(recipientPanel, BorderLayout.PAGE_START);
        panel.add(listPanel.getPane(), BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.PAGE_END);
    }

    public JPanel getPanel() {
        return panel;
    }

    public void clearMessages() {
        listPanel.clear();
    }

    public void addMessage(MessagePanel messages) {
        listPanel.add(messages);
    }

    public void addSendTextListener(SendListener<String> listener) {
        sendButton.addActionListener(e -> onSend(listener));
        messageTextField.addActionListener(e -> onSend(listener));
    }

    public void addSendFileListener(SendListener<String> listener) {
        fileButton.addActionListener(e -> onFileButton(listener));
    }

    /**
     * File button pressed event
     */
    private void onFileButton(SendListener<String> listener) {
        // TODO: FILE CHOOSER
        // int isApproved = ui.showFileDialog("Choose an image");

        // if (isApproved == JFileChooser.APPROVE_OPTION) {
        //     File f = ui.getSelectedFile();
        //     System.out.println(f.getAbsolutePath());
        // }
    }

    /**
     * Send event from button
     */
    private void onSend(SendListener<String> listener) {
        System.out.println("hkkhke");
        String text = messageTextField.getText();
        if (!text.isBlank() && listener.send(text)) messageTextField.setText("");
    }

    /**
     * Typing event from the input field
     */
    private void onTyping() {
        System.out.print(DATE_FORMAT.format(LocalDateTime.now()));
        System.out.println(": Typing...");
    }
}

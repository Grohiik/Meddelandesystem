package client.boundary;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;

public class ChatPanel {
    private final DateTimeFormatter DATE_FORMAT =
        DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    private ClientUI ui;
    private JPanel panel;

    private JButton fileButton, sendButton;
    private JTextField messageTextField;
    private JTextPane txtPane;

    private JPanel recipientPanel;
    private JLabel recipientName;

    public ChatPanel(ClientUI ui) {
        this.ui = ui;
        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        recipientPanel = new JPanel();
        recipientPanel.setLayout(new FlowLayout(FlowLayout.LEADING));

        recipientName = new JLabel("Cat");
        recipientPanel.add(recipientName);

        JPanel inputPanel = new JPanel();
        JPanel btnPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());
        messageTextField = new JTextField(); // accepts upto 10 characters
        messageTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        messageTextField.addActionListener(e -> onSend());
        messageTextField.addCaretListener(e -> onTyping());

        fileButton = new JButton("File");
        fileButton.addActionListener(e -> onFileButton());
        sendButton = new JButton("Send");
        sendButton.addActionListener(e -> onSend());
        inputPanel.add(messageTextField, BorderLayout.CENTER); // Components Added using Flow Layout
        btnPanel.add(fileButton);
        btnPanel.add(sendButton);
        inputPanel.add(btnPanel, BorderLayout.LINE_END);

        txtPane = new JTextPane();
        JScrollPane jsp = new JScrollPane(txtPane);
        jsp.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);

        panel.add(recipientPanel, BorderLayout.PAGE_START);
        panel.add(jsp, BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.PAGE_END);
    }

    void setMessages(String[] messages) {
        String text = "";
        for (String string : messages) {
            text += string + "\n";
        }
        txtPane.setText(text);
    }

    /**
     * File button pressed event
     */
    private void onFileButton() {
        int isApproved = ui.showFileDialog("Choose an image");

        if (isApproved == JFileChooser.APPROVE_OPTION) {
            File f = ui.getSelectedFile();
            System.out.println(f.getAbsolutePath());
        }
    }

    public JPanel getPanel() {
        return panel;
    }

    /**
     * Send event from button
     */
    private void onSend() {
        if (ui.sendText(messageTextField.getText())) messageTextField.setText("");
    }

    /**
     * Typing event from the input field
     */
    private void onTyping() {
        System.out.print(DATE_FORMAT.format(LocalDateTime.now()));
        System.out.println(": Typing...");
    }
}

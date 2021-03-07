package client.boundary.panel;

import client.boundary.ClientUI;
import client.boundary.component.ListPanel;
import client.boundary.component.MessagePanel;
import client.boundary.event.IOnEvent;
import client.boundary.event.IOnEventParam;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * ChatPanel is a custom panel for rendering the chat and input field for sending messages. It has
 * event interface you can attach to and have an interface for setting it to different state.
 *
 * @author  Pratchaya Khansomboon
 * @author  Eric Lundin
 * @version 1.0
 */
public class ChatPanel {
    private JPanel panel;

    private JButton fileButton, sendButton;
    private JTextField messageTextField;

    private JPanel recipientPanel;
    private JTextArea recipientNamesTF;

    private ListPanel<MessagePanel> listPanel;
    private ClientUI clientUI;

    /**
     * Create ChatPanel to display the messages and inputs for sending message.
     *
     * @param clientUI Object reference to the client ui for attaching events.
     */
    public ChatPanel(ClientUI clientUI) {
        this.clientUI = clientUI;

        panel = new JPanel();
        panel.setLayout(new BorderLayout());
        recipientPanel = new JPanel();
        recipientPanel.setLayout(new BorderLayout());

        recipientNamesTF = new JTextArea();
        recipientNamesTF.setWrapStyleWord(true);
        recipientNamesTF.setLineWrap(true);
        recipientNamesTF.setEditable(false);
        recipientPanel.add(recipientNamesTF, BorderLayout.CENTER);

        JPanel inputPanel = new JPanel();
        JPanel btnPanel = new JPanel();
        inputPanel.setLayout(new BorderLayout());

        messageTextField = new JTextField(); // accepts upto 10 characters
        messageTextField.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));

        fileButton = new JButton("File");
        sendButton = new JButton("Send");
        inputPanel.add(messageTextField,
                       BorderLayout.CENTER); // Components Added using Flow Layout
        btnPanel.add(fileButton);
        btnPanel.add(sendButton);
        inputPanel.add(btnPanel, BorderLayout.LINE_END);

        listPanel = new ListPanel<>();
        listPanel.setEnabled(false);

        panel.add(recipientPanel, BorderLayout.PAGE_START);
        panel.add(listPanel.getPane(), BorderLayout.CENTER);
        panel.add(inputPanel, BorderLayout.PAGE_END);
    }

    /**
     * Get the parent panel.
     *
     * @return JPanel that contains the chat panel components.
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Set recipients list.
     *
     * @param names List of texts.
     */
    public void setRecipient(String[] names) {
        if (names.length == 0) {
            recipientNamesTF.setText("");
            return;
        }
        String formattedName = "";
        for (String name : names) {
            formattedName += name + ", ";
        }
        formattedName = formattedName.substring(0, formattedName.length() - 2);
        recipientNamesTF.setText(formattedName);
    }

    /**
     * Clear all the messages in the chat.
     */
    public void clearMessages() {
        listPanel.clear();
    }

    /**
     *
     * @param messages
     */
    public void addMessage(MessagePanel messages) {
        listPanel.add(messages);
    }

    /**
     *
     * @param listener
     */
    public void addSendTextListener(IOnEventParam<String> listener) {
        sendButton.addActionListener(e -> onSend(listener));
        messageTextField.addActionListener(e -> onSend(listener));
    }

    /**
     *
     * @param listener
     */
    public void addSendFileListener(IOnEventParam<String> listener) {
        fileButton.addActionListener(e -> onFileButton(listener));
    }

    /**
     *
     * @param onTyping
     */
    public void setOnTyping(IOnEvent onTyping) {
        messageTextField.addCaretListener(e -> onTyping.signal());
    }

    /**
     * File button pressed event
     */
    private void onFileButton(IOnEventParam<String> listener) {
        int isApproved = clientUI.showFileDialog("Choose an image");
        if (isApproved == JFileChooser.APPROVE_OPTION) {
            File imageFile = clientUI.getSelectedFile();
            listener.signal(imageFile.getAbsolutePath());
        }
    }

    /**
     * Send event from button
     */
    private void onSend(IOnEventParam<String> listener) {
        final var text = messageTextField.getText();
        if (!text.isBlank()) {
            listener.signal(text);
            messageTextField.setText("");
        }
    }
}

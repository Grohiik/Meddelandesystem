package client.boundary.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.io.Serial;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * MessagePanel is the custom panel uses by {@link ListPanel} for rendering messages with user,
 * timestamp and text/images.
 *
 * @author  Pratchaya Khansomboon
 * @version 1.0
 */
public class MessagePanel extends JPanel {
    @Serial
    private static final long serialVersionUID = 6468922715498168398L;
    private JPanel infoPanel;

    private JLabel nameLabel;
    private JLabel timeLabel;

    /**
     * Create MessagePanel that display images.
     *
     * @param time  The formatted sent time.
     * @param name  The name of the sender.
     * @param image The image object to be displayed.
     */
    public MessagePanel(String time, String name, ImageIcon image) {
        this(time, name);
        add(new JLabel(image), BorderLayout.LINE_START);
    }

    /**
     * Create MessagePanel that display texts.
     *
     * @param time The formatted sent time.
     * @param name The name of the sender.
     * @param text The text to be displayed.
     */
    public MessagePanel(String time, String name, String text) {
        this(time, name);
        var textArea = new JTextArea();
        textArea.setText(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);

        add(textArea, BorderLayout.CENTER);
    }

    /**
     * Private constructor for setting time and username and the panels.
     *
     * @param time The formatted time.
     * @param name The name of the sender.
     */
    private MessagePanel(String time, String name) {
        setLayout(new BorderLayout());
        infoPanel = new JPanel();
        infoPanel.setLayout(new FlowLayout(FlowLayout.LEADING));
        infoPanel.setOpaque(false);
        setBackground(Color.WHITE);

        timeLabel = new JLabel(time);
        nameLabel = new JLabel(name);

        infoPanel.add(nameLabel);
        infoPanel.add(timeLabel);
        add(infoPanel, BorderLayout.PAGE_START);
    }
}

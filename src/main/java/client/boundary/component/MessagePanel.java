package client.boundary.component;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * MessagePanel
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class MessagePanel extends JPanel {
    private static final long serialVersionUID = 6468922715498168398L;
    private JPanel infoPanel;

    private JLabel nameLabel;
    private JLabel timeLabel;

    public MessagePanel(String time, String name, ImageIcon image) {
        this(time, name);
        add(new JLabel(image), BorderLayout.LINE_START);
    }

    public MessagePanel(String time, String name, String text) {
        this(time, name);
        var textArea = new JTextArea();
        textArea.setText(text);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setOpaque(false);

        add(textArea, BorderLayout.CENTER);
    }

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

package client.boundary;

import java.awt.BorderLayout;
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

public class ClientUI {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
    final JFileChooser fc = new JFileChooser();

    public ClientUI() {
        JFrame frame = new JFrame("Chat Frame");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400);
        changeLook();

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
        JTextField tf = new JTextField(); // accepts upto 10 characters
        tf.setBorder(BorderFactory.createEmptyBorder(0, 5, 0, 5));
        tf.addActionListener(e -> onEnter());
        tf.addCaretListener(e -> onTyping());

        JButton fileButton = new JButton("File");
        fileButton.addActionListener(e -> onFileButton());
        JButton sendButton = new JButton("Send");
        sendButton.addActionListener(e -> onSendButton());
        panel.add(tf, BorderLayout.CENTER); // Components Added using Flow Layout
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

    private void onFileButton() {
        System.out.println("File!");
    }

    private void onSendButton() {
        System.out.println("Send!");
    }

    private void onTyping() {
        System.out.print(dtf.format(LocalDateTime.now()));
        System.out.println(": Typing...");
    }

    private void onEnter() {
        System.out.println("Enter!");
    }

    private void changeLook() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                 | UnsupportedLookAndFeelException e1) {
        }
    }
}

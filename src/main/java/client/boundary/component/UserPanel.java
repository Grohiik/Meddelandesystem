package client.boundary.component;

import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * UserPanel
 *
 * @author Pratchaya Khansomboon
 * @author Eric Lundin
 * @version 1.0
 */
public class UserPanel extends JPanel {
    private static final long serialVersionUID = 6497655480580447853L;
    private ImageIcon image;
    private String username;

    public UserPanel(ImageIcon image, String username) {
        this.username = username;
        this.image = image;

        setLayout(new FlowLayout(FlowLayout.LEADING));
        add(new JLabel(image));
        add(new JLabel(username));
    }
}
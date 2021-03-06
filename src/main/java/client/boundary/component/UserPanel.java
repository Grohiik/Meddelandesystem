package client.boundary.component;

import java.awt.FlowLayout;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * UserPanel is the custom renderer for displaying the username and user profile image.
 *
 * @author  Pratchaya Khansomboon
 * @author  Eric Lundin
 * @version 1.0
 */
public class UserPanel extends JPanel {
    private static final long serialVersionUID = 6497655480580447853L;

    /**
     * Create UserPanel with image and username.
     *
     * @param image    The image icon object.
     * @param username The username text.
     */
    public UserPanel(ImageIcon image, String username) {
        setLayout(new FlowLayout(FlowLayout.LEADING));
        add(new JLabel(image));
        add(new JLabel(username));
    }
}
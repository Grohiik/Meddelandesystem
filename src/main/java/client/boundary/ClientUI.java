package client.boundary;

import client.boundary.listener.IOnLogin;
import client.boundary.page.LoginPage;
import client.boundary.page.MainPage;
import client.control.ClientController;
import java.awt.Dimension;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
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

    private JFrame frame;

    private MainPage mainPage;
    private LoginPage loginPage;

    /**
     * Create the main gui
     *
     * @param controller Reference to the ClientController
     */
    public ClientUI(ClientController controller) {
        frame = new JFrame("Message Cat");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // frame.setLocation(0, -800); // FIXME: For dual screen only

        changeLook();
        setupFileChooser();

        mainPage = new MainPage(controller);
        loginPage = new LoginPage(controller);

        frame.setVisible(true);
    }

    public void showLogin(IOnLogin onLogin) {
        loginPage.setOnConfirm(onLogin);

        frame.setContentPane(loginPage.getPanel());
        frame.pack();
    }

    public void showMain() {
        frame.setSize(850, 520);
        frame.setMinimumSize(new Dimension(850, 520));

        frame.setContentPane(mainPage.getPanel());
        frame.pack();
    }

    // FIXME: Remove this
    public String getName() {
        return JOptionPane.showInputDialog(frame, "Name: ", "Enter your name.",
                                           JOptionPane.QUESTION_MESSAGE);
    }

    /**
     * Default file chooser dialog
     *
     * @return Status if the there is a file selected
     * @see JFileChooser
     * @since 1.0
     */
    public int showFileDialog() {
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
    public int showFileDialog(String msg) {
        FILE_CHOOSER.setDialogTitle(msg);
        return FILE_CHOOSER.showOpenDialog(frame);
    }

    /**
     * Get the selected file from the file chooser
     *
     * @return The selected file
     */
    public File getSelectedFile() {
        return FILE_CHOOSER.getSelectedFile();
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

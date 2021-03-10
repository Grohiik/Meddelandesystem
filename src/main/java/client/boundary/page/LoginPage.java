package client.boundary.page;

import client.boundary.ClientUI;
import client.boundary.event.IOnLogin;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * LoginPage is the first page the user will see when opening the application.
 *
 * @author  Pratchaya Khansomboon
 * @version 1.0
 */
public class LoginPage {
    private JPanel panel;

    private JTextField nameTextField;
    private JButton chooseImageButton;
    private JButton confirmButton;

    private ClientUI clientUI;

    private IOnLogin onLogin;

    /**
     * Create LoginPage that shows input field and two buttons for different action.
     *
     * @param clientUI The reference to ClientUI for setting and using different actions.
     */
    public LoginPage(ClientUI clientUI) {
        this.clientUI = clientUI;

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));

        chooseImageButton = new JButton("Choose Image");
        chooseImageButton.addActionListener(e -> clientUI.showFileDialog("Choose image profile"));
        nameTextField = new JTextField(16);
        nameTextField.addActionListener(e -> onConfirm());
        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> onConfirm());

        panel.add(chooseImageButton);
        panel.add(nameTextField);
        panel.add(confirmButton);
    }

    /**
     * Get the parent panel.
     *
     * @return JPanel that contains the login page components.
     */
    public JPanel getPanel() {
        return panel;
    }

    /**
     * Set callback event on user confirmation.
     *
     * @param onLogin The callback function with no parameter.
     */
    public void setOnConfirm(IOnLogin onLogin) {
        this.onLogin = onLogin;
    }

    /**
     * Sends a username and filename with a onLogin CallBack.
     */
    private void onConfirm() {
        String username = nameTextField.getText();
        var file = clientUI.getSelectedFile();
        if (username.isBlank() || file == null) return;

        String filename = file.getAbsolutePath();
        onLogin.login(username, filename);
    }
}

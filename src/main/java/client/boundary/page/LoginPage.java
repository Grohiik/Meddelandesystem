package client.boundary.page;

import client.boundary.ClientUI;
import client.boundary.listener.IOnLogin;
import client.control.ClientController;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * LoginView
 *
 * @author Pratchaya Khansomboon
 * @version 1.0
 */
public class LoginPage {
    private JPanel panel;

    private JTextField nameTextField;
    private JButton chooseImageButton;
    private JButton confirmButton;

    private ClientUI clientUI;

    private IOnLogin onLogin;

    public LoginPage(ClientController controller, ClientUI clientUI) {
        this.clientUI = clientUI;

        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));

        chooseImageButton = new JButton("Choose Image");
        chooseImageButton.addActionListener(e -> clientUI.showFileDialog("Choose image profile"));
        chooseImageButton.addActionListener(e -> onChooseImage());
        nameTextField = new JTextField(16);
        nameTextField.addActionListener(e -> onConfirm());
        confirmButton = new JButton("Confirm");
        confirmButton.addActionListener(e -> onConfirm());

        panel.add(chooseImageButton);
        panel.add(nameTextField);
        panel.add(confirmButton);
    }

    public JPanel getPanel() {
        return panel;
    }

    private void onChooseImage() {
        System.out.println("Choosing an image");
    }

    private void onConfirm() {
        String username = nameTextField.getText();
        String filename = clientUI.getSelectedFile().getAbsolutePath();
        if (username.isBlank() || filename.isBlank()) return;

        onLogin.login(username, filename);
    }

    public void setOnConfirm(IOnLogin onLogin) {
        this.onLogin = onLogin;
    }
}

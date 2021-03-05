package client.boundary.page;

import client.boundary.ClientUI;
import client.boundary.event.IOnLogin;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * LoginView
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

    public LoginPage(ClientUI clientUI) {
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
        var file = clientUI.getSelectedFile();
        if (username.isBlank() || file == null) return;

        String filename = file.getAbsolutePath();
        onLogin.login(username, filename);
    }

    public void setOnConfirm(IOnLogin onLogin) {
        this.onLogin = onLogin;
    }
}

package client.boundary.page;

import client.boundary.listener.IOnEvent;
import client.boundary.listener.IOnLogin;
import client.control.ClientController;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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

    private IOnLogin onLogin;

    public LoginPage(ClientController controller) {
        panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEADING));

        chooseImageButton = new JButton("Choose Image");
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
        String filename = "/Users/k/Downloads/unnamed.jpg";
        if (username.isBlank() || filename.isBlank()) return;

        onLogin.login(username, filename);
    }

    public void setOnConfirm(IOnLogin onLogin) {
        this.onLogin = onLogin;
    }
}

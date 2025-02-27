package org.example;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.io.IOException;

public class LoginController {
    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblMessage;

    @FXML
    private void handleLogin() throws IOException {
        String username = txtUsername.getText();
        String password = txtPassword.getText();

        if (username.equals("admin") && password.equals("password")) {
            Main.switchScene("MovieCatalog.fxml");
        } else {
            lblMessage.setText("Invalid credentials. Try again.");
        }
    }
}

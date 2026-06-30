package com.messenger;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    private Client client;

    @FXML
    private void login() {

        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Enter Username");
            alert.showAndWait();
            return;
        }

        if (password.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Login");
            alert.setHeaderText(null);
            alert.setContentText("Enter Password");
            alert.showAndWait();
            return;
        }

        client = new Client();

        System.out.println("Connecting to server...");

        boolean connected = client.connect(username, password);
System.out.println("CONNECTED = " + connected);

if (!connected) {
    Alert alert = new Alert(Alert.AlertType.ERROR);
    alert.setTitle("Login Failed");
    alert.setHeaderText(null);
    alert.setContentText("Login failed.");
    alert.showAndWait();
    return;
}

        try {

            FXMLLoader loader =
                    new FXMLLoader(getClass().getResource("/view/Home.fxml"));

            Parent root = loader.load();

            HomeController controller = loader.getController();
            controller.setClient(client);

            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("BlueLink Messenger");
            stage.show();

            System.out.println("Home page loaded successfully.");

        } catch (Exception e) {
            e.printStackTrace();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("FXML Error");
            alert.setHeaderText("Unable to open Home page");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }
}
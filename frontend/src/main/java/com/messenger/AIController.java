package com.messenger;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class AIController {

    @FXML
    private VBox chatBox;

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private TextField questionField;

    @FXML
    private Button sendButton;

    private Client client;

    public void setClient(Client client) {
        this.client = client;
    }

    @FXML
    private void sendMessage() {

        String prompt = questionField.getText().trim();

        if (prompt.isEmpty()) {
            return;
        }

        addUserBubble(prompt);

        if (client != null) {

            client.askAI(prompt);

            new Thread(() -> {

                String response = client.receiveAIResponse();

                Platform.runLater(() -> {
                    addAIBubble(response);
                });

            }).start();

        } else {

            addAIBubble("Client is not connected.");

        }

        questionField.clear();
    }

    private void addUserBubble(String text) {

        Label label = new Label("You: " + text);

        label.setWrapText(true);

        label.setStyle(
                "-fx-background-color:#DCF8C6;" +
                "-fx-padding:10;" +
                "-fx-background-radius:12;");

        chatBox.getChildren().add(label);

        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }

    private void addAIBubble(String text) {

        Label label = new Label("BlueLink AI: " + text);

        label.setWrapText(true);

        label.setStyle(
                "-fx-background-color:#EEEEEE;" +
                "-fx-padding:10;" +
                "-fx-background-radius:12;");

        chatBox.getChildren().add(label);

        Platform.runLater(() -> scrollPane.setVvalue(1.0));
    }
}
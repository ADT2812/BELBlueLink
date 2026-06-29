package com.messenger;

import java.io.File;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Side;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.control.Hyperlink;
import java.awt.Desktop;

public class HomeController {

    @FXML
    private ListView<String> contactsListView;

    @FXML
    private TextField messageField;

    @FXML
    private TextField searchField;

    @FXML
    private Label headerTitleLabel;

    @FXML
    private Label headerSubtitleLabel;

    @FXML
    private VBox messagesContainer;

    @FXML
    private VBox welcomePane;

    @FXML
    private VBox chatPane;

    @FXML
    private Button aiAssistantButton;

    @FXML
    private Button SettingButton;

    @FXML
    private Button attachButton;

    @FXML
    private Button sendButton;

    @FXML
private Label chatUserLabel;

@FXML
private Label chatStatusLabel;

@FXML
private ScrollPane chatScrollPane;

@FXML
private Button callButton;

@FXML
private Button moreButton;


    private Client client;

    private String selectedUser = "";
    private ObservableList<String> allContacts =
        FXCollections.observableArrayList(
                "Aditi",
                "Rudraksh",
                "Madhumitha"
        );

    private String currentTime() {
    return java.time.LocalTime.now()
            .format(java.time.format.DateTimeFormatter.ofPattern("hh:mm a"));
}

    public void setClient(Client client) {

        this.client = client;

        contactsListView.getItems().clear();
        contactsListView.setItems(allContacts);
        contactsListView.getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldValue, newValue) -> {

                    if (newValue == null)
                        return;

                    selectedUser = newValue;
                    chatUserLabel.setText(newValue);
chatStatusLabel.setText("Online");

                    if (welcomePane != null) {
                        welcomePane.setVisible(false);
                        welcomePane.setManaged(false);
                    }

                    if (chatPane != null) {
                        chatPane.setVisible(true);
                        chatPane.setManaged(true);
                    }

                    messagesContainer.getChildren().clear();
                });

        startReceiver();
    }

    @FXML
private void initialize() {

    // Show welcome screen initially
    chatPane.setVisible(false);
    chatPane.setManaged(false);

    welcomePane.setVisible(true);
    welcomePane.setManaged(true);

    

    // Buttons
    sendButton.setOnAction(e -> sendMessage());
    attachButton.setOnAction(e -> attachFile());
    aiAssistantButton.setOnAction(e -> openAIAssistant());
    SettingButton.setOnAction(e -> logout());

    messageField.setOnAction(e -> sendMessage());

    // Live search
    searchField.textProperty().addListener((obs, oldValue, newValue) -> {

    ObservableList<String> filtered =
            FXCollections.observableArrayList();

    for(String contact : allContacts){

        if(contact.toLowerCase().startsWith(newValue.toLowerCase())){

            filtered.add(contact);

        }

    }

    contactsListView.setItems(filtered);

});
}

    @FXML
    private void sendMessage() {

        String text = messageField.getText().trim();

        if (text.isEmpty())
            return;

        if (selectedUser.isEmpty()) {
            System.out.println("Select a user first.");
            return;
        }

        client.sendMessage(selectedUser, text);

        addOutgoingMessage(text);
        messagesContainer.layout();

        messageField.clear();
    }

    @FXML
    private void attachFile() {

        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select File");

        File file = chooser.showOpenDialog(null);

        if (file == null)
            return;

        if (selectedUser.isEmpty()) {
            System.out.println("Select a user first.");
            return;
        }

        client.sendFile(selectedUser, file, false);

        addOutgoingMessage("[File] " + file.getName());
        messagesContainer.layout();
    }

    @FXML
    private void logout() {

        if (client != null)
            client.disconnect();

        System.exit(0);
    }
    @FXML
public void openAIAssistant() {

    System.out.println("AI button clicked");

    try {

        FXMLLoader loader =
                new FXMLLoader(getClass().getResource("/view/AI.fxml"));

        Parent root = loader.load();

        AIController controller = loader.getController();
        controller.setClient(client);

        Stage stage = new Stage();
        stage.setTitle("BlueLink AI");
        stage.setScene(new Scene(root));
        stage.show();

    } catch (Exception e) {
        e.printStackTrace();
    }
} 

    @FXML
private void openSettings() {

    ContextMenu menu = new ContextMenu();

    MenuItem profile = new MenuItem("Profile");
    MenuItem darkMode = new MenuItem("Dark Mode");
    MenuItem downloads = new MenuItem("Downloads");
    MenuItem notifications = new MenuItem("Notifications");
    MenuItem logout = new MenuItem("Logout");

    logout.setOnAction(e -> logout());

    menu.getItems().addAll(
            profile,
            darkMode,
            downloads,
            notifications,
            logout
    );

    menu.show(SettingButton, Side.BOTTOM, 0, 0);
}

    private void startReceiver() {

    Thread thread = new Thread(() -> {

        while (true) {

            String msg = client.receiveMessage();

            if (msg == null)
                break;

            Platform.runLater(() -> {

                try {

                    if (msg.startsWith("FILE:")) {

                        String[] parts = msg.split(":", 6);

                        String sender = parts[1];
                        String fileName = parts[2];
                        String base64 = parts[5];

                        String path = FileManager.saveIncomingFile(
                                sender,
                                fileName,
                                base64);

                        addIncomingFile(fileName, path);

                    } else {

                        addIncomingMessage(msg);

                    }

                    messagesContainer.layout();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            });

        }

    });

    thread.setDaemon(true);
    thread.start();
}

private void addIncomingFile(String fileName, String path) {

    Hyperlink link = new Hyperlink("📎 " + fileName);

    link.setOnAction(e -> {

        try {

            java.awt.Desktop.getDesktop().open(new File(path));

        } catch (Exception ex) {
            ex.printStackTrace();
        }

    });

    VBox bubble = new VBox(link);

    bubble.setStyle(
            "-fx-background-color:white;" +
            "-fx-padding:10;" +
            "-fx-background-radius:15;"
    );

    HBox box = new HBox(bubble);

    messagesContainer.getChildren().add(box);

    Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
}

    private void addIncomingMessage(String text) {

    if (text.startsWith("FILE:")) {

        String[] parts = text.split(":", 6);

        String sender = parts[1];
        String fileName = parts[2];
        String base64 = parts[5];

        try {

            String path = FileManager.saveIncomingFile(sender, fileName, base64);

            javafx.scene.control.Hyperlink link =
                    new javafx.scene.control.Hyperlink("📎 " + fileName);

            link.setOnAction(e -> {

                try {
                    java.awt.Desktop.getDesktop().open(new java.io.File(path));
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            });

            Label time = new Label(currentTime());
            time.setStyle("-fx-font-size:10; -fx-text-fill:gray;");

            VBox bubble = new VBox(link, time);

            HBox box = new HBox(bubble);

            messagesContainer.getChildren().add(box);

            Platform.runLater(() -> chatScrollPane.setVvalue(1.0));

        }

        catch (Exception e) {

            e.printStackTrace();

        }

        return;
    }

    Label label = new Label(text);

    label.setWrapText(true);

    label.setStyle(
            "-fx-background-color:white;" +
            "-fx-background-radius:15;" +
            "-fx-padding:10;" +
            "-fx-border-color:#D9E3F0;" +
            "-fx-border-radius:15;"
    );

    Label time = new Label(currentTime());

    time.setStyle("-fx-font-size:10; -fx-text-fill:gray;");

    VBox bubble = new VBox(label, time);

    HBox box = new HBox(bubble);

    messagesContainer.getChildren().add(box);

    Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
}

    private void addOutgoingMessage(String text) {

        Label label = new Label(text);

        label.setWrapText(true);

        label.setStyle(
                "-fx-background-color:#005BAC;" +
                "-fx-text-fill:white;" +
                "-fx-background-radius:15;" +
                "-fx-padding:10;"
        );

        HBox spacer = new HBox();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        Label time = new Label(currentTime());
time.setStyle("-fx-font-size:10; -fx-text-fill:gray;");

VBox bubble = new VBox(label, time);

HBox box = new HBox();
box.getChildren().addAll(spacer, bubble);

messagesContainer.getChildren().add(box);
Platform.runLater(() -> chatScrollPane.setVvalue(1.0));
    }

}
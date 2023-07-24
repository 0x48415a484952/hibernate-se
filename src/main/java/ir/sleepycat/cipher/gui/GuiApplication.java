package ir.sleepycat.cipher.gui;

import ir.sleepycat.cipher.crypto.AESDecryption;
import ir.sleepycat.cipher.crypto.AESEncryption;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class GuiApplication extends Application {
    private final TextArea messageTextArea = new TextArea();

    private final TextArea encryptedMessageTextArea = new TextArea();

    private final TextArea resultTextArea = new TextArea();
    private final TextArea passwordField = new TextArea();
    private final Button encryptButton = new Button("Encrypt");
    private final Button decryptButton = new Button("Decrypt");

    @Override
    public void start(Stage primaryStage) {
        VBox root = new VBox();
        root.setSpacing(10);

        Label messageLabel = new Label("Enter the message:");
        root.getChildren().addAll(messageLabel, messageTextArea);

        Label passwordLabel = new Label("Enter the password:");
        root.getChildren().addAll(passwordLabel, passwordField);

        Label encryptedMessageLabel = new Label("Enter the encrypted message:");
        root.getChildren().addAll(encryptedMessageLabel, encryptedMessageTextArea);


        encryptButton.setOnAction(e -> {
            String password = passwordField.getText();
            String originalText = messageTextArea.getText();

            try {
                String encryptedText = AESEncryption.encrypt(originalText, password);
                resultTextArea.setText("Encrypted: " + encryptedText);
            } catch (Exception ex) {
                resultTextArea.setText("Error: " + ex.getMessage());
            }
        });

        decryptButton.setOnAction(e -> {
            String password = passwordField.getText();
            String encryptedText = encryptedMessageTextArea.getText();

            try {
                String decryptedText = AESDecryption.decrypt(encryptedText, password);
                resultTextArea.setText("Decrypted: " + decryptedText);
            } catch (Exception ex) {
                resultTextArea.setText("Error: " + ex.getMessage());
            }
        });

        root.getChildren().addAll(encryptButton, decryptButton, resultTextArea);

        Scene scene = new Scene(root, 400, 300);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Encryption/Decryption Tool");
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

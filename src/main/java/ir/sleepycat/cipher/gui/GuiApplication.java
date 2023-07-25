package ir.sleepycat.cipher.gui;

import ir.sleepycat.cipher.crypto.AESDecryption;
import ir.sleepycat.cipher.crypto.AESEncryption;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Objects;



public class GuiApplication extends Application {
    private final TextArea messageTextArea = new TextArea();
    private final TextArea encryptedMessageTextArea = new TextArea();
    private final Button encryptButton = createStyledButton("Encrypt");
    private final Button decryptButton = createStyledButton("Decrypt");
    private final Label resultLabel = createStyledLabel("Result:");
    private final TextArea resultTextArea = new TextArea();

    @Override
    public void start(Stage primaryStage) {
        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20));

        // Define flexible column constraints
        ColumnConstraints column1 = new ColumnConstraints();
        column1.setHgrow(Priority.ALWAYS);
        gridPane.getColumnConstraints().addAll(column1);

        // Define flexible row constraints
        RowConstraints row1 = new RowConstraints();
        RowConstraints row2 = new RowConstraints();
        row2.setVgrow(Priority.ALWAYS);
        gridPane.getRowConstraints().addAll(row1, row2);

        gridPane.add(createStyledLabel("Enter the message:"), 0, 0);
        gridPane.add(messageTextArea, 0, 1);
        gridPane.add(createStyledLabel("Enter the password:"), 0, 2);

        // Use a custom PasswordField with a ToggleButton for the eye button
        PasswordField passwordField = createPasswordFieldWithEyeButton();
        gridPane.add(passwordField, 0, 3);

        gridPane.add(createStyledLabel("Enter the encrypted message:"), 0, 4);
        gridPane.add(encryptedMessageTextArea, 0, 5);

        // Use HBox to align buttons horizontally
        HBox buttonsBox = new HBox(10, encryptButton, decryptButton);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox resultBox = new VBox(10, resultLabel, resultTextArea);
        resultBox.setAlignment(Pos.CENTER);

        VBox root = new VBox(20, gridPane, buttonsBox, resultBox);
        root.setPadding(new Insets(20));
        root.setFillWidth(true);
        root.setAlignment(Pos.CENTER);

        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Encryption/Decryption Tool");
        primaryStage.show();

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
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("styled-button");
        return button;
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("styled-label");
        return label;
    }


    private PasswordField createPasswordFieldWithEyeButton() {
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");

        Region rightPadding = new Region();
        rightPadding.setMinWidth(15); // Add some padding between password field and eye icon

        Label eyeIcon = new Label("\uD83D\uDC41"); // Unicode for an eye symbol: U+1F441 (you can use any other Unicode eye icon)
        eyeIcon.setFont(Font.font(18));

        // Use Tooltip to show "Show Password" or "Hide Password" when hovering over the eye icon
        Tooltip tooltip = new Tooltip("Show Password");
        eyeIcon.setTooltip(tooltip);

        StackPane eyeIconPane = new StackPane(eyeIcon);
        eyeIconPane.setMinSize(Control.USE_PREF_SIZE, Control.USE_PREF_SIZE); // Set fixed size for the StackPane

        eyeIconPane.setOnMousePressed(e -> {
            if (passwordField.getFont().equals(Font.getDefault())) {
                passwordField.setFont(Font.font(0)); // Set font size to 0 to hide the asterisks
                tooltip.setText("Hide Password");
            } else {
                passwordField.setFont(Font.getDefault()); // Reset font size to default to show the password
                tooltip.setText("Show Password");
            }
        });

        GridPane gridPane = new GridPane();
        gridPane.setHgap(5);
        gridPane.add(passwordField, 0, 0);
        gridPane.add(rightPadding, 1, 0);
        gridPane.add(eyeIconPane, 2, 0);

        // Apply some styling to the password field container
//        gridPane.getStyleClass().add("password-field-container");

        return passwordField;
    }


    public static void main(String[] args) {
        launch(args);
    }
}

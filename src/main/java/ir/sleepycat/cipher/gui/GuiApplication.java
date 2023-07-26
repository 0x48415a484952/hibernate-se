package ir.sleepycat.cipher.gui;

import ir.sleepycat.cipher.crypto.AESDecryption;
import ir.sleepycat.cipher.crypto.AESEncryption;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.Objects;



public class GuiApplication extends Application {
    private static final String PASSWORD_FIELD_ID ="passwordField";
    private final TextArea messageTextArea = new TextArea();
    private final TextArea encryptedMessageTextArea = new TextArea();
    private final Button encryptButton = createStyledButton("Encrypt");
    private final Button decryptButton = createStyledButton("Decrypt");
    private final Label resultLabel = createStyledLabel("Result:");
    private final TextArea resultTextArea = new TextArea();

    private GridPane passwordLayout;

    private String encryptedText; // Variable to store the encrypted text without the "Encrypted: " label

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
        passwordLayout = createPasswordFieldWithEyeButton();
        gridPane.add(passwordLayout, 0, 3);

        gridPane.add(createStyledLabel("Enter the encrypted message:"), 0, 4);
        gridPane.add(encryptedMessageTextArea, 0, 5);

        // Use HBox to align buttons horizontally
        HBox buttonsBox = new HBox(10, encryptButton, decryptButton);
        buttonsBox.setAlignment(Pos.CENTER);

        VBox resultBox = new VBox(10, resultLabel, resultTextArea);
        resultBox.setAlignment(Pos.CENTER);

        // Create a new button to save encrypted text to a file
        Button saveToFileButton = createStyledButton("Save Encrypted Text to File");
        saveToFileButton.setOnAction(e -> saveEncryptedTextToFile());

        // Add the new button to the layout
        buttonsBox.getChildren().add(saveToFileButton);


        VBox root = new VBox(20, gridPane, buttonsBox, resultBox, saveToFileButton);


        Scene scene = new Scene(root, 500, 500);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Encryption/Decryption Tool");
        primaryStage.show();

        encryptButton.setOnAction(e -> {
            CustomPasswordField passwordField = (CustomPasswordField) passwordLayout.getChildren().stream().filter(x-> {
                return x.getId().equals(PASSWORD_FIELD_ID);
            }).findFirst().orElse(null);
            assert passwordField != null;
            String password = passwordField.getText();
            String originalText = messageTextArea.getText();

            try {
                encryptedText = AESEncryption.encrypt(originalText, password);
                resultTextArea.setText("Encrypted: " + encryptedText);
            } catch (Exception ex) {
                resultTextArea.setText("Error: Failed to encrypt the message.");
            }
        });

        decryptButton.setOnAction(e -> {
            CustomPasswordField passwordField = (CustomPasswordField) passwordLayout.getChildren().stream()
                    .filter(x->x.getId().equals(PASSWORD_FIELD_ID)).findFirst().orElse(null);
            assert passwordField != null;
            String password = passwordField.getText();
            encryptedText = encryptedMessageTextArea.getText();

            try {
                String decryptedText = AESDecryption.decrypt(encryptedText, password);
                resultTextArea.setText("Decrypted: " + decryptedText);
            } catch (Exception ex) {
                resultTextArea.setText("Error: Failed to decrypt the message.");
            }
        });




        // Use VBox to stack the elements vertically
        root.setPadding(new Insets(20));
        root.setFillWidth(true);
        root.setAlignment(Pos.CENTER);
    }

    // Method to save encrypted text to a file
    private void saveEncryptedTextToFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        File file = fileChooser.showSaveDialog(null);

        if (file != null) {
            try (PrintWriter writer = new PrintWriter(file)) {
                // Save the encrypted text to the file
                writer.write(encryptedText);
                writer.flush();
            } catch (IOException ex) {
                System.err.println("Error: Failed to save the file.");
            }
        }
    }

    private Button createStyledButton(String text) {
        Button button = new Button(text);
        button.getStyleClass().add("styled-button");
        return button;
    }

    private Label createStyledLabel(String text) {
        Label label = new Label(text);
        label.getStyleClass().add("styled-label");
        label.setWrapText(true); // Allow label text to wrap if it's too long
        return label;
    }


    private GridPane createPasswordFieldWithEyeButton() {
        CustomPasswordField passwordField = new CustomPasswordField();
        passwordField.setPromptText("Enter password");
        passwordField.setId(PASSWORD_FIELD_ID);

        Region rightPadding = new Region();
        rightPadding.setMinWidth(15); // Add some padding between password field and eye icon

        Label eyeIcon = new Label("\uD83D\uDC41"); // Unicode for an eye symbol: U+1F441 (you can use any other Unicode eye icon)
        eyeIcon.setFont(Font.font(18));

        // Use Tooltip to show "Show Password" or "Hide Password" when hovering over the eye icon
        Tooltip tooltip = new Tooltip("Show Password");
        eyeIcon.setTooltip(tooltip);

        StackPane eyeIconPane = new StackPane(eyeIcon);
        eyeIconPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE); // Set fixed size for the StackPane

        eyeIconPane.setOnMousePressed(e -> {
            passwordField.togglePasswordMode();
            tooltip.setText(passwordField.passwordMode ? "Show Password" : "Hide Password");
        });


        passwordLayout = new GridPane();
        passwordLayout.setHgap(5);
        passwordLayout.add(passwordField, 0, 0);
        passwordLayout.add(rightPadding, 1, 0);
        passwordLayout.add(eyeIconPane, 2, 0);

        // Set the password field to grow horizontally with the window
        GridPane.setHgrow(passwordField, Priority.ALWAYS);
        GridPane.setHalignment(passwordField, HPos.CENTER);

        // Apply some styling to the password field container
//        gridPane.getStyleClass().add("password-field-container");

        return passwordLayout;
    }


    public static void main(String[] args) {
        launch(args);
    }

    private static class CustomPasswordField extends TextField {
        private boolean passwordMode = false;
        private String actualText = "";
        private String maskText = "";

        public CustomPasswordField() {
            setPromptText("Enter password");

            // Add a listener to the textProperty()
            textProperty().addListener((observable, oldValue, newValue) -> {
                // Only update caret position when passwordMode is true (masked password)
                if (passwordMode) {
                    Platform.runLater(() -> positionCaret(getText().length()));
                }
            });
        }

        public void togglePasswordMode() {
            passwordMode = !passwordMode;
            if (passwordMode) {
                maskText = generatePasswordMask(actualText);
                setText(maskText);
            } else {
                setText(actualText);
            }
            Platform.runLater(() -> positionCaret(getText().length()));
        }

        private String generatePasswordMask(String originalText) {
            return "*".repeat(originalText.length());
        }

        @Override
        public void replaceText(int start, int end, String text) {
            if (passwordMode) {
                String newText = maskText.substring(0, start) + text + maskText.substring(Math.min(end, maskText.length()));
                int actualStart = Math.min(start, actualText.length());
                int actualEnd = Math.min(end, actualText.length());
                actualText = actualText.substring(0, actualStart) + text + actualText.substring(actualEnd);
                maskText = generatePasswordMask(newText);
                setText(maskText);
            } else {
                int actualEnd = Math.min(end, actualText.length());
                actualText = actualText.substring(0, start) + text + actualText.substring(actualEnd);
                super.replaceText(start, actualEnd, text);
            }
        }

        @Override
        public void replaceSelection(String text) {
            if (passwordMode) {
                int start = getSelection().getStart();
                int end = getSelection().getEnd();
                int actualStart = Math.min(start, maskText.length());
                int actualEnd = Math.min(end, maskText.length());
                String newText = maskText.substring(0, actualStart) + text + maskText.substring(actualEnd);
                maskText = generatePasswordMask(newText);
                setText(maskText);
                selectRange(start, start + text.length());
            } else {
                int start = getSelection().getStart();
                int end = getSelection().getEnd();
                int actualStart = Math.min(start, actualText.length());
                int actualEnd = Math.min(end, actualText.length());
                actualText = actualText.substring(0, actualStart) + text + actualText.substring(actualEnd);
                super.replaceSelection(text);
            }
        }
    }


    private void addEncryptedTextFromFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Add Encrypted Text from File");

        // Set the initial directory (optional)
        File initialDirectory = new File(System.getProperty("user.home"));
        fileChooser.setInitialDirectory(initialDirectory);

        // Set the file extension filter (optional)
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Text files (*.txt)", "*.txt");
        fileChooser.getExtensionFilters().add(extFilter);

        // Show the file open dialog
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                StringBuilder encryptedText = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    encryptedText.append(line).append("\n");
                }

                CustomPasswordField passwordField = (CustomPasswordField) passwordLayout.getChildren()
                        .stream().filter(x -> x.getId().equals(PASSWORD_FIELD_ID)).findFirst().orElse(null);

                assert passwordField != null;
                String password = passwordField.getText();

                String decryptedText = AESDecryption.decrypt(encryptedText.toString(), password);
                resultTextArea.setText("Decrypted: " + decryptedText);
            } catch (IOException e) {
                System.err.println("Error reading encrypted text from file: " + e.getMessage());
            } catch (Exception ex) {
                resultTextArea.setText("Error: Failed to decrypt the message.");
            }
        }
    }


}

package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable; // Import baru
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import util.DataStore;
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

// Tambahkan "implements Initializable"
public class RegisterController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;       // Kolom Password (***** )
    @FXML private TextField passwordVisibleField;    // Kolom Password (abcde )

    // CSS Style
    private final String STYLE_NORMAL = "-fx-background-color: #e0e0e0; -fx-background-radius: 8; -fx-border-width: 0;";
    private final String STYLE_ERROR = "-fx-background-color: #e0e0e0; -fx-background-radius: 8; -fx-border-color: red; -fx-border-width: 2; -fx-border-radius: 8;";

    // Method ini otomatis jalan saat layar dibuka
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // PENTING: Sinkronkan isi kedua kolom secara real-time
        // Apa yang diketik di PasswordField akan muncul di VisibleField, dan sebaliknya.
        passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());
    }

    // Aksi Tombol Mata (👁)
    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        if (passwordField.isVisible()) {
            // Ubah ke Mode Terlihat
            passwordField.setVisible(false);
            passwordField.setManaged(false); // Supaya layout tidak berantakan
            
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
        } else {
            // Ubah ke Mode Tersembunyi (*****)
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
        }
    }

    @FXML
    private void handleRegister(ActionEvent event) throws IOException {
        String user = usernameField.getText();
        String pass = passwordField.getText(); // Ambil dari passwordField saja (karena sudah sinkron)
        boolean isValid = true;
        StringBuilder errorMsg = new StringBuilder();

        // 1. Reset Style (Reset kedua kolom password supaya aman)
        usernameField.setStyle(STYLE_NORMAL);
        passwordField.setStyle(STYLE_NORMAL);
        passwordVisibleField.setStyle(STYLE_NORMAL);

        // 2. Validasi Username (Min 5)
        if (user.length() < 5) {
            usernameField.setStyle(STYLE_ERROR);
            errorMsg.append("- Username minimal 5 karakter.\n");
            isValid = false;
        }

        // 3. Validasi Password (Min 8)
        if (pass.length() < 8) {
            // Merahkan kedua kolom (karena kita tidak tahu user sedang liat yang mana)
            passwordField.setStyle(STYLE_ERROR);
            passwordVisibleField.setStyle(STYLE_ERROR);
            
            errorMsg.append("- Password minimal 8 karakter.\n");
            isValid = false;
        }

        if (!isValid) {
            showAlert("Registrasi Gagal", errorMsg.toString());
            return;
        }

        // --- Sukses ---
        User newUser = new User(user, user, pass); 
        DataStore.addUser(newUser);
        
        showAlert("Berhasil", "Akun berhasil dibuat! Silakan Login.");
        handleToLogin(event); 
    }

    @FXML
    private void handleToLogin(ActionEvent event) throws IOException {
        Parent loginPage = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(loginPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
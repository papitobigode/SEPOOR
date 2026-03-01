package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import util.DataStore; // Pastikan Import ini ada
import model.User;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private TextField passwordVisibleField;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if (passwordVisibleField != null && passwordField != null) {
            passwordVisibleField.textProperty().bindBidirectional(passwordField.textProperty());
        }
    }

    @FXML
    private void togglePasswordVisibility(ActionEvent event) {
        if (passwordField.isVisible()) {
            passwordField.setVisible(false);
            passwordField.setManaged(false);
            passwordVisibleField.setVisible(true);
            passwordVisibleField.setManaged(true);
        } else {
            passwordField.setVisible(true);
            passwordField.setManaged(true);
            passwordVisibleField.setVisible(false);
            passwordVisibleField.setManaged(false);
        }
    }

    // --- BAGIAN UTAMA YANG DIUBAH ---
    @FXML
    private void handleLogin(ActionEvent event) {
        String user = usernameField.getText();
        String pass = passwordField.getText();

        User validUser = DataStore.validateUser(user, pass);

        if (validUser != null) {
            // LOGIN BERHASIL
            try {
                // [PENTING] SIMPAN USER KE DATASTORE
                // Ini langkah kunci agar Dashboard & Profile bisa baca data user.
                DataStore.currentUser = validUser; 
                
                System.out.println("Login sukses! User: " + validUser.getNama());

                // Load Halaman Dashboard
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/dashboard.fxml"));
                Parent root = loader.load();

                // [DIHAPUS] Baris ini tidak perlu lagi karena Dashboard otomatis baca DataStore:
                // dashboardController.setUsername(validUser.getNama()); 

                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException e) {
                e.printStackTrace();
                showAlert("Error", "Gagal memuat halaman Dashboard.");
            }
            
        } else {
            showAlert("Login Gagal", "Username atau Password salah/tidak ditemukan.");
        }
    }

    @FXML
    private void handleToRegister(ActionEvent event) throws IOException {
        Parent registerPage = FXMLLoader.load(getClass().getResource("/view/register.fxml"));
        Scene scene = new Scene(registerPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    @FXML
    private void handleForgotPassword(ActionEvent event) throws IOException {
        Parent resetPage = FXMLLoader.load(getClass().getResource("/view/reset_password.fxml"));
        Scene scene = new Scene(resetPage);
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(scene);
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
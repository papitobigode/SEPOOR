package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;
import java.io.IOException;

public class ResetPasswordController {

    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    private void handleResetPassword(ActionEvent event) throws IOException {
        String pass = newPasswordField.getText();
        String confirm = confirmPasswordField.getText();

        // Validasi Input Kosong
        if (pass.isEmpty() || confirm.isEmpty()) {
            showAlert("Error", "Mohon isi semua kolom password.");
            return;
        }

        // Validasi Kesesuaian Password
        if (!pass.equals(confirm)) {
            showAlert("Error", "Password dan Konfirmasi Password tidak cocok!");
            return;
        }

        // Jika Sukses
        // (Di aplikasi nyata, di sini kita update database user terkait)
        showAlert("Sukses", "Password berhasil diubah! Silakan login kembali.");
        
        // Kembali ke halaman Login
        handleBackToLogin(event);
    }

    @FXML
    private void handleBackToLogin(ActionEvent event) throws IOException {
        Parent loginPage = FXMLLoader.load(getClass().getResource("/view/login.fxml"));
        Scene scene = new Scene(loginPage);
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

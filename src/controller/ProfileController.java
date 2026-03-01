package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import util.DataStore; // [PENTING] Import DataStore
import java.io.File;
import java.io.IOException;

public class ProfileController {

    @FXML private Circle profileCircle; // Lingkaran Foto
    @FXML private Label usernameLabel;

    // --- 1. INISIALISASI (PENTING) ---
    // Method ini otomatis jalan saat halaman dibuka
    @FXML
    public void initialize() {
        // Cek apakah ada user yang login di DataStore
        if (DataStore.currentUser != null) {
            
            // A. BINDING USERNAME
            // Agar label otomatis mengikuti data User di DataStore
            usernameLabel.textProperty().bind(DataStore.currentUser.usernameProperty());

            // B. LOAD FOTO PROFIL (Jika ada)
            // Cek apakah user ini punya riwayat foto sebelumnya
            String path = DataStore.currentUser.getImagePath();
            
            if (path != null && !path.isEmpty()) {
                // Jika ada, pasang fotonya ke lingkaran
                Image image = new Image(path);
                profileCircle.setFill(new ImagePattern(image));
            }
        }
    }

    // --- 2. GANTI FOTO PROFIL ---
    @FXML
    private void handleChangePhoto(MouseEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Pilih Foto Profil");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);

        if (selectedFile != null) {
            // Ubah file menjadi URL String
            String imagePath = selectedFile.toURI().toString();
            
            // A. Update Tampilan (UI)
            Image image = new Image(imagePath);
            profileCircle.setFill(new ImagePattern(image));
            
            // B. [PENTING] SIMPAN KE DATASTORE (MODEL)
            // Agar saat pindah halaman, fotonya tidak hilang
        if (DataStore.currentUser != null) {
            DataStore.currentUser.setImagePath(imagePath);
            
            // [TAMBAHAN BARU] 
            // Perintahkan DataStore untuk menyimpan perubahan ini ke file text
            DataStore.updateUserData(); 
            
            System.out.println("Foto disimpan permanen.");
}
        }
    }

    // --- 3. LOGOUT ---
    @FXML
    private void handleLogout(ActionEvent event) {
        try {
            // Hapus sesi user saat ini
            DataStore.currentUser = null; 

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            
            System.out.println("Logout berhasil.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- NAVIGASI BAWAH (Disederhanakan) ---
    // Kita tidak perlu lagi oper-operan data (setUser) karena sudah ada DataStore
    
    @FXML
    private void handleMenuBeranda(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleMenuTiket(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/ticket.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleMenuProfil(MouseEvent event) {
        // Sedang aktif
    }
}
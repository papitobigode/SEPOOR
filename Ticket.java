package controller;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.ImagePattern; // [BARU] Untuk isi lingkaran dengan gambar
import javafx.scene.shape.Circle;     // [BARU] Ganti ImageView jadi Circle
import javafx.stage.Stage;
import util.DataStore; 

public class DashboardController {

    @FXML 
    private Label welcomeLabel;

    // [UPDATE] Ubah dari ImageView menjadi Circle
    // Pastikan di SceneBuilder, benda itu adalah 'Circle', dan fx:id="profileImage"
    @FXML 
    private Circle profileImage; 

    @FXML
    public void initialize() {
        if (DataStore.currentUser != null) {
            
            // 1. TAMPILKAN NAMA
            welcomeLabel.textProperty().bind(DataStore.currentUser.usernameProperty());

            // 2. TAMPILKAN FOTO (Logic untuk Circle)
            String path = DataStore.currentUser.getImagePath();
            if (path != null && !path.isEmpty()) {
                try {
                    // Cara pasang gambar di Circle itu pakai 'setFill', bukan 'setImage'
                    Image image = new Image(path);
                    profileImage.setFill(new ImagePattern(image)); 
                } catch (Exception e) {
                    System.out.println("Gagal memuat gambar: " + e.getMessage());
                }
            }
        }
    }

    // --- NAVIGASI ---

    @FXML
    private void handleMenuBeranda(MouseEvent event) {
        System.out.println("Sudah di Beranda");
    }

    @FXML
    private void handleMenuTiket(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/ticket.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMenuProfil(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/profile.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handlePesanTiket(ActionEvent event) {
        try {
            // Pindah ke halaman Booking
            Parent root = FXMLLoader.load(getClass().getResource("/view/booking.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
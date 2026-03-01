package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label; // Jangan lupa import Label
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.Station;
import util.DataStore;

import java.io.IOException;

public class BookingController {

    @FXML private TextField inputAsal; 
    @FXML private TextField inputTujuan;
    @FXML private DatePicker inputTanggal;
    
    // [BARU] Inject Label Error dan Tombol Cari
    @FXML private Label errorLabel; 
    @FXML private Button btnCari; // Pastikan tambah fx:id="btnCari" di FXML tombol cari jika mau disable tombolnya

    private static String selectedAsal = "";
    private static String selectedTujuan = "";

    @FXML
    public void initialize() {
        updateUI();
    }

    public void setStationData(Station station) {
        if (DataStore.isSelectingOrigin) {
            selectedAsal = station.getName();
        } else {
            selectedTujuan = station.getName();
        }
        updateUI(); 
    }

    private void updateUI() {
        // 1. Update Teks Stasiun Asal
        if (selectedAsal != null && !selectedAsal.isEmpty()) {
            inputAsal.setText(selectedAsal);
            inputAsal.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-color: transparent; -fx-cursor: hand;");
        } else {
            inputAsal.setText("");
            inputAsal.setPromptText("Pilih stasiun keberangkatan");
            inputAsal.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        }

        // 2. Update Teks Stasiun Tujuan
        if (selectedTujuan != null && !selectedTujuan.isEmpty()) {
            inputTujuan.setText(selectedTujuan);
            inputTujuan.setStyle("-fx-text-fill: black; -fx-font-weight: bold; -fx-font-size: 14px; -fx-background-color: transparent; -fx-cursor: hand;");
        } else {
            inputTujuan.setText("");
            inputTujuan.setPromptText("Pilih stasiun tujuan");
            inputTujuan.setStyle("-fx-background-color: transparent; -fx-cursor: hand;");
        }

        // 3. [PENTING] Panggil Validasi Error
        validateSelection();
    }

    /**
     * [BARU] Method untuk mengecek apakah Asal == Tujuan
     */
    private void validateSelection() {
        // Cek jika kedua stasiun sudah terisi
        if (!selectedAsal.isEmpty() && !selectedTujuan.isEmpty()) {
            
            if (selectedAsal.equals(selectedTujuan)) {
                // KASUS ERROR: Stasiun Sama
                errorLabel.setVisible(true);
                errorLabel.setManaged(true); // Agar mengambil ruang layout
                
                // Opsional: Matikan tombol cari visual
                if (btnCari != null) btnCari.setOpacity(0.5);
                
            } else {
                // KASUS AMAN
                errorLabel.setVisible(false);
                errorLabel.setManaged(false); // Sembunyikan dan hilangkan ruangnya
                
                if (btnCari != null) btnCari.setOpacity(1.0);
            }
        } else {
            // Jika salah satu masih kosong, sembunyikan error
            errorLabel.setVisible(false);
            errorLabel.setManaged(false);
            if (btnCari != null) btnCari.setOpacity(1.0);
        }
    }

    @FXML
    private void handleSwap(ActionEvent event) {
        String temp = selectedAsal;
        selectedAsal = selectedTujuan;
        selectedTujuan = temp;
        
        updateUI(); // validateSelection() otomatis dipanggil di dalam updateUI
    }

    // ... (Sisa method navigasi sama seperti sebelumnya) ...
    
    @FXML
    private void handleCari(ActionEvent event) {
        // ... (Validasi lama biarkan saja) ...
        if (selectedAsal.isEmpty() || selectedTujuan.isEmpty() || inputTanggal.getValue() == null) {
            showAlert("Belum Lengkap", "Harap isi stasiun asal, tujuan, dan tanggal.");
            return;
        }

        try {
            // --- [UPDATE] SIMPAN KE DATASTORE ---
            DataStore.journeyAsal = selectedAsal;
            DataStore.journeyTujuan = selectedTujuan;
            DataStore.journeyTanggal = inputTanggal.getValue().toString();
            // ------------------------------------

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/train_selection.fxml"));
            Parent root = loader.load();

            // Kita TIDAK PERLU lagi kirim manual via controller.setSearchData(...)
            // Karena TrainSelectionController akan ambil sendiri dari DataStore.

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // ... Copy paste sisa method handleBack, handlePilihAsal, dll ...
    @FXML private void handlePilihAsal(MouseEvent event) { DataStore.isSelectingOrigin = true; openSelectStation(event); }
    @FXML private void handlePilihTujuan(MouseEvent event) { DataStore.isSelectingOrigin = false; openSelectStation(event); }
    private void openSelectStation(MouseEvent event) {
    try {
        // PERHATIKAN BARIS INI:
        // Pastikan nama filenya sama persis dengan yang ada di folder view
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/select_station.fxml")); 
        
        Parent root = loader.load();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
    } catch (IOException e) { e.printStackTrace(); }
}
    @FXML private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
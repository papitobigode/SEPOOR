package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import model.Ticket;
import model.Train;
import util.DataStore;

import java.io.IOException;

public class PaymentController {

    @FXML private Label trainNameLabel;
    @FXML private Label trainTypeLabel;
    @FXML private Label dateLabel;
    @FXML private Label routeLabel;
    @FXML private Label seatLabel;
    @FXML private Label priceLabel;

    /**
     * Dipanggil dari SeatSelectionController untuk mengisi data
     */
    public void setBookingData(Train train, String seatCode) {
        // Isi Data Kereta
        trainNameLabel.setText(train.getName());
        trainTypeLabel.setText(train.getType());
        priceLabel.setText(train.getPrice());

        // Isi Data Perjalanan (Dari DataStore)
        dateLabel.setText(DataStore.journeyTanggal);
        routeLabel.setText(train.getDepartureStation() + " ➝ " + train.getArrivalStation());

        // Isi Data Kursi (Format: EKO-1 / 5A)
        // Kita ubah underscore (_) jadi miring (/) biar rapi
        String formattedSeat = seatCode.replace("_", " / ");
        seatLabel.setText(formattedSeat);
    }

   @FXML
    private void handlePay(ActionEvent event) {
        // 1. SIMPAN TIKET KE DATASTORE
        // Ambil data dari Label yang ada di layar Payment
        Ticket newTicket = new Ticket(
            trainNameLabel.getText(),           // Nama Kereta
            routeLabel.getText(),               // Rute
            dateLabel.getText(),                // Tanggal
            seatLabel.getText(),                // Kursi
            "LUNAS / AKTIF"                     // Status
        );
        
        // Masukkan ke list tiket saya
        DataStore.myTickets.add(newTicket);

        // 2. Tampilkan Pop-up Sukses
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Pembayaran Berhasil");
        alert.setHeaderText("Tiket Berhasil Dipesan!");
        alert.setContentText("Tiket Anda telah tersimpan di menu 'Tiket Saya'.");
        alert.showAndWait();

        // 3. Balik ke Dashboard / Tiket Saya
        try {
            // Kita arahkan langsung ke halaman Tiket Saya (ticket.fxml) agar user lihat hasilnya
            Parent root = FXMLLoader.load(getClass().getResource("/view/ticket.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        // Balik ke Pilih Kursi (Opsional, tapi agak ribet karena harus kirim data train lagi)
        // Sederhananya kita balik ke pencarian saja atau seat selection
        try {
            // Kita balik ke halaman pencarian saja agar aman
            Parent root = FXMLLoader.load(getClass().getResource("/view/train_selection.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}

package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Train;
import util.DataStore;

import java.io.IOException;
// --- [1. TAMBAH IMPORT INI] ---
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class TrainSelectionController {

    @FXML private ListView<Train> trainListView;
    @FXML private Label routeLabel;
    @FXML private Label dateLabel;

    @FXML
    public void initialize() {
        // 1. AMBIL DATA DARI DATASTORE
        String asalFull = (DataStore.journeyAsal != null) ? DataStore.journeyAsal : "Jakarta";
        String tujuanFull = (DataStore.journeyTujuan != null) ? DataStore.journeyTujuan : "Solo";
        
        // Ambil tanggal mentah (Format: 2025-07-10)
        String rawDate = (DataStore.journeyTanggal != null) ? DataStore.journeyTanggal : null;

        // 2. DAPATKAN KODE KOTA
        String kodeAsal = getKodeKota(asalFull);
        String kodeTujuan = getKodeKota(tujuanFull);

        // 3. SET HEADER
        routeLabel.setText(String.format("%s (%s) ➝ %s (%s)", asalFull, kodeAsal, tujuanFull, kodeTujuan));
        
        // --- [2. PANGGIL METHOD FORMAT TANGGAL] ---
        // Ubah "2025-07-10" jadi "Kamis, 10 Juli 2025"
        dateLabel.setText(formatTanggalIndonesia(rawDate));

        // 4. ISI DATA KERETA
        ObservableList<Train> trains = FXCollections.observableArrayList(
            new Train("ARGO LAWU", kodeAsal, kodeTujuan, "Rp 184.000,00", "Ekonomi"),
            new Train("WIJAYAKUSUMA", kodeAsal, kodeTujuan, "Rp 199.000,00", "Ekonomi"),
            new Train("KERTANEGARA", kodeAsal, kodeTujuan, "Rp 300.000,00", "Eksekutif"),
            new Train("GAJAYANA", kodeAsal, kodeTujuan, "Rp 350.000,00", "Eksekutif")
        );
        
        trainListView.setItems(trains);

        // 5. RENDER TAMPILAN KARTU
        setupListView();
    }

    /**
     * --- [3. METHOD BARU: FORMAT TANGGAL INDONESIA] ---
     * Mengubah string "YYYY-MM-DD" menjadi "Hari, dd MMMM yyyy"
     */
    private String formatTanggalIndonesia(String dateString) {
        if (dateString == null || dateString.isEmpty()) {
            return "Tanggal Belum Dipilih";
        }
        try {
            // 1. Parse string ISO (2025-07-10) ke Objek LocalDate
            LocalDate date = LocalDate.parse(dateString);

            // 2. Siapkan pola format Indonesia
            // EEEE = Nama Hari Full (Sabtu)
            // dd   = Tanggal (10)
            // MMMM = Nama Bulan Full (Juli)
            // yyyy = Tahun (2025)
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", new Locale("id", "ID"));

            // 3. Kembalikan hasil format
            return date.format(formatter);

        } catch (Exception e) {
            // Jika error parsing, kembalikan apa adanya
            return dateString;
        }
    }

    private String getKodeKota(String namaStasiun) {
        if (namaStasiun == null) return "XXX";
        String lower = namaStasiun.toLowerCase();
        
        if (lower.contains("pasar senen") || lower.contains("gambir") || lower.contains("jakarta")) return "JKT";
        if (lower.contains("solo") || lower.contains("balapan")) return "SLO";
        if (lower.contains("purwokerto")) return "PWT";
        if (lower.contains("bandung")) return "BDG";
        if (lower.contains("yogyakarta") || lower.contains("lempuyangan")) return "YK";
        if (lower.contains("surabaya") || lower.contains("gubeng") || lower.contains("pasar turi")) return "SBY";
        if (lower.contains("malang")) return "MLG";
        if (lower.contains("semarang")) return "SMG";
        if (lower.contains("bogor")) return "BGR";
        
        if (namaStasiun.length() >= 3) {
            return namaStasiun.substring(0, 3).toUpperCase();
        }
        return "XXX";
    }

    private void setupListView() {
        trainListView.setCellFactory(param -> new ListCell<Train>() {
            @Override
            protected void updateItem(Train item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    VBox card = new VBox(10);
                    card.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 5, 0, 0, 2); -fx-padding: 15;");

                    Label nameLbl = new Label(item.getName());
                    nameLbl.setFont(Font.font("System", FontWeight.BOLD, 14));
                    nameLbl.setTextFill(Color.web("#333333"));

                    HBox routeBox = new HBox(); 
                    routeBox.setAlignment(Pos.CENTER);
                    
                    Label depLbl = new Label(item.getDepartureStation());
                    depLbl.setFont(Font.font("System", FontWeight.BOLD, 16));
                    depLbl.setTextFill(Color.BLACK); 
                    
                    Label lineLbl = new Label("o-----------------------o"); 
                    lineLbl.setTextFill(Color.GRAY);
                    lineLbl.setFont(Font.font("System", 12));
                    
                    Label arrLbl = new Label(item.getArrivalStation());
                    arrLbl.setFont(Font.font("System", FontWeight.BOLD, 16));
                    arrLbl.setTextFill(Color.BLACK);
                    
                    Region spacerLeft = new Region();
                    HBox.setHgrow(spacerLeft, Priority.ALWAYS); 
                    
                    Region spacerRight = new Region();
                    HBox.setHgrow(spacerRight, Priority.ALWAYS); 
                    
                    routeBox.getChildren().addAll(depLbl, spacerLeft, lineLbl, spacerRight, arrLbl);


                    Region spacer = new Region();
                    spacer.setPrefHeight(1);
                    spacer.setStyle("-fx-background-color: #f3f4f6;");

                    HBox bottomBox = new HBox();
                    bottomBox.setAlignment(Pos.CENTER_LEFT);
                    Label priceLbl = new Label(item.getPrice());
                    priceLbl.setTextFill(Color.web("#0ea5e9"));
                    priceLbl.setFont(Font.font("System", FontWeight.BOLD, 14));
                    
                    Region flex = new Region();
                    HBox.setHgrow(flex, Priority.ALWAYS);
                    
                    Label typeLbl = new Label(item.getType());
                    typeLbl.setTextFill(Color.GRAY);
                    typeLbl.setStyle("-fx-font-style: italic;");
                    
                    bottomBox.getChildren().addAll(priceLbl, flex, typeLbl);

                    card.getChildren().addAll(nameLbl, routeBox, spacer, bottomBox);

                    card.setOnMouseClicked(event -> {
                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/seat_selection.fxml"));
                            Parent root = loader.load();
                            SeatSelectionController controller = loader.getController();
                            controller.setTrain(item);
                            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                            stage.setScene(new Scene(root));
                        } catch (IOException e) { e.printStackTrace(); }
                    });

                    setGraphic(card);
                    setStyle("-fx-background-color: transparent; -fx-padding: 5;");
                }
            }
        });
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/booking.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}
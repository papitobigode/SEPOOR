package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.Ticket;
import model.Train;
import util.DataStore;

import java.io.IOException;

public class SeatSelectionController {

    @FXML private HBox carriageContainer;
    @FXML private GridPane seatGrid;      
    @FXML private Label selectedSeatLabel;

    private Train selectedTrain;
    private String currentCarriage = "";
    private String selectedSeat = ""; // Format ID: EKO-1_1A

    public void setTrain(Train train) {
        this.selectedTrain = train;
        setupCarriageTabs();
    }

    private void setupCarriageTabs() {
        carriageContainer.getChildren().clear();
        String type = (selectedTrain != null) ? selectedTrain.getType() : "Ekonomi";

        String[] gerbong;
        if (type.equalsIgnoreCase("Eksekutif") || type.equalsIgnoreCase("Eksekutif")) {
            gerbong = new String[]{"EKS-1", "EKS-2"};
        } else {
            gerbong = new String[]{"EKO-1", "EKO-2", "EKO-3"};
        }

        for (String g : gerbong) {
            Button btn = new Button(g);
            btn.setPrefWidth(80);
            btn.setStyle("-fx-background-color: white; -fx-text-fill: gray; -fx-border-color: #e5e7eb; -fx-cursor: hand;");

            btn.setOnAction(e -> {
                carriageContainer.getChildren().forEach(node -> 
                    node.setStyle("-fx-background-color: white; -fx-text-fill: gray; -fx-border-color: #e5e7eb; -fx-cursor: hand;")
                );
                btn.setStyle("-fx-background-color: #eff6ff; -fx-text-fill: #1e3a8a; -fx-border-color: #1e3a8a; -fx-font-weight: bold;");
                loadSeats(g);
            });

            carriageContainer.getChildren().add(btn);
        }

        if (!carriageContainer.getChildren().isEmpty()) {
            ((Button) carriageContainer.getChildren().get(0)).fire();
        }
    }

    private void loadSeats(String carriage) {
        this.currentCarriage = carriage;
        seatGrid.getChildren().clear();

        // Header Huruf
        seatGrid.add(createLabel("A"), 1, 0); 
        seatGrid.add(createLabel("B"), 2, 0);
        seatGrid.add(createLabel("C"), 4, 0);
        seatGrid.add(createLabel("D"), 5, 0);

        int totalRows = 10;
        String[] seatChars = {"A", "B", "C", "D"};

        for (int row = 1; row <= totalRows; row++) {
            // Label Nomor Baris
            Label rowNum = createLabel(String.valueOf(row));
            rowNum.setAlignment(Pos.CENTER_RIGHT); 
            rowNum.setPrefWidth(20); 
            seatGrid.add(rowNum, 0, row);

            for (int i = 0; i < seatChars.length; i++) {
                String colChar = seatChars[i];
                
                // ID Kursi untuk logika program: "EKO-1_1A"
                String seatID = carriage + "_" + row + colChar;
                
                int gridCol = (i >= 2) ? i + 2 : i + 1;

                // --- [UPDATE PENTING] CEK STATUS KURSI DI DATABASE ---
                boolean isBooked = checkIsBookedReal(seatID);

                Button seatBtn = new Button();
                seatBtn.setPrefSize(45, 45); 

                if (isBooked) {
                    // JIKA SUDAH DIPESAN -> MATIKAN TOMBOL & WARNA ABU GELAP
                    seatBtn.setStyle("-fx-background-color: #4b5563; -fx-background-radius: 8;");
                    seatBtn.setDisable(true); 
                } else if (seatID.equals(selectedSeat)) {
                    // JIKA SEDANG DIPILIH USER SEKARANG
                    seatBtn.setStyle("-fx-background-color: #0ea5e9; -fx-background-radius: 8;");
                } else {
                    // JIKA KOSONG
                    seatBtn.setStyle("-fx-background-color: #e5e7eb; -fx-background-radius: 8; -fx-cursor: hand;");
                    seatBtn.setOnAction(e -> handleSelectSeat(seatID));
                }
                
                seatGrid.add(seatBtn, gridCol, row);
            }
        }
    }

    // --- [LOGIKA BARU] CEK KETERSEDIAAN KURSI ---
    private boolean checkIsBookedReal(String currentSeatID) {
        // Format ID di sini: "EKO-1_1A"
        // Format di Ticket (DataStore): "EKO-1 - 1A" (karena kita replace saat simpan)
        
        // Kita ubah ID grid jadi format tampilan agar bisa dibandingkan
        String seatToCompare = currentSeatID.replace("_", " - ");
        
        // Ambil Data Konteks Saat Ini
        String currentTrainName = selectedTrain.getName();
        String currentDate = DataStore.journeyTanggal;

        // Loop semua tiket di DataStore
        for (Ticket ticket : DataStore.myTickets) {
            // Cek 3 Syarat: Nama Kereta SAMA && Tanggal SAMA && Kursi SAMA
            boolean sameTrain = ticket.getTrainName().equals(currentTrainName);
            boolean sameDate = ticket.getDate().equals(currentDate);
            boolean sameSeat = ticket.getSeat().equals(seatToCompare);

            if (sameTrain && sameDate && sameSeat) {
                return true; // Kursi sudah ada yang punya!
            }
        }
        return false; // Kursi kosong
    }

    private Label createLabel(String text) {
        Label lbl = new Label(text);
        lbl.setFont(Font.font("System", FontWeight.BOLD, 14));
        lbl.setTextFill(javafx.scene.paint.Color.GRAY);
        lbl.setAlignment(Pos.CENTER);
        lbl.setMaxWidth(Double.MAX_VALUE); 
        return lbl;
    }

    private void handleSelectSeat(String fullSeatID) {
        this.selectedSeat = fullSeatID;
        loadSeats(currentCarriage); 

        String[] parts = fullSeatID.split("_");
        selectedSeatLabel.setText("Kursi dipilih: " + parts[0] + " - " + parts[1]);
    }

    @FXML
    private void handleKonfirmasi(ActionEvent event) {
        if (selectedSeat.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Silakan pilih kursi terlebih dahulu!");
            alert.show();
            return;
        }
        
        // Tampilkan Dialog Konfirmasi
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        showConfirmationDialog(mainStage);
    }

    private void showConfirmationDialog(Stage mainStage) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/booking_confirm.fxml"));
            Parent popupRoot = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); 
            popupStage.initStyle(StageStyle.TRANSPARENT); 
            popupStage.initOwner(mainStage); 

            Scene scene = new Scene(popupRoot);
            scene.setFill(Color.TRANSPARENT); 
            popupStage.setScene(scene);

            Button btnYes = (Button) popupRoot.lookup("#btnYes");
            Button btnNo = (Button) popupRoot.lookup("#btnNo");

            btnNo.setOnAction(e -> popupStage.close());

            btnYes.setOnAction(e -> {
                processBooking();
                popupStage.close(); 
                navigateToMyTickets(mainStage);
            });

            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Gagal memuat dialog: " + e.getMessage());
            alert.show();
        }
    }

    private void processBooking() {
        // Ubah format "EKO-1_1A" menjadi "EKO-1 - 1A" untuk disimpan
        String cleanSeat = selectedSeatLabel.getText().replace("Kursi dipilih: ", "");
        
        Ticket newTicket = new Ticket(
            selectedTrain.getName(),
            selectedTrain.getDepartureStation() + " - " + selectedTrain.getArrivalStation(),
            DataStore.journeyTanggal,
            cleanSeat, 
            "LUNAS"
        );
        
        DataStore.myTickets.add(newTicket);
    }

    private void navigateToMyTickets(Stage stage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/ticket.fxml"));
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleBack(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/train_selection.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}
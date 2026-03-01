package controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality; // [PENTING] Import Modality
import javafx.stage.Stage;    // [PENTING] Import Stage
import javafx.stage.StageStyle; // [PENTING] Import StageStyle
import model.Ticket;
import util.DataStore;

import java.io.IOException;

public class TicketController {

    @FXML private ListView<Ticket> ticketListView;

    @FXML
    public void initialize() {
        ticketListView.setItems(DataStore.myTickets);

        ticketListView.setCellFactory(param -> new ListCell<Ticket>() {
            @Override
            protected void updateItem(Ticket item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                    setStyle("-fx-background-color: transparent;");
                } else {
                    // --- WADAH KARTU ---
                    VBox card = new VBox(10);
                    card.setStyle("-fx-background-color: #fffbeb; -fx-background-radius: 15; -fx-border-color: #f59e0b; -fx-border-radius: 15; -fx-border-width: 1; -fx-padding: 15; -fx-effect: dropshadow(three-pass-box, rgba(0,0,0,0.1), 3, 0, 0, 1);");

                    // --- HEADER ---
                    HBox topBox = new HBox(10);
                    topBox.setAlignment(Pos.CENTER_LEFT);

                    Label nameLbl = new Label(item.getTrainName());
                    nameLbl.setFont(Font.font("System", FontWeight.BOLD, 16));
                    
                    Region spacer = new Region();
                    HBox.setHgrow(spacer, Priority.ALWAYS);
                    
                    // TOMBOL BATAL
                    Button btnBatal = new Button("Batal");
                    btnBatal.setStyle("-fx-background-color: #fee2e2; -fx-text-fill: #dc2626; -fx-background-radius: 8; -fx-font-weight: bold; -fx-cursor: hand; -fx-font-size: 12px;");
                    
                    // [UPDATE] LOGIKA POP-UP CUSTOM
                    btnBatal.setOnAction(event -> {
                        showCustomCancelDialog(item);
                    });
                    
                    topBox.getChildren().addAll(nameLbl, spacer, btnBatal);

                    // --- BODY ---
                    Label routeLbl = new Label(item.getRoute());
                    routeLbl.setFont(Font.font("System", FontWeight.BOLD, 14));
                    
                    HBox detailBox = new HBox(15);
                    Label dateLbl = new Label("📅 " + item.getDate());
                    dateLbl.setTextFill(Color.GRAY);
                    dateLbl.setFont(Font.font("System", 12));
                    
                    Label seatLbl = new Label("💺 " + item.getSeat());
                    seatLbl.setTextFill(Color.web("#b45309")); 
                    seatLbl.setFont(Font.font("System", FontWeight.BOLD, 12));
                    
                    Label statusLbl = new Label(item.getStatus());
                    statusLbl.setTextFill(Color.web("#15803d")); 
                    statusLbl.setStyle("-fx-background-color: #dcfce7; -fx-padding: 2 6; -fx-background-radius: 4; -fx-font-size: 10px;");

                    detailBox.getChildren().addAll(dateLbl, seatLbl, statusLbl);

                    card.getChildren().addAll(topBox, routeLbl, detailBox);
                    setGraphic(card);
                    setStyle("-fx-background-color: transparent; -fx-padding: 5;");
                }
            }
        });
    }

    // --- METHOD BARU: MENAMPILKAN POP-UP CUSTOM ---
    private void showCustomCancelDialog(Ticket ticketToDelete) {
        try {
            // 1. Load FXML Pop-up yang baru kita buat
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/cancel.fxml"));
            Parent popupRoot = loader.load();

            // 2. Buat Stage (Jendela) Baru
            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL); // Blokir jendela belakang (tidak bisa diklik sebelum ini tutup)
            popupStage.initStyle(StageStyle.TRANSPARENT); // Hilangkan bingkai window (Minimize/Close bawaan OS) agar terlihat bulat
            
            // 3. Set Scene Transparan
            Scene scene = new Scene(popupRoot);
            scene.setFill(Color.TRANSPARENT);
            popupStage.setScene(scene);

            // 4. Ambil Tombol dari FXML dan Beri Aksi
            // Kita cari tombol berdasarkan fx:id yang ditulis di FXML
            Button btnYes = (Button) popupRoot.lookup("#btnYes");
            Button btnNo = (Button) popupRoot.lookup("#btnNo");

            // Aksi Tombol TIDAK (Tutup Pop-up saja)
            btnNo.setOnAction(e -> popupStage.close());

            // Aksi Tombol YA (Hapus Data & Tutup)
            btnYes.setOnAction(e -> {
                // Hapus dari DataStore (Otomatis hilang dari layar karena ObservableList)
                DataStore.myTickets.remove(ticketToDelete); 
                popupStage.close(); // Tutup pop-up
            });

            // 5. Tampilkan di tengah layar
            popupStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- NAVIGASI ---
    @FXML
    private void handleMenuBeranda(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/dashboard.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }

    @FXML
    private void handleMenuProfil(MouseEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/profile.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
        } catch (IOException e) { e.printStackTrace(); }
    }
}
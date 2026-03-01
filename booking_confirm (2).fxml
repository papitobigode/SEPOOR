package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Station;
import util.DataStore;

import java.io.IOException;

public class SelectStationController {

    @FXML private ListView<Station> stationListView;
    @FXML private TextField searchField;

    private ObservableList<Station> masterData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Isi Data Dummy Stasiun
        masterData.add(new Station("Pasar Senen", "Jakarta"));
        masterData.add(new Station("Gambir", "Jakarta"));
        masterData.add(new Station("Bandung", "Bandung"));
        masterData.add(new Station("Yogyakarta", "Yogyakarta"));
        masterData.add(new Station("Solo Balapan", "Solo"));
        masterData.add(new Station("Purwokerto", "Purwokerto"));
        masterData.add(new Station("Surabaya Gubeng", "Surabaya"));
        masterData.add(new Station("Malang", "Malang"));

        // 2. Siapkan Filter (untuk fitur search)
        FilteredList<Station> filteredData = new FilteredList<>(masterData, p -> true);
        stationListView.setItems(filteredData);

        // 3. Logika Search Bar
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(station -> {
                if (newValue == null || newValue.isEmpty()) return true;
                String lowerCaseFilter = newValue.toLowerCase();
                return station.getName().toLowerCase().contains(lowerCaseFilter) || 
                       station.getCity().toLowerCase().contains(lowerCaseFilter);
            });
        });

        // 4. CUSTOM CELL FACTORY (Agar tampilan ada 2 baris: Nama & Kota)
        stationListView.setCellFactory(param -> new ListCell<Station>() {
            @Override
            protected void updateItem(Station item, boolean empty) {
                super.updateItem(item, empty);

                if (empty || item == null) {
                    setText(null);
                    setGraphic(null);
                } else {
                    // Buat layout VBox untuk menampung 2 teks
                    VBox vBox = new VBox(2);
                    
                    Text nameText = new Text(item.getName());
                    nameText.setFont(Font.font("System", FontWeight.BOLD, 14));
                    
                    Text cityText = new Text(item.getCity());
                    cityText.setFont(Font.font("System", 12));
                    cityText.setFill(javafx.scene.paint.Color.GRAY);

                    vBox.getChildren().addAll(nameText, cityText);
                    setGraphic(vBox); // Pasang VBox ke dalam sel
                }
            }
        });

        // 5. Saat item diklik
        stationListView.setOnMouseClicked(event -> {
            Station selected = stationListView.getSelectionModel().getSelectedItem();
            if (selected != null) {
                handleStationSelected(selected, event);
            }
        });
    }

    private void handleStationSelected(Station station, javafx.scene.input.MouseEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/booking.fxml"));
            Parent root = loader.load();

            // Kembalikan data ke BookingController
            BookingController controller = loader.getController();
            
            // Kita oper data stasiun yang dipilih + data lama user
            controller.setStationData(station); 

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            
        } catch (IOException e) {
            e.printStackTrace();
        }
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
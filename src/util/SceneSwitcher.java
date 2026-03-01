package util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class SceneSwitcher {

    /**
     * Method statis untuk pindah halaman.
     * @param event - Event dari tombol yang diklik (agar tahu Stage mana yang dipakai)
     * @param fxmlFileName - Nama file FXML tujuan (misal: "register.fxml")
     */
    public static void switchScene(ActionEvent event, String fxmlFileName) {
        try {
            // 1. Load file FXML tujuan
            // Kita asumsikan semua fxml ada di folder /view/
            FXMLLoader loader = new FXMLLoader(SceneSwitcher.class.getResource("/view/" + fxmlFileName));
            Parent root = loader.load();

            // 2. Ambil Stage (Jendela) saat ini dari tombol yang diklik
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

            // 3. Pasang Scene baru ke Stage
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("Gagal memuat file FXML: " + fxmlFileName);
            e.printStackTrace();
        }
    }
}

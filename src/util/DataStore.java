package util;

import model.User;
import model.Ticket; // [PENTING] Import Model Ticket
import javafx.collections.FXCollections; // [PENTING] Import untuk List
import javafx.collections.ObservableList; // [PENTING] Import untuk List

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class DataStore {

    // 1. DATA USER LOGIN
    public static User currentUser; 

    // 2. DATA PENCARIAN SEMENTARA
    public static String journeyAsal;
    public static String journeyTujuan;
    public static String journeyTanggal;

    // --- [BARU] LIST TIKET SAYA ---
    // Variabel ini menyimpan semua tiket yang sudah dibayar (Lunas)
    // Kita pakai ObservableList agar bisa langsung dimasukkan ke ListView
    public static ObservableList<Ticket> myTickets = FXCollections.observableArrayList();
    // ---------------------------------------------------------------------

    private static List<User> users = new ArrayList<>();
    
    // Nama file penyimpanan user
    private static final String FILE_PATH = "data_users.txt";

    // --- STATIC BLOCK (Jalan otomatis saat aplikasi mulai) ---
    static {
        loadData(); // Baca file saat aplikasi nyala
        
        // Jika file kosong/belum ada, kita buat admin default
        if (users.isEmpty()) {
            users.add(new User("Administrator", "admin", "admin123"));
            saveData(); // Simpan admin ke file
        }
    }

    public static void addUser(User user) {
        users.add(user);
        saveData(); 
    }

    public static User validateUser(String username, String password) {
        for (User u : users) {
            if (u.getUsername().equals(username) && u.getPassword().equals(password)) {
                return u;
            }
        }
        return null;
    }

    // --- UPDATE DATA ---
    public static void updateUserData() {
        saveData(); 
    }

    // --- FUNGSI MENYIMPAN KE FILE (Save) ---
    private static void saveData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH))) {
            for (User u : users) {
                String line = String.format("%s;%s;%s;%s",
                        u.getUsername(),
                        u.getPassword(),
                        u.getNama(),
                        (u.getImagePath() == null ? "null" : u.getImagePath())
                );
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // --- FUNGSI MEMBACA DARI FILE (Load) ---
    private static void loadData() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(";");
                
                if (data.length >= 3) {
                    User u = new User(data[2], data[0], data[1]); 
                    
                    if (data.length > 3 && !"null".equals(data[3])) {
                        u.setImagePath(data[3]);
                    }
                    users.add(u);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    // Flag untuk memilih stasiun
    public static boolean isSelectingOrigin = true; 
}
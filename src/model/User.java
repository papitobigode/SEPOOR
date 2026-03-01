package model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class User {
    // 1. Property yang sudah ada
    private final StringProperty nama = new SimpleStringProperty();
    private final StringProperty username = new SimpleStringProperty();
    private final StringProperty password = new SimpleStringProperty();
    
    // --- [BARU] Property untuk menyimpan lokasi gambar ---
    private final StringProperty imagePath = new SimpleStringProperty();

    // 2. Constructor
    public User(String nama, String username, String password) {
        this.nama.set(nama);
        this.username.set(username);
        this.password.set(password);
        
        // --- [BARU] Default gambar kosong (null) saat user baru dibuat ---
        this.imagePath.set(null); 
    }

    // --- GETTER & SETTER LAMA (NAMA, USERNAME, PASSWORD) ---
    
    // A. Untuk NAMA
    public String getNama() { return nama.get(); }
    public void setNama(String value) { this.nama.set(value); }
    public StringProperty namaProperty() { return nama; }

    // B. Untuk USERNAME
    public String getUsername() { return username.get(); }
    public void setUsername(String value) { this.username.set(value); }
    public StringProperty usernameProperty() { return username; }

    // C. Untuk PASSWORD
    public String getPassword() { return password.get(); }
    public void setPassword(String value) { this.password.set(value); }
    public StringProperty passwordProperty() { return password; }

    // --- [BARU] GETTER & SETTER UNTUK IMAGE PATH ---
    // Ini wajib ada supaya ProfileController bisa simpan, dan Dashboard bisa baca.

    public String getImagePath() {
        return imagePath.get(); // Mengambil string URL gambar
    }

    public void setImagePath(String value) {
        this.imagePath.set(value); // Menyimpan URL gambar baru
    }

    public StringProperty imagePathProperty() {
        return imagePath; // Untuk binding (jika perlu)
    }
}
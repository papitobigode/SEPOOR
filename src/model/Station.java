package model;

public class Station {
    private String name;
    private String city;

    public Station(String name, String city) {
        this.name = name;
        this.city = city;
    }

    public String getName() { return name; }
    public String getCity() { return city; }

    // Override toString agar kalau diprint hasilnya rapi
    @Override
    public String toString() {
        return name + " - " + city;
    }
}

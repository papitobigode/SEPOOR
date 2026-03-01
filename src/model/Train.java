package model;

public class Train {
    private String name;
    private String departureStation; // misal: JKT
    private String arrivalStation;   // misal: SLO
    private String price;
    private String type; // Ekonomi, Eksekutif

    public Train(String name, String dep, String arr, String price, String type) {
        this.name = name;
        this.departureStation = dep;
        this.arrivalStation = arr;
        this.price = price;
        this.type = type;
    }

    // Getters
    public String getName() { return name; }
    public String getDepartureStation() { return departureStation; }
    public String getArrivalStation() { return arrivalStation; }
    public String getPrice() { return price; }
    public String getType() { return type; }
}

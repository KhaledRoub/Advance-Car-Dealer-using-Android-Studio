package com.example.project_temp2.database;

public class CarDetails {
    private String manufacturer;
    private String type;
    private double miles;
    private String model;

    public CarDetails(String manufacturer, String type, double miles, String model) {
        this.manufacturer = manufacturer;
        this.type = type;
        this.miles = miles;
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getType() {
        return type;
    }

    public double getMiles() {
        return miles;
    }

    public String getModel() {
        return model;
    }
}
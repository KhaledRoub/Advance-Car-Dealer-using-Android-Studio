package com.example.project_temp2.database;

public class ReservedCarDetails {
    private int carId;
    private String manufacturer;
    private String type;
    private double price;
    private String model;
    private double miles;
    private int image;
    private double promoPercentage;
    private int status;
    private int reserved;

    private String reservationDT;
    private String returnDT;
    private double reservationPrice;
    private double offer;
    private String reserverName;
    private String reserverEmail;

    public ReservedCarDetails(int carId, String manufacturer, String type, double price, String model, double miles, int image, double promoPercentage, int status, int reserved, String reservationDT, String returnDT, double reservationPrice, double offer, String reserverName, String reserverEmail) {
        this.carId = carId;
        this.manufacturer = manufacturer;
        this.type = type;
        this.price = price;
        this.model = model;
        this.miles = miles;
        this.image = image;
        this.promoPercentage = promoPercentage;
        this.status = status;
        this.reserved = reserved;
        this.reservationDT = reservationDT;
        this.returnDT = returnDT;
        this.reservationPrice = reservationPrice;
        this.offer = offer;
        this.reserverName = reserverName;
        this.reserverEmail = reserverEmail;
    }

    public int getCarId() {
        return carId;
    }

    public void setCarId(int carId) {
        this.carId = carId;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getMiles() {
        return miles;
    }

    public void setMiles(double miles) {
        this.miles = miles;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public double getPromoPercentage() {
        return promoPercentage;
    }

    public void setPromoPercentage(double promoPercentage) {
        this.promoPercentage = promoPercentage;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getReserved() {
        return reserved;
    }

    public void setReserved(int reserved) {
        this.reserved = reserved;
    }

    public String getReservationDT() {
        return reservationDT;
    }

    public void setReservationDT(String reservationDT) {
        this.reservationDT = reservationDT;
    }

    public double getReservationPrice() {
        return reservationPrice;
    }

    public void setReservationPrice(double reservationPrice) {
        this.reservationPrice = reservationPrice;
    }

    public String getReturnDT() {
        return returnDT;
    }

    public void setReturnDT(String returnDT) {
        this.returnDT = returnDT;
    }

    public double getOffer() {
        return offer;
    }

    public void setOffer(double offer) {
        this.offer = offer;
    }

    public String getReserverName() {
        return reserverName;
    }

    public void setReserverName(String reserverName) {
        this.reserverName = reserverName;
    }

    public String getReserverEmail() {
        return reserverEmail;
    }

    public void setReserverEmail(String reserverEmail) {
        this.reserverEmail = reserverEmail;
    }
}

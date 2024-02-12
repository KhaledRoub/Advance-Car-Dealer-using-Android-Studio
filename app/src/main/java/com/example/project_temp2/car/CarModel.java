package com.example.project_temp2.car;

import com.example.project_temp2.R;
import com.example.project_temp2.shared.AllCars;

public class CarModel {

    private int id;
    private String manufacturer;
    private String type;
    private float price;
    private String model;

    private float miles;
    private int image;

    private float promoPercentage;
    private int status = 0;

    private int reserved = 0;
    private double rating = 0.0;

    private String carDealer;

    public CarModel() {
    }

    public CarModel(int id, String manufacturer, String type, float price, String model, float miles, int image, float promoPercentage, int status, int reserved, double rating, String carDealer) {
        this.id = id;
        this.manufacturer = manufacturer;
        this.type = type;
        this.price = price;
        this.model = model;
        this.miles = miles;
        this.image = image;
        this.promoPercentage = promoPercentage;
        this.status = status;
        this.reserved = reserved;
        this.rating = rating;
        this.carDealer = carDealer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public float getMiles() {
        return miles;
    }

    public void setMiles(float miles) {
        this.miles = miles;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public float getPromoPercentage() {
        return promoPercentage;
    }

    public void setPromoPercentage(float promoPercentage) {
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

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public String getCarDealer() {
        return carDealer;
    }

    public void setCarDealer(String carDealer) {
        this.carDealer = carDealer;
    }

    public static void seedCars() {
        AllCars listCars = AllCars.getInstance();

        String[] manufacturers = {"Jeep", "Jeep", "Dodge", "Tesla", "Lamborghini", "Tesla", "Ford", "Ford", "Alpine", "Tesla", "Honda", "Tesla", "Toyota", "Jeep", "Lamborghini", "Lamborghini", "Jeep", "Ford", "Tesla", "Honda", "Honda", "Chevrolet", "Volkswagen"};
        int[] years = {2019, 2020, 2019, 2021, 2022, 2024, 2021, 2019, 2017, 2026, 2023, 2022, 2021, 2022, 2010, 1985, 2024, 2022, 2023, 2022, 2011, 2019, 2022};
        int[] ids = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23};
        int[] prices = {500, 800, 400, 200, 300, 100, 300, 350, 750, 555, 120, 532, 122, 743, 320, 750, 650, 230, 100, 210, 140, 850, 420};
        int[] promoPercentages = {26, 0, 0, 0, 0, 0, 0, 0, 0, 0, 10, 0, 0, 0, 0, 0, 0, 33, 5, 0, 0, 0, 0};
        int[] images = {R.drawable.a1, R.drawable.a2, R.drawable.a3, R.drawable.a4, R.drawable.a5, R.drawable.a6, R.drawable.a7, R.drawable.a8, R.drawable.a9, R.drawable.a10, R.drawable.a11, R.drawable.a12, R.drawable.a13, R.drawable.a14, R.drawable.a15, R.drawable.a16, R.drawable.a17, R.drawable.a18, R.drawable.a19, R.drawable.a20, R.drawable.a21, R.drawable.a22, R.drawable.a23};

        for (int i = 0; i < ids.length; i++) {
            if (i <= 7)
                listCars.cars.get(i).setCarDealer("yousef@dealer.com");
            else if (i <= 15)
                listCars.cars.get(i).setCarDealer("khaled@dealer.com");
            else
                listCars.cars.get(i).setCarDealer("mohammed@dealer.com");

            listCars.cars.get(i).setId(ids[i]);
            listCars.cars.get(i).setManufacturer(manufacturers[i]);
            listCars.cars.get(i).setPrice(prices[i]);
            listCars.cars.get(i).setModel(Integer.toString(years[i]));
            listCars.cars.get(i).setMiles(0);
            listCars.cars.get(i).setImage(images[i]);
            listCars.cars.get(i).setPromoPercentage(promoPercentages[i]);
            listCars.cars.get(i).setReserved(0);
            listCars.cars.get(i).setRating(0);
        }
        listCars.cars.get(22).setType("T-Cross");
    }

}

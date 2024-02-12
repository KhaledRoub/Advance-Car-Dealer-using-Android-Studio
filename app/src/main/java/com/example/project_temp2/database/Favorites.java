package com.example.project_temp2.database;

public class Favorites {

    private int id;
    private int carID;
    private String userEmail;


    public Favorites() {
    }

    public Favorites(int carID, String userEmail) {
        this.carID = carID;
        this.userEmail = userEmail;
    }

    public Favorites(int id, int carID, String userEmail) {
        this.id = id;
        this.carID = carID;
        this.userEmail = userEmail;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCarID() {
        return carID;
    }

    public void setCarID(int carID) {
        this.carID = carID;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }
}

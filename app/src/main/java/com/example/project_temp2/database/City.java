package com.example.project_temp2.database;

public class City {
    private int id;
    private int countryID;
    private String name;

    public City() {
    }

    public City(int id, int countryID, String name) {
        this.id = id;
        this.countryID = countryID;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCountryID() {
        return countryID;
    }

    public void setCountryID(int countryID) {
        this.countryID = countryID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Cities{" +
                "id=" + id +
                ", countryID=" + countryID +
                ", name='" + name + '\'' +
                '}';
    }
}

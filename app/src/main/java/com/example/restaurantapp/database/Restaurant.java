package com.example.restaurantapp.database;

import java.io.Serializable;

public class Restaurant implements Serializable {
    private int id;
    private String nom;
    private double latitude ;
    private double longitude;
    private String etat;
    private String tel;
    private  String image;
    private String description;
    public Restaurant(int id, String nom, String tele,double latitude, double longitude, String etat,String description,String img) {
        this.id = id;
        this.nom = nom;
        this.tel=tele;
        this.latitude = latitude;
        this.longitude = longitude;
        this.etat = etat;
        this.image=img;
        this.description=description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getEtat() {
        return etat;
    }

    public void setEtat(String etat) {
        this.etat = etat;
    }
}

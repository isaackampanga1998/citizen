package com.example.myapplication.Modelretrofit;

import com.google.gson.annotations.SerializedName;

public class Bris {
    @SerializedName("_id")
    private String id;
    @SerializedName("username")
    private String username;
    @SerializedName("nameBris")
    private String nomBris;

    @SerializedName("address")
    private String adresse;

    @SerializedName("image")
    private String image;

    @SerializedName("state")
    private String etat;

    @SerializedName("userID")
    private String idUtilisateur;

    @SerializedName("date")
    private String date;

    public Bris(String id, String nomBris, String adresse, String image, String etat, String idUtilisateur, String date) {
        this.id = id;
        this.nomBris = nomBris;
        this.adresse = adresse;
        this.image = image;
        this.etat = etat;
        this.idUtilisateur = idUtilisateur;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public String getUsername(){
        return this.username;
    }
    public String getNomBris() {
        return nomBris;
    }

    public String getAdresse() {
        return adresse;
    }

    public String getImage() {
        return image;
    }

    public String getEtat() {
        return etat;
    }

    public String getIdUtilisateur() {
        return idUtilisateur;
    }

    public String getDate() {
        return date;
    }

    public void setImage(String url){
        this.image = url;
    }

    public void setDate (String date){
        this.date = date;
    }
}

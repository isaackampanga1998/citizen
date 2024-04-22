package com.example.myapplication.Modelretrofit;

import com.example.myapplication.Model.User;
import com.google.gson.annotations.SerializedName;

import java.util.Locale;

public class UserDTO {
    @SerializedName("_id")
    private String id;

    @SerializedName("nom")
    private String nom;

    @SerializedName("prenom")
    private String prenom;

    @SerializedName("adresse")
    private String adresse;

    @SerializedName("courriel")
    private String courriel;

    @SerializedName("telephone")
    private String telephone;

    @SerializedName("no_agent")
    private int noAgent;

    @SerializedName("ville")
    private String ville;

    @SerializedName("photo")
    private String photoUrl;

    @SerializedName("roles_id")
    private int rolesId;

    @SerializedName("password")
    private String password;

    // Constructor
    public UserDTO(String prenom, String nom, String courriel, String adresse, String telephone, int rolesId, String password) {
        this.prenom = prenom;
        this.nom = nom;
        this.courriel = courriel;
        this.adresse = adresse;
        this.telephone = telephone;
        this.rolesId = rolesId;
        this.password = password;
    }

    // Getters and setters  for the fields
    public String formatted() {
        return String.format(Locale.getDefault(), "User[id=%s, nom=%s, prenom=%s, adresse=%s, courriel=%s, telephone=%s, noAgent=%d, ville=%s, photoUrl=%s, rolesId=%d, password=%s]",
                id, nom, prenom, adresse, courriel, telephone, noAgent, ville, photoUrl, rolesId, password);
    }

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }

    public int getNoAgent() {
        return noAgent;
    }
    public void setNoAgent(int noAgent) {
        this.noAgent = noAgent;
    }

    public int getRolesId() {
        return rolesId;
    }
    public void setRolesId(int rolesId) {
        this.rolesId = rolesId;
    }

    public String getAdresse() {
        return adresse;
    }
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getCourriel() {
        return courriel;
    }
    public void setCourriel(String courriel) {
        this.courriel = courriel;
    }

    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }

    public String getPrenom() {
        return prenom;
    }
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getTelephone() {
        return telephone;
    }
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getVille() {
        return ville;
    }
    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }
    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

}
package com.example.myapplication.Model;

import android.graphics.Bitmap;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName = "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "first_name")
    private String firstName;
    @ColumnInfo(name = "last_name")
    private String lastName;
    @ColumnInfo(name = "address")
    private String address;
    @ColumnInfo(name = "email")
    private String email;
    @ColumnInfo(name = "phone")
    private String phone;
    @ColumnInfo(name = "password")
    private String password;

    @Ignore
    private Bitmap profilePicture;

    public User(int id, String firstName, String lastName, String address, String email, String phone, String password) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
    }

    // Constructor with @Ignore for Bitmap field
    @Ignore
    public User(int id, String firstName, String lastName, String address, String email, String phone, String password, Bitmap profilePicture) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.profilePicture = profilePicture;
    }

    public User() {

    }

    // Getter and setter for Bitmap field
    @Ignore
    public Bitmap getProfilePicture() {
        return profilePicture;
    }

    @Ignore
    public void setProfilePicture(Bitmap profilePicture) {
        this.profilePicture = profilePicture;
    }

    // Getters and setters for other fields

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getAddress() {
        return address;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getPassword() {
        return password;
    }

    // Setters for other fields

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String formatted() {
        return String.format("%d@/@%s@/@%s@/@%s@/@%s@/@%s@/@%s", id, firstName, lastName, address, email, phone, password);
    }

    public static User parseFormatted(String formattedString) {
        String[] parts = formattedString.split("@/@");
        if (parts.length != 7) {
            throw new IllegalArgumentException("Invalid formatted string");
        }
        int id = Integer.parseInt(parts[0]);
        String firstName = parts[1];
        String lastName = parts[2];
        String address = parts[3];
        String email = parts[4];
        String phone = parts[5];
        String password = parts[6];
        return new User(id, firstName, lastName, address, email, phone, password);
    }

}

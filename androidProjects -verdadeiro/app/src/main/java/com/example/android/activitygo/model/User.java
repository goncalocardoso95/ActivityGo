package com.example.android.activitygo.model;

import java.util.ArrayList;

public class User {

    private String firstName;
    private String lastName;
    private String date;
    private String gender;
    private String country;
    private String email;
    private String weight;
    private String hight;
    private String username;
    private String password;
    private ArrayList<String> sports;
    private String photoPath;
    private int pontos;

    public User() {

    }

    public User(String firstName, String lastName, String date, String gender, String country, String email, String weight, String hight, String username, String password, ArrayList<String> sports, String photoPath, int pontos) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.date = date;
        this.gender = gender;
        this.country = country;
        this.email = email;
        this.weight = weight;
        this.hight = hight;
        this.username = username;
        this.password = password;
        this.sports = sports;
        this.photoPath = photoPath;
        this.pontos = pontos;
    }

    public int getPontos() {
        return pontos;
    }

    public String getPhotoPath() {
        return photoPath;
    }

    public ArrayList<String> getSports() {
        return sports;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDate() {
        return date;
    }

    public String getGender() {
        return gender;
    }

    public String getCountry() {
        return country;
    }

    public String getEmail() {
        return email;
    }

    public String getWeight() {
        return weight;
    }

    public String getHight() {
        return hight;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public void setHight(String hight) {
        this.hight = hight;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setSports(ArrayList<String> sports) {
        this.sports = sports;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public void setPontos(int pontos) {
        this.pontos = pontos;
    }
}
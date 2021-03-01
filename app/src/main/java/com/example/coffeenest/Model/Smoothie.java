package com.example.coffeenest.Model;

public class Smoothie {

    private String SmoothieName,SmoothiePrice,SmoothieDescription,SmoothieImage;


    public Smoothie() {
    }

    public Smoothie(String smoothieName, String smoothiePrice, String smoothieDescription, String smoothieImage) {
        SmoothieName = smoothieName;
        SmoothiePrice = smoothiePrice;
        SmoothieDescription = smoothieDescription;
        SmoothieImage = smoothieImage;
    }

    public String getSmoothieName() {
        return SmoothieName;
    }

    public void setSmoothieName(String smoothieName) {
        SmoothieName = smoothieName;
    }

    public String getSmoothiePrice() {
        return SmoothiePrice;
    }

    public void setSmoothiePrice(String smoothiePrice) {
        SmoothiePrice = smoothiePrice;
    }

    public String getSmoothieDescription() {
        return SmoothieDescription;
    }

    public void setSmoothieDescription(String smoothieDescription) {
        SmoothieDescription = smoothieDescription;
    }

    public String getSmoothieImage() {
        return SmoothieImage;
    }

    public void setSmoothieImage(String smoothieImage) {
        SmoothieImage = smoothieImage;
    }
}

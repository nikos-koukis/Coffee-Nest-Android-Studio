package com.example.coffeenest.Model;

public class Snack {

    private String SnackName,SnackPrice,SnackDescription,SnackImage;

    public Snack() {
    }

    public Snack(String snackName, String snackPrice, String snackDescription, String snackImage) {
        SnackName = snackName;
        SnackPrice = snackPrice;
        SnackDescription = snackDescription;
        SnackImage = snackImage;
    }

    public String getSnackName() {
        return SnackName;
    }

    public void setSnackName(String snackName) {
        SnackName = snackName;
    }

    public String getSnackPrice() {
        return SnackPrice;
    }

    public void setSnackPrice(String snackPrice) {
        SnackPrice = snackPrice;
    }

    public String getSnackDescription() {
        return SnackDescription;
    }

    public void setSnackDescription(String snackDescription) {
        SnackDescription = snackDescription;
    }

    public String getSnackImage() {
        return SnackImage;
    }

    public void setSnackImage(String snackImage) {
        SnackImage = snackImage;
    }
}

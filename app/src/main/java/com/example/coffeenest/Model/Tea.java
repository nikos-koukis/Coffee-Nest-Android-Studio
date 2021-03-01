package com.example.coffeenest.Model;

public class Tea {

    private String TeaName,TeaPrice,TeaDescription,TeaImage;

    public Tea() {
    }

    public Tea(String teaName, String teaPrice, String teaDescription, String teaImage) {
        TeaName = teaName;
        TeaPrice = teaPrice;
        TeaDescription = teaDescription;
        TeaImage = teaImage;
    }

    public String getTeaName() {
        return TeaName;
    }

    public void setTeaName(String teaName) {
        TeaName = teaName;
    }

    public String getTeaPrice() {
        return TeaPrice;
    }

    public void setTeaPrice(String teaPrice) {
        TeaPrice = teaPrice;
    }

    public String getTeaDescription() {
        return TeaDescription;
    }

    public void setTeaDescription(String teaDescription) {
        TeaDescription = teaDescription;
    }

    public String getTeaImage() {
        return TeaImage;
    }

    public void setTeaImage(String teaImage) {
        TeaImage = teaImage;
    }
}

package com.example.coffeenest.Model;

public class Drink {

    private String DrinkName,DrinkPrice,DrinkDescription,DrinkImage;

    public Drink() {
    }

    public Drink(String drinkName, String drinkPrice, String drinkDescription, String drinkImage) {
        DrinkName = drinkName;
        DrinkPrice = drinkPrice;
        DrinkDescription =  drinkDescription;
        DrinkImage = drinkImage;
    }

    public String getDrinkName() {
        return DrinkName;
    }

    public void setDrinkName(String drinkName) {
        DrinkName = drinkName;
    }

    public String getDrinkPrice() {
        return DrinkPrice;
    }

    public void setDrinkPrice(String drinkPrice) {
        DrinkPrice = drinkPrice;
    }

    public String getDrinkDescription() {
        return DrinkDescription;
    }

    public void setDrinkDescription(String drinkDescription) {
        DrinkDescription = drinkDescription;
    }

    public String getDrinkImage() {
        return DrinkImage;
    }

    public void setDrinkImage(String drinkImage) {
        DrinkImage = drinkImage;
    }
}

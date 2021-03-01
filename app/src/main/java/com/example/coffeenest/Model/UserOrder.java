package com.example.coffeenest.Model;

import java.sql.Timestamp;
import java.util.Map;

public class UserOrder {

    private String userOrderName;
    private String userOrderAddress;
    private String userOrderDateTime;
    private String userOrderPaymentMethod;
    private String userOrderTotalPrice;

    public UserOrder() {
    }

    public UserOrder(String userOrderName, String userOrderAddress, String userOrderDateTime, String userOrderPaymentMethod, String userOrderTotalPrice) {
        this.userOrderName = userOrderName;
        this.userOrderAddress = userOrderAddress;
        this.userOrderDateTime = userOrderDateTime;
        this.userOrderPaymentMethod = userOrderPaymentMethod;
        this.userOrderTotalPrice = userOrderTotalPrice;
    }

    public String getUserOrderName() {
        return userOrderName;
    }

    public void setUserOrderName(String userOrderName) {
        this.userOrderName = userOrderName;
    }

    public String getUserOrderAddress() {
        return userOrderAddress;
    }

    public void setUserOrderAddress(String userOrderAddress) {
        this.userOrderAddress = userOrderAddress;
    }

    public String getUserOrderDateTime() {
        return userOrderDateTime;
    }

    public void setUserOrderDateTime(String userOrderDateTime) {
        this.userOrderDateTime = userOrderDateTime;
    }

    public String getUserOrderPaymentMethod() {
        return userOrderPaymentMethod;
    }

    public void setUserOrderPaymentMethod(String userOrderPaymentMethod) {
        this.userOrderPaymentMethod = userOrderPaymentMethod;
    }

    public String getUserOrderTotalPrice() {
        return userOrderTotalPrice;
    }

    public void setUserOrderTotalPrice(String userOrderTotalPrice) {
        this.userOrderTotalPrice = userOrderTotalPrice;
    }

}

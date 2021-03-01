package com.example.coffeenest.Model;


import java.util.List;

public class Request {
    private String name;
    private String address;
    private String total;
    private String datetime;
    private String paymentMethod;
    private List<CoffeeOrder> order;


    public Request() {
    }

    public Request(String name, String address, String total, String datetime, String paymentMethod, List<CoffeeOrder> order) {
        this.name = name;
        this.address = address;
        this.total = total;
        this.datetime = datetime;
        this.paymentMethod = paymentMethod;
        this.order = order;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getDatetime() {
        return datetime;
    }

    public void setDatetime(String datetime) {
        this.datetime = datetime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public List<CoffeeOrder> getOrder() {
        return order;
    }

    public void setOrder(List<CoffeeOrder> order) {
        this.order = order;
    }
}


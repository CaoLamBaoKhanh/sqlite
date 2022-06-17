package com.example.models;

import java.io.Serializable;

public class Product implements Serializable {
    private int productID;
    private String productName;
    private double productPrice;

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public Product(int productID, String productName, double productPrice) {
        this.productID = productID;
        this.productName = productName;
        this.productPrice = productPrice;
    }

    @Override
    public String toString() {
        return
                "ID=" + productID +"\t"+
                ", Name='" + productName + '\'' +
                ", Price=" + productPrice ;
    }
}

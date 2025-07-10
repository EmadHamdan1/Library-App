package com.emad.myapplicationminiproject;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Book {

    private int id;
    private String name;
    private double price;
    private int quantity;
    private boolean isTake;
    private double takePrice;
    private Bitmap bookImage;
    private String Disc;
    private int ownerId;
    private double totalPriceBook;

    public double getTotalPriceBook() {
        return totalPriceBook;
    }

    public void setTotalPriceBook(double totalPriceBook) {
        this.totalPriceBook = totalPriceBook;
    }

    public Book(int id, String name, double price, int quantity, boolean isTake, double takePrice, Bitmap bookImage, String disc) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isTake = isTake;
        this.takePrice = takePrice;
        this.bookImage = bookImage;
        Disc = disc;
    }

    public Book(String name, double price, int quantity, boolean isTake, double takePrice, Bitmap bookImage, String disc) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isTake = isTake;
        this.takePrice = takePrice;
        this.bookImage = bookImage;
        Disc = disc;
    }

    public Book(String name, double price, int quantity, boolean isTake, double takePrice, Bitmap bookImage, String disc, int ownerId) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isTake = isTake;
        this.takePrice = takePrice;
        this.bookImage = bookImage;
        Disc = disc;
        this.ownerId = ownerId;
    }

    public Book(int id, String name, double price, int quantity, boolean isTake, double takePrice, Bitmap bookImage, String disc, int ownerId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.isTake = isTake;
        this.takePrice = takePrice;
        this.bookImage = bookImage;
        Disc = disc;
        this.ownerId = ownerId;
    }

    public Book() {

    }

    public int getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(int ownerId) {
        this.ownerId = ownerId;
    }

    public double getTakePrice() {
        return takePrice;
    }

    public void setTakePrice(double takePrice) {
        this.takePrice = takePrice;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsTake() {
        return isTake;
    }

    public void setIsTake(Boolean isTake) {
        this.isTake = isTake;
    }

    public Bitmap getBookImage() {
        return bookImage;
    }

    public void setBookImage(Bitmap bookImage) {
        this.bookImage = bookImage;
    }

    public String getDisc() {
        return Disc;
    }

    public void setDisc(String disc) {
        Disc = disc;
    }

    public void updateTotalPrice() {
        this.totalPriceBook = this.price * this.quantity;
    }

}

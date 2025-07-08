package com.mycompany.bookstore.model;

public class Book {
    private int id;
    private String title;
    private String author;
    private int year;
    private double price;
    private int stock;

    // Default constructor (required for JSON serialization/deserialization)
    public Book() {}

    // Parameterized constructor
    public Book(int id, String title, String author,  int year, double price, int stock) {
        this.id = id;
        this.title = title;
        this.author = author;
        this.year = year;
        this.price = price;
        this.stock = stock;
    }

    // Getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }
    public boolean isValid() {
    return title != null && !title.trim().isEmpty()
        && author != null && !author.trim().isEmpty()
        && year > 999
        && price > 0
        && stock >= 0;
}

}

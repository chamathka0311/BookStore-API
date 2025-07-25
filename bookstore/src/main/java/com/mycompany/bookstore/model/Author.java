package com.mycompany.bookstore.model;

public class Author {
    private int id;
    private String name;

    // Default constructor
    public Author() {}

    // Parameterized constructor
    public Author(int id, String name) {
        this.id = id;
        this.name = name;
    }

    // Getters and setters
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

    // ✅ Input validation method
    public boolean isValid() {
        return id > 0 && name != null && !name.trim().isEmpty();
    }
}

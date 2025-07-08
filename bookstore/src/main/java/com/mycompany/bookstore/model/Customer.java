package com.mycompany.bookstore.model;

public class Customer {
    private int id;
    private String name;
    private String email;
    private String contact;

    // Default constructor
    public Customer() {}

    // Parameterized constructor
    public Customer(int id, String name, String email,String contact) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.contact = contact;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getContact() {
        return contact;
    }

    public void setVontact(String contact) {
        this.contact = contact;
    }

    // ✅ Validation method
    public boolean isValid() {
        return id > 0
            && name != null && !name.trim().isEmpty()
            && email != null && !email.trim().isEmpty()
            && contact!= null && !contact.matches("\\d{9}");
    }
}

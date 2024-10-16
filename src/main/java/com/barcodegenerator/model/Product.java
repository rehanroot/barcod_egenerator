package com.barcodegenerator.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String color; // Keep this for color
    private String size; // Keep this for size
    private String shape; // Add new field for shape
    private String companyName; // Add new field for company name
    private String barcodePath;

    // Constructors, getters, and setters
    public Product() {
    }

    public Product(String name, String color, String size, String shape, String companyName, String barcodePath) {
        this.name = name;
        this.color = color;
        this.size = size;
        this.shape = shape;
        this.companyName = companyName;
        this.barcodePath = barcodePath;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getShape() {
        return shape;
    }

    public void setShape(String shape) {
        this.shape = shape;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getBarcodePath() {
        return barcodePath;
    }

    public void setBarcodePath(String barcodePath) {
        this.barcodePath = barcodePath;
    }
}

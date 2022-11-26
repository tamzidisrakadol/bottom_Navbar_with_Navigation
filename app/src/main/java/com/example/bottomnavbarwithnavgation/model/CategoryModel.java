package com.example.bottomnavbarwithnavgation.model;

public class CategoryModel {
    String category;

    long id,timestamp;

    public CategoryModel() {
    }

    public CategoryModel(long id, String category, long timestamp) {
        this.id = id;
        this.category = category;
        this.timestamp = timestamp;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}

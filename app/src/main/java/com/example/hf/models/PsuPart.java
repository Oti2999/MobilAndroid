package com.example.hf.models;

public class PsuPart {
    private String name;
    private String power;

    public PsuPart() {}

    public PsuPart(String name, String power) {
        this.name = name;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public String getPower() {
        return power;
    }
}

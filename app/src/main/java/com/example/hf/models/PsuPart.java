package com.example.hf.models;

public class PsuPart {
    private String name;
    private int power;

    public PsuPart() {}

    public PsuPart(String name, int power) {
        this.name = name;
        this.power = power;
    }

    public String getName() {
        return name;
    }

    public int getPower() {
        return power;
    }
}

package com.example.hf.models;

public class RamPart {
    private String name;
    private String clock;
    private String ram;

    public RamPart() {} // Firestore-hoz kell

    public String getName() {
        return name;
    }

    public String getClock() {
        return clock;
    }

    public String getRam(){
        return ram;
    }

    public void setRam(String ram){
        this.ram = ram;
    }
}

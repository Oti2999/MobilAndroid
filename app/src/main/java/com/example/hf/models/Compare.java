package com.example.hf.models;

public class Compare {
    private String name;
    private String type;
    private int price;
    private int core;
    private String socket;

    // Üres konstruktor - kötelező Firestore számára
    public Compare() {}

    public Compare(String name, String type, int price, int core, String socket) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.core = core;
        this.socket = socket;
    }

    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public int getPrice() {
        return price;
    }

    public int getCore(){
        return core;
    }

    public String getSocket(){
        return socket;
    }

}

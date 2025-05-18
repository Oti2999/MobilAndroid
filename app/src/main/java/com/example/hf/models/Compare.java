package com.example.hf.models;

public class Compare {
    private String name;
    private String type;
    private int price;
    private int core;
    private String socket;
    private String vram;
    private String ram;
    private String clock;
    private String power;

    public Compare() {}

    public Compare(String name, String type, int price, int core, String socket, String vram, String ram, String clock, String power) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.core = core;
        this.socket = socket;
        this.vram = vram;
        this.ram = ram;
        this.clock = clock;
        this.power = power;
    }

    public String getPower(){
        return power;
    }

    public String getVram() {
        return vram;
    }

    public String getClock(){
        return clock;
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

    public String getRam(){
        return ram;
    }

}

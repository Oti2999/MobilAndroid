package com.example.hf.models;

public class CpuPart {
    private String name;
    private String socket;
    private int core;

    public CpuPart() {
        // Üres konstruktor szükséges Firestore-hoz
    }

    public CpuPart(String name, String socket, int core) {
        this.name = name;
        this.socket = socket;
        this.core = core;
    }

    public String getName() {
        return name;
    }
    public String getSocket() {
        return socket;
    }
    public int getCore() {
        return core;
    }
}

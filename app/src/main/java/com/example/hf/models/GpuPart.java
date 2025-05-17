package com.example.hf.models;

public class GpuPart {
    private String name;
    private String vram;

    public GpuPart() {
        // Szükséges Firestore-hoz
    }

    public GpuPart(String name, String vram) {
        this.name = name;
        this.vram = vram;
    }

    public String getName() {
        return name;
    }

    public String getVram() {
        return vram;
    }
}

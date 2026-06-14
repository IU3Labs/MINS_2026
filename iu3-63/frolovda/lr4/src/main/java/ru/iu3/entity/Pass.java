package ru.iu3.entity;

public class Pass {
    private int id;
    private String holderName;
    private boolean isActive;

    public Pass(int id, String name) {
        this.id = id;
        this.holderName = name;
        this.isActive = true;
    }

    public int getId() {
        return id;
    }

    public String getHolderName() {
        return holderName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void deactivate() {
        isActive = false;
    }
}

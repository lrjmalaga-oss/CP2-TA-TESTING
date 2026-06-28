package com.motorph.backend;

public class Month {
    private String name;
    private int value;

    public Month(String name, int value) {
        this.name = name;
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public String toString() {
        return name;
    }
}

package edu.csulb.bacindicator.models;

public class Drink {
    private String alcohol;
    private String measure;
    private int quantity;
    private long time;

    public Drink(String a, String m, int q) {
        alcohol = a;
        measure = m;
        quantity = q;
        time = System.nanoTime();
    }

    public String getAlcohol() {
        return alcohol;
    }

    public String getMeasure() {
        return measure;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getTime() {
        return time;
    }
}

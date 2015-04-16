package edu.csulb.bacindicator;

/**
 * Created by Johan on 16/04/2015.
 */
public class Drink {
    private String alcohol;
    private String measure;
    private int quantity;

    public Drink(String a, String m, int q) {
        alcohol = a;
        measure = m;
        quantity = q;
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
}

package edu.csulb.bacindicator.models;

import java.io.Serializable;

public class Drink implements Serializable {
    public long id;
    public String alcohol;
    public String measure;
    public int quantity;
    public float liters;
    public float vol;
    public long time;
}

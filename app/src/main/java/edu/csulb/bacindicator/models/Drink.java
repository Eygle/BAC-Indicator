package edu.csulb.bacindicator.models;

import java.io.Serializable;

/**
 * Created by Johan
 */
public class Drink implements Serializable {
    public long id;
    public String alcohol;
    public String measure;
    public int quantity;
    public float liters;
    public float vol;
    public long time;
}

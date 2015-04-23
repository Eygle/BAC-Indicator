package edu.csulb.bacindicator.models;

import android.graphics.Color;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class BAC {

    /*
    http://en.wikipedia.org/wiki/Blood_alcohol_content#Estimated_blood_ethanol_concentration_.28EBAC.29
     */
    public static float calculate(List<Drink> drinks) {
        int drinkCount = 0;

        for (Drink d : drinks)
            drinkCount += d.getQuantity();
        float bac = (0.806f * drinkCount * 1.2f) / (bodyWaterConstant() * Integer.valueOf(Settings.getWeight())) - (0.017f * 2);
        return (Math.max(bac, 0.0f));
    }

    public static int getColor(float bac) {
        if (bac > 0.08f) {
            return Color.RED;
        } else if (bac > 0.04f) {
            return Color.rgb(0xe6, 0x7e, 0x22);
        } else if (bac > 0.025f) {
            return Color.YELLOW;
        }
            return Color.GREEN;
    }

    public static long NanoToMillis(long time) {
        return TimeUnit.MILLISECONDS.convert(time, TimeUnit.NANOSECONDS);
    }

    private static float bodyWaterConstant() {
        String gender = Settings.getGender();

        return (gender.equals("1") ? 0.58f : 0.49f);
    }
}

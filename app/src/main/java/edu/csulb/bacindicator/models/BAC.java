package edu.csulb.bacindicator.models;

import android.graphics.Color;

import java.util.List;

public class BAC {

    // http://www.teamdui.com/bac-widmarks-formula/
    public static float calculate(List<Drink> drinks) {
        float bac = 0.0f;
        long currTime = System.currentTimeMillis();

        for (Drink drink : drinks) {
            float A = (drink.liters * 33.814f) * (drink.vol / 100) * drink.quantity;
            float H = ((float) currTime - drink.time) / (1000 * 60 * 60);
            bac += Math.max((A * 5.14f / 160 /* Integer.valueOf(Settings.getWeight()) */ * alcoholDistributionRatio()) - (0.015f * H), 0.0f);
        }
        return (bac);
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

    private static float alcoholDistributionRatio() {
        String gender = Settings.getGender();

        return (gender.equals("1") ? 0.73f : 0.66f);
    }
}

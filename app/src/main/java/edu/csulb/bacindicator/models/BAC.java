package edu.csulb.bacindicator.models;

import android.graphics.Color;

import java.util.List;

public class BAC {

    public static final float BAC_LEGAL_LIMIT = 0.08f;
    public static final float BAC_RECOMMENDED_LIMIT = 0.03f;

    private static final float KGS_TO_LBS = 2.20462262f;

    private static final float DEFAULT_WEIGHT = 130;

    /*
     http://www.teamdui.com/bac-widmarks-formula/
      */
    public static float calculate(List<Drink> drinks) {
        long currTime = System.currentTimeMillis();
        long lastDrink = currTime;
        float alcoholDistributionRatio = alcoholDistributionRatio();
        float bac = 0.0f;

        for (Drink drink : drinks) {
            float A = (drink.liters * 33.814f) * (drink.vol / 100) * drink.quantity;
            float H = ((float) currTime - drink.time) / (1000 * 60 * 60);
            float tmpBac = (A * 5.14f / getWeight() * alcoholDistributionRatio);
            if ((tmpBac - 0.015f * H) > 0.0f) { // if drink not dissipated, use it
                bac += tmpBac;
                lastDrink = Math.min(lastDrink, drink.time);
            }
        }
        float H = ((float) currTime - lastDrink) / (1000 * 60 * 60);
        return (Math.max(bac - (0.015f * H), 0.0f));
    }

    public static int getColor(float bac) {
        if (bac > BAC_LEGAL_LIMIT) {
            return Color.RED;
        } else if (bac > 0.05f) {
            return Color.rgb(0xe6, 0x7e, 0x22);
        } else if (bac > BAC_RECOMMENDED_LIMIT) {
            return Color.YELLOW;
        }
            return Color.GREEN;
    }

    private static float getWeight() {
        int weight = Integer.valueOf(Settings.getWeight());

        if (weight < 50 || weight > 800) {
            return DEFAULT_WEIGHT;
        }
        return (Settings.getUnit().equals("Metric") ? weight * KGS_TO_LBS : weight);
    }

    private static float alcoholDistributionRatio() {
        String gender = Settings.getGender();

        return (gender.equals("1") ? 0.73f : 0.66f);
    }
}

package edu.csulb.bacindicator.utils;

import java.util.Locale;

/**
 * Created by Johan on 30/04/2015.
 */
public class Language {
    public static String getLanguage() {
        String lang = Locale.getDefault().getLanguage();

        return lang.equals("fr") ? lang : "en";
    }
}

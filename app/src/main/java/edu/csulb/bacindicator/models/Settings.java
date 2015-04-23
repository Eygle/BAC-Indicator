package edu.csulb.bacindicator.models;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Settings {

	private static long	contact;
	private static String	height;
	private static String	weight;
	private static String gender;
	private static String	message;
	private static boolean	gameMode;
	private static boolean	appText;
	private static String unit;

	public static void init(Context context)
	{
    	SharedPreferences appPreferences = PreferenceManager.getDefaultSharedPreferences(context);

    	contact = appPreferences.getLong("friend", -1);
    	height = appPreferences.getString("height", "1");
    	weight = appPreferences.getString("weight", "1");
    	gameMode = appPreferences.getBoolean("gameMode", false);
    	appText = appPreferences.getBoolean("appText", false);
    	gender = appPreferences.getString("gender", "1");
    	message = appPreferences.getString("message", "-1");
    	setUnit(appPreferences.getString("unit", "1"));

		/*
    	System.out.println(appPreferences.getLong("friend", -1));

    	System.out.println(appPreferences.getString("height", "-1"));
    	System.out.println(appPreferences.getString("weight", "-1"));
    	System.out.println(appPreferences.getBoolean("gameMode", false));
    	System.out.println(appPreferences.getBoolean("appText", false));
    	
    	System.out.println(appPreferences.getString("gender", "-1"));
    	System.out.println(appPreferences.getString("message", "-1"));
    	System.out.println(appPreferences.getString("unit", "-1"));*/

	}
	
	
	/**
	 * @return the contact
	 */
	public static long getContact() {
		return contact;
	}
	/**
	 * @param contact the contact to set
	 */
	public static void setContact(long contact) {
		Settings.contact = contact;
	}
	/**
	 * @return the height
	 */
	public static String getHeight() {
		return height;
	}
	/**
	 * @param height the height to set
	 */
	public static void setHeight(String height) {
		Settings.height = height;
	}
	/**
	 * @return the weight
	 */
	public static String getWeight() {
		return weight;
	}
	/**
	 * @param weight the weight to set
	 */
	public static void setWeight(String weight) {
		Settings.weight = weight;
	}
	/**
	 * @return the gender
	 */
	public static String getGender() {
		return gender;
	}
	/**
	 * @param gender the gender to set
	 */
	public static void setGender(String gender) {
		Settings.gender = gender;
	}
	/**
	 * @return the message
	 */
	public static String getMessage() {
		return message;
	}
	/**
	 * @param message the message to set
	 */
	public static void setMessage(String message) {
		Settings.message = message;
	}
	/**
	 * @return the gameMode
	 */
	public static boolean isGameMode() {
		return gameMode;
	}
	/**
	 * @param gameMode the gameMode to set
	 */
	public static void setGameMode(boolean gameMode) {
		Settings.gameMode = gameMode;
	}
	/**
	 * @return the appText
	 */
	public static boolean isAppText() {
		return appText;
	}
	/**
	 * @param appText the appText to set
	 */
	public static void setAppText(boolean appText) {
		Settings.appText = appText;
	}


	public static String getUnit() {
		return unit;
	}


	public static void setUnit(String unit) {
		Settings.unit = unit;
	}
	
	
	
}

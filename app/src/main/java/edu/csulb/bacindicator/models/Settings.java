package edu.csulb.bacindicator.models;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.utils.MyContact;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.util.Log;

public class Settings {

	private static String idContact;
	private static String height;
	private static String weight;
	private static String gender;
	private static String message;
	private static String AppMessage;
	private static boolean gameMode;
	private static boolean appText;
	private static String unit;
	private static MyContact contact;
	private static Context context;

	public static void init(Context c) {
		
		AppMessage = c.getString(R.string.appMessage);
		
		context = c;
		SharedPreferences appPreferences = PreferenceManager
				.getDefaultSharedPreferences(c);

		idContact = appPreferences.getString("friend", "-1");
		height = appPreferences.getString("height", "-1");
		weight = appPreferences.getString("weight", "-1");
		gameMode = appPreferences.getBoolean("gameMode", false);
		appText = appPreferences.getBoolean("appText", false);
		gender = appPreferences.getString("gender", "Male");
		message = appPreferences.getString("message", "-1");
		setUnit(appPreferences.getString("unit", "Metric"));

		if (idContact.compareTo("-1") != 0)
			createMyContact();
	}

	
	public static String	getMessageToSend()
	{  
		addAppMessage();
		return appText ? AppMessage : message;
	}
	
	
	
	public static void createMyContact() {
		System.out.println("CRETAE CONTACT");
		String name = null;
		String number = null;
		Log.d("id_contact", Settings.getIdContact());
		Cursor cursorPhone = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
				new String[] { ContactsContract.CommonDataKinds.Phone.NUMBER,
						ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME },

				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND "
						+ ContactsContract.CommonDataKinds.Phone.TYPE + " = "
						+ ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

				new String[] { Settings.getIdContact() }, null);

		if (cursorPhone.moveToFirst()) {
			System.out.println("GO GET NAME !!");
			name = cursorPhone
					.getString(cursorPhone
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
			number = cursorPhone
					.getString(cursorPhone
							.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			Settings.addContact(new MyContact(Settings.getIdContact(), name,
					number));
		} else
			Log.d("create my contact", "No contact");
		cursorPhone.close();
	}

	public static void addContact(MyContact c) {
		contact = c;
	}

	public static MyContact getContact() {
		return contact;
	}

	/**
	 * @return the contact
	 */
	public static String getIdContact() {
		return idContact;
	}

	/**
	 * @param contact
	 *            the contact to set
	 */
	public static void setIdContact(String idcontact) {
		Settings.idContact = idcontact;
	}

	/**
	 * @return the height
	 */
	public static String getHeight() {
		return height;
	}

	/**
	 * @param height
	 *            the height to set
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
	 * @param weight
	 *            the weight to set
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
	 * @param gender
	 *            the gender to set
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
	 * @param message
	 *            the message to set
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
	 * @param gameMode
	 *            the gameMode to set
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
	 * @param appText
	 *            the appText to set
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
	
	
	public static void addAppMessage()
	{
		LocationManager locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
	        @Override
	        public void onStatusChanged(String s, int i, Bundle bundle) {}
	        @Override
	        public void onProviderEnabled(String s) {}
	        @Override
	        public void onProviderDisabled(String s) {}
	        @Override
	        public void onLocationChanged(final Location location) {}
	    });
	    Location myLocation = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
	    if (myLocation == null)
	    {
	    	Log.d("Location status", "NULL");
	    	return;
	    }
	    double longitude = myLocation.getLongitude();
	    double latitude = myLocation.getLatitude();
	    
		
		//Locations loc = new Locations(context);
		
	    AppMessage += "https://www.google.co.id/maps/@"+latitude+","+longitude;
	    Log.d("App Message: " , AppMessage);
	}

}

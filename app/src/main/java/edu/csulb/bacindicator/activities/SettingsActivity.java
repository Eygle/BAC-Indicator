package edu.csulb.bacindicator.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.models.Settings;

@SuppressLint("NewApi")
public class SettingsActivity extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	private Context context = this;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_app);

		Settings.init(context);

		
		final CheckBoxPreference checkbox = (CheckBoxPreference) findPreference("appText");
		final EditTextPreference message = (EditTextPreference) findPreference("message");

		final EditTextPreference weight = (EditTextPreference) findPreference("weight");

		final ListPreference gender = (ListPreference) findPreference("gender");
		final ListPreference units = (ListPreference) findPreference("unit");
		final Preference contact = findPreference("friend");

		
		
		if (Settings.getMessage().compareTo("-1") != 0)
		message.setSummary(Settings.getMessage());
		
		if (Settings.getWeight().compareTo("-1") != 0)
		{
			if (Settings.getUnit().compareTo("Metric") == 0)
			weight.setSummary(Settings.getWeight() + " kg");
			else
			weight.setSummary(Settings.getWeight() + " lbs");
		}
		if (Settings.getGender().compareTo("-1") != 0)
		gender.setSummary(Settings.getGender());
		if (Settings.getUnit().compareTo("-1") != 0)
		units.setSummary(Settings.getUnit());
		if (Settings.getContact() != null)
			contact.setSummary(Settings.getContact().getName());

		
		
		
		message.setEnabled(!checkbox.isChecked());

		OnPreferenceClickListener checkclick = new OnPreferenceClickListener() {
			public boolean onPreferenceClick(Preference preference) {
				message.setEnabled(!checkbox.isChecked());
				return true;
			}
		};

		checkbox.setOnPreferenceClickListener(checkclick);

		PreferenceManager.getDefaultSharedPreferences(this)
				.registerOnSharedPreferenceChangeListener(this);

		Preference customContact = findPreference("friend");

		customContact.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {
						Intent i = new Intent(Intent.ACTION_PICK,
								Contacts.CONTENT_URI);
						startActivityForResult(i, 1);
						return true;
					}
				});
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		
		Settings.init(context);
		
		@SuppressWarnings("deprecation")
		Preference pref = findPreference(key);
		if (key.compareTo("unit") == 0)
		{
			@SuppressWarnings("deprecation")
			Preference k = findPreference("weight");
			EditTextPreference etp = (EditTextPreference) k;

			if (Settings.getUnit().compareTo("Metric") == 0)
                k.setSummary(etp.getText() + " kg");
    			else
                k.setSummary(etp.getText() + " lbs");
		}
        if (pref instanceof EditTextPreference) {
        	EditTextPreference etp = (EditTextPreference) pref;
            pref.setSummary(etp.getText());
            System.out.println(key);
        	if (key.compareTo("weight") == 0)
        	{
        		if (Settings.getUnit().compareTo("Metric") == 0)
                    pref.setSummary(etp.getText() + " kg");
        			else
                    pref.setSummary(etp.getText() + " lbs");
        	}
            
        }
        else  if (pref instanceof ListPreference) {
        	ListPreference etp = (ListPreference) pref;
            pref.setSummary(etp.getValue());
        }
        else  if (pref.getKey().compareTo("friend") == 0 && Settings.getContact() != null) {
        	//Preference etp = (Preference) pref;
            pref.setSummary(Settings.getContact().getName());
        }
   	
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			if (resultCode == RESULT_OK) {

				Uri contactData = data.getData();
				Cursor cursor = managedQuery(contactData, null, null, null,
						null);
				if (cursor.moveToFirst()) {
					String newId = cursor.getString(cursor.getColumnIndexOrThrow(Contacts._ID));

					
					SharedPreferences appPreferences = PreferenceManager
							.getDefaultSharedPreferences(this);
					SharedPreferences.Editor editor = appPreferences.edit();

					editor.putString("friend", newId);
					editor.apply();
					Settings.setIdContact(newId);
					 String name =
					 cursor.getString(cursor.getColumnIndexOrThrow(Contacts.DISPLAY_NAME));
					 Log.i("New contact Added",
					 "ID of newly added contact is : " + newId + " Name is : "
					 + name);
						
					// System.out.println("name: " + name);
					 Settings.createMyContact();

					//System.out.println("activity result" + Settings.getContact().getName());
				}
			}
		}
	}
	
	
	
	
	
}
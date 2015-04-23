package edu.csulb.bacindicator.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.provider.ContactsContract.Contacts;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.models.Settings;

public class SettingsActivity extends PreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {

	private Context context = this;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preference_app);

		final CheckBoxPreference checkbox = (CheckBoxPreference) findPreference("appText");
		final EditTextPreference message = (EditTextPreference) findPreference("message");

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

		customContact
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

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
					long newId = cursor.getLong(cursor
							.getColumnIndexOrThrow(Contacts._ID));

					SharedPreferences appPreferences = PreferenceManager
							.getDefaultSharedPreferences(this);
					SharedPreferences.Editor editor = appPreferences.edit();

					editor.putLong("friend", newId);
					editor.apply();

					// String name =
					// cursor.getString(cursor.getColumnIndexOrThrow(Contacts.DISPLAY_NAME));
					// Log.i("New contact Added",
					// "ID of newly added contact is : " + newId + " Name is : "
					// + name);
				}
			}
		}
	}

}
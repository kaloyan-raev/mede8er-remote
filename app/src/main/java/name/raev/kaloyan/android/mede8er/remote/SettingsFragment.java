package name.raev.kaloyan.android.mede8er.remote;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

public class SettingsFragment extends PreferenceFragmentCompat implements
		OnSharedPreferenceChangeListener {

	public static final String KEY_PREF_IP = "pref_ip";
	public static final String KEY_PREF_VIBRATE = "pref_vibrate";

	@Override
	public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
		setPreferencesFromResource(R.xml.preferences, rootKey);
		
		updateIPPrefSummary(getPreferenceScreen().getSharedPreferences());
	}

	@Override
	public void onResume() {
		super.onResume();
		getPreferenceScreen().getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
	}

	@Override
	public void onPause() {
		super.onPause();
		getPreferenceScreen().getSharedPreferences()
				.unregisterOnSharedPreferenceChangeListener(this);
	}

	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key.equals(KEY_PREF_IP)) {
			updateIPPrefSummary(sharedPreferences);
		}
	}
	
	private void updateIPPrefSummary(SharedPreferences sharedPreferences) {
		Preference pref = findPreference(KEY_PREF_IP);
		// Set summary to be the entered IP address
		pref.setSummary(sharedPreferences.getString(KEY_PREF_IP, ""));
	}

}

package name.raev.kaloyan.android.mede8er.remote;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;

public class SettingsActivity extends FragmentActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		// Display the fragment as the main content.
        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, new SettingsFragment())
                .commit();
	}

}

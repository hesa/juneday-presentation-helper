package se.juneday.presentationhelper.ui.pref;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import se.juneday.presentationhelper.R;

public class PreferenceFragment extends PreferenceFragmentCompat {

    private String LOG_TAG = PreferenceFragment.class.getCanonicalName();

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.preferences, rootKey);

        PreferenceManager.setDefaultValues(getActivity(), R.xml.preferences, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        prefs.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
    }

    private void updateCheckBox(String id, boolean value) {
        ((CheckBoxPreference)getPreferenceManager().findPreference(id)).setChecked(value);
    }

    SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
            Log.d(LOG_TAG, "onSharedPreferenceChanged()  " + key );

            if (key.equals("auth_pin")) {
                boolean checked = ((CheckBoxPreference)getPreferenceManager().findPreference(key)).isChecked();
 //               Log.d(LOG_TAG, " pin");
                updateCheckBox("auth_user", !checked);
            } else if (key.equals("auth_user")) {
                boolean checked = ((CheckBoxPreference)getPreferenceManager().findPreference(key)).isChecked();
   //             Log.d(LOG_TAG, " user");
                updateCheckBox("auth_pin", !checked);
            } else {
                Log.d(LOG_TAG, "  uh oh: " + key);
                Log.d(LOG_TAG, "  uh oh: " + getString(R.string.auth_pin_code));
            }
        }
    };

}

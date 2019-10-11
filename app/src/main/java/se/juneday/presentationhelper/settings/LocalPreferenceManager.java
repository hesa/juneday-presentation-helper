package se.juneday.presentationhelper.settings;

import android.app.Activity;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.preference.PreferenceManager;

import java.text.ParseException;

import se.juneday.presentationhelper.domain.AuthType;

public class LocalPreferenceManager implements SettingsManager {

    private static final String LOG_TAG = LocalPreferenceManager.class.getSimpleName();
    private SharedPreferences preferences;
    private Activity activity;
    private static LocalPreferenceManager instance;

    public static LocalPreferenceManager getInstance(Activity activity) {
        if (instance==null) {
            instance = new LocalPreferenceManager(activity);
        }
        return instance;
    }

    private LocalPreferenceManager(Activity activity) {
        preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        this.activity = activity;
    }

    @Override
    public AuthType authType() {
        if (preferences.getBoolean("auth_user", false)) {
            return AuthType.AUTH_TYPE_USER_PASSWORD;
        }
        return AuthType.AUTH_TYPE_PIN;
    }

    @Override
    public int pinCode() {
        Log.d(LOG_TAG, "pinCode()");
        Log.d(LOG_TAG, "pinCode()  preferences: " + preferences);
        Log.d(LOG_TAG, "pinCode()  activity:    " + activity);
        Log.d(LOG_TAG, "pinCode()  value:       " + preferences.getString("pref_pin", "uh oh"));
        return Integer.parseInt(preferences.getString("pref_pin", "-1"));

    }

    @Override
    public String user() {
        return null;
    }

    @Override
    public String password() {
        return null;
    }
}

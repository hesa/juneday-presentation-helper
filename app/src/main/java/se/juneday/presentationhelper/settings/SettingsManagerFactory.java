package se.juneday.presentationhelper.settings;

import android.app.Activity;

public class SettingsManagerFactory {

    public static SettingsManager getSettingsManager(Activity activity) {
        return LocalPreferenceManager.getInstance(activity);
    }

}

package se.juneday.presentationhelper.ui.pref;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import se.juneday.presentationhelper.R;

public class PreferenceActivity extends AppCompatActivity {

    private static final String LOG_TAG = PreferenceActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_preference);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.settings_container, new PreferenceFragment())
                .commit();

    }


}

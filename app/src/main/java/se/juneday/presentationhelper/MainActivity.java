package se.juneday.presentationhelper;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import se.juneday.presentationhelper.network.ServerConnection;
import se.juneday.presentationhelper.network.SocketConnection;
import se.juneday.presentationhelper.settings.SettingsManager;
import se.juneday.presentationhelper.settings.SettingsManagerFactory;
import se.juneday.presentationhelper.ui.loudmouth.LoudMouthFragment;
import se.juneday.presentationhelper.ui.main.MainFragment;
import se.juneday.presentationhelper.ui.main.MainViewModel;
import se.juneday.presentationhelper.ui.pref.PreferenceActivity;

public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    FragmentPagerAdapter adapterViewPager;
    private Menu menu;
    private boolean online;

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 3;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Fragment # 0 - This will show FirstFragment
                    return MainFragment.newInstance();
                case 1: // Fragment # 0 - This will show FirstFragment different title
                    return LoudMouthFragment.newInstance();
                case 2: // Fragment # 1 - This will show SecondFragment
                    return MainFragment.newInstance();
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            return "Page " + position;
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
/*        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, MainFragment.newInstance())
                    .commitNow();

        }
*/
        ViewPager vpPager = (ViewPager) findViewById(R.id.pager);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);

/*
        if (connection==null) {
            try {
                connection = new SocketConnection("einar", "hackner");
            } catch (SocketConnection.SocketException se) {
                Log.d(LOG_TAG, " failed creating socket");
            }
        }

*/
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        Log.d(LOG_TAG, "Setting toolbar: " + myToolbar);
        setSupportActionBar(myToolbar);


        Session.connection = new ServerConnection(this);
        Session.connection.setServerStatusListener(online -> { this.online = online; handleNetworkUpdate(online);});
    }


    private void handleNetworkUpdate(boolean online) {
        Log.d(LOG_TAG, " update status: " + online);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                setNetworkOnline(online);
                // change UI elements here
            }
        });
    }


    // Menu icons are inflated just as they were with actionbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.action_bar_menu, menu);
        this.menu = menu;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about_item:
                return true;

            case R.id.settings_item:
                Intent intent = new Intent(this, PreferenceActivity.class);
                startActivity(intent);
                return true;

            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void setNetworkOnline(boolean online) {
        if (online) {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_wifi_24px));
        } else {
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_wifi_off_24px));
        }
    }


}
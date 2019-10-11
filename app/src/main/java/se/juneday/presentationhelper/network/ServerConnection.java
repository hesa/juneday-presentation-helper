package se.juneday.presentationhelper.network;

import android.app.Activity;
import android.util.Log;

import se.juneday.presentationhelper.domain.AuthType;
import se.juneday.presentationhelper.settings.SettingsManager;
import se.juneday.presentationhelper.settings.SettingsManagerFactory;

public class ServerConnection {

    private Activity activity;
    private static final String LOG_TAG = ServerConnection.class.getSimpleName();
    private SocketConnection connection;
    private ServerStatusListener listener;


    public ServerConnection(Activity activity) {
        this.activity = activity;
    }

    public interface ServerStatusListener {
        public void networkStatusOnline(boolean online);
    }

    public void setServerStatusListener(ServerStatusListener listener) {
        this.listener = listener;
    }

    public boolean connectionOk() {
        if (connection!=null) {
            Log.d(LOG_TAG, "connectionOk()  =>  " + connection.connectionOk());
            return connection.connectionOk();
        }
        Log.d(LOG_TAG, "connectionOk()  =>  false");
        return false;
    }

    public boolean check() {
        try {
            return connection.check();
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void sendCommand(String command) {
        Log.d(LOG_TAG, "sendCommand " + command);
        try {
            if (connection == null) {
                SettingsManager manager = SettingsManagerFactory.getSettingsManager(activity);
                Log.d(LOG_TAG, "Logging in with auth type: " + manager.authType());
                if ( manager.authType()== AuthType.AUTH_TYPE_PIN) {
                    Log.d(LOG_TAG, "Logging in with pin code: " + manager.pinCode());
                    connection = new SocketConnection(manager.pinCode());
                    connection.setSocketStatusListener(listener);
                } else if ( manager.authType()== AuthType.AUTH_TYPE_USER_PASSWORD) {
                    System.exit(1);
                }
            }
            connection.sendCommand(command);
        } catch (SocketConnection.SocketException e) {
            e.printStackTrace();
            listener.networkStatusOnline(false);
            connection = null;
        }
    }

}

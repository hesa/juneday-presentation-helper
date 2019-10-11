package se.juneday.presentationhelper.network;

import android.os.Handler;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class SocketConnection {

    private static final String LOG_TAG = SocketConnection.class.getSimpleName();
    private static String REMOTE_SERVER_IP = "10.0.2.2";
    private static int REMOTE_SERVER_PORT = 9876;
    private String user;
    private String password;
    private int pinCode = -1;
    private boolean authorized;
    private boolean socketWorking;
    private ServerConnection.ServerStatusListener listener;

    public class SocketException extends Exception {
        public SocketException(String msg, Exception cause) {
            super(msg, cause);
        }
    }

    public void setSocketStatusListener(ServerConnection.ServerStatusListener  listener) {
        this.listener = listener;
    }


    public boolean connectionOk() {
        Log.d(LOG_TAG, "connectionOk() " + authorized + " && " + socketWorking + " ==> " + (authorized && socketWorking));
        return authorized && socketWorking;
    }

  //  private static SocketConnection instance;
    private Socket socket;
    private PrintWriter output;
    private OutputStream out;
    private BufferedReader input;

    public SocketConnection(String user, String password) throws SocketException {
        this.user = user;
        this.password = password;
        Log.d(LOG_TAG, "SocketConnection()");
    }

    public SocketConnection(int pin) throws SocketException {
        this.pinCode = pin;
        Log.d(LOG_TAG, "SocketConnection(): " + pin);
    }

/*    public static SocketConnection getInstance() throws SocketException{
        if (instance == null) {
            instance = new SocketConnection();
        }
        return instance;
    }
*/

    public boolean check() throws IOException {
        return sendCommandAndCheck("check", "OK");
    }

    private boolean sendCommandAndCheck(String command, String expectedResponse) throws IOException{
        Log.d(LOG_TAG, "write to server: " + command);
        output.println(command);
        output.flush();
        input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        Log.d(LOG_TAG, "read ack from server");
        String serverMessage = input.readLine();
        Log.d(LOG_TAG, "from server: " + serverMessage);

        boolean returnValue = serverMessage!=null && serverMessage.contains(expectedResponse);
        listener.networkStatusOnline(returnValue);
        return (returnValue);
    }

    /* must be called from a thread */
    private void setupSocket() throws SocketException, IOException {
        Log.d(LOG_TAG, "run() something is null: " + socket + " | " + output + " | " + authorized);
        socket = new Socket(REMOTE_SERVER_IP, REMOTE_SERVER_PORT);
        out = socket.getOutputStream();
        output = new PrintWriter(out);
        Log.d(LOG_TAG, "logging in ...");



        if ( user != null &&
                sendCommandAndCheck("login " + user + " " + password, "login succeded") ) {
            Log.d(LOG_TAG, "authorized");
            authorized = true;
            socketWorking = true;
            if (listener != null) {
                listener.networkStatusOnline(true);
            }
        } else if ( pinCode != -1 &&
                sendCommandAndCheck("pin " + pinCode, "login with pin succeded") ) {
            Log.d(LOG_TAG, "authorized");
            authorized = true;
            socketWorking = true;
            if (listener != null) {
                listener.networkStatusOnline(true);
            }
        } else {
            authorized = false;
            socketWorking = false;
            Log.d(LOG_TAG, "failed to authorized");
            if (listener != null) {
                listener.networkStatusOnline(false);
            }
            throw new SocketException("Failed to authorize user", null);
        }
    }

    public void sendCommand(final String command) throws SocketException{
        final Handler handler = new Handler();
        Thread thread = new Thread(new Runnable() {
        @Override
        public void run() {
            Log.d(LOG_TAG, "run()");
            try {
                if (socket == null || output == null || !authorized || !socketWorking) {
                    setupSocket();
                }

                socketWorking = sendCommandAndCheck(command, "OK");
                listener.networkStatusOnline(true);
            } catch (java.io.IOException e) {
                    e.printStackTrace();
                    output=null;
                    out=null;
                    socket=null;
                    authorized = false;
                    socketWorking = false;
                    listener.networkStatusOnline(false);
                    return;
                } catch (SocketException e) {
                    e.printStackTrace();
                    return;

            }
        }
        });
        thread.start();
    }
}
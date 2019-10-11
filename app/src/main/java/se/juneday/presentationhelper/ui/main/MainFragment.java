package se.juneday.presentationhelper.ui.main;

import androidx.core.content.ContextCompat;
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

import se.juneday.presentationhelper.MainActivity;
import se.juneday.presentationhelper.R;
import se.juneday.presentationhelper.Session;
import se.juneday.presentationhelper.Utils;
import se.juneday.presentationhelper.network.ServerConnection;

public class MainFragment extends Fragment {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();
    private MainViewModel mViewModel;
    private int stopPressCounter;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        Log.d(LOG_TAG, "onCreateView()");

        return inflater.inflate(R.layout.main_fragment, container, false);
    }

    private void setupListener(int id, View.OnClickListener l) {
        ((ImageButton)getActivity().findViewById(id)).setOnClickListener(l);
    }

    private void setupListeners() {
        Log.d(LOG_TAG, "setupListeners()");
        setupListener(R.id.next_button, v -> onForwardClick(v));
        setupListener(R.id.prev_button, v -> onBackClick(v));
        setupListener(R.id.stop_button, v -> onStopClick(v));
    }


    /*    public void setupPinCode(LinearLayout layout) {
        TextView pinLabel = new TextView(getContext());
        pinLabel.setText(R.string.pin_code_label);
        EditText pinEdit = new EditText(getContext());

        layout.addView(pinLabel);
        layout.addView(pinEdit);

        pinEdit.setInputType(InputType.TYPE_CLASS_NUMBER);
        pinEdit.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                int value = Integer.parseInt(String.valueOf(s));
                Log.d(LOG_TAG, " setting pin code: " + value);
                mViewModel.pinCode = value;
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

    }
*/
    public void onStart() {
        super.onStart();

/*        LinearLayout layout = getActivity().findViewById(R.id.auth_layout);
        Log.d(LOG_TAG, " auth layout: " + layout);
        setupPinCode(layout);
*/
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(getActivity()).get(MainViewModel.class);
        Log.d(LOG_TAG, " fragment model: " + mViewModel);
        Log.d(LOG_TAG, " what is this????: ");
        // TODO: Use the ViewModel
        setupListeners();
        Log.d(LOG_TAG, " what is this????: ");
    }


    public void onForwardClick(View v) {
        stopPressCounter = 0;
        Log.d(LOG_TAG, "onForwardClick() status: " + Session.connection.connectionOk());
        Session.connection.sendCommand("right");
    }

    public void onBackClick(View v) {
        stopPressCounter = 0;
        Log.d(LOG_TAG, "onBackClick");
        Session.connection.sendCommand("left");
    }

    public void onStopClick(View v) {
        Log.d(LOG_TAG, "stop");
        if (stopPressCounter==0) {
            Log.d(LOG_TAG, "stop: no");
        } else if (stopPressCounter == 1){
            Log.d(LOG_TAG, "stop: yes, stoping");
            Utils.showToast(getActivity(), "Stopping presentation - one more to stop server");
        } else if (stopPressCounter == 2){
            Utils.showToast(getActivity(), "Stopping server");
            Log.d(LOG_TAG, "stop: yes, quit. Really, will do");
            Session.connection.sendCommand("exit");
        } else {
            Log.d(LOG_TAG, "stop: yes, woops");
        }
        Log.d(LOG_TAG, "stop end");
        stopPressCounter++;

    }


}

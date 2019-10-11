package se.juneday.presentationhelper;

import android.app.Activity;
import android.widget.Toast;

public class Utils {

    public static void showToast(Activity activity, String message) {
        Toast toast=Toast.makeText(activity,message,Toast.LENGTH_SHORT);
        toast.setMargin(50,50);
        toast.show();
    }

}

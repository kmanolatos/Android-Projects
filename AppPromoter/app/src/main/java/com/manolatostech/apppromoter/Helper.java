package com.manolatostech.apppromoter;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.regex.Pattern;

/**
 * Created by kmanolatos on 17/1/2018.
 */

public class Helper extends AppCompatActivity {
    static public boolean checkField(EditText edit) {
        String regex = "^[\\p{L}0-9\\p{Blank}]+";
        if (edit.getText().toString().trim().contains("drop") || edit.getText().toString().trim().contains("select") || edit.getText().toString().trim().contains("delete") || edit.getText().toString().trim().contains("update") || edit.getText().toString().trim().contains("insert"))
            return false;
        else {
            if (edit.getText().toString().trim().matches(regex))
                return true;
            else
                return false;
        }
    }

    static public boolean checkPasswordField(EditText edit) {
        if (edit.getText().toString().trim().contains("drop") || edit.getText().toString().trim().contains("select") || edit.getText().toString().trim().contains("delete") || edit.getText().toString().trim().contains("update") || edit.getText().toString().trim().contains("insert"))
            return false;
        else
            return true;
    }

    static public boolean isOnline(Context context) {
        boolean connected = false;
        try {
            ConnectivityManager connectivityManager;
            NetworkInfo wifiInfo, mobileInfo;
            connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            connected = networkInfo != null && networkInfo.isAvailable() &&
                    networkInfo.isConnected();
            return connected;

        } catch (Exception e) {

        }
        return connected;
    }

    public static boolean checkEmail(String email) {
        Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
    }

    public static void hideSoftKeyboard(Activity activity) {
        if (activity.getCurrentFocus() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        }
    }
}


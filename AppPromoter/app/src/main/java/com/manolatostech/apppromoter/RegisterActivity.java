package com.manolatostech.apppromoter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.widget.TextView;
import android.widget.Toast;

import java.util.UUID;


/**
 * Created by kmanolatos on 8/1/2018.
 */

public class RegisterActivity extends AppCompatActivity {
    EditText email, user, pass, surname, name, confPass;
    TextView toastMessage;
    Button register;
    Toast toast;
    ProgressBar progressBar;
    int h, w;
    String tmDevice, tmSerial, androidId;
    RelativeLayout relativeLayout;
    RegisterActivity registerActivity;

    @SuppressLint("MissingPermission")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#0193D7"));
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(RegisterActivity.this);
                return false;
            }
        });
        registerActivity = this;
        TelephonyManager tm = (TelephonyManager) getBaseContext().getSystemService(Context.TELEPHONY_SERVICE);
        tmDevice = "" + tm.getDeviceId();
        tmSerial = "" + tm.getSimSerialNumber();
        androidId = "" + android.provider.Settings.Secure.getString(getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);

        UUID deviceUuid = new UUID(androidId.hashCode(), ((long) tmDevice.hashCode() << 32) | tmSerial.hashCode());
        final String deviceId = deviceUuid.toString();
        Display display = getWindowManager().getDefaultDisplay();
        w = display.getWidth();
        h = display.getHeight();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getLayoutParams().height = h * 95 / 100;
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl);
        name = (EditText) findViewById(R.id.name);
        surname = (EditText) findViewById(R.id.surname);
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        confPass = (EditText) findViewById(R.id.confPass);
        email = (EditText) findViewById(R.id.email);
        register = (Button) findViewById(R.id.register);
        RelativeLayout.LayoutParams relativeParams;
        toastMessage = new TextView(this);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setTypeface(null, Typeface.BOLD);
        toastMessage.setPadding(w * 5 / 100, h * 2 / 100, w * 5 / 100, h * 2 / 100);
        toastMessage.setTextSize((float) (w * 1 / 100));
        toast = Toast.makeText(getApplicationContext(), null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        relativeParams = (RelativeLayout.LayoutParams) name.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 2 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        name.setLayoutParams(relativeParams);
        name.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);


        relativeParams = (RelativeLayout.LayoutParams) surname.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 14 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        surname.setLayoutParams(relativeParams);
        surname.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        surname.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);


        relativeParams = (RelativeLayout.LayoutParams) user.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 26 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        user.setLayoutParams(relativeParams);
        user.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        user.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);


        relativeParams = (RelativeLayout.LayoutParams) pass.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 38 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        pass.setLayoutParams(relativeParams);
        pass.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        pass.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);


        relativeParams = (RelativeLayout.LayoutParams) confPass.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 50 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        confPass.setLayoutParams(relativeParams);
        confPass.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        confPass.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);


        relativeParams = (RelativeLayout.LayoutParams) email.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 62 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        email.setLayoutParams(relativeParams);
        email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        email.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        relativeParams = (RelativeLayout.LayoutParams) register.getLayoutParams();
        relativeParams.setMargins(w * 36 / 100, h * 79 / 100, 0, 0);
        relativeParams.width = w * 30 / 100;
        relativeParams.height = h * 10 / 100;
        register.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        register.setLayoutParams(relativeParams);
        register.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                if (!Helper.isOnline(getApplicationContext())) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("No internet connection!");
                    toast.setView(toastMessage);
                    toast.show();
                } else if (name.getText().toString().trim().length() == 0 || surname.getText().toString().trim().length() == 0 || user.getText().toString().trim().length() == 0 || pass.getText().toString().trim().length() == 0 || confPass.getText().toString().trim().length() == 0 || email.getText().toString().trim().length() == 0) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("No empty fields allowed!");
                    toast.setView(toastMessage);
                    toast.show();
                } else if (!Helper.checkField(name) || !Helper.checkField(surname) || !Helper.checkField(user) || !Helper.checkPasswordField(pass) || !Helper.checkPasswordField(confPass) || !Helper.checkEmail(email.getText().toString().trim())) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("Invalid fields!");
                    toast.setView(toastMessage);
                    toast.show();
                } else if (!pass.getText().toString().equals(confPass.getText().toString())) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("Passwords don't match!");
                    toast.setView(toastMessage);
                    toast.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(RegisterActivity.this);
                    builder.setTitle("Confirm");
                    builder.setMessage("Are you sure?");
                    builder.setCancelable(false);
                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            if (!Helper.isOnline(getApplicationContext())) {
                                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                toastMessage.setText("No internet connection!");
                                toast.setView(toastMessage);
                                toast.show();
                            } else
                                new RegisterUser(registerActivity, toastMessage, h, progressBar, name, surname, user, pass, confPass, email, register).execute(name.getText().toString().trim(), surname.getText().toString().trim(), user.getText().toString().trim(), pass.getText().toString().trim(), email.getText().toString().trim(), deviceId);
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();
                    alert.getButton(alert.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    alert.getButton(alert.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                }
            }
        });
    }
}

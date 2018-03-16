package com.manolatostech.apppromoter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class ForgotActivity extends AppCompatActivity {
    TextView toastMessage;
    Toast toast;
    EditText email;
    Button retrieve;
    ProgressBar progressBar;
    int h, w;
    ForgotActivity forgotActivity;
    AdView mAdView;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forgot_activity);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#0193D7"));
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(ForgotActivity.this);
                return false;
            }
        });
        forgotActivity = this;
        Display display = getWindowManager().getDefaultDisplay();
        w = display.getWidth();
        h = display.getHeight();
        mAdView = (AdView) findViewById(R.id.adView);
        adRequest = new AdRequest.Builder()
                .build();

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {

            }

            @Override
            public void onAdClosed() {

            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mAdView.loadAd(adRequest);
            }

            @Override
            public void onAdLeftApplication() {

            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
            }
        });
        mAdView.loadAd(adRequest);
        email = (EditText) findViewById(R.id.email);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getLayoutParams().height = h * 95 / 100;
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        retrieve = (Button) findViewById(R.id.retrieve);
        RelativeLayout.LayoutParams relativeParams;
        toastMessage = new TextView(this);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setTypeface(null, Typeface.BOLD);
        toastMessage.setPadding(w * 5 / 100, h * 2 / 100, w * 5 / 100, h * 2 / 100);
        toastMessage.setTextSize((float) (w * 1 / 100));
        toast = Toast.makeText(getApplicationContext(), null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);

        relativeParams = (RelativeLayout.LayoutParams) email.getLayoutParams();
        relativeParams.setMargins(w * 18 / 100, h * 32 / 100, 0, 0);
        relativeParams.width = w * 62 / 100;
        relativeParams.height = h * 10 / 100;
        email.setLayoutParams(relativeParams);
        email.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        email.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);

        relativeParams = (RelativeLayout.LayoutParams) retrieve.getLayoutParams();
        relativeParams.setMargins(w * 34 / 100, h * 50 / 100, 0, 0);
        relativeParams.width = w * 30 / 100;
        relativeParams.height = h * 10 / 100;
        retrieve.setLayoutParams(relativeParams);
        retrieve.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        retrieve.setOnClickListener(new View.OnClickListener() {
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
                } else if (email.getText().toString().trim().length() == 0) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("The field is required!");
                    toast.setView(toastMessage);
                    toast.show();
                } else if (!Helper.checkEmail(email.getText().toString().trim())) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("Invalid email!");
                    toast.setView(toastMessage);
                    toast.show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotActivity.this);
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
                                new RetrieveAccount(forgotActivity, toastMessage, h, progressBar, email, retrieve).execute(email.getText().toString().trim());
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

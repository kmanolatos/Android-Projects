package com.manolatostech.apppromoter;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
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

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.Arrays;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    TextView toastMessage, text;
    Toast toast;
    EditText user, pass;
    Button logIn, register, forgot, logInFb;
    ProgressBar progressBar;
    int h, w, fbLogin = 0;
    String latestVersion = "", currentVersion;
    TimerTask timerTask;
    MainActivity mainActivity;
    AdView mAdView;
    AdRequest adRequest;
    private InterstitialAd mInterstitialAd;
    CallbackManager callbackManager;
    LoginButton loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#0193D7"));
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(MainActivity.this);
                return false;
            }
        });
        callbackManager = CallbackManager.Factory.create();
        mainActivity = this;
        MyDbHelper dbData = new MyDbHelper(mainActivity);
        dbData.create();
        user = (EditText) findViewById(R.id.user);
        pass = (EditText) findViewById(R.id.pass);
        currentVersion = getCurrentVersion();
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
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-5593953116329757/2507603251");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                mInterstitialAd.loadAd(new AdRequest.Builder().build());
                checkLogIn();
            }
        });
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getLayoutParams().height = h * 95 / 100;
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        logIn = (Button) findViewById(R.id.logIn);
        logInFb = (Button) findViewById(R.id.loginFb);
        register = (Button) findViewById(R.id.register);
        forgot = (Button) findViewById(R.id.forgot);
        text = (TextView) findViewById(R.id.myText);
        loginButton = (LoginButton) findViewById(R.id.login_button);
        RelativeLayout.LayoutParams relativeParams;
        toastMessage = new TextView(this);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setTypeface(null, Typeface.BOLD);
        toastMessage.setPadding(w * 5 / 100, h * 2 / 100, w * 5 / 100, h * 2 / 100);
        toastMessage.setTextSize((float) (w * 1 / 100));
        toast = Toast.makeText(getApplicationContext(), null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        relativeParams = (RelativeLayout.LayoutParams) text.getLayoutParams();
        relativeParams.setMargins(w * 28 / 100, h * 5 / 100, 0, 0);
        relativeParams.width = w;
        relativeParams.height = h * 10 / 100;
        text.setLayoutParams(relativeParams);
        text.setTypeface(null, Typeface.BOLD_ITALIC);
        text.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 7 / 100);


        relativeParams = (RelativeLayout.LayoutParams) user.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 18 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        user.setLayoutParams(relativeParams);
        user.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        user.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);


        relativeParams = (RelativeLayout.LayoutParams) pass.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 30 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        pass.setLayoutParams(relativeParams);
        pass.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        pass.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);


        relativeParams = (RelativeLayout.LayoutParams) logIn.getLayoutParams();
        relativeParams.setMargins(w * 18 / 100, h * 44 / 100, 0, 0);
        relativeParams.width = w * 30 / 100;
        relativeParams.height = h * 10 / 100;
        logIn.setLayoutParams(relativeParams);
        logIn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fbLogin = 0;
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                } else {
                    checkLogIn();
                }
            }
        });


        relativeParams = (RelativeLayout.LayoutParams) register.getLayoutParams();
        relativeParams.setMargins(w * 52 / 100, h * 44 / 100, 0, 0);
        relativeParams.width = w * 30 / 100;
        relativeParams.height = h * 10 / 100;
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
                } else if (!currentVersion.equals(latestVersion)) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("You have to update to the latest version!");
                    toast.setView(toastMessage);
                    toast.show();
                } else {
                    register.setEnabled(false);
                    logIn.setEnabled(false);
                    forgot.setEnabled(false);
                    logInFb.setEnabled(false);
                    Intent myIntent = new Intent(MainActivity.this, RegisterActivity.class);
                    startActivity(myIntent);
                    register.setEnabled(true);
                    logIn.setEnabled(true);
                    forgot.setEnabled(true);
                    logInFb.setEnabled(true);
                    user.setText("");
                    pass.setText("");
                    user.requestFocus();
                }
            }
        });


        relativeParams = (RelativeLayout.LayoutParams) forgot.getLayoutParams();
        relativeParams.setMargins(w * 28 / 100, h * 58 / 100, 0, 0);
        relativeParams.width = w * 45 / 100;
        relativeParams.height = h * 10 / 100;
        forgot.setLayoutParams(relativeParams);
        forgot.setOnClickListener(new View.OnClickListener() {
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
                } else if (!currentVersion.equals(latestVersion)) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("You have to update to the latest version!");
                    toast.setView(toastMessage);
                    toast.show();
                } else {
                    register.setEnabled(false);
                    logIn.setEnabled(false);
                    forgot.setEnabled(false);
                    logInFb.setEnabled(false);
                    Intent myIntent = new Intent(MainActivity.this, ForgotActivity.class);
                    startActivity(myIntent);
                    register.setEnabled(true);
                    logIn.setEnabled(true);
                    forgot.setEnabled(true);
                    logInFb.setEnabled(true);
                    user.setText("");
                    pass.setText("");
                    user.requestFocus();
                }
            }
        });

        relativeParams = (RelativeLayout.LayoutParams) logInFb.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 74 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 6 / 100;
        logInFb.setLayoutParams(relativeParams);
        logInFb.setBackgroundResource(R.drawable.facebook);
        logInFb.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fbLogin = 1;
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                if (!Helper.isOnline(getApplicationContext())) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("No internet connection!");
                    toast.setView(toastMessage);
                    toast.show();
                } else if (!currentVersion.equals(latestVersion)) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("You have to update to the latest version!");
                    toast.setView(toastMessage);
                    toast.show();
                } else {
                    LoginManager.getInstance().logInWithReadPermissions(MainActivity.this, Arrays.asList("public_profile"));
                    loginButton.setReadPermissions((Arrays.asList(
                            "public_profile", "email", "user_birthday", "user_friends")));
                    // If you are using in a fragment, call loginButton.setFragment(this);

                    // Callback registration
                    loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
                        @Override
                        public void onSuccess(LoginResult loginResult) {
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            } else {
                                checkLogIn();
                            }
                        }

                        @Override
                        public void onCancel() {
                            // App code
                        }

                        @Override
                        public void onError(FacebookException exception) {
                            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                            toastMessage.setText("No internet connection!");
                            toast.setView(toastMessage);
                            toast.show();
                        }
                    });
                }
            }
        });

        /**/

        try

        {
            latestVersion = new GetLatestVersion().execute().get();
        } catch (
                InterruptedException e)

        {
            e.printStackTrace();
        } catch (
                ExecutionException e)

        {
            e.printStackTrace();
        }

        if (latestVersion == null) {
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("No internet connection!");
            toast.setView(toastMessage);
            toast.show();
            final Handler handler = new Handler();
            Timer t = new Timer();
            timerTask = new TimerTask() {
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            if (latestVersion == null) {
                                try {
                                    latestVersion = new GetLatestVersion().execute().get();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                } catch (ExecutionException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                timerTask.cancel();
                                timerTask = null;
                                checkVersion();
                            }
                        }
                    });
                }
            };
            // public void schedule (TimerTask task, long delay, long period)
            t.schedule(timerTask, 0, 1000);  //
        } else

        {
            checkVersion();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void checkVersion() {
        if (!currentVersion.equals(latestVersion)) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("An Update is Available");
            builder.setCancelable(false);
            builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Click button action
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.manolatostech.apppromoter")));
                }
            });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
            alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
        } else {
            int MyVersion = Build.VERSION.SDK_INT;
            if (MyVersion > Build.VERSION_CODES.LOLLIPOP_MR1) {
                if (!checkIfAlreadyhavePermission()) {
                    requestForSpecificPermission();
                }
            }
            MyDbHelper dbData = new MyDbHelper(mainActivity);
            dbData.check();
        }
    }

    private boolean checkIfAlreadyhavePermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        if (result == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }
    }

    private void requestForSpecificPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_PHONE_STATE}, 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //granted
                } else if (Build.VERSION.SDK_INT >= 23 && !shouldShowRequestPermissionRationale(permissions[0])) {
                    // User selected the Never Ask Again Option Change settings in app settings manually
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                    alertDialogBuilder.setTitle("Change Permission in Settings");
                    alertDialogBuilder
                            .setMessage("" +
                                    "\nClick SETTINGS to manually set\n" + "Permission")
                            .setCancelable(false)
                            .setPositiveButton("SETTINGS", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                                    intent.setData(uri);
                                    startActivityForResult(intent, 1000);     // Comment 3.
                                }
                            });

                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                } else {
                    // User selected Deny Dialog to EXIT App ==> OR <== RETRY to have a second chance to Allow Permissions
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_DENIED) {

                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                        alertDialogBuilder.setTitle("You have to set permission to allow ");
                        alertDialogBuilder
                                .setMessage("Click ALLOW to restart the app\n\n" + "Click EXIT to close app")
                                .setCancelable(false)
                                .setPositiveButton("ALLOW", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent i = new Intent(MainActivity.this, MainActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(i);
                                    }
                                })
                                .setNegativeButton("EXIT", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                            finishAndRemoveTask();
                                        } else {
                                            finish();
                                        }
                                        dialog.cancel();
                                    }
                                });
                        AlertDialog alertDialog = alertDialogBuilder.create();
                        alertDialog.show();
                        alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                        alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private class GetLatestVersion extends AsyncTask<String, String, String> {
        String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                //It retrieves the latest version by scraping the content of current version from play store at runtime
                String urlOfAppFromPlayStore = "https://play.google.com/store/apps/details?id=com.manolatostech.apppromoter";
                Document doc = Jsoup.connect(urlOfAppFromPlayStore).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop", "softwareVersion").first().text();

            } catch (Exception e) {
                e.printStackTrace();

            }

            return latestVersion;
        }
    }

    private String getCurrentVersion() {
        PackageManager pm = this.getPackageManager();
        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            e1.printStackTrace();
        }
        String currentVersion = pInfo.versionName;

        return currentVersion;
    }

    public class MyDbHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "AppPromoter.db";

        public MyDbHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void check() {
            int id = 0;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("select * from user", null);
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                id = cursor.getInt(cursor.getColumnIndex("id"));
                user.setText(cursor.getString(cursor.getColumnIndex("user")));
                pass.setText(cursor.getString(cursor.getColumnIndex("pass")));
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                cursor.moveToNext();
            }
            if (id > 0) {
                new SigningActivity(getApplicationContext(), toastMessage, h, progressBar, user, pass, logIn, register, forgot, text, logInFb, id, currentVersion).execute(user.getText().toString().trim().replace(" ", "+"), pass.getText().toString().trim().replace(" ", "+"));
            }
        }

        public void create() {
            SQLiteDatabase db = this.getReadableDatabase();
            DBHelper mydb = new DBHelper(mainActivity);
            mydb.onCreate(db);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

    }

    public void checkLogIn() {
        if (!Helper.isOnline(mainActivity.getApplicationContext())) {
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("No internet connection!");
            toast.setView(toastMessage);
            toast.show();
        } else if (!currentVersion.equals(latestVersion)) {
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("You have to update to the latest version!");
            toast.setView(toastMessage);
            toast.show();
        } else {
            if (fbLogin == 0) {
                if (user.getText().toString().trim().length() == 0 || pass.getText().toString().trim().length() == 0) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("No empty fields allowed!");
                    toast.setView(toastMessage);
                    toast.show();
                } else if (!Helper.checkField(user) || !Helper.checkPasswordField(pass)) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("Invalid fields!");
                    toast.setView(toastMessage);
                    toast.show();
                } else
                    new SigningActivity(getApplicationContext(), toastMessage, h, progressBar, user, pass, logIn, register, forgot, text, logInFb, 0, currentVersion).execute(user.getText().toString().trim().replace(" ", "+"), pass.getText().toString().trim().replace(" ", "+"));
            } else {
                GraphRequest request = GraphRequest.newMeRequest(
                        AccessToken.getCurrentAccessToken(),
                        new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(
                                    JSONObject object,
                                    GraphResponse response) {
                                try {
                                    String fbId = object.getString("id");
                                    String first_name = object.getString("first_name");
                                    String last_name = object.getString("last_name");
                                    String email = "";
                                    if (object.has("email")) {
                                        email = object.getString("email");
                                    }
                                    new LoginUserFacebook(getApplicationContext(), toastMessage, h, progressBar, user, pass, logIn, register, forgot, text, logInFb, 0, currentVersion).execute(first_name, last_name, email, fbId);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,first_name,last_name,email,gender,birthday");
                request.setParameters(parameters);
                request.executeAsync();
            }
        }
    }
}

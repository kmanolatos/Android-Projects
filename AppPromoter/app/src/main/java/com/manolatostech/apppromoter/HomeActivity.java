package com.manolatostech.apppromoter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.util.TypedValue;
import android.view.Display;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.reward.RewardItem;
import com.google.android.gms.ads.reward.RewardedVideoAd;
import com.google.android.gms.ads.reward.RewardedVideoAdListener;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, RewardedVideoAdListener {
    AdView mAdView;
    static ProgressBar progressBar;
    RelativeLayout.LayoutParams relativeParams;
    static RelativeLayout mainLayout, relativeLayout, relativeLayout1;
    ArrayList<ApplicationModel> apps = new ArrayList<ApplicationModel>();
    ArrayList<ApplicationModel> myApps = new ArrayList<ApplicationModel>();
    static ArrayList<ApplicationModel> statistics = new ArrayList<ApplicationModel>();
    UserModel userModel = new UserModel();
    static Long userId;
    int w, h, index, logIn, prevSelectedId;
    static int selectedId;
    static TextView toastMessage;
    Toast toast;
    TextView[] appTextViewArray, downloadsTextViewArray;
    Button[] btns;
    PackageManager pm;
    String userName;
    NavigationView navigationView;
    static HomeActivity homeActivity;
    Menu menu;
    ScrollView sv;
    RewardedVideoAd mRewardedVideoAd;
    EditText link, name;
    Button btn;
    static DrawerLayout drawer;
    String currentEmailtext, latestVersion = "", currentVersion, startedEmail;
    AdRequest adRequest;
    EditText[] editTextArray;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);
        findViewById(android.R.id.content).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Helper.hideSoftKeyboard(HomeActivity.this);
                return false;
            }
        });
        homeActivity = this;
        mydb = new DBHelper(homeActivity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        Display display = getWindowManager().getDefaultDisplay();
        w = display.getWidth();
        h = display.getHeight();
        mainLayout = (RelativeLayout) findViewById(R.id.mainrl);
        mainLayout.setBackgroundColor(Color.parseColor("#A6AAAD"));
        sv = (ScrollView) findViewById(R.id.sv);
        relativeParams = (RelativeLayout.LayoutParams) sv.getLayoutParams();
        relativeParams.setMargins(0, 0, 0, 0);
        relativeParams.height = h * 75 / 100;
        relativeParams.width = w;
        sv.setLayoutParams(relativeParams);
        relativeLayout = (RelativeLayout) findViewById(R.id.rl);
        relativeLayout.getLayoutParams().height = h * 75 / 100;
        relativeLayout1 = (RelativeLayout) findViewById(R.id.rl1);
        RelativeLayout relativeLayoutAd = (RelativeLayout) findViewById(R.id.rlad);
        relativeParams = (RelativeLayout.LayoutParams) relativeLayoutAd.getLayoutParams();
        relativeParams.setMargins(0, h * 75 / 100, 0, 0);
        relativeParams.height = h * 10 / 100;
        relativeParams.width = w;
        relativeLayoutAd.setLayoutParams(relativeParams);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getLayoutParams().height = h * 80 / 100;
        progressBar.setVisibility(INVISIBLE);
        progressBar.getIndeterminateDrawable().setColorFilter(
                getResources().getColor(R.color.colorPrimaryDark),
                android.graphics.PorterDuff.Mode.SRC_IN);
        toastMessage = new TextView(this);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setTypeface(null, Typeface.BOLD);
        toastMessage.setPadding(w * 5 / 100, h * 2 / 100, w * 5 / 100, h * 2 / 100);
        toastMessage.setTextSize((float) (w * 1 / 100));
        toast = Toast.makeText(getApplicationContext(), null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        Bundle b = getIntent().getExtras();
        if (b != null) {
            userModel = (UserModel) b.getSerializable("userModel");
            currentVersion = userModel.version;
            startedEmail = userModel.email;
            setTitle("Points: " + userModel.points);
            userId = userModel.id;
            userName = userModel.name + " " + userModel.surname;
            menu.getItem(0).setTitle(userName);
            myApps = (ArrayList<ApplicationModel>) b.getSerializable("myApps");
            apps = (ArrayList<ApplicationModel>) b.getSerializable("apps");
            logIn = (Integer) b.get("logIn");
        }
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
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this);
        mRewardedVideoAd.setRewardedVideoAdListener(this);
        loadRewardedVideoAd();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().findItem(R.id.nav_allApps).setChecked(true);
        pm = this.getPackageManager();
        relativeLayout.removeAllViews();
        relativeLayout1.removeAllViews();
        relativeLayout.addView(progressBar);
        selectedId = R.id.nav_allApps;
        prevSelectedId = R.id.nav_allApps;
        if (logIn > 1) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            new LogOut(homeActivity, toastMessage, h, progressBar, relativeLayout, drawer, 1).execute(String.valueOf(userId));
        } else {
            MyDbHelper dbData = new MyDbHelper(homeActivity);
            dbData.insert();
            startService(new Intent(getBaseContext(), MyService.class));
            new loadingTask().execute();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Helper.hideSoftKeyboard(HomeActivity.this);
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        if (mAdView != null) {
            mAdView.resume();
        }
        super.onResume();
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        LinearLayout nav_ll = (LinearLayout) findViewById(R.id.nav_ll);
        nav_ll.getLayoutParams().height = h * 25 / 100;
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setPadding(0, h * 2 / 100, 0, 0);
        imageView.getLayoutParams().height = h * 18 / 100;
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            if (!progressBar.isShown()) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Are you sure you want to logout?");
                builder.setCancelable(false);
                builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (selectedId == R.id.nav_statistics) {
                            mainLayout.removeView(appTextViewArray[0]);
                            mainLayout.removeView(downloadsTextViewArray[0]);
                            relativeParams = (RelativeLayout.LayoutParams) sv.getLayoutParams();
                            relativeParams.setMargins(0, 0, 0, 0);
                            relativeParams.height = sv.getHeight() + (h * 16) / 100;
                            sv.setLayoutParams(relativeParams);
                        }
                        relativeLayout.removeAllViews();
                        relativeLayout.addView(progressBar);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        new LogOut(homeActivity, toastMessage, h, progressBar, relativeLayout, drawer, 0).execute(String.valueOf(userId));
                    }
                });
                builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                alertDialog.getButton(alertDialog.BUTTON_POSITIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                alertDialog.getButton(alertDialog.BUTTON_NEGATIVE).setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (selectedId != id) {
            selectedId = id;
            sv.scrollTo(0, 0);
            relativeLayout.removeAllViews();
            relativeLayout.addView(progressBar);
            if (prevSelectedId == R.id.nav_statistics) {
                mainLayout.removeView(appTextViewArray[0]);
                mainLayout.removeView(downloadsTextViewArray[0]);
                relativeParams = (RelativeLayout.LayoutParams) sv.getLayoutParams();
                relativeParams.setMargins(0, 0, 0, 0);
                relativeParams.height = sv.getHeight() + (h * 16) / 100;
                sv.setLayoutParams(relativeParams);
            }
            prevSelectedId = id;
            if (selectedId == R.id.nav_me) {
                changeInformation(userModel.email);
            } else if (selectedId == R.id.nav_upload) {
                uploadApplication();
            } else {
                new loadingTask().execute();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void changeInformation(final String emailText) {
        if (emailText != null)
            currentEmailtext = emailText;
        editTextArray = new EditText[5];
        btns = new Button[2];
        final RelativeLayout[] tabs = new RelativeLayout[2];
        int y, x = 20, start = 0, end = 3, tabY = 4, tabX = 5;
        for (int i = 0; i < tabs.length; i++) {
            tabs[i] = new RelativeLayout(this);
            relativeLayout.addView(tabs[i]);
            relativeParams = (RelativeLayout.LayoutParams) tabs[i].getLayoutParams();
            relativeParams.setMargins(w * tabX / 100, h * tabY / 100, 0, h * 5 / 100);
            relativeParams.width = w * 90 / 100;
            if (i == 0)
                relativeParams.height = h * 50 / 100;
            else
                relativeParams.height = h * 38 / 100;
            tabs[i].setLayoutParams(relativeParams);
            tabs[i].setBackgroundColor(Color.parseColor("#BBB9B9"));
            y = 3;
            for (int j = start; j < end; j++) {
                editTextArray[j] = new EditText(this);
                tabs[i].addView(editTextArray[j]);
                relativeParams = (RelativeLayout.LayoutParams) editTextArray[j].getLayoutParams();
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, 0);
                relativeParams.width = w * 50 / 100;
                relativeParams.height = h * 10 / 100;
                editTextArray[j].setLayoutParams(relativeParams);
                editTextArray[j].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                editTextArray[j].getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
                editTextArray[j].setSingleLine(true);
                try {
                    Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(editTextArray[j], R.drawable.cursor);
                } catch (Exception ignored) {
                }
                if (j == 0) {
                    editTextArray[j].setHint("Name");
                    editTextArray[j].setText(userModel.name);
                } else if (j == 1) {
                    editTextArray[j].setHint("Surname");
                    editTextArray[j].setText(userModel.surname);
                } else if (j == 2) {
                    editTextArray[j].setHint("Email");
                    editTextArray[j].setText(userModel.email);
                    y += 12;

                    btns[0] = new Button(this);
                    tabs[i].addView(btns[0]);
                    relativeParams = (RelativeLayout.LayoutParams) btns[0].getLayoutParams();
                    relativeParams.setMargins(w * 25 / 100, h * y / 100, 0, 0);
                    relativeParams.width = w * 40 / 100;
                    relativeParams.height = h * 8 / 100;
                    btns[0].setLayoutParams(relativeParams);
                    btns[0].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                    btns[0].setText("Change");
                    btns[0].setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                latestVersion = new GetLatestVersion().execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            if (!Helper.isOnline(getApplicationContext())) {
                                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                toastMessage.setText("No internet connection!");
                                toast.setView(toastMessage);
                                toast.show();
                            } else if (editTextArray[0].getText().toString().trim().length() == 0 || editTextArray[1].getText().toString().trim().length() == 0 || (startedEmail.trim().length() != 0 && editTextArray[2].getText().toString().trim().length() == 0)) {
                                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                toastMessage.setText("No empty fields allowed!");
                                toast.setView(toastMessage);
                                toast.show();
                            } else if (!Helper.checkField(editTextArray[0]) || !Helper.checkField(editTextArray[1]) || (startedEmail.trim().length() != 0 && !Helper.checkEmail(editTextArray[2].getText().toString().trim()))) {
                                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                toastMessage.setText("Invalid fields!");
                                toast.setView(toastMessage);
                                toast.show();
                            } else if (checkVersion()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
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
                                        } else {
                                            sv.scrollTo(0, 0);
                                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                            new UpdateUser(homeActivity, toastMessage, h, progressBar, userModel, relativeLayout, tabs, menu, currentEmailtext, drawer).execute(String.valueOf(userId), editTextArray[0].getText().toString().trim(), editTextArray[1].getText().toString().trim(), editTextArray[2].getText().toString().trim());
                                        }
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
                } else if (j == 3) {
                    editTextArray[j].setHint("Change Password");
                    editTextArray[j].setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editTextArray[j].setTransformationMethod(PasswordTransformationMethod.getInstance());
                } else if (j == 4) {
                    editTextArray[j].setHint("Confirm Password");
                    editTextArray[j].setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    editTextArray[j].setTransformationMethod(PasswordTransformationMethod.getInstance());
                    y += 12;

                    btns[1] = new Button(this);
                    tabs[i].addView(btns[1]);
                    relativeParams = (RelativeLayout.LayoutParams) btns[1].getLayoutParams();
                    relativeParams.setMargins(w * 25 / 100, h * y / 100, 0, 0);
                    relativeParams.width = w * 40 / 100;
                    relativeParams.height = h * 8 / 100;
                    btns[1].setLayoutParams(relativeParams);
                    btns[1].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                    btns[1].setText("Change");
                    btns[1].setOnClickListener(new View.OnClickListener() {
                        public void onClick(View v) {
                            try {
                                latestVersion = new GetLatestVersion().execute().get();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            if (!Helper.isOnline(getApplicationContext())) {
                                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                toastMessage.setText("No internet connection!");
                                toast.setView(toastMessage);
                                toast.show();
                            } else if (editTextArray[3].getText().toString().trim().length() == 0 || editTextArray[4].getText().toString().trim().length() == 0) {
                                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                toastMessage.setText("No empty fields allowed!");
                                toast.setView(toastMessage);
                                toast.show();
                            } else if (!editTextArray[3].getText().toString().equals(editTextArray[4].getText().toString())) {
                                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                toastMessage.setText("Passwords don't match!");
                                toast.setView(toastMessage);
                                toast.show();
                            } else if (!Helper.checkPasswordField(editTextArray[3]) || !Helper.checkPasswordField(editTextArray[4])) {
                                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                toastMessage.setText("Invalid password!");
                                toast.setView(toastMessage);
                                toast.show();
                            } else if (checkVersion()) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
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
                                        } else {
                                            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                            sv.scrollTo(0, 0);
                                            new UpdateUser(homeActivity, toastMessage, h, progressBar, userModel, relativeLayout, tabs, menu, null, drawer).execute(String.valueOf(userId), editTextArray[3].getText().toString().trim());
                                        }
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
                y += 12;
            }
            tabY += 58;
            start = end;
            end = 5;
        }
    }

    public void uploadApplication() {
        link = new EditText(this);
        name = new EditText(this);
        btn = new Button(this);
        relativeLayout.addView(name);
        relativeParams = (RelativeLayout.LayoutParams) name.getLayoutParams();
        relativeParams.setMargins(w * 25 / 100, h * 20 / 100, 0, 0);
        relativeParams.width = w * 50 / 100;
        relativeParams.height = h * 10 / 100;
        name.setLayoutParams(relativeParams);
        name.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);
        name.setHint("Name of application");
        name.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        name.setSingleLine(true);
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(name, R.drawable.cursor);
        } catch (Exception ignored) {
        }


        relativeLayout.addView(link);
        relativeParams = (RelativeLayout.LayoutParams) link.getLayoutParams();
        relativeParams.setMargins(w * 10 / 100, h * 35 / 100, 0, 0);
        relativeParams.width = w * 80 / 100;
        relativeParams.height = h * 10 / 100;
        link.setLayoutParams(relativeParams);
        link.setHint("Play Store link");
        link.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);
        link.getBackground().mutate().setColorFilter(getResources().getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);
        link.setSingleLine(true);
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(link, R.drawable.cursor);
        } catch (Exception ignored) {
        }


        relativeLayout.addView(btn);
        relativeParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
        relativeParams.setMargins(w * 30 / 100, h * 55 / 100, 0, 0);
        relativeParams.width = w * 40 / 100;
        relativeParams.height = h * 10 / 100;
        btn.setLayoutParams(relativeParams);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        btn.setText("Upload");
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    latestVersion = new GetLatestVersion().execute().get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if (!Helper.isOnline(getApplicationContext())) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("No internet connection!");
                    toast.setView(toastMessage);
                    toast.show();
                } else if (link.getText().toString().trim().length() == 0 || name.getText().toString().trim().length() == 0) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("No empty fields allowed!");
                    toast.setView(toastMessage);
                    toast.show();
                } else if (!Helper.checkField(name) || !link.getText().toString().contains("https://play.google.com/store/apps/details?id=")) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    toastMessage.setText("Invalid fields!");
                    toast.setView(toastMessage);
                    toast.show();
                } else {
                    if (myApps.size() >= 5) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("You can upload only 5 apps!");
                        toast.setView(toastMessage);
                        toast.show();
                    } else if (checkVersion()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
                        builder.setTitle("Confirm");
                        builder.setMessage("You won't be able to change the app link after you upload.You can only change the application name.Are you sure?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                if (!Helper.isOnline(getApplicationContext())) {
                                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                    toastMessage.setText("No internet connection!");
                                    toast.setView(toastMessage);
                                    toast.show();
                                } else {
                                    sv.scrollTo(0, 0);
                                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                    if (mRewardedVideoAd.isLoaded()) {
                                        mRewardedVideoAd.show();
                                    } else
                                        new InsertApplication(homeActivity, toastMessage, h, progressBar, myApps, name, link, btn, drawer).execute(name.getText().toString().trim(), link.getText().toString().trim(), userId.toString());
                                }
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
            }
        });
    }

    public void showAllApps() {
        appTextViewArray = new TextView[apps.size()];
        btns = new Button[apps.size()];
        int y = 4;
        if (apps.size() == 0) {
            TextView txt = new TextView(this);
            relativeLayout1.addView(txt);
            relativeParams = (RelativeLayout.LayoutParams) txt.getLayoutParams();
            relativeParams.setMargins(w * 3 / 100, h * y / 100, 0, 0);
            relativeParams.height = h * 73 / 100;
            relativeParams.width = w * 96 / 100;
            txt.setLayoutParams(relativeParams);
            txt.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            Typeface boldTypeface = Typeface.defaultFromStyle(Typeface.BOLD);
            txt.setTypeface(boldTypeface);
            txt.setGravity(Gravity.CENTER);
            txt.setText("No apps to display.Please check again later!");
        }
        for (int i = 0; i < apps.size(); i++) {
            appTextViewArray[i] = new TextView(this);
            relativeLayout1.addView(appTextViewArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) appTextViewArray[i].getLayoutParams();
            relativeParams.setMargins(w * 3 / 100, h * y / 100, 0, 0);
            relativeParams.height = h * 10 / 100;
            relativeParams.width = w * 57 / 100;
            appTextViewArray[i].setLayoutParams(relativeParams);
            appTextViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            appTextViewArray[i].setGravity(Gravity.CENTER);
            appTextViewArray[i].setText(apps.get(i).appName);
            appTextViewArray[i].setTypeface(null, Typeface.BOLD);
            appTextViewArray[i].setTag(i);
            appTextViewArray[i].setBackgroundColor(Color.parseColor("#0379D4"));
            appTextViewArray[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    index = (int) v.getTag();
                    startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(apps.get(index).appLink)));
                }
            });
            btns[i] = new Button(this);
            relativeLayout1.addView(btns[i]);
            relativeParams = (RelativeLayout.LayoutParams) btns[i].getLayoutParams();
            relativeParams.setMargins(w * 65 / 100, h * y / 100, 0, 0);
            relativeParams.height = h * 10 / 100;
            relativeParams.width = w * 32 / 100;
            btns[i].setLayoutParams(relativeParams);
            btns[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            btns[i].setText("+1 Point");
            btns[i].setTag(i);
            btns[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    try {
                        latestVersion = new GetLatestVersion().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    index = (int) v.getTag();
                    String appPackage = apps.get(index).appLink.substring(apps.get(index).appLink.indexOf("=")).replace("=", "");
                    boolean isInstalled = isPackageInstalled(appPackage, pm);
                    if (!Helper.isOnline(getApplicationContext())) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("No internet connection!");
                        toast.setView(toastMessage);
                        toast.show();
                    } else if (!isInstalled) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("You have to install the app first!");
                        toast.setView(toastMessage);
                        toast.show();
                    } else if (checkVersion()) {
                        sv.scrollTo(0, 0);
                        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                        if (mRewardedVideoAd.isLoaded()) {
                            mRewardedVideoAd.show();
                        } else
                            new UpdatePoints(homeActivity, toastMessage, h, progressBar, apps, userModel, index, relativeLayout, drawer).execute(userId.toString(), String.valueOf(apps.get(index).appId));
                    }
                }
            });
            y += 15;
        }
    }

    public void showMyApps() {
        editTextArray = new EditText[myApps.size()];
        final ImageView[] imageViewLink = new ImageView[myApps.size()];
        final ImageView[] imageViewDelete = new ImageView[myApps.size()];
        final ImageView[] imageViewEdit = new ImageView[myApps.size()];
        int y = 4;
        for (int i = 0; i < myApps.size(); i++) {
            editTextArray[i] = new EditText(this);
            relativeLayout1.addView(editTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) editTextArray[i].getLayoutParams();
            relativeParams.setMargins(w * 3 / 100, h * y / 100, 0, 0);
            relativeParams.height = h * 10 / 100;
            relativeParams.width = w * 43 / 100;
            editTextArray[i].setLayoutParams(relativeParams);
            editTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            editTextArray[i].setText(myApps.get(i).appName);
            editTextArray[i].setSingleLine(true);
            editTextArray[i].setHint("Name of application");


            imageViewLink[i] = new ImageView(this);
            relativeLayout1.addView(imageViewLink[i]);
            relativeParams = (RelativeLayout.LayoutParams) imageViewLink[i].getLayoutParams();
            relativeParams.width = w * 11 / 100;
            relativeParams.height = h * 6 / 100;
            relativeParams.setMargins(w * 53 / 100, h * (y + 3) / 100, 0, 0);
            imageViewLink[i].setLayoutParams(relativeParams);
            imageViewLink[i].setBackgroundResource(R.drawable.ic_link);
            imageViewLink[i].setTag(i);
            imageViewLink[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Helper.isOnline(getApplicationContext())) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("No internet connection!");
                        toast.setView(toastMessage);
                        toast.show();
                    } else {
                        index = (int) v.getTag();
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(myApps.get(index).appLink)));
                    }
                }
            });


            imageViewEdit[i] = new ImageView(this);
            relativeLayout1.addView(imageViewEdit[i]);
            relativeParams = (RelativeLayout.LayoutParams) imageViewEdit[i].getLayoutParams();
            relativeParams.width = w * 10 / 100;
            relativeParams.height = h * 5 / 100;
            relativeParams.setMargins(w * 70 / 100, h * (y + 3) / 100, 0, 0);
            imageViewEdit[i].setLayoutParams(relativeParams);
            imageViewEdit[i].setBackgroundResource(R.drawable.ic_edit);
            imageViewEdit[i].setTag(i);
            imageViewEdit[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    index = (int) v.getTag();
                    try {
                        latestVersion = new GetLatestVersion().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (!Helper.isOnline(getApplicationContext())) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("No internet connection!");
                        toast.setView(toastMessage);
                        toast.show();
                    } else if (!Helper.checkField(editTextArray[index])) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("Invalid field!");
                        toast.setView(toastMessage);
                        toast.show();
                    } else if (checkVersion()) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure you want to edit the Application to: " + editTextArray[index].getText().toString().trim() + "?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                if (!Helper.isOnline(getApplicationContext())) {
                                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                    toastMessage.setText("No internet connection!");
                                    toast.setView(toastMessage);
                                    toast.show();
                                } else {
                                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                    sv.scrollTo(0, 0);
                                    new UpdateApplication(homeActivity, toastMessage, h, progressBar, myApps, index, relativeLayout, editTextArray, imageViewLink, imageViewEdit, imageViewDelete, drawer).execute(String.valueOf(myApps.get(index).appId), editTextArray[index].getText().toString().trim());
                                }
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


            imageViewDelete[i] = new ImageView(this);
            relativeLayout1.addView(imageViewDelete[i]);
            relativeParams = (RelativeLayout.LayoutParams) imageViewDelete[i].getLayoutParams();
            relativeParams.width = w * 9 / 100;
            relativeParams.height = h * 5 / 100;
            relativeParams.setMargins(w * 86 / 100, h * (y + 3) / 100, 0, 0);
            imageViewDelete[i].setLayoutParams(relativeParams);
            imageViewDelete[i].setBackgroundResource(R.drawable.ic_delete);
            imageViewDelete[i].setTag(i);
            imageViewDelete[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        latestVersion = new GetLatestVersion().execute().get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    if (!Helper.isOnline(getApplicationContext())) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("No internet connection!");
                        toast.setView(toastMessage);
                        toast.show();
                    } else if (checkVersion()) {
                        index = (int) v.getTag();
                        AlertDialog.Builder builder = new AlertDialog.Builder(homeActivity);
                        builder.setTitle("Confirm");
                        builder.setMessage("Are you sure you want to delete Application: " + myApps.get(index).appName + "?");
                        builder.setCancelable(false);
                        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int which) {
                                if (!Helper.isOnline(getApplicationContext())) {
                                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                    toastMessage.setText("No internet connection!");
                                    toast.setView(toastMessage);
                                    toast.show();
                                } else {
                                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                                    sv.scrollTo(0, 0);
                                    new DeleteApplication(homeActivity, toastMessage, h, progressBar, myApps, index, relativeLayout, editTextArray, imageViewLink, imageViewEdit, imageViewDelete, drawer).execute(String.valueOf(myApps.get(index).appId));
                                }
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
            y += 15;
        }
    }

    public void getStatistics() {
        relativeParams = (RelativeLayout.LayoutParams) sv.getLayoutParams();
        relativeParams.setMargins(0, h * 16 / 100, 0, 0);
        relativeParams.height = sv.getHeight() - (h * 16) / 100;
        sv.setLayoutParams(relativeParams);
        int i, found;
        for (i = 0; i < myApps.size(); i++) {
            found = -1;
            for (int j = 0; j < statistics.size(); j++) {
                if (myApps.get(i).appName.equals(statistics.get(j).appName)) {
                    found = -1;
                    j = statistics.size();
                } else {
                    found = i;
                }
            }
            if (found != -1) {
                ApplicationModel temp = new ApplicationModel();
                temp.appName = myApps.get(found).appName;
                temp.appLink = myApps.get(found).appLink;
                temp.downloads = 0;
                statistics.add(temp);
            }
        }
        appTextViewArray = new TextView[statistics.size() + 1];
        downloadsTextViewArray = new TextView[statistics.size() + 1];
        int y = 3;

        appTextViewArray[0] = new TextView(this);
        mainLayout.addView(appTextViewArray[0]);
        relativeParams = (RelativeLayout.LayoutParams) appTextViewArray[0].getLayoutParams();
        relativeParams.setMargins(w * 8 / 100, h * y / 100, 0, 0);
        relativeParams.height = h * 10 / 100;
        relativeParams.width = w * 43 / 100;
        appTextViewArray[0].setLayoutParams(relativeParams);
        appTextViewArray[0].setGravity(Gravity.CENTER);
        appTextViewArray[0].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        appTextViewArray[0].setTypeface(null, Typeface.BOLD);
        appTextViewArray[0].setPaintFlags(appTextViewArray[0].getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        appTextViewArray[0].setText("Application Name");

        downloadsTextViewArray[0] = new TextView(this);
        mainLayout.addView(downloadsTextViewArray[0]);
        relativeParams = (RelativeLayout.LayoutParams) downloadsTextViewArray[0].getLayoutParams();
        relativeParams.setMargins(w * 60 / 100, h * y / 100, 0, 0);
        relativeParams.height = h * 10 / 100;
        relativeParams.width = w * 32 / 100;
        downloadsTextViewArray[0].setLayoutParams(relativeParams);
        downloadsTextViewArray[0].setGravity(Gravity.CENTER);
        downloadsTextViewArray[0].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        downloadsTextViewArray[0].setTypeface(null, Typeface.BOLD);
        downloadsTextViewArray[0].setPaintFlags(downloadsTextViewArray[0].getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        downloadsTextViewArray[0].setText("Downloads");
        y = 0;

        for (i = 1; i < statistics.size() + 1; i++) {
            appTextViewArray[i] = new TextView(this);
            relativeLayout.addView(appTextViewArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) appTextViewArray[i].getLayoutParams();
            relativeParams.setMargins(w * 8 / 100, h * y / 100, 0, 0);
            relativeParams.height = h * 10 / 100;
            relativeParams.width = w * 43 / 100;
            appTextViewArray[i].setLayoutParams(relativeParams);
            appTextViewArray[i].setGravity(Gravity.CENTER);
            appTextViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            appTextViewArray[i].setTypeface(null, Typeface.BOLD);
            appTextViewArray[i].setText(statistics.get(i - 1).appName);
            appTextViewArray[i].setTag(i - 1);
            appTextViewArray[i].setBackgroundResource(R.drawable.border);
            appTextViewArray[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!Helper.isOnline(getApplicationContext())) {
                        toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                        toastMessage.setText("No internet connection!");
                        toast.setView(toastMessage);
                        toast.show();
                    } else {
                        index = (int) v.getTag();
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(statistics.get(index).appLink)));
                    }
                }
            });

            downloadsTextViewArray[i] = new TextView(this);
            relativeLayout.addView(downloadsTextViewArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) downloadsTextViewArray[i].getLayoutParams();
            relativeParams.setMargins(w * 60 / 100, h * y / 100, 0, 0);
            relativeParams.height = h * 10 / 100;
            relativeParams.width = w * 32 / 100;
            downloadsTextViewArray[i].setLayoutParams(relativeParams);
            downloadsTextViewArray[i].setGravity(Gravity.CENTER);
            downloadsTextViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            downloadsTextViewArray[i].setTypeface(null, Typeface.BOLD);
            downloadsTextViewArray[i].setText("" + statistics.get(i - 1).downloads);
            downloadsTextViewArray[i].setBackgroundResource(R.drawable.border);
            y += 14;
        }
    }

    private boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void loadRewardedVideoAd() {

        mRewardedVideoAd.loadAd("ca-app-pub-5593953116329757/5873375494",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewarded(RewardItem reward) {

    }

    @Override
    public void onRewardedVideoAdLeftApplication() {

    }

    @Override
    public void onRewardedVideoAdClosed() {
        mRewardedVideoAd.loadAd("ca-app-pub-5593953116329757/5873375494",
                new AdRequest.Builder().build());
        if (!Helper.isOnline(getApplicationContext())) {
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("No internet connection!");
            toast.setView(toastMessage);
            toast.show();
        } else {
            if (selectedId == R.id.nav_allApps)
                new UpdatePoints(homeActivity, toastMessage, h, progressBar, apps, userModel, index, relativeLayout, drawer).execute(userId.toString(), String.valueOf(apps.get(index).appId));
            else
                new InsertApplication(homeActivity, toastMessage, h, progressBar, myApps, name, link, btn, drawer).execute(name.getText().toString().trim(), link.getText().toString().trim(), userId.toString());
        }
    }

    @Override
    public void onRewardedVideoAdFailedToLoad(int errorCode) {
        mRewardedVideoAd.loadAd("ca-app-pub-5593953116329757/5873375494",
                new AdRequest.Builder().build());
    }

    @Override
    public void onRewardedVideoAdLoaded() {

    }

    @Override
    public void onRewardedVideoAdOpened() {

    }

    @Override
    public void onRewardedVideoStarted() {

    }

    public static class loadingTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (selectedId == R.id.nav_allApps) {
                homeActivity.showAllApps();
            } else if (selectedId == R.id.nav_myApps) {
                homeActivity.showMyApps();
            } else if (selectedId == R.id.nav_statistics) {
                statistics = new ArrayList<ApplicationModel>();
                new GetStatistics(homeActivity, progressBar, statistics, drawer).execute(String.valueOf(userId));
            }
            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {
            if (selectedId == R.id.nav_allApps || selectedId == R.id.nav_myApps) {
                progressBar.setVisibility(INVISIBLE);
                relativeLayout.addView(relativeLayout1);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            }
        }

        @Override
        protected void onPreExecute() {
            relativeLayout1.removeAllViews();
            progressBar.setVisibility(VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    public class MyDbHelper extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "AppPromoter.db";

        public MyDbHelper(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void insert() {
            mydb.insert((int) userModel.id, userModel.user, userModel.pass);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

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

    public boolean checkVersion() {
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
            return false;
        } else
            return true;
    }
}
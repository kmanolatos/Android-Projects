package com.manolatostech.customersmanagement;

import android.Manifest;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.method.ScrollingMovementMethod;
import android.text.style.RelativeSizeSpan;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static ArrayList<CustomersModel> customersModel = new ArrayList<CustomersModel>();
    static ArrayList<CustomersGuaranteeModel> customersGuaranteeModel = new ArrayList<CustomersGuaranteeModel>();
    static ArrayList<CustomersTasksModel> customersTasksModel = new ArrayList<CustomersTasksModel>();
    static ArrayList<CustomersTasksExtendedModel> customersNotificationTasksModel = new ArrayList<CustomersTasksExtendedModel>();
    static ArrayList<CustomersTasksExtendedModel> customersNotificationTasksTempModel = new ArrayList<CustomersTasksExtendedModel>();
    static ArrayList<CustomersTasksExtendedModel> customersTasksTodayModel = new ArrayList<CustomersTasksExtendedModel>();
    static ArrayList<CustomersTasksExtendedModel> customersTasksTomorrowModel = new ArrayList<CustomersTasksExtendedModel>();
    static ArrayList<CustomersGuaranteeExtendedModel> customersDeletedGuaranteeModel = new ArrayList<CustomersGuaranteeExtendedModel>();
    static ArrayList<CustomersTasksExtendedModel> customersPendingTasksModel = new ArrayList<CustomersTasksExtendedModel>();
    static ArrayList<PersonalNotesModel> personalNotesModel = new ArrayList<PersonalNotesModel>();
    static ArrayList<CustomersCreditDebtModel> customersCreditDebtModel = new ArrayList<CustomersCreditDebtModel>();
    static ArrayList<CustomersCreditDebtModel> tempCustomersCreditDebtModel = new ArrayList<CustomersCreditDebtModel>();
    static boolean ignoreChange = false, spinnerFirstCall;
    static Context context;
    static Resources res;
    static MainActivity mainActivity;
    static NotificationCompat.Builder mBuilder;
    static NotificationManager mNotificationManager;
    static DecimalFormat decimalFormat = new DecimalFormat();
    SQLiteDatabase db;
    static DrawerLayout drawer;
    static ProgressDialog progressDialog;
    static ProgressBar progressBar;
    static Date strDate;
    static DBHelper mydb;
    static Menu menu, menuLanguages;
    static GradientDrawable gd, detailsTextViewBorderGd;
    static ImageView[] imageViewEdit, imageViewDelete;
    static ScrollView scrollViewTable, scrollView;
    static TextView newDebt, toastMessage, totalDebtText;
    static Button[] btns;
    static EditText[] editTextArray, customerDateEdit, customerDetailsEdit, customerTimeEdit;
    static TextView[] customerNameTextArray, oldDebtTextArray, creditDebtTextArray, currentDebtTextArray, customerSurnameTextArray, customerPhone1TextArray, customerPhone2TextArray, customerAddressTextArray, customerCityTextArray, customerDetailsText,
            customerEmailTextArray, customerGuaranteeCountTextArray, customerDebtTextArray, customerTaskCountTextArray, textViewArray, headerΤextViewArray, customerTimeText, customerDateText;
    static Button btn;
    static RelativeLayout.LayoutParams relativeParams;
    static RelativeLayout relativeLayout, horizontalRelativeLayout, verticalRelativeLayout, tab;
    static SearchView searchView;
    static Spinner spinner;
    WebView browser;
    static HorizontalScrollView horizontalScrollView;
    static DatePickerDialog datePickerDialog;
    static TimePickerDialog timePickerDialog;
    static SimpleDateFormat dateFormat;
    static TimerTask timerTask;
    static Toast toast;
    static double customerDebt, totalDebt, newCustomerDebt, progress, debtDoubleParse;
    static int h, w, itemSelectedId, prevItemSelectedId = 0, prevLanguageId = 0, customerIndex, itemIndex, customerGuaranteeCount, customerTaskCount, todayTasks = 0, tomorrowTasks = 0, deletedGuarantees = 0, pendingTasks = 0, customerId, spinnerSelectedItem = 0, navigateMeFunction = 0, prevLength = 0, notificationTaskCounter = 0, notificationTaskCounterTemp = 0, resourceID, dataLength, currentTimeFormatted, taskTime;
    static String searchText, dateTimePicker, debtFormatted, prevText = "", prevTime = "", packageName, functionNow, countryCodeValue, dayStarted;
    static ArrayList<String> customerTextArray;
    static String[] header = {
            "name",
            "surname",
            "phone1",
            "phone2",
            "area",
            "address",
            "email",
            "debt",
            "guarantees",
            "tasks",
    };
    static String[] addSearchSubmenuXML, editSubmenuXML, notificationsSubmenuXML, historySubmenuXML, menuXML, spinnerXML;
    static ArrayList<String> events = new ArrayList<String>();
    static ArrayList<String> prevDate = new ArrayList<String>();
    static List<Integer> customerIds = new ArrayList<Integer>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Display display = getWindowManager().getDefaultDisplay();
        w = (display.getWidth());
        h = (display.getHeight());
        packageName = getPackageName();
        mainActivity = this;
        context = getApplicationContext();
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        countryCodeValue = tm.getNetworkCountryIso();
        if (countryCodeValue.toLowerCase().equals("gr"))
            prevLanguageId = R.id.greek;
        else if (countryCodeValue.trim().length() == 0) {
            if (Locale.getDefault().getLanguage().equals("gr")) {
                countryCodeValue = "gr";
                prevLanguageId = R.id.greek;
            } else {
                countryCodeValue = "en";
                prevLanguageId = R.id.english;
            }
        } else {
            countryCodeValue = "en";
            prevLanguageId = R.id.english;
        }
        Resources res2 = getApplicationContext().getResources();
        DisplayMetrics dm2 = res2.getDisplayMetrics();
        android.content.res.Configuration conf2 = res2.getConfiguration();
        conf2.locale = new Locale(countryCodeValue);
        res2.updateConfiguration(conf2, dm2);
        res = context.getResources();
        menuXML = res.getStringArray(R.array.menu);
        addSearchSubmenuXML = res.getStringArray(R.array.addSearchSubmenu);
        editSubmenuXML = res.getStringArray(R.array.editSubmenu);
        notificationsSubmenuXML = res.getStringArray(R.array.notificationsSubmenu);
        historySubmenuXML = res.getStringArray(R.array.historySubmenu);
        spinnerXML = res.getStringArray(R.array.customer_search);
        toastMessage = new TextView(this);
        toastMessage.setTextColor(Color.WHITE);
        toastMessage.setTypeface(null, Typeface.BOLD);
        toastMessage.setPadding(w * 5 / 100, h * 2 / 100, w * 5 / 100, h * 2 / 100);
        toastMessage.setTextSize((float) (w * 1 / 100));
        toast = Toast.makeText(getApplicationContext(), null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        DecimalFormatSymbols decimalFormateSymbol = new DecimalFormatSymbols();
        decimalFormateSymbol.setDecimalSeparator(',');
        decimalFormateSymbol.setGroupingSeparator('.');
        decimalFormat.setDecimalFormatSymbols(decimalFormateSymbol);
        gd = new GradientDrawable();
        gd.setColor(Color.WHITE);
        gd.setCornerRadius(5);
        detailsTextViewBorderGd = new GradientDrawable();
        detailsTextViewBorderGd.setStroke(3, Color.BLACK);
        scrollViewTable = (ScrollView) findViewById(R.id.tablesv);
        scrollView = (ScrollView) findViewById(R.id.sv);
        scrollView.setBackgroundColor(Color.parseColor("#ADAAAA"));
        scrollView.getLayoutParams().height = h;
        scrollView.getLayoutParams().width = w;
        relativeLayout = (RelativeLayout) findViewById(R.id.rl);
        horizontalRelativeLayout = (RelativeLayout) findViewById(R.id.horizontalrl);
        horizontalScrollView = (HorizontalScrollView) findViewById(R.id.horizontalsv);
        verticalRelativeLayout = (RelativeLayout) findViewById(R.id.vertical);
        spinner = (Spinner) findViewById(R.id.spinner);
        browser = (WebView) this.findViewById(R.id.webView);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getLayoutParams().height = h * 95 / 100;
        progressBar.setVisibility(View.INVISIBLE);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                if (getCurrentFocus() != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        resourceID = getResources().getIdentifier("loadingData", "string", packageName);
        SpannableString ss = new SpannableString(res.getString(resourceID));
        ss.setSpan(new RelativeSizeSpan(w * 0.08f / 100), 0, ss.length(), 0);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(ss);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        progressDialog.setCancelable(false);
        progressDialog.show();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        menu = navigationView.getMenu();
        MenuItem home = menu.findItem(R.id.nav_home);
        home.setChecked(true);
        itemSelectedId = R.id.nav_home;
        prevItemSelectedId = itemSelectedId;
        setTitle(res.getString(R.string.home));
        home();
        mydb = new DBHelper(context);
        dayStarted = dateFormat.format(new Date());
        CustomersDataOnStartup dbData = new CustomersDataOnStartup(mainActivity);
        dbData.getCustomersData();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; context adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        menuLanguages = menu;
        if (countryCodeValue.equals("gr"))
            menu.getItem(1).setIcon(ContextCompat.getDrawable(this, R.mipmap.selected_greek));
        else
            menu.getItem(0).setIcon(ContextCompat.getDrawable(this, R.mipmap.selected_english));
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
        if (id != prevLanguageId) {
            prevLanguageId = id;
            if (id == R.id.greek) {
                menuLanguages.getItem(0).setIcon(ContextCompat.getDrawable(this, R.mipmap.english));
                menuLanguages.getItem(1).setIcon(ContextCompat.getDrawable(this, R.mipmap.selected_greek));
                Resources res2 = getApplicationContext().getResources();
                DisplayMetrics dm2 = res2.getDisplayMetrics();
                android.content.res.Configuration conf2 = res2.getConfiguration();
                conf2.locale = new Locale("gr");
                res2.updateConfiguration(conf2, dm2);
            } else {
                menuLanguages.getItem(0).setIcon(ContextCompat.getDrawable(this, R.mipmap.selected_english));
                menuLanguages.getItem(1).setIcon(ContextCompat.getDrawable(this, R.mipmap.greek));
                Resources res2 = getApplicationContext().getResources();
                DisplayMetrics dm2 = res2.getDisplayMetrics();
                android.content.res.Configuration conf2 = res2.getConfiguration();
                conf2.locale = new Locale("en");
                res2.updateConfiguration(conf2, dm2);
            }
            res = context.getResources();
            menuXML = res.getStringArray(R.array.menu);
            addSearchSubmenuXML = res.getStringArray(R.array.addSearchSubmenu);
            editSubmenuXML = res.getStringArray(R.array.editSubmenu);
            notificationsSubmenuXML = res.getStringArray(R.array.notificationsSubmenu);
            historySubmenuXML = res.getStringArray(R.array.historySubmenu);
            spinnerXML = res.getStringArray(R.array.customer_search);
            translateMenu();
            if (itemSelectedId == R.id.nav_home) {
                setTitle(res.getString(R.string.home));
                for (int i = 0; i < textViewArray.length; i++) {
                    if (i == 0) {
                        resourceID = mainActivity.getResources().getIdentifier("welcome", "string", packageName);
                    } else if (i == 1) {
                        resourceID = mainActivity.getResources().getIdentifier("createdBy", "string", packageName);
                    } else {
                        resourceID = mainActivity.getResources().getIdentifier("sendEmail", "string", packageName);
                    }
                    textViewArray[i].setText(res.getString(resourceID));
                }
            } else if (itemSelectedId == R.id.nav_customer_add) {
                setTitle(addSearchSubmenuXML[0]);
                for (int i = 0; i < 7; i++) {
                    resourceID = getResources().getIdentifier(header[i], "string", packageName);
                    textViewArray[i].setText(res.getString(resourceID) + " :");
                    if (i == 0 || i == 1)
                        textViewArray[i].setText("* " + textViewArray[i].getText());
                }
                resourceID = getResources().getIdentifier("insert", "string", packageName);
                btn.setText(res.getString(resourceID));
            } else if (itemSelectedId == R.id.nav_customer_search || itemSelectedId == R.id.nav_customer_edit) {
                if (functionNow == "updateCustomersAndDebt" && itemSelectedId == R.id.nav_customer_edit) {
                    resourceID = getResources().getIdentifier("change", "string", packageName);
                    btn.setText(res.getString(resourceID));
                } else {
                    for (int i = 0; i < headerΤextViewArray.length; i++) {
                        resourceID = getResources().getIdentifier(header[i], "string", packageName);
                        headerΤextViewArray[i].setText(res.getString(resourceID));
                    }
                    if (itemSelectedId == R.id.nav_customer_search) {
                        setTitle(addSearchSubmenuXML[1]);
                    } else if (itemSelectedId == R.id.nav_customer_edit) {
                        setTitle(editSubmenuXML[0]);
                    }
                }
            } else if (itemSelectedId == R.id.nav_add_note) {
                resourceID = getResources().getIdentifier("insert", "string", packageName);
                btn.setText(res.getString(resourceID));
                setTitle(addSearchSubmenuXML[2]);
            } else if (itemSelectedId == R.id.nav_customer_debt || itemSelectedId == R.id.nav_customer_payment || itemSelectedId == R.id.nav_customer_guarantee || itemSelectedId == R.id.nav_customer_task) {
                if (functionNow == "") {
                    for (int i = 0; i < headerΤextViewArray.length; i++) {
                        if (i == 2) {
                            if (itemSelectedId == R.id.nav_customer_debt || itemSelectedId == R.id.nav_customer_payment) {
                                resourceID = getResources().getIdentifier("debt", "string", packageName);
                                headerΤextViewArray[i].setText(res.getString(resourceID));
                            } else if (itemSelectedId == R.id.nav_customer_guarantee) {
                                resourceID = getResources().getIdentifier("guarantees", "string", packageName);
                                headerΤextViewArray[i].setText(res.getString(resourceID));
                            } else {
                                resourceID = getResources().getIdentifier("tasks", "string", packageName);
                                headerΤextViewArray[i].setText(res.getString(resourceID));
                            }
                        } else {
                            resourceID = getResources().getIdentifier(header[i], "string", packageName);
                            headerΤextViewArray[i].setText(res.getString(resourceID));
                        }

                    }
                }
                if (itemSelectedId == R.id.nav_customer_debt)
                    setTitle(editSubmenuXML[1]);
                else if (itemSelectedId == R.id.nav_customer_payment)
                    setTitle(editSubmenuXML[2]);
                else if (itemSelectedId == R.id.nav_customer_guarantee)
                    setTitle(editSubmenuXML[3]);
                else
                    setTitle(editSubmenuXML[4]);
            } else if (itemSelectedId == R.id.nav_tasks_today) {
                setTitle(notificationsSubmenuXML[0] + " (" + todayTasks + ")");
            } else if (itemSelectedId == R.id.nav_tasks_tomorrow) {
                setTitle(notificationsSubmenuXML[1] + " (" + tomorrowTasks + ")");
            } else if (itemSelectedId == R.id.nav_pending_tasks) {
                setTitle(notificationsSubmenuXML[2] + " (" + pendingTasks + ")");
            } else if (itemSelectedId == R.id.nav_notes) {
                {
                    setTitle(notificationsSubmenuXML[3]);
                    for (int i = 0; i < personalNotesModel.size(); i++) {
                        resourceID = mainActivity.getResources().getIdentifier("change", "string", packageName);
                        btns[i].setText(res.getString(resourceID));
                    }
                }
            } else if (itemSelectedId == R.id.nav_customer_deleted_guarantees) {
                setTitle(historySubmenuXML[0] + " (" + deletedGuarantees + ")");
            } else if (itemSelectedId == R.id.nav_customer_payment_history) {
                setTitle(historySubmenuXML[1]);
                int i;
                if (functionNow == "seeCreditDebtHistory") {
                    for (i = 0; i < dataLength; i++) {
                        debtDoubleParse = Double.valueOf(tempCustomersCreditDebtModel.get(i).oldDebt);
                        debtFormatted = decimalFormat.format(debtDoubleParse);
                        resourceID = mainActivity.getResources().getIdentifier("totalDebt", "string", packageName);
                        oldDebtTextArray[i].setText(res.getString(resourceID) + " " + debtFormatted);

                        debtDoubleParse = Double.valueOf(tempCustomersCreditDebtModel.get(i).creditDebt);
                        debtFormatted = decimalFormat.format(debtDoubleParse);
                        resourceID = mainActivity.getResources().getIdentifier("credit", "string", packageName);
                        creditDebtTextArray[i].setText(res.getString(resourceID) + " : " + debtFormatted);

                        debtDoubleParse = Double.valueOf(tempCustomersCreditDebtModel.get(i).currentDebt);
                        debtFormatted = decimalFormat.format(debtDoubleParse);
                        resourceID = mainActivity.getResources().getIdentifier("newDebt", "string", packageName);
                        currentDebtTextArray[i].setText(res.getString(resourceID) + " " + debtFormatted);
                    }
                } else {
                    for (i = 0; i < headerΤextViewArray.length; i++) {
                        resourceID = mainActivity.getResources().getIdentifier(header[i], "string", packageName);
                        headerΤextViewArray[i].setText(res.getString(resourceID));
                    }
                }
            }
            if (functionNow == "updateGuaranteeAndTask") {
                for (int i = 0; i < dataLength; i++) {
                    if (itemSelectedId == R.id.nav_customer_task) {
                        resourceID = mainActivity.getResources().getIdentifier("time", "string", packageName);
                        customerTimeText[i].setText(res.getString(resourceID));
                    }
                    resourceID = mainActivity.getResources().getIdentifier("date", "string", packageName);
                    customerDateText[i].setText(res.getString(resourceID));
                    resourceID = mainActivity.getResources().getIdentifier("details", "string", packageName);
                    customerDetailsText[i].setText(res.getString(resourceID));
                    resourceID = mainActivity.getResources().getIdentifier("details", "string", packageName);
                    btns[i].setText(res.getString(resourceID));
                }
            } else if (functionNow == "getNotificationTasks") {
                setTitle(menuXML[2] + " (" + notificationTaskCounter + ")");
            } else if (functionNow == "updateCustomersAndDebt" && (itemSelectedId == R.id.nav_customer_debt || itemSelectedId == R.id.nav_customer_payment)) {
                resourceID = getResources().getIdentifier("totalDebt", "string", packageName);
                totalDebtText.setText(res.getString(resourceID) + " " + decimalFormat.format(debtDoubleParse));
                if (itemSelectedId == R.id.nav_customer_debt)
                    resourceID = getResources().getIdentifier("insert", "string", packageName);
                else
                    resourceID = getResources().getIdentifier("credit", "string", packageName);
                btn.setText(res.getString(resourceID).toUpperCase());
                resourceID = getResources().getIdentifier("newDebt", "string", packageName);
                if (newCustomerDebt == 0.00)
                    newDebt.setText(res.getString(resourceID) + " 0");
                else
                    newDebt.setText(res.getString(resourceID) + " " + debtFormatted);
            } else if (functionNow == "addItem") {
                for (int i = 0; i < editTextArray.length; i++) {
                    if (itemSelectedId == R.id.nav_customer_guarantee) {
                        if (i == 0) {
                            resourceID = mainActivity.getResources().getIdentifier("dateExpiration", "string", packageName);
                        } else {
                            resourceID = mainActivity.getResources().getIdentifier("details", "string", packageName);
                        }
                        textViewArray[i].setText(res.getString(resourceID));
                    } else {
                        if (i == 0) {
                            resourceID = mainActivity.getResources().getIdentifier("date", "string", packageName);
                        } else if (i == 1) {
                            resourceID = mainActivity.getResources().getIdentifier("time", "string", packageName);
                        } else {
                            resourceID = mainActivity.getResources().getIdentifier("details", "string", packageName);
                        }
                        textViewArray[i].setText(res.getString(resourceID));
                    }
                }
                resourceID = mainActivity.getResources().getIdentifier("insert", "string", packageName);
                btn.setText(res.getString(resourceID));
            }
            if (!(itemSelectedId == R.id.nav_customer_deleted_guarantees || itemSelectedId == R.id.nav_customer_add || itemSelectedId == R.id.nav_home || itemSelectedId == R.id.nav_tasks_today || itemSelectedId == R.id.nav_tasks_tomorrow || itemSelectedId == R.id.nav_pending_tasks || itemSelectedId == R.id.nav_notes || itemSelectedId == 0 || itemSelectedId == R.id.nav_add_note)) {
                resourceID = getResources().getIdentifier("search", "string", packageName);
                searchView.setQueryHint(res.getString(resourceID));

                List<String> list = new ArrayList<String>();
                for (int i = 0; i < spinnerXML.length; i++)
                    list.add(spinnerXML[i]);
                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mainActivity,
                        android.R.layout.simple_spinner_item, list);
                dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(dataAdapter);
                spinner.setSelection(spinnerSelectedItem);
                spinnerFirstCall = true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        itemSelectedId = item.getItemId();
        if (prevItemSelectedId != itemSelectedId) {
            MenuItem selectedItemText = menu.findItem(itemSelectedId);
            setTitle(selectedItemText.getTitle());
            MenuItem deselectePrevItem = menu.findItem(prevItemSelectedId);
            deselectePrevItem.setChecked(false);
            navigateMeFunction = 0;
            searchText = "";
            functionNow = "";
            removeSomeViews();
            prevItemSelectedId = itemSelectedId;
            if (itemSelectedId == R.id.nav_customer_add) {
                addCustomer();
            } else if (itemSelectedId == R.id.nav_add_note) {
                addPersonalNote();
            } else if (itemSelectedId == R.id.nav_home) {
                relativeLayout.addView(browser);
                home();
            } else {
                new loadingTask().execute();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void addCustomer() {
        textViewArray = new TextView[7];
        editTextArray = new EditText[7];
        int y = 10;
        for (int i = 0; i < 7; i++) {
            textViewArray[i] = new TextView(context);
            relativeLayout.addView(textViewArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) textViewArray[i].getLayoutParams();
            relativeParams.setMargins(w * 29 / 100, h * y / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            textViewArray[i].setLayoutParams(relativeParams);
            textViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            textViewArray[i].setTypeface(null, Typeface.BOLD);
            textViewArray[i].setTextColor(Color.BLACK);
            resourceID = getResources().getIdentifier(header[i], "string", packageName);
            textViewArray[i].setText(res.getString(resourceID) + " :");
            if (i == 0 || i == 1)
                textViewArray[i].setText("* " + textViewArray[i].getText());


            editTextArray[i] = new EditText(context);
            relativeLayout.addView(editTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) editTextArray[i].getLayoutParams();
            relativeParams.setMargins(w * 29 / 100, h * (y + 3) / 100, 0, h * 5 / 100);
            relativeParams.width = w * 40 / 100;
            editTextArray[i].setLayoutParams(relativeParams);
            if (i == 2 || i == 3)
                editTextArray[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            editTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);
            editTextArray[i].setSingleLine(true);
            editTextArray[i].setTextColor(Color.BLACK);
            editTextArray[i].getBackground().mutate().setColorFilter(mainActivity.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            try {
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(editTextArray[i], R.drawable.cursor);
            } catch (Exception ignored) {
            }
            y += 18;
        }


        btn = new Button(context);
        relativeLayout.addView(btn);
        relativeParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
        relativeParams.setMargins(w * 33 / 100, h * (y + 1) / 100, 0, h * 10 / 100);
        relativeParams.width = w * 33 / 100;
        relativeParams.height = h * 10 / 100;
        btn.setLayoutParams(relativeParams);
        resourceID = getResources().getIdentifier("insert", "string", packageName);
        btn.setText(res.getString(resourceID));
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        btn.setTextColor(Color.BLACK);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setBackgroundColor(Color.parseColor("#D6D7D7"));
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkRequiredFields()) {
                    resourceID = getResources().getIdentifier("confirm", "string", packageName);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle(res.getString(resourceID));
                    builder.setCancelable(false);
                    resourceID = getResources().getIdentifier("confirmMessage", "string", packageName);
                    builder.setMessage(res.getString(resourceID));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (mydb.insertCustomer(editTextArray[0].getText().toString(), editTextArray[1].getText().toString(), editTextArray[2].getText().toString(), editTextArray[3].getText().toString(), editTextArray[4].getText().toString(), editTextArray[5].getText().toString(), editTextArray[6].getText().toString())) {
                                CustomersModel temp = new CustomersModel();
                                temp.id = mydb.getMaxCustomerId();
                                temp.name = editTextArray[0].getText().toString();
                                temp.surname = editTextArray[1].getText().toString();
                                temp.phone1 = editTextArray[2].getText().toString();
                                temp.phone2 = editTextArray[3].getText().toString();
                                temp.city = editTextArray[4].getText().toString();
                                temp.address = editTextArray[5].getText().toString();
                                temp.email = editTextArray[6].getText().toString();
                                temp.guaranteeCount = 0;
                                temp.debt = 0.0;
                                temp.taskCount = 0;
                                customersModel.add(temp);
                                toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                                resourceID = getResources().getIdentifier("insertSuccess", "string", packageName);
                                toastMessage.setText(res.getString(resourceID));
                                toast.setView(toastMessage);
                                toast.show();
                                scrollView.fullScroll(ScrollView.FOCUS_UP);
                                removeSomeViews();
                                addCustomer();
                            } else {
                                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                resourceID = getResources().getIdentifier("insertUnsuccess", "string", packageName);
                                toastMessage.setText(res.getString(resourceID));
                                toast.setView(toastMessage);
                                toast.show();
                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    public void addPersonalNote() {
        editTextArray = new EditText[1];
        editTextArray[0] = new EditText(context);
        relativeLayout.addView(editTextArray[0]);
        relativeParams = (RelativeLayout.LayoutParams) editTextArray[0].getLayoutParams();
        relativeParams.setMargins(w * 2 / 100, h * 15 / 100, 0, 0);
        relativeParams.width = w * 90 / 100;
        relativeParams.height = h * 45 / 100;
        editTextArray[0].setLayoutParams(relativeParams);
        editTextArray[0].setGravity(Gravity.TOP);
        editTextArray[0].setSingleLine(false);
        editTextArray[0].setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
        editTextArray[0].setMaxLines(h * 30 / 100);
        editTextArray[0].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        editTextArray[0].setVerticalScrollBarEnabled(true);
        editTextArray[0].setHorizontalScrollBarEnabled(true);
        editTextArray[0].setMovementMethod(new ScrollingMovementMethod());
        editTextArray[0].setTextColor(Color.BLACK);
        editTextArray[0].getBackground().mutate().setColorFilter(mainActivity.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
        try {
            Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
            f.setAccessible(true);
            f.set(editTextArray[0], R.drawable.cursor);
        } catch (Exception ignored) {
        }
        editTextArray[0].setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        editTextArray[0].setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(final View v, final MotionEvent motionEvent) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                return false;
            }
        });
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
            editTextArray[0].setBackgroundDrawable(detailsTextViewBorderGd);
        else
            editTextArray[0].setBackground(detailsTextViewBorderGd);


        btn = new Button(context);
        relativeLayout.addView(btn);
        relativeParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
        relativeParams.setMargins(w * 31 / 100, h * 70 / 100, 0, h * 10 / 100);
        relativeParams.width = w * 33 / 100;
        relativeParams.height = h * 10 / 100;
        btn.setLayoutParams(relativeParams);
        resourceID = getResources().getIdentifier("insert", "string", packageName);
        btn.setText(res.getString(resourceID));
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        btn.setTextColor(Color.BLACK);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setBackgroundColor(Color.parseColor("#D6D7D7"));
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resourceID = getResources().getIdentifier("confirm", "string", packageName);
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle(res.getString(resourceID));
                builder.setCancelable(false);
                resourceID = getResources().getIdentifier("confirmMessage", "string", packageName);
                builder.setMessage(res.getString(resourceID));
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        if (mydb.insertPersonalNote(editTextArray[0].getText().toString())) {
                            PersonalNotesModel temp = new PersonalNotesModel();
                            temp.id = mydb.getMaxPersonalNoteId();
                            temp.note = editTextArray[0].getText().toString();
                            personalNotesModel.add(temp);
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            resourceID = getResources().getIdentifier("insertSuccess", "string", packageName);
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                            scrollView.fullScroll(ScrollView.FOCUS_UP);
                            removeSomeViews();
                            addPersonalNote();
                        } else {
                            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                            resourceID = getResources().getIdentifier("insertUnsuccess", "string", packageName);
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    public static void searchEditCustomer() {
        if (!horizontalRelativeLayout.isShown()) {
            horizontalScrollView.setY(h * 25 / 100);
            horizontalScrollView.setX(w * 2 / 100);
            horizontalScrollView.getLayoutParams().height = h * 97 / 100;
            horizontalScrollView.getLayoutParams().width = w * 96 / 100;
        }
        scrollViewTable.setY(h * 10 / 100);
        scrollViewTable.setX(w * -1 / 100);
        scrollViewTable.getLayoutParams().height = h * 65 / 100;
        scrollViewTable.fullScroll(ScrollView.FOCUS_UP);
        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        customerNameTextArray = new TextView[customersModel.size()];
        customerSurnameTextArray = new TextView[customersModel.size()];
        customerPhone1TextArray = new TextView[customersModel.size()];
        customerPhone2TextArray = new TextView[customersModel.size()];
        customerAddressTextArray = new TextView[customersModel.size()];
        customerCityTextArray = new TextView[customersModel.size()];
        customerEmailTextArray = new TextView[customersModel.size()];
        if (itemSelectedId == R.id.nav_customer_search) {
            customerDebtTextArray = new TextView[customersModel.size()];
            customerGuaranteeCountTextArray = new TextView[customersModel.size()];
            customerTaskCountTextArray = new TextView[customersModel.size()];
            headerΤextViewArray = new TextView[header.length];
        } else if (itemSelectedId == R.id.nav_customer_edit) {
            headerΤextViewArray = new TextView[7];
            imageViewEdit = new ImageView[customersModel.size()];
            imageViewDelete = new ImageView[customersModel.size()];
        }
        int i, x = 0, y = 0;
        for (i = 0; i < headerΤextViewArray.length; i++) {
            headerΤextViewArray[i] = new TextView(context);
            horizontalRelativeLayout.addView(headerΤextViewArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) headerΤextViewArray[i].getLayoutParams();
            relativeParams.width = w * 40 / 100;
            relativeParams.height = h * 7 / 100;
            relativeParams.setMargins(w * x / 100, 0, 0, 0);
            headerΤextViewArray[i].setLayoutParams(relativeParams);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                headerΤextViewArray[i].setBackgroundDrawable(gd);
            else
                headerΤextViewArray[i].setBackground(gd);
            headerΤextViewArray[i].setGravity(Gravity.CENTER);
            headerΤextViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            headerΤextViewArray[i].setTypeface(null, Typeface.BOLD);
            headerΤextViewArray[i].setTextColor(Color.BLACK);
            resourceID = mainActivity.getResources().getIdentifier(header[i], "string", packageName);
            headerΤextViewArray[i].setText(res.getString(resourceID));
            x += 43;
        }
        for (i = 0; i < customersModel.size(); i++) {
            if ((customersModel.get(i).name.trim().toLowerCase().startsWith(searchText.trim().toLowerCase()) && spinnerSelectedItem == 0) || (customersModel.get(i).surname.trim().toLowerCase().startsWith(searchText.trim().toLowerCase()) && spinnerSelectedItem == 1)) {
                customerNameTextArray[i] = new TextView(context);
                customerSurnameTextArray[i] = new TextView(context);
                customerPhone1TextArray[i] = new TextView(context);
                customerPhone2TextArray[i] = new TextView(context);
                customerCityTextArray[i] = new TextView(context);
                customerAddressTextArray[i] = new TextView(context);
                customerEmailTextArray[i] = new TextView(context);
                verticalRelativeLayout.addView(customerNameTextArray[i]);
                verticalRelativeLayout.addView(customerSurnameTextArray[i]);
                verticalRelativeLayout.addView(customerPhone1TextArray[i]);
                verticalRelativeLayout.addView(customerPhone2TextArray[i]);
                verticalRelativeLayout.addView(customerAddressTextArray[i]);
                verticalRelativeLayout.addView(customerCityTextArray[i]);
                verticalRelativeLayout.addView(customerEmailTextArray[i]);
                if (itemSelectedId == R.id.nav_customer_search) {
                    customerDebtTextArray[i] = new TextView(context);
                    customerGuaranteeCountTextArray[i] = new TextView(context);
                    customerTaskCountTextArray[i] = new TextView(context);
                    verticalRelativeLayout.addView(customerDebtTextArray[i]);
                    verticalRelativeLayout.addView(customerGuaranteeCountTextArray[i]);
                    verticalRelativeLayout.addView(customerTaskCountTextArray[i]);
                } else if (itemSelectedId == R.id.nav_customer_edit) {
                    imageViewEdit[i] = new ImageView(context);
                    imageViewDelete[i] = new ImageView(context);
                    verticalRelativeLayout.addView(imageViewEdit[i]);
                    verticalRelativeLayout.addView(imageViewDelete[i]);
                }
                x = 0;


                relativeParams = (RelativeLayout.LayoutParams) customerNameTextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                customerNameTextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerNameTextArray[i].setBackgroundDrawable(gd);
                else
                    customerNameTextArray[i].setBackground(gd);
                customerNameTextArray[i].setGravity(Gravity.CENTER);
                customerNameTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerNameTextArray[i].setTypeface(null, Typeface.BOLD);
                customerNameTextArray[i].setTextColor(Color.BLACK);
                customerNameTextArray[i].setText(customersModel.get(i).name);
                x += 43;


                relativeParams = (RelativeLayout.LayoutParams) customerSurnameTextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                customerSurnameTextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerSurnameTextArray[i].setBackgroundDrawable(gd);
                else
                    customerSurnameTextArray[i].setBackground(gd);
                customerSurnameTextArray[i].setGravity(Gravity.CENTER);
                customerSurnameTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerSurnameTextArray[i].setTypeface(null, Typeface.BOLD);
                customerSurnameTextArray[i].setTextColor(Color.BLACK);
                customerSurnameTextArray[i].setText(customersModel.get(i).surname);
                x += 43;


                relativeParams = (RelativeLayout.LayoutParams) customerPhone1TextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                customerPhone1TextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerPhone1TextArray[i].setBackgroundDrawable(gd);
                else
                    customerPhone1TextArray[i].setBackground(gd);
                customerPhone1TextArray[i].setGravity(Gravity.CENTER);
                customerPhone1TextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerPhone1TextArray[i].setTypeface(null, Typeface.BOLD);
                customerPhone1TextArray[i].setTextColor(Color.BLACK);
                customerPhone1TextArray[i].setText(customersModel.get(i).phone1);
                x += 43;


                relativeParams = (RelativeLayout.LayoutParams) customerPhone2TextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                customerPhone2TextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerPhone2TextArray[i].setBackgroundDrawable(gd);
                else
                    customerPhone2TextArray[i].setBackground(gd);
                customerPhone2TextArray[i].setGravity(Gravity.CENTER);
                customerPhone2TextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerPhone2TextArray[i].setTypeface(null, Typeface.BOLD);
                customerPhone2TextArray[i].setTextColor(Color.BLACK);
                customerPhone2TextArray[i].setText(customersModel.get(i).phone2);
                x += 43;


                relativeParams = (RelativeLayout.LayoutParams) customerCityTextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                customerCityTextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerCityTextArray[i].setBackgroundDrawable(gd);
                else
                    customerCityTextArray[i].setBackground(gd);
                customerCityTextArray[i].setGravity(Gravity.CENTER);
                customerCityTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerCityTextArray[i].setTypeface(null, Typeface.BOLD);
                customerCityTextArray[i].setTextColor(Color.BLACK);
                customerCityTextArray[i].setText(customersModel.get(i).city);
                x += 43;


                relativeParams = (RelativeLayout.LayoutParams) customerAddressTextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                customerAddressTextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerAddressTextArray[i].setBackgroundDrawable(gd);
                else
                    customerAddressTextArray[i].setBackground(gd);
                customerAddressTextArray[i].setGravity(Gravity.CENTER);
                customerAddressTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerAddressTextArray[i].setTypeface(null, Typeface.BOLD);
                customerAddressTextArray[i].setTextColor(Color.BLACK);
                customerAddressTextArray[i].setText(customersModel.get(i).address);
                x += 43;


                relativeParams = (RelativeLayout.LayoutParams) customerEmailTextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                customerEmailTextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerEmailTextArray[i].setBackgroundDrawable(gd);
                else
                    customerEmailTextArray[i].setBackground(gd);
                customerEmailTextArray[i].setGravity(Gravity.CENTER);
                customerEmailTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerEmailTextArray[i].setTypeface(null, Typeface.BOLD);
                customerEmailTextArray[i].setTextColor(Color.BLACK);
                customerEmailTextArray[i].setText(customersModel.get(i).email);
                x += 43;
                if (itemSelectedId == R.id.nav_customer_search) {
                    relativeParams = (RelativeLayout.LayoutParams) customerDebtTextArray[i].getLayoutParams();
                    relativeParams.width = w * 40 / 100;
                    relativeParams.height = h * 7 / 100;
                    relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                    customerDebtTextArray[i].setLayoutParams(relativeParams);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                        customerDebtTextArray[i].setBackgroundDrawable(gd);
                    else
                        customerDebtTextArray[i].setBackground(gd);
                    customerDebtTextArray[i].setGravity(Gravity.CENTER);
                    customerDebtTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                    customerDebtTextArray[i].setTypeface(null, Typeface.BOLD);
                    customerDebtTextArray[i].setTextColor(Color.BLACK);
                    debtFormatted = decimalFormat.format(customersModel.get(i).debt);
                    customerDebtTextArray[i].setText(debtFormatted);
                    x += 43;


                    relativeParams = (RelativeLayout.LayoutParams) customerGuaranteeCountTextArray[i].getLayoutParams();
                    relativeParams.width = w * 40 / 100;
                    relativeParams.height = h * 7 / 100;
                    relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                    customerGuaranteeCountTextArray[i].setLayoutParams(relativeParams);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                        customerGuaranteeCountTextArray[i].setBackgroundDrawable(gd);
                    else
                        customerGuaranteeCountTextArray[i].setBackground(gd);
                    customerGuaranteeCountTextArray[i].setGravity(Gravity.CENTER);
                    customerGuaranteeCountTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                    customerGuaranteeCountTextArray[i].setTypeface(null, Typeface.BOLD);
                    customerGuaranteeCountTextArray[i].setTextColor(Color.BLACK);
                    customerGuaranteeCountTextArray[i].setText("" + customersModel.get(i).guaranteeCount);
                    x += 43;


                    relativeParams = (RelativeLayout.LayoutParams) customerTaskCountTextArray[i].getLayoutParams();
                    relativeParams.width = w * 40 / 100;
                    relativeParams.height = h * 7 / 100;
                    relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                    customerTaskCountTextArray[i].setLayoutParams(relativeParams);
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                        customerTaskCountTextArray[i].setBackgroundDrawable(gd);
                    else
                        customerTaskCountTextArray[i].setBackground(gd);
                    customerTaskCountTextArray[i].setGravity(Gravity.CENTER);
                    customerTaskCountTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                    customerTaskCountTextArray[i].setTypeface(null, Typeface.BOLD);
                    customerTaskCountTextArray[i].setTextColor(Color.BLACK);
                    customerTaskCountTextArray[i].setText("" + customersModel.get(i).taskCount);
                } else if (itemSelectedId == R.id.nav_customer_edit) {
                    relativeParams = (RelativeLayout.LayoutParams) imageViewEdit[i].getLayoutParams();
                    relativeParams.width = w * 8 / 100;
                    relativeParams.height = h * 5 / 100;
                    relativeParams.setMargins(w * (x + 3) / 100, h * (y + 1) / 100, 0, h * 9 / 100);
                    imageViewEdit[i].setLayoutParams(relativeParams);
                    imageViewEdit[i].setBackgroundResource(R.drawable.ic_edit);
                    imageViewEdit[i].setTag(i);
                    imageViewEdit[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customerIndex = (int) v.getTag();
                            resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                            builder.setTitle(res.getString(resourceID));
                            builder.setCancelable(false);
                            resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                            builder.setMessage(res.getString(resourceID));
                            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    customerId = customersModel.get(customerIndex).id;
                                    customerDebt = customersModel.get(customerIndex).debt;
                                    customerGuaranteeCount = customersModel.get(customerIndex).guaranteeCount;
                                    customerTaskCount = customersModel.get(customerIndex).taskCount;
                                    customerTextArray = new ArrayList<String>();
                                    customerTextArray.add(customerNameTextArray[customerIndex].getText().toString());
                                    customerTextArray.add(customerSurnameTextArray[customerIndex].getText().toString());
                                    customerTextArray.add(customerPhone1TextArray[customerIndex].getText().toString());
                                    customerTextArray.add(customerPhone2TextArray[customerIndex].getText().toString());
                                    customerTextArray.add(customerCityTextArray[customerIndex].getText().toString());
                                    customerTextArray.add(customerAddressTextArray[customerIndex].getText().toString());
                                    customerTextArray.add(customerEmailTextArray[customerIndex].getText().toString());
                                    updateCustomersAndDebt();
                                }
                            });
                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                    x += 17;


                    relativeParams = (RelativeLayout.LayoutParams) imageViewDelete[i].getLayoutParams();
                    relativeParams.width = w * 8 / 100;
                    relativeParams.height = h * 5 / 100;
                    relativeParams.setMargins(w * x / 100, h * (y + 1) / 100, w * 5 / 100, h * 9 / 100);
                    imageViewDelete[i].setLayoutParams(relativeParams);
                    imageViewDelete[i].setBackgroundResource(R.drawable.ic_customer_delete);
                    imageViewDelete[i].setTag(i);
                    imageViewDelete[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customerIndex = (int) v.getTag();
                            resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                            builder.setTitle(res.getString(resourceID));
                            builder.setCancelable(false);
                            resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                            builder.setMessage(res.getString(resourceID));
                            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    customerId = customersModel.get(customerIndex).id;
                                    mydb.deleteCustomer(customerId);
                                    mydb.deleteCustomerGuarantee(customerId);
                                    mydb.deleteCustomerTask(customerId);
                                    mydb.deleteCustomerCreditDebt(customerId, -1);
                                    customersCreditDebtModel = new ArrayList<CustomersCreditDebtModel>();
                                    customersCreditDebtModel = mydb.getAllCustomersCreditDebt();
                                    customersModel = new ArrayList<CustomersModel>();
                                    customersModel = mydb.getCustomers();
                                    resourceID = mainActivity.getResources().getIdentifier("deleteSuccess", "string", packageName);
                                    toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                                    toastMessage.setText(res.getString(resourceID));
                                    toast.setView(toastMessage);
                                    toast.show();
                                    navigateMe();
                                }
                            });
                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
                y += 8;
            }
        }
    }

    //debt, guarantee and task
    public static void customerDataSearch() {
        if (!horizontalRelativeLayout.isShown()) {
            horizontalScrollView.setY(h * 25 / 100);
            horizontalScrollView.setX(w * 2 / 100);
            horizontalScrollView.getLayoutParams().height = h * 97 / 100;
            horizontalScrollView.getLayoutParams().width = w * 96 / 100;
        }
        scrollViewTable.setY(h * 10 / 100);
        scrollViewTable.setX(w * -1 / 100);
        scrollViewTable.getLayoutParams().height = h * 65 / 100;
        scrollViewTable.fullScroll(ScrollView.FOCUS_UP);
        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        headerΤextViewArray = new TextView[3];
        int i, x = 0, y = 0;
        for (i = 0; i < headerΤextViewArray.length; i++) {
            headerΤextViewArray[i] = new TextView(context);
            horizontalRelativeLayout.addView(headerΤextViewArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) headerΤextViewArray[i].getLayoutParams();
            relativeParams.width = w * 40 / 100;
            relativeParams.height = h * 7 / 100;
            relativeParams.setMargins(w * x / 100, 0, 0, 0);
            headerΤextViewArray[i].setLayoutParams(relativeParams);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                headerΤextViewArray[i].setBackgroundDrawable(gd);
            else
                headerΤextViewArray[i].setBackground(gd);
            headerΤextViewArray[i].setGravity(Gravity.CENTER);
            headerΤextViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            headerΤextViewArray[i].setTextColor(Color.BLACK);
            headerΤextViewArray[i].setTypeface(null, Typeface.BOLD);
            if (i == 2) {
                if (itemSelectedId == R.id.nav_customer_debt || itemSelectedId == R.id.nav_customer_payment) {
                    resourceID = mainActivity.getResources().getIdentifier("debt", "string", packageName);
                    headerΤextViewArray[i].setText(res.getString(resourceID));
                } else if (itemSelectedId == R.id.nav_customer_guarantee) {
                    resourceID = mainActivity.getResources().getIdentifier("guarantees", "string", packageName);
                    headerΤextViewArray[i].setText(res.getString(resourceID));
                } else {
                    resourceID = mainActivity.getResources().getIdentifier("tasks", "string", packageName);
                    headerΤextViewArray[i].setText(res.getString(resourceID));
                }
            } else {
                resourceID = mainActivity.getResources().getIdentifier(header[i], "string", packageName);
                headerΤextViewArray[i].setText(res.getString(resourceID));
            }
            x += 43;
        }
        customerNameTextArray = new TextView[customersModel.size()];
        customerSurnameTextArray = new TextView[customersModel.size()];
        textViewArray = new TextView[customersModel.size()];
        imageViewEdit = new ImageView[customersModel.size()];
        ImageView[] imageViewAdd = new ImageView[customersModel.size()];
        for (i = 0; i < customersModel.size(); i++) {
            if ((customersModel.get(i).name.trim().toLowerCase().startsWith(searchText.trim().toLowerCase()) && spinnerSelectedItem == 0) || (customersModel.get(i).surname.trim().toLowerCase().startsWith(searchText.trim().toLowerCase()) && spinnerSelectedItem == 1)) {
                int noEdit = 0;
                x = 0;

                customerNameTextArray[i] = new TextView(context);
                customerSurnameTextArray[i] = new TextView(context);
                textViewArray[i] = new TextView(context);
                imageViewAdd[i] = new ImageView(context);
                verticalRelativeLayout.addView(customerNameTextArray[i]);
                verticalRelativeLayout.addView(customerSurnameTextArray[i]);
                verticalRelativeLayout.addView(textViewArray[i]);
                verticalRelativeLayout.addView(imageViewAdd[i]);
                if (itemSelectedId == R.id.nav_customer_payment || itemSelectedId == R.id.nav_customer_debt) {
                    imageViewEdit[i] = new ImageView(context);
                    verticalRelativeLayout.addView(imageViewEdit[i]);
                }
                relativeParams = (RelativeLayout.LayoutParams) customerNameTextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                customerNameTextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerNameTextArray[i].setBackgroundDrawable(gd);
                else
                    customerNameTextArray[i].setBackground(gd);
                customerNameTextArray[i].setGravity(Gravity.CENTER);
                customerNameTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerNameTextArray[i].setTypeface(null, Typeface.BOLD);
                customerNameTextArray[i].setTextColor(Color.BLACK);
                customerNameTextArray[i].setText(customersModel.get(i).name);
                x += 43;


                relativeParams = (RelativeLayout.LayoutParams) customerSurnameTextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 100, h * 9 / 100);
                customerSurnameTextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerSurnameTextArray[i].setBackgroundDrawable(gd);
                else
                    customerSurnameTextArray[i].setBackground(gd);
                customerSurnameTextArray[i].setGravity(Gravity.CENTER);
                customerSurnameTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerSurnameTextArray[i].setTypeface(null, Typeface.BOLD);
                customerSurnameTextArray[i].setTextColor(Color.BLACK);
                customerSurnameTextArray[i].setText(customersModel.get(i).surname);
                x += 43;


                relativeParams = (RelativeLayout.LayoutParams) textViewArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 100, h * 9 / 100);
                textViewArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    textViewArray[i].setBackgroundDrawable(gd);
                else
                    textViewArray[i].setBackground(gd);
                textViewArray[i].setGravity(Gravity.CENTER);
                textViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                textViewArray[i].setTypeface(null, Typeface.BOLD);
                textViewArray[i].setTextColor(Color.BLACK);
                if (itemSelectedId == R.id.nav_customer_guarantee)
                    textViewArray[i].setText("" + customersModel.get(i).guaranteeCount);
                else if (itemSelectedId == R.id.nav_customer_task)
                    textViewArray[i].setText("" + customersModel.get(i).taskCount);
                else {
                    debtFormatted = decimalFormat.format(customersModel.get(i).debt);
                    textViewArray[i].setText(debtFormatted);
                }
                x += 46;

                if (itemSelectedId == R.id.nav_customer_payment || itemSelectedId == R.id.nav_customer_debt) {
                    createImageViewEdit(i, x, y);
                } else {
                    if (itemSelectedId == R.id.nav_customer_guarantee && customersModel.get(i).guaranteeCount == 0) {
                        noEdit = 1;
                    } else if (itemSelectedId == R.id.nav_customer_task && customersModel.get(i).taskCount == 0) {
                        noEdit = 1;
                    }
                    if (noEdit == 0) {
                        imageViewEdit[i] = new ImageView(context);
                        verticalRelativeLayout.addView(imageViewEdit[i]);
                        createImageViewEdit(i, x, y);
                        x += 15;
                    }


                    relativeParams = (RelativeLayout.LayoutParams) imageViewAdd[i].getLayoutParams();
                    relativeParams.width = w * 8 / 100;
                    relativeParams.height = h * 5 / 100;
                    relativeParams.setMargins(w * x / 100, h * (y + 1) / 100, w * 5 / 100, h * 9 / 100);
                    imageViewAdd[i].setLayoutParams(relativeParams);
                    imageViewAdd[i].setBackgroundResource(R.drawable.ic_customer_add);
                    imageViewAdd[i].setTag(i);
                    imageViewAdd[i].setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customerIndex = (int) v.getTag();
                            resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                            AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                            builder.setTitle(res.getString(resourceID));
                            builder.setCancelable(false);
                            resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                            builder.setMessage(res.getString(resourceID));
                            builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    customerId = customersModel.get(customerIndex).id;
                                    addItem();
                                }
                            });
                            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                    dialog.dismiss();
                                }
                            });
                            AlertDialog dialog = builder.create();
                            dialog.show();
                        }
                    });
                }
                y += 8;
            }

        }
    }

    //guarantee and task
    public static void addItem() {
        functionNow = "addItem";
        if (mainActivity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mainActivity.getCurrentFocus().getWindowToken(), 0);
        }
        removeSomeViews();
        if (itemSelectedId == R.id.nav_customer_guarantee) {
            textViewArray = new TextView[2];
            editTextArray = new EditText[2];
        } else {
            textViewArray = new TextView[3];
            editTextArray = new EditText[3];
        }
        ImageView imageViewBack = new ImageView(context);
        relativeLayout.addView(imageViewBack);
        relativeParams = (RelativeLayout.LayoutParams) imageViewBack.getLayoutParams();
        relativeParams.width = w * 12 / 100;
        relativeParams.height = h * 7 / 100;
        relativeParams.setMargins(w * 2 / 100, h * 8 / 100, 0, 0);
        imageViewBack.setLayoutParams(relativeParams);
        imageViewBack.setBackgroundResource(R.drawable.ic_arrow_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateMe();
            }
        });
        int y = 18;
        TextView customerName = new TextView(context);
        relativeLayout.addView(customerName);
        relativeParams = (RelativeLayout.LayoutParams) customerName.getLayoutParams();
        relativeParams.setMargins(0, h * y / 100, 0, h * 5 / 100);
        relativeParams.width = w;
        customerName.setLayoutParams(relativeParams);
        customerName.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
        customerName.setTypeface(null, Typeface.BOLD);
        customerName.setGravity(Gravity.CENTER);
        customerName.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        customerName.setTextColor(Color.BLACK);
        customerName.setText(customerNameTextArray[customerIndex].getText() + " " + customerSurnameTextArray[customerIndex].getText());
        y += 18;
        for (int i = 0; i < editTextArray.length; i++) {
            textViewArray[i] = new TextView(context);
            relativeLayout.addView(textViewArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) textViewArray[i].getLayoutParams();
            relativeParams.setMargins(w * 29 / 100, h * y / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            textViewArray[i].setLayoutParams(relativeParams);
            textViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            textViewArray[i].setTypeface(null, Typeface.BOLD);
            textViewArray[i].setTextColor(Color.BLACK);
            if (itemSelectedId == R.id.nav_customer_guarantee) {
                if (i == 0) {
                    resourceID = mainActivity.getResources().getIdentifier("dateExpiration", "string", packageName);
                } else {
                    resourceID = mainActivity.getResources().getIdentifier("details", "string", packageName);
                }
                textViewArray[i].setText(res.getString(resourceID));
            } else {
                if (i == 0) {
                    resourceID = mainActivity.getResources().getIdentifier("date", "string", packageName);
                } else if (i == 1) {
                    resourceID = mainActivity.getResources().getIdentifier("time", "string", packageName);
                } else {
                    resourceID = mainActivity.getResources().getIdentifier("details", "string", packageName);
                }
                textViewArray[i].setText(res.getString(resourceID));
            }
            editTextArray[i] = new EditText(context);
            relativeLayout.addView(editTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) editTextArray[i].getLayoutParams();
            relativeParams.setMargins(w * 29 / 100, h * (y + 3) / 100, 0, h * 5 / 100);
            relativeParams.width = w * 40 / 100;
            editTextArray[i].setLayoutParams(relativeParams);
            editTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);
            editTextArray[i].setTextColor(Color.BLACK);
            editTextArray[i].getBackground().mutate().setColorFilter(mainActivity.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            try {
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(editTextArray[i], R.drawable.cursor);
            } catch (Exception ignored) {
            }
            if (i == 0) {
                editTextArray[i].setFocusable(false);
                editTextArray[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateTimePicker = "addItem";
                        dateDialog();
                        datePickerDialog.show();
                    }
                });
            } else if (i == 1 && itemSelectedId == R.id.nav_customer_task) {
                editTextArray[i].setFocusable(false);
                editTextArray[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dateTimePicker = "addItem";
                        timeDialog();
                        timePickerDialog.show();
                    }
                });
            } else if (i == editTextArray.length - 1) {
                relativeParams = (RelativeLayout.LayoutParams) editTextArray[i].getLayoutParams();
                relativeParams.setMargins(w * 2 / 100, h * (y + 5) / 100, 0, h * 5 / 100);
                relativeParams.width = w * 90 / 100;
                relativeParams.height = h * 30 / 100;
                editTextArray[i].setLayoutParams(relativeParams);
                editTextArray[i].setGravity(Gravity.TOP);
                editTextArray[i].setSingleLine(false);
                editTextArray[i].setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
                editTextArray[i].setMaxLines(h * 30 / 100);
                editTextArray[i].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
                editTextArray[i].setVerticalScrollBarEnabled(true);
                editTextArray[i].setHorizontalScrollBarEnabled(true);
                editTextArray[i].setMovementMethod(new ScrollingMovementMethod());
                editTextArray[i].setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
                editTextArray[i].setOnTouchListener(new View.OnTouchListener() {
                    public boolean onTouch(final View v, final MotionEvent motionEvent) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_UP:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                        return false;
                    }
                });
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    editTextArray[i].setBackgroundDrawable(detailsTextViewBorderGd);
                else
                    editTextArray[i].setBackground(detailsTextViewBorderGd);
            } else {
                editTextArray[i].setSingleLine(true);
            }
            y += 18;
        }
        y += 28;
        btn = new Button(context);
        relativeLayout.addView(btn);
        relativeParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
        relativeParams.setMargins(w * 31 / 100, h * y / 100, 0, h * 10 / 100);
        relativeParams.width = w * 33 / 100;
        relativeParams.height = h * 10 / 100;
        btn.setLayoutParams(relativeParams);
        btn.setTextColor(Color.BLACK);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setBackgroundColor(Color.parseColor("#D6D7D7"));
        resourceID = mainActivity.getResources().getIdentifier("insert", "string", packageName);
        btn.setText(res.getString(resourceID));
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setTitle(res.getString(resourceID));
                builder.setCancelable(false);
                resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                builder.setMessage(res.getString(resourceID));
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        int errorFound = 1;
                        String dateText;
                        if (editTextArray[0].getText().toString().length() == 0) {
                            strDate = new Date();
                            dateText = dateFormat.format(new Date());
                        } else {
                            try {
                                strDate = dateFormat.parse(editTextArray[0].getText().toString());
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                            dateText = editTextArray[0].getText().toString();
                        }
                        if (itemSelectedId == R.id.nav_customer_guarantee && (strDate.after(new Date()) || dateText.equals(dateFormat.format(new Date())))) {
                            errorFound = 0;
                            CustomersGuaranteeModel temp = new CustomersGuaranteeModel();
                            temp.id = customerId;
                            temp.date = editTextArray[0].getText().toString();
                            temp.details = editTextArray[1].getText().toString();
                            customersModel.get(customerIndex).guaranteeCount += 1;
                            temp.guaranteeRow = customersModel.get(customerIndex).guaranteeCount;
                            mydb.insertCustomerGuarantee(temp);
                        } else {
                            Calendar calTomorrow = Calendar.getInstance();
                            calTomorrow.add(Calendar.DATE, +1);
                            String tomorrow = dateFormat.format(calTomorrow.getTime());
                            int textTimeFormatted;
                            if (editTextArray[1].getText().toString().length() == 0) {
                                textTimeFormatted = 99999;
                            } else {
                                textTimeFormatted = Integer.parseInt(editTextArray[1].getText().toString().replace(":", ""));
                            }
                            String time;
                            Calendar mcurrentTime = Calendar.getInstance();
                            int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                            final int minute = mcurrentTime.get(Calendar.MINUTE);
                            if (minute < 10)
                                time = hour + ":0" + minute;
                            else
                                time = hour + ":" + minute;
                            currentTimeFormatted = Integer.parseInt(time.replace(":", ""));
                            if (strDate.after(new Date()) || (dateText.equals(dateFormat.format(new Date())) && currentTimeFormatted < textTimeFormatted)) {
                                errorFound = 0;
                                CustomersTasksModel temp = new CustomersTasksModel();
                                temp.id = customerId;
                                temp.date = editTextArray[0].getText().toString();
                                temp.time = editTextArray[1].getText().toString();
                                temp.details = editTextArray[2].getText().toString();
                                customersModel.get(customerIndex).taskCount += 1;
                                temp.taskRow = customersModel.get(customerIndex).taskCount;
                                mydb.insertCustomerTask(temp);
                                if (editTextArray[0].getText().toString().equals(dateFormat.format(new Date()))) {
                                    todayTasks++;
                                    CustomersModel model = mydb.getCustomerById(customerId);
                                    CustomersTasksExtendedModel _temp = new CustomersTasksExtendedModel();
                                    _temp.id = customerId;
                                    _temp.name = model.name + " " + model.surname;
                                    _temp.phone1 = model.phone1;
                                    _temp.phone2 = model.phone2;
                                    _temp.city = model.city;
                                    _temp.address = model.address;
                                    _temp.taskRow = temp.taskRow;
                                    _temp.date = temp.date;
                                    _temp.time = temp.time;
                                    _temp.details = temp.details;
                                    customersTasksTodayModel.add(_temp);
                                    MenuItem taskToday = menu.findItem(R.id.nav_tasks_today);
                                    taskToday.setTitle(notificationsSubmenuXML[0] + " (" + todayTasks + ")");
                                } else if (editTextArray[0].getText().toString().equals(tomorrow)) {
                                    tomorrowTasks++;
                                    CustomersModel model = mydb.getCustomerById(customerId);
                                    CustomersTasksExtendedModel _temp = new CustomersTasksExtendedModel();
                                    _temp.id = customerId;
                                    _temp.name = model.name + " " + model.surname;
                                    _temp.phone1 = model.phone1;
                                    _temp.phone2 = model.phone2;
                                    _temp.city = model.city;
                                    _temp.address = model.address;
                                    _temp.taskRow = temp.taskRow;
                                    _temp.date = temp.date;
                                    _temp.time = temp.time;
                                    _temp.details = temp.details;
                                    customersTasksTomorrowModel.add(_temp);
                                    MenuItem taskTomorrow = menu.findItem(R.id.nav_tasks_tomorrow);
                                    taskTomorrow.setTitle(notificationsSubmenuXML[1] + " (" + tomorrowTasks + ")");
                                }
                            }
                        }
                        if (errorFound == 1) {
                            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                            if (itemSelectedId == R.id.nav_customer_guarantee)
                                resourceID = mainActivity.getResources().getIdentifier("dateError", "string", packageName);
                            else
                                resourceID = mainActivity.getResources().getIdentifier("dateTimeError", "string", packageName);
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                        } else {
                            mydb.updateCustomers(customersModel.get(customerIndex));
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            resourceID = mainActivity.getResources().getIdentifier("insertSuccess", "string", packageName);
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                            navigateMe();
                        }
                    }
                });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    //customers and debt
    public static void updateCustomersAndDebt() {
        functionNow = "updateCustomersAndDebt";
        removeSomeViews();
        if (itemSelectedId == R.id.nav_customer_edit) {
            textViewArray = new TextView[7];
            editTextArray = new EditText[7];
        } else {
            textViewArray = new TextView[1];
            editTextArray = new EditText[1];
        }
        ImageView imageViewBack = new ImageView(context);
        relativeLayout.addView(imageViewBack);
        relativeParams = (RelativeLayout.LayoutParams) imageViewBack.getLayoutParams();
        relativeParams.width = w * 12 / 100;
        relativeParams.height = h * 7 / 100;
        relativeParams.setMargins(w * 2 / 100, h * 8 / 100, 0, 0);
        imageViewBack.setLayoutParams(relativeParams);
        imageViewBack.setBackgroundResource(R.drawable.ic_arrow_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateMe();
            }
        });
        int y = 15, x = 5;

        if (itemSelectedId == R.id.nav_customer_debt || itemSelectedId == R.id.nav_customer_payment) {
            debtDoubleParse = Double.valueOf(customerTextArray.get(0));
            debtFormatted = decimalFormat.format(debtDoubleParse);


            TextView customerNameSurname = new TextView(context);
            relativeLayout.addView(customerNameSurname);
            relativeParams = (RelativeLayout.LayoutParams) customerNameSurname.getLayoutParams();
            relativeParams.setMargins(0, h * y / 100, 0, 0);
            relativeParams.width = w;
            customerNameSurname.setLayoutParams(relativeParams);
            customerNameSurname.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            customerNameSurname.setTypeface(null, Typeface.BOLD);
            customerNameSurname.setTextColor(Color.BLACK);
            customerNameSurname.setGravity(Gravity.CENTER);
            customerNameSurname.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
            customerNameSurname.setText(customerNameTextArray[customerIndex].getText() + " " + customerSurnameTextArray[customerIndex].getText());
            y += 7;

            GradientDrawable tabGd = new GradientDrawable();
            tabGd.setColor(Color.parseColor("#676262"));
            tabGd.setCornerRadius(9);
            tab = new RelativeLayout(context);
            relativeLayout.addView(tab);
            relativeParams = (RelativeLayout.LayoutParams) tab.getLayoutParams();
            relativeParams.width = w * 92 / 100;
            relativeParams.height = h * 72 / 100;
            relativeParams.setMargins(0, h * y / 100, 0, h * 9 / 100);
            tab.setLayoutParams(relativeParams);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                tab.setBackgroundDrawable(tabGd);
            else
                tab.setBackground(tabGd);

            y = 1;
            totalDebtText = new TextView(context);
            tab.addView(totalDebtText);
            relativeParams = (RelativeLayout.LayoutParams) totalDebtText.getLayoutParams();
            relativeParams.setMargins(w * 24 / 100, h * y / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            totalDebtText.setLayoutParams(relativeParams);
            totalDebtText.setX(customerNameSurname.getX() + customerNameSurname.getPaddingLeft());
            totalDebtText.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            totalDebtText.setTypeface(null, Typeface.BOLD);
            totalDebtText.setTextColor(Color.BLACK);
            resourceID = mainActivity.getResources().getIdentifier("totalDebt", "string", packageName);
            totalDebtText.setText(res.getString(resourceID) + " " + debtFormatted);
            y += 6;

            newDebt = new TextView(context);
            tab.addView(newDebt);
            relativeParams = (RelativeLayout.LayoutParams) newDebt.getLayoutParams();
            relativeParams.setMargins(w * 24 / 100, h * y / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            newDebt.setLayoutParams(relativeParams);
            newDebt.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            newDebt.setTypeface(null, Typeface.BOLD);
            newDebt.setTextColor(Color.BLACK);
            resourceID = mainActivity.getResources().getIdentifier("newDebt", "string", packageName);
            newDebt.setText(res.getString(resourceID) + " " + debtFormatted);

            y += 6;
        }
        for (int i = 0; i < customerTextArray.size(); i++) {
            textViewArray[i] = new TextView(context);
            if (itemSelectedId == R.id.nav_customer_debt || itemSelectedId == R.id.nav_customer_payment)
                tab.addView(textViewArray[i]);
            else
                relativeLayout.addView(textViewArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) textViewArray[i].getLayoutParams();
            textViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            textViewArray[i].setTypeface(null, Typeface.BOLD);
            if (itemSelectedId != R.id.nav_customer_edit) {
                relativeParams.width = w * 82 / 100;
                relativeParams.height = h * 10 / 100;
                relativeParams.setMargins(w * 5 / 100, h * y / 100, 0, h * 5 / 100);
                textViewArray[i].setBackgroundColor(Color.WHITE);
                textViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, h * 7 / 100);
                textViewArray[i].setGravity(Gravity.CENTER);
                textViewArray[i].setTypeface(null, Typeface.BOLD);
                textViewArray[i].addTextChangedListener(new TextWatcher() {
                    @Override
                    public void afterTextChanged(Editable arg0) {

                    }

                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count,
                                                  int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                        if (!ignoreChange) {
                            resourceID = mainActivity.getResources().getIdentifier("newDebt", "string", packageName);
                            ignoreChange = true;
                            char prevLast = '1', last;
                            int pos, currentLength;
                            currentLength = textViewArray[0].getText().length();
                            if (prevLength > currentLength && textViewArray[0].getText().length() > 0) {
                                if (textViewArray[0].getText().length() == 1 && textViewArray[0].getText().charAt(0) == '0')
                                    textViewArray[0].setText("");
                                else if (textViewArray[0].getText().charAt(textViewArray[0].getText().length() - 1) == ',')
                                    textViewArray[0].setText(textViewArray[0].getText().toString().replace(",", ""));
                            }
                            if (textViewArray[0].getText().length() > 0) {
                                if (textViewArray[0].getText().length() > 1) {
                                    prevLast = textViewArray[0].getText().charAt(textViewArray[0].getText().toString().length() - 2);
                                }
                                if ((textViewArray[0].getText().charAt(textViewArray[0].getText().length() - 1) == '.' || textViewArray[0].getText().charAt(textViewArray[0].getText().length() - 1) == ',') && prevText.contains(",")) {
                                    textViewArray[0].setText(textViewArray[0].getText().toString().substring(0, textViewArray[0].getText().toString().length() - 1) + "");
                                }
                                last = textViewArray[0].getText().charAt(textViewArray[0].getText().toString().length() - 1);
                                if (textViewArray[0].getText().length() > 4) {
                                    if (textViewArray[0].getText().charAt(textViewArray[0].getText().toString().length() - 4) == ',')
                                        textViewArray[0].setText(textViewArray[0].getText().toString().substring(0, textViewArray[0].getText().toString().length() - 1) + "");
                                }
                                if (textViewArray[0].getText().charAt(0) == '.' || textViewArray[0].getText().charAt(0) == ',')
                                    textViewArray[0].setText(textViewArray[0].getText().toString().replace(".", "").replace(",", ""));
                                else if (textViewArray[0].getText().charAt(textViewArray[0].getText().length() - 1) == '.' && prevText.contains(".")) {
                                    pos = textViewArray[0].getText().toString().lastIndexOf(".");
                                    textViewArray[0].setText(textViewArray[0].getText().toString().substring(0, pos) + "," + textViewArray[0].getText().toString().substring(pos + 1));
                                } else if (textViewArray[0].getText().charAt(textViewArray[0].getText().length() - 1) == '.') {
                                    textViewArray[0].setText(textViewArray[0].getText().toString().replace(".", ","));
                                } else if (last != ',') {
                                    if (!(prevLast == ',' && last == '0')) {
                                        double debt = Double.parseDouble(textViewArray[0].getText().toString().replaceAll("\\.", "").replace(",", "."));
                                        debtFormatted = decimalFormat.format(debt);
                                        if (debt == 0.0)
                                            textViewArray[0].setText("0,0");
                                        else
                                            textViewArray[0].setText(debtFormatted);
                                        if (itemSelectedId == R.id.nav_customer_debt) {
                                            newCustomerDebt = totalDebt + debt;
                                            debtFormatted = decimalFormat.format(newCustomerDebt);
                                            newDebt.setText(res.getString(resourceID) + " " + debtFormatted);
                                        } else {
                                            newCustomerDebt = totalDebt - debt;
                                            if (debt > totalDebt) {
                                                newCustomerDebt = 0.00;
                                                debtFormatted = decimalFormat.format(totalDebt);
                                                newDebt.setText(res.getString(resourceID) + " 0");
                                                textViewArray[0].setText(debtFormatted);
                                            } else {
                                                debtFormatted = decimalFormat.format(newCustomerDebt);
                                                newDebt.setText(res.getString(resourceID) + " " + debtFormatted);
                                            }
                                        }
                                    }
                                }
                            } else {
                                newCustomerDebt = totalDebt;
                                debtFormatted = decimalFormat.format(debtDoubleParse);
                                newDebt.setText(res.getString(resourceID) + " " + debtFormatted);
                            }
                        }
                        ignoreChange = false;
                        prevLength = textViewArray[0].getText().length();
                        prevText = textViewArray[0].getText().toString();
                    }
                });
            } else {
                relativeParams.width = w;
                relativeParams.setMargins(w * 29 / 100, h * y / 100, 0, h * 5 / 100);
                resourceID = mainActivity.getResources().getIdentifier(header[i], "string", packageName);
                if (i == 0 || i == 1)
                    textViewArray[i].setText("* " + res.getString(resourceID) + " :");
                else
                    textViewArray[i].setText(res.getString(resourceID) + " :");
            }
            textViewArray[i].setLayoutParams(relativeParams);
            textViewArray[i].setTextColor(Color.BLACK);

            if (itemSelectedId == R.id.nav_customer_edit) {
                editTextArray[i] = new EditText(context);
                relativeLayout.addView(editTextArray[i]);
                relativeParams = (RelativeLayout.LayoutParams) editTextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                if (i == 2 || i == 3)
                    editTextArray[i].setInputType(InputType.TYPE_CLASS_NUMBER);
                relativeParams.setMargins(w * 29 / 100, h * (y + 3) / 100, 0, h * 5 / 100);
                editTextArray[i].setLayoutParams(relativeParams);
                editTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);
                editTextArray[i].setSingleLine(true);
                editTextArray[i].setText(customerTextArray.get(i));
                editTextArray[i].setTextColor(Color.BLACK);
                editTextArray[i].getBackground().mutate().setColorFilter(mainActivity.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                try {
                    Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(editTextArray[i], R.drawable.cursor);
                } catch (Exception ignored) {
                }
            }
            y += 18;
        }

        if (itemSelectedId == R.id.nav_customer_debt || itemSelectedId == R.id.nav_customer_payment) {
            btns = new Button[12];
            int count = 1;
            y -= 5;
            for (int i = 0; i < btns.length; i++) {
                btns[i] = new Button(context);
                tab.addView(btns[i]);
                relativeParams = (RelativeLayout.LayoutParams) btns[i].getLayoutParams();
                relativeParams.width = w * 18 / 100;
                relativeParams.height = h * 10 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, 0);
                btns[i].setLayoutParams(relativeParams);
                btns[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                btns[i].setTextColor(Color.BLACK);
                btns[i].setTypeface(null, Typeface.BOLD);
                btns[i].setBackgroundColor(Color.parseColor("#D6D7D7"));
                btns[i].setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        // TODO Auto-generated method stub
                        Button b = (Button) v;
                        if (textViewArray[0].getText().length() == 0 && b.getText().toString() == "0")
                            textViewArray[0].setText("0,");
                        else
                            textViewArray[0].setText(textViewArray[0].getText() + b.getText().toString());
                    }
                });
                if (i == 9)
                    btns[i].setText(",");
                else if (i == 10)
                    btns[i].setText("0");
                else if (i == 11)
                    btns[i].setText(".");
                else
                    btns[i].setText("" + count);
                if ((i != 0 && i != 11) && (count % 3) == 0) {
                    x = 5;
                    y += 11;
                } else
                    x += 22;
                count++;
            }
        }

        if (itemSelectedId == R.id.nav_customer_debt || itemSelectedId == R.id.nav_customer_payment) {
            ImageButton imgButton = new ImageButton(context);
            tab.addView(imgButton);
            relativeParams = (RelativeLayout.LayoutParams) imgButton.getLayoutParams();
            relativeParams.width = w * 18 / 100;
            relativeParams.height = h * 10 / 100;
            relativeParams.setMargins(w * x / 100, h * y / 100, 0, 0);
            imgButton.setLayoutParams(relativeParams);
            imgButton.setBackgroundColor(Color.parseColor("#D6D7D7"));
            imgButton.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (textViewArray[0].length() > 0) {
                        if (textViewArray[0].getText().toString().substring(0, textViewArray[0].length() - 1).equals("0,0"))
                            textViewArray[0].setText("");
                        else
                            textViewArray[0].setText(textViewArray[0].getText().toString().substring(0, textViewArray[0].length() - 1));
                    }
                }
            });
            imgButton.setImageResource(R.drawable.ic_backspace);
        }
        btn = new Button(context);
        relativeLayout.addView(btn);
        relativeParams = (RelativeLayout.LayoutParams) btn.getLayoutParams();
        relativeParams.width = w * 33 / 100;
        relativeParams.height = h * 10 / 100;
        if (itemSelectedId == R.id.nav_customer_edit) {
            relativeParams.setMargins(w * 33 / 100, h * (y + 1) / 100, 0, h * 10 / 100);
            resourceID = mainActivity.getResources().getIdentifier("change", "string", packageName);
        } else if (itemSelectedId == R.id.nav_customer_debt) {
            relativeParams.setMargins(w * 28 / 100, h, 0, h * 10 / 100);
            resourceID = mainActivity.getResources().getIdentifier("insert", "string", packageName);
            btn.setText(res.getString(resourceID));
        } else {
            relativeParams.setMargins(w * 28 / 100, h, 0, h * 10 / 100);
            resourceID = mainActivity.getResources().getIdentifier("credit", "string", packageName);
            btn.setText(res.getString(resourceID).toUpperCase());
        }
        btn.setText(res.getString(resourceID));
        btn.setLayoutParams(relativeParams);
        btn.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
        btn.setTextColor(Color.BLACK);
        btn.setTypeface(null, Typeface.BOLD);
        btn.setBackgroundColor(Color.parseColor("#D6D7D7"));
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (checkRequiredFields()) {
                    resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle(res.getString(resourceID));
                    builder.setCancelable(false);
                    resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                    builder.setMessage(res.getString(resourceID));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (itemSelectedId == R.id.nav_customer_edit) {
                                CustomersModel temp = new CustomersModel();
                                temp.id = customerId;
                                temp.name = editTextArray[0].getText().toString();
                                temp.surname = editTextArray[1].getText().toString();
                                temp.phone1 = editTextArray[2].getText().toString();
                                temp.phone2 = editTextArray[3].getText().toString();
                                temp.city = editTextArray[4].getText().toString();
                                temp.address = editTextArray[5].getText().toString();
                                temp.email = editTextArray[6].getText().toString();
                                temp.guaranteeCount = customerGuaranteeCount;
                                temp.debt = customerDebt;
                                temp.taskCount = customerTaskCount;
                                customersModel.set(customerIndex, temp);
                                mydb.updateCustomers(temp);
                            } else {
                                customersModel.get(customerIndex).debt = newCustomerDebt;
                                mydb.updateCustomers(customersModel.get(customerIndex));
                                if (itemSelectedId == R.id.nav_customer_payment) {
                                    CustomersCreditDebtModel temp = new CustomersCreditDebtModel();
                                    int creditDebtId = mydb.getMaxCustomerCreditDebtId();
                                    creditDebtId++;
                                    temp.id = creditDebtId;
                                    int creditDebtRow = mydb.getMaxCustomerCreditDebtRow(customersModel.get(customerIndex).id);
                                    creditDebtRow++;
                                    temp.creditDebtRow = creditDebtRow;
                                    temp.name = customersModel.get(customerIndex).name;
                                    temp.surname = customersModel.get(customerIndex).surname;
                                    temp.date = dateFormat.format(new Date());
                                    temp.oldDebt = Double.valueOf(customerTextArray.get(0));
                                    temp.creditDebt = Double.valueOf(textViewArray[0].getText().toString().replace(".", "").replace(",", "."));
                                    temp.currentDebt = newCustomerDebt;
                                    temp.customerId = customersModel.get(customerIndex).id;
                                    mydb.insertCustomerCreditDebt(temp);
                                    customersCreditDebtModel.add(temp);
                                }
                            }
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            resourceID = mainActivity.getResources().getIdentifier("changeSuccess", "string", packageName);
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                            navigateMe();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    //guarantee and task
    public static void updateGuaranteeAndTask() {
        functionNow = "updateGuaranteeAndTask";
        removeSomeViews();
        ImageView imageViewBack = new ImageView(context);
        relativeLayout.addView(imageViewBack);
        relativeParams = (RelativeLayout.LayoutParams) imageViewBack.getLayoutParams();
        relativeParams.width = w * 12 / 100;
        relativeParams.height = h * 7 / 100;
        relativeParams.setMargins(w * 2 / 100, h * 8 / 100, 0, 0);
        imageViewBack.setLayoutParams(relativeParams);
        imageViewBack.setBackgroundResource(R.drawable.ic_arrow_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateMe();
            }
        });
        TextView customerNameSurname = new TextView(context);
        relativeLayout.addView(customerNameSurname);
        relativeParams = (RelativeLayout.LayoutParams) customerNameSurname.getLayoutParams();
        relativeParams.setMargins(0, h * 15 / 100, 0, 0);
        relativeParams.width = w;
        customerNameSurname.setLayoutParams(relativeParams);
        customerNameSurname.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
        customerNameSurname.setTypeface(null, Typeface.BOLD);
        customerNameSurname.setTextColor(Color.BLACK);
        customerNameSurname.setGravity(Gravity.CENTER);
        customerNameSurname.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        customerNameSurname.setText(customerNameTextArray[customerIndex].getText() + " " + customerSurnameTextArray[customerIndex].getText());
        relativeLayout.addView(horizontalScrollView);
        horizontalScrollView.setY(h * 24 / 100);
        horizontalScrollView.setX(0);
        horizontalScrollView.getLayoutParams().height = h * 97 / 100;
        horizontalScrollView.getLayoutParams().width = w * 96 / 100;
        scrollViewTable.setY(0);
        scrollViewTable.setX(0);
        scrollViewTable.getLayoutParams().height = h * 76 / 100;
        scrollViewTable.fullScroll(ScrollView.FOCUS_UP);
        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        horizontalRelativeLayout.addView(scrollViewTable);
        if (itemSelectedId == R.id.nav_customer_guarantee) {
            customersGuaranteeModel = new ArrayList<CustomersGuaranteeModel>();
            customersGuaranteeModel = mydb.getCustomersGuaranteeById(customerId);
            dataLength = customersGuaranteeModel.size();

        } else {
            prevDate = new ArrayList<String>();
            customersTasksModel = new ArrayList<CustomersTasksModel>();
            customersTasksModel = mydb.getCustomersTaskById(customerId);
            dataLength = customersTasksModel.size();
            customerTimeEdit = new EditText[dataLength];
        }
        imageViewDelete = new ImageView[dataLength];
        btns = new Button[dataLength];
        customerTimeText = new TextView[dataLength];
        customerDateText = new TextView[dataLength];
        customerDetailsText = new TextView[dataLength];
        customerDateEdit = new EditText[dataLength];
        customerDetailsEdit = new EditText[dataLength];
        RelativeLayout[] tabs = new RelativeLayout[dataLength];
        int y = 0, textX = 28, textY, tabHeight, tabYPos;
        if (itemSelectedId == R.id.nav_customer_guarantee) {
            tabHeight = 98;
            tabYPos = 108;
        } else {
            tabHeight = 118;
            tabYPos = 128;
        }
        for (int i = 0; i < dataLength; i++) {
            tabs[i] = new RelativeLayout(context);
            verticalRelativeLayout.addView(tabs[i]);
            relativeParams = (RelativeLayout.LayoutParams) tabs[i].getLayoutParams();
            relativeParams.width = w * 89 / 100;
            relativeParams.height = h * tabHeight / 100;
            relativeParams.setMargins(0, h * y / 100, 0, h * 9 / 100);
            tabs[i].setLayoutParams(relativeParams);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                tabs[i].setBackgroundDrawable(gd);
            else
                tabs[i].setBackground(gd);

            imageViewDelete[i] = new ImageView(context);
            tabs[i].addView(imageViewDelete[i]);
            relativeParams = (RelativeLayout.LayoutParams) imageViewDelete[i].getLayoutParams();
            relativeParams.width = w * 12 / 100;
            relativeParams.height = h * 7 / 100;
            relativeParams.setMargins(w * 75 / 100, h * 2 / 100, 0, 0);
            imageViewDelete[i].setLayoutParams(relativeParams);
            imageViewDelete[i].setBackgroundResource(R.drawable.ic_customer_delete);
            imageViewDelete[i].setTag(i);
            imageViewDelete[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemIndex = (int) v.getTag();
                    resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle(res.getString(resourceID));
                    builder.setCancelable(false);
                    resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                    builder.setMessage(res.getString(resourceID));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int row = itemIndex;
                            row++;
                            if (itemSelectedId == R.id.nav_customer_guarantee) {
                                mydb.deleteCustomerGuaranteeRow(customerId, row);
                                mydb.updateCustomerGuaranteeRows(customerId);
                                customersModel.get(customerIndex).guaranteeCount -= 1;
                                mydb.updateCustomers(customersModel.get(customerIndex));
                                if (customersModel.get(customerIndex).guaranteeCount == 0)
                                    navigateMe();
                                else
                                    updateGuaranteeAndTask();
                            } else {
                                int i;
                                mydb.deleteCustomerTaskRow(customerId, row);
                                mydb.updateCustomerTaskRows(customerId);
                                customersModel.get(customerIndex).taskCount -= 1;
                                mydb.updateCustomers(customersModel.get(customerIndex));
                                for (i = 0; i < customersTasksTomorrowModel.size(); i++) {
                                    if (customersTasksTomorrowModel.get(i).id == customerId && customersTasksTomorrowModel.get(i).taskRow == row) {
                                        customersTasksTomorrowModel.remove(i);
                                        tomorrowTasks--;
                                        MenuItem taskTomorrow = menu.findItem(R.id.nav_tasks_tomorrow);
                                        taskTomorrow.setTitle(notificationsSubmenuXML[1] + " (" + tomorrowTasks + ")");
                                        break;
                                    }
                                }
                                for (i = 0; i < customersTasksTodayModel.size(); i++) {
                                    if (customersTasksTodayModel.get(i).id == customerId && customersTasksTodayModel.get(i).taskRow == row) {
                                        customersTasksTodayModel.remove(i);
                                        todayTasks--;
                                        MenuItem taskToday = menu.findItem(R.id.nav_tasks_today);
                                        taskToday.setTitle(notificationsSubmenuXML[0] + " (" + todayTasks + ")");
                                        break;
                                    }
                                }
                                if (mydb.deleteCustomerPendingTaskRow(customerId, row) == 1) {
                                    pendingTasks--;
                                    MenuItem customerPendingTask = menu.findItem(R.id.nav_pending_tasks);
                                    customerPendingTask.setTitle(notificationsSubmenuXML[2] + " (" + pendingTasks + ")");
                                    mydb.updateCustomerPendingTaskRows(customerId);
                                }
                                if (customersModel.get(customerIndex).taskCount == 0)
                                    navigateMe();
                                else {
                                    updateGuaranteeAndTask();
                                }
                            }
                            resourceID = mainActivity.getResources().getIdentifier("deleteSuccess", "string", packageName);
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });


            textY = 9;
            customerDateText[i] = new TextView(context);
            tabs[i].addView(customerDateText[i]);
            relativeParams = (RelativeLayout.LayoutParams) customerDateText[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            customerDateText[i].setLayoutParams(relativeParams);
            customerDateText[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            customerDateText[i].setTypeface(null, Typeface.BOLD);
            customerDateText[i].setTextColor(Color.BLACK);
            if (itemSelectedId == R.id.nav_customer_guarantee) {
                resourceID = mainActivity.getResources().getIdentifier("dateExpiration", "string", packageName);
                customerDateText[i].setText(res.getString(resourceID));
            } else {
                resourceID = mainActivity.getResources().getIdentifier("date", "string", packageName);
                customerDateText[i].setText(res.getString(resourceID));
            }
            customerDateEdit[i] = new EditText(context);
            tabs[i].addView(customerDateEdit[i]);
            relativeParams = (RelativeLayout.LayoutParams) customerDateEdit[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * (textY + 3) / 100, 0, 0);
            relativeParams.width = w * 40 / 100;
            customerDateEdit[i].setLayoutParams(relativeParams);
            customerDateEdit[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);
            customerDateEdit[i].setFocusable(false);
            customerDateEdit[i].setTag(i);
            customerDateEdit[i].setTextColor(Color.BLACK);
            customerDateEdit[i].getBackground().mutate().setColorFilter(mainActivity.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            try {
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(customerDateEdit[i], R.drawable.cursor);
            } catch (Exception ignored) {
            }
            customerDateEdit[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerIndex = (int) v.getTag();
                    dateTimePicker = "updateGuaranteeAndTask";
                    dateDialog();
                    datePickerDialog.show();
                }
            });
            if (itemSelectedId == R.id.nav_customer_guarantee)
                customerDateEdit[i].setText("" + customersGuaranteeModel.get(i).date);
            else
                customerDateEdit[i].setText("" + customersTasksModel.get(i).date);
            prevDate.add(customerDateEdit[i].getText().toString());
            textY += 20;


            if (itemSelectedId == R.id.nav_customer_task) {
                customerTimeText[i] = new TextView(context);
                tabs[i].addView(customerTimeText[i]);
                relativeParams = (RelativeLayout.LayoutParams) customerTimeText[i].getLayoutParams();
                relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
                relativeParams.width = w;
                customerTimeText[i].setLayoutParams(relativeParams);
                customerTimeText[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerTimeText[i].setTypeface(null, Typeface.BOLD);
                customerTimeText[i].setTextColor(Color.BLACK);
                resourceID = mainActivity.getResources().getIdentifier("time", "string", packageName);
                customerTimeText[i].setText(res.getString(resourceID));


                customerTimeEdit[i] = new EditText(context);
                tabs[i].addView(customerTimeEdit[i]);
                relativeParams = (RelativeLayout.LayoutParams) customerTimeEdit[i].getLayoutParams();
                relativeParams.setMargins(w * textX / 100, h * (textY + 3) / 100, 0, 0);
                relativeParams.width = w * 40 / 100;
                customerTimeEdit[i].setLayoutParams(relativeParams);
                customerTimeEdit[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);
                customerTimeEdit[i].setFocusable(false);
                customerTimeEdit[i].setTag(i);
                customerTimeEdit[i].setTextColor(Color.BLACK);
                customerTimeEdit[i].getBackground().mutate().setColorFilter(mainActivity.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
                try {
                    Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                    f.setAccessible(true);
                    f.set(customerTimeEdit[i], R.drawable.cursor);
                } catch (Exception ignored) {
                }
                customerTimeEdit[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customerIndex = (int) v.getTag();
                        dateTimePicker = "updateGuaranteeAndTask";
                        timeDialog();
                        timePickerDialog.show();
                    }
                });
                customerTimeEdit[i].setText("" + customersTasksModel.get(i).time);
                textY += 20;
            }


            customerDetailsText[i] = new TextView(context);
            tabs[i].addView(customerDetailsText[i]);
            relativeParams = (RelativeLayout.LayoutParams) customerDetailsText[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            customerDetailsText[i].setLayoutParams(relativeParams);
            customerDetailsText[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            customerDetailsText[i].setTypeface(null, Typeface.BOLD);
            customerDetailsText[i].setTextColor(Color.BLACK);
            resourceID = mainActivity.getResources().getIdentifier("details", "string", packageName);
            customerDetailsText[i].setText(res.getString(resourceID));


            customerDetailsEdit[i] = new EditText(context);
            tabs[i].addView(customerDetailsEdit[i]);
            relativeParams = (RelativeLayout.LayoutParams) customerDetailsEdit[i].getLayoutParams();
            relativeParams.setMargins(w * 2 / 100, h * (textY + 5) / 100, 0, 0);
            relativeParams.width = w * 86 / 100;
            relativeParams.height = h * 30 / 100;
            customerDetailsEdit[i].setLayoutParams(relativeParams);
            customerDetailsEdit[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);
            customerDetailsEdit[i].setSingleLine(false);
            customerDetailsEdit[i].setGravity(Gravity.TOP);
            customerDetailsEdit[i].setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            customerDetailsEdit[i].setMaxLines(h * 30 / 100);
            customerDetailsEdit[i].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            customerDetailsEdit[i].setVerticalScrollBarEnabled(true);
            customerDetailsEdit[i].setHorizontalScrollBarEnabled(true);
            customerDetailsEdit[i].setTextColor(Color.BLACK);
            customerDetailsEdit[i].getBackground().mutate().setColorFilter(mainActivity.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            try {
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(customerDetailsEdit[i], R.drawable.cursor);
            } catch (Exception ignored) {
            }
            customerDetailsEdit[i].setMovementMethod(new ScrollingMovementMethod());
            customerDetailsEdit[i].setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
            customerDetailsEdit[i].setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(final View v, final MotionEvent motionEvent) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                customerDetailsEdit[i].setBackgroundDrawable(detailsTextViewBorderGd);
            else
                customerDetailsEdit[i].setBackground(detailsTextViewBorderGd);
            if (itemSelectedId == R.id.nav_customer_guarantee)
                customerDetailsEdit[i].setText("" + customersGuaranteeModel.get(i).details);
            else
                customerDetailsEdit[i].setText("" + customersTasksModel.get(i).details);
            textY += 48;


            btns[i] = new Button(context);
            tabs[i].addView(btns[i]);
            relativeParams = (RelativeLayout.LayoutParams) btns[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 2 / 100);
            relativeParams.width = w * 33 / 100;
            relativeParams.height = h * 10 / 100;
            btns[i].setLayoutParams(relativeParams);
            resourceID = mainActivity.getResources().getIdentifier("change", "string", packageName);
            btns[i].setText(res.getString(resourceID));
            btns[i].setTag(i);
            btns[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            btns[i].setTextColor(Color.BLACK);
            btns[i].setTypeface(null, Typeface.BOLD);
            btns[i].setBackgroundColor(Color.parseColor("#D6D7D7"));
            btns[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    itemIndex = (int) v.getTag();
                    resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle(res.getString(resourceID));
                    builder.setCancelable(false);
                    resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                    builder.setMessage(res.getString(resourceID));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int row = itemIndex, errorFound = 1;
                            row++;
                            String dateText;
                            if (customerDateEdit[itemIndex].getText().toString().length() == 0) {
                                strDate = new Date();
                                dateText = dateFormat.format(new Date());
                            } else {
                                try {
                                    strDate = dateFormat.parse(customerDateEdit[itemIndex].getText().toString());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                dateText = customerDateEdit[itemIndex].getText().toString();
                            }
                            if (itemSelectedId == R.id.nav_customer_guarantee && (strDate.after(new Date()) || dateText.equals(dateFormat.format(new Date())))) {
                                errorFound = 0;
                                CustomersGuaranteeModel model = new CustomersGuaranteeModel();
                                model.date = customerDateEdit[itemIndex].getText().toString();
                                model.details = customerDetailsEdit[itemIndex].getText().toString();
                                mydb.updateCustomersGuarantee(customerId, row, model);
                            } else if (itemSelectedId == R.id.nav_customer_task) {
                                Calendar calTomorrow = Calendar.getInstance();
                                calTomorrow.add(Calendar.DATE, +1);
                                String tomorrow = dateFormat.format(calTomorrow.getTime());
                                int textTimeFormatted;
                                if (customerTimeEdit[itemIndex].getText().toString().length() == 0) {
                                    textTimeFormatted = 99999;
                                } else {
                                    textTimeFormatted = Integer.parseInt(customerTimeEdit[itemIndex].getText().toString().replace(":", ""));
                                }
                                String time;
                                Calendar mcurrentTime = Calendar.getInstance();
                                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                                final int minute = mcurrentTime.get(Calendar.MINUTE);
                                if (minute < 10)
                                    time = hour + ":0" + minute;
                                else
                                    time = hour + ":" + minute;
                                currentTimeFormatted = Integer.parseInt(time.replace(":", ""));
                                if (strDate.after(new Date()) || (dateText.equals(dateFormat.format(new Date())) && currentTimeFormatted < textTimeFormatted)) {
                                    errorFound = 0;
                                    int i;
                                    CustomersTasksModel temp = new CustomersTasksModel();
                                    temp.date = customerDateEdit[itemIndex].getText().toString();
                                    temp.time = customerTimeEdit[itemIndex].getText().toString();
                                    temp.details = customerDetailsEdit[itemIndex].getText().toString();
                                    mydb.updateCustomersTask(customerId, row, temp);
                                    if (prevDate.get(itemIndex).length() != 0) {
                                        if (prevDate.get(itemIndex).equals(dateFormat.format(new Date()))) {
                                            for (i = 0; i < customersTasksTodayModel.size(); i++) {
                                                if (customersTasksTodayModel.get(i).date.equals(dateFormat.format(new Date())) && customersTasksTodayModel.get(i).id == customerId) {
                                                    customersTasksTodayModel.remove(i);
                                                    break;
                                                }
                                            }
                                            todayTasks--;
                                            MenuItem taskToday = menu.findItem(R.id.nav_tasks_today);
                                            taskToday.setTitle(notificationsSubmenuXML[0] + " (" + todayTasks + ")");
                                        } else if (prevDate.get(itemIndex).equals(tomorrow)) {
                                            for (i = 0; i < customersTasksTomorrowModel.size(); i++) {
                                                if (customersTasksTomorrowModel.get(i).date.equals(tomorrow) && customersTasksTomorrowModel.get(i).id == customerId) {
                                                    customersTasksTomorrowModel.remove(i);
                                                    break;
                                                }
                                            }
                                            tomorrowTasks--;
                                            MenuItem taskTomorrow = menu.findItem(R.id.nav_tasks_tomorrow);
                                            taskTomorrow.setTitle(notificationsSubmenuXML[1] + " (" + tomorrowTasks + ")");
                                        }
                                    }
                                    if (customerDateEdit[itemIndex].getText().toString().equals(dateFormat.format(new Date()))) {
                                        todayTasks++;
                                        CustomersModel model = mydb.getCustomerById(customerId);
                                        CustomersTasksExtendedModel _temp = new CustomersTasksExtendedModel();
                                        _temp.id = customerId;
                                        _temp.name = model.name + " " + model.surname;
                                        _temp.phone1 = model.phone1;
                                        _temp.phone2 = model.phone2;
                                        _temp.city = model.city;
                                        _temp.address = model.address;
                                        _temp.taskRow = row;
                                        _temp.time = temp.time;
                                        _temp.date = temp.date;
                                        _temp.details = temp.details;
                                        customersTasksTodayModel.add(_temp);
                                        MenuItem taskToday = menu.findItem(R.id.nav_tasks_today);
                                        taskToday.setTitle(notificationsSubmenuXML[0] + " (" + todayTasks + ")");
                                    } else if (customerDateEdit[customerIndex].getText().toString().equals(tomorrow)) {
                                        tomorrowTasks++;
                                        CustomersModel model = mydb.getCustomerById(customerId);
                                        CustomersTasksExtendedModel _temp = new CustomersTasksExtendedModel();
                                        _temp.id = customerId;
                                        _temp.name = model.name + " " + model.surname;
                                        _temp.phone1 = model.phone1;
                                        _temp.phone2 = model.phone2;
                                        _temp.city = model.city;
                                        _temp.address = model.address;
                                        _temp.taskRow = temp.taskRow;
                                        _temp.time = temp.time;
                                        _temp.date = temp.date;
                                        _temp.details = temp.details;
                                        customersTasksTomorrowModel.add(_temp);
                                        MenuItem taskTomorrow = menu.findItem(R.id.nav_tasks_tomorrow);
                                        taskTomorrow.setTitle(notificationsSubmenuXML[1] + " (" + tomorrowTasks + ")");
                                    }
                                }
                            }
                            if (errorFound == 1) {
                                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                                if (itemSelectedId == R.id.nav_customer_guarantee)
                                    resourceID = mainActivity.getResources().getIdentifier("dateError", "string", packageName);
                                else
                                    resourceID = mainActivity.getResources().getIdentifier("dateTimeError", "string", packageName);
                                toastMessage.setText(res.getString(resourceID));
                                toast.setView(toastMessage);
                                toast.show();
                            } else {
                                toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                                resourceID = mainActivity.getResources().getIdentifier("changeSuccess", "string", packageName);
                                toastMessage.setText(res.getString(resourceID));
                                toast.setView(toastMessage);
                                toast.show();
                                prevDate = new ArrayList<String>();
                                for (int i = 0; i < customerDateEdit.length; i++) {
                                    prevDate.add(customerDateEdit[i].getText().toString());
                                }
                            }

                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            y += tabYPos;
        }
    }

    //tasks tomorrow, tasks today, pending tasks and notification tasks
    public static void getCustomersTasks() {
        if (!horizontalRelativeLayout.isShown()) {
            horizontalScrollView.setY(h * 10 / 100);
            horizontalScrollView.setX(0);
            horizontalScrollView.getLayoutParams().height = h * 97 / 100;
            horizontalScrollView.getLayoutParams().width = w * 96 / 100;
        }
        scrollViewTable.setY(0);
        scrollViewTable.setX(0);
        scrollViewTable.getLayoutParams().height = h * 90 / 100;
        scrollViewTable.fullScroll(ScrollView.FOCUS_UP);
        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        if (itemSelectedId == 0)
            dataLength = customersNotificationTasksModel.size();
        else {
            if (itemSelectedId == R.id.nav_tasks_tomorrow)
                dataLength = customersTasksTomorrowModel.size();
            else if (itemSelectedId == R.id.nav_tasks_today)
                dataLength = customersTasksTodayModel.size();
            else {
                customersPendingTasksModel = new ArrayList<CustomersTasksExtendedModel>();
                customersPendingTasksModel = mydb.getCustomerPendingTasks();
                dataLength = customersPendingTasksModel.size();
                textViewArray = new TextView[dataLength];
            }
        }
        imageViewDelete = new ImageView[dataLength];
        customerNameTextArray = new TextView[dataLength];
        customerPhone1TextArray = new TextView[dataLength];
        customerCityTextArray = new TextView[dataLength];
        customerAddressTextArray = new TextView[dataLength];
        TextView[] customersTimeText = new TextView[dataLength];
        TextView[] customersDetailsText = new TextView[dataLength];
        RelativeLayout[] tabs = new RelativeLayout[dataLength];
        int y = 0, textX = 0, textY;
        for (int i = 0; i < dataLength; i++) {
            tabs[i] = new RelativeLayout(context);
            verticalRelativeLayout.addView(tabs[i]);
            relativeParams = (RelativeLayout.LayoutParams) tabs[i].getLayoutParams();
            relativeParams.width = w * 89 / 100;
            if (itemSelectedId == R.id.nav_pending_tasks)
                relativeParams.height = h * 100 / 100;
            else
                relativeParams.height = h * 92 / 100;
            relativeParams.setMargins(0, h * y / 100, 0, h * 9 / 100);
            tabs[i].setLayoutParams(relativeParams);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                tabs[i].setBackgroundDrawable(gd);
            else
                tabs[i].setBackground(gd);
            textY = 9;

            imageViewDelete[i] = new ImageView(context);
            tabs[i].addView(imageViewDelete[i]);
            relativeParams = (RelativeLayout.LayoutParams) imageViewDelete[i].getLayoutParams();
            relativeParams.width = w * 12 / 100;
            relativeParams.height = h * 7 / 100;
            relativeParams.setMargins(w * 75 / 100, h * 2 / 100, 0, 0);
            imageViewDelete[i].setLayoutParams(relativeParams);
            imageViewDelete[i].setBackgroundResource(R.drawable.ic_customer_delete);
            imageViewDelete[i].setTag(i);
            imageViewDelete[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemIndex = (int) v.getTag();
                    resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle(res.getString(resourceID));
                    builder.setCancelable(false);
                    resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                    builder.setMessage(res.getString(resourceID));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int row, i;
                            if (itemSelectedId == 0) {
                                row = customersNotificationTasksModel.get(itemIndex).taskRow;
                                customerId = customersNotificationTasksModel.get(itemIndex).id;
                                for (i = 0; i < customersTasksTodayModel.size(); i++) {
                                    if (customersTasksTodayModel.get(i).id == customerId && customersTasksTodayModel.get(i).taskRow == row) {
                                        customersTasksTodayModel.remove(i);
                                        customersNotificationTasksModel.remove(itemIndex);
                                        todayTasks--;
                                        MenuItem taskToday = menu.findItem(R.id.nav_tasks_today);
                                        taskToday.setTitle(notificationsSubmenuXML[0] + " (" + todayTasks + ")");
                                        notificationTaskCounter--;
                                        mainActivity.setTitle(menuXML[2] + " (" + notificationTaskCounter + ")");
                                        break;
                                    }
                                }
                            } else {
                                if (itemSelectedId == R.id.nav_tasks_tomorrow) {
                                    row = customersTasksTomorrowModel.get(itemIndex).taskRow;
                                    customerId = customersTasksTomorrowModel.get(itemIndex).id;
                                    customersTasksTomorrowModel.remove(itemIndex);
                                    tomorrowTasks--;
                                    MenuItem taskTomorrow = menu.findItem(R.id.nav_tasks_tomorrow);
                                    taskTomorrow.setTitle(notificationsSubmenuXML[1] + " (" + tomorrowTasks + ")");
                                    mainActivity.setTitle(taskTomorrow.getTitle().toString());
                                } else if (itemSelectedId == R.id.nav_tasks_today) {
                                    row = customersTasksTodayModel.get(itemIndex).taskRow;
                                    customerId = customersTasksTodayModel.get(itemIndex).id;
                                    customersTasksTodayModel.remove(itemIndex);
                                    todayTasks--;
                                    MenuItem taskToday = menu.findItem(R.id.nav_tasks_today);
                                    taskToday.setTitle(notificationsSubmenuXML[0] + " (" + todayTasks + ")");
                                    mainActivity.setTitle(taskToday.getTitle().toString());
                                } else {
                                    row = customersPendingTasksModel.get(itemIndex).taskRow;
                                    customerId = customersPendingTasksModel.get(itemIndex).id;
                                    customersPendingTasksModel.remove(itemIndex);
                                    pendingTasks--;
                                    MenuItem customerPendingTask = menu.findItem(R.id.nav_pending_tasks);
                                    customerPendingTask.setTitle(notificationsSubmenuXML[2] + " (" + pendingTasks + ")");
                                    mainActivity.setTitle(customerPendingTask.getTitle().toString());
                                    mydb.deleteCustomerPendingTaskRow(customerId, row);
                                    mydb.updateCustomerPendingTaskRows(customerId);
                                }
                            }
                            mydb.deleteCustomerTaskRow(customerId, row);
                            mydb.updateCustomerTaskRows(customerId);
                            for (i = 0; i < customersModel.size(); i++) {
                                if (customersModel.get(i).id == customerId) {
                                    customerIndex = i;
                                    break;
                                }
                            }
                            customersModel.get(customerIndex).taskCount -= 1;
                            mydb.updateCustomers(customersModel.get(customerIndex));
                            resourceID = mainActivity.getResources().getIdentifier("deleteSuccess", "string", packageName);
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                            navigateMe();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });

            if (itemSelectedId == R.id.nav_pending_tasks) {
                textViewArray[i] = new TextView(context);
                tabs[i].addView(textViewArray[i]);
                relativeParams = (RelativeLayout.LayoutParams) textViewArray[i].getLayoutParams();
                relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
                relativeParams.width = w;
                textViewArray[i].setLayoutParams(relativeParams);
                textViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
                textViewArray[i].setTypeface(null, Typeface.BOLD);
                textViewArray[i].setGravity(Gravity.CENTER);
                textViewArray[i].setTextColor(Color.BLACK);
                textViewArray[i].setText(customersPendingTasksModel.get(i).date);
                textY += 10;
            }

            customersTimeText[i] = new TextView(context);
            tabs[i].addView(customersTimeText[i]);
            relativeParams = (RelativeLayout.LayoutParams) customersTimeText[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            customersTimeText[i].setLayoutParams(relativeParams);
            customersTimeText[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            customersTimeText[i].setTypeface(null, Typeface.BOLD);
            customersTimeText[i].setTextColor(Color.BLACK);
            customersTimeText[i].setGravity(Gravity.CENTER);
            if (itemSelectedId == 0)
                customersTimeText[i].setText(customersNotificationTasksModel.get(i).time);
            else {
                if (itemSelectedId == R.id.nav_tasks_tomorrow)
                    customersTimeText[i].setText(customersTasksTomorrowModel.get(i).time);
                else if (itemSelectedId == R.id.nav_tasks_today)
                    customersTimeText[i].setText(customersTasksTodayModel.get(i).time);
                else
                    customersTimeText[i].setText(customersPendingTasksModel.get(i).time);
            }
            textY += 10;


            customerNameTextArray[i] = new TextView(context);
            tabs[i].addView(customerNameTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) customerNameTextArray[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            customerNameTextArray[i].setLayoutParams(relativeParams);
            customerNameTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            customerNameTextArray[i].setTypeface(null, Typeface.BOLD);
            customerNameTextArray[i].setTextColor(Color.BLACK);
            customerNameTextArray[i].setGravity(Gravity.CENTER);
            if (itemSelectedId == 0)
                customerNameTextArray[i].setText(customersNotificationTasksModel.get(i).name);
            else {
                if (itemSelectedId == R.id.nav_tasks_tomorrow)
                    customerNameTextArray[i].setText(customersTasksTomorrowModel.get(i).name);
                else if (itemSelectedId == R.id.nav_tasks_today)
                    customerNameTextArray[i].setText(customersTasksTodayModel.get(i).name);
                else
                    customerNameTextArray[i].setText(customersPendingTasksModel.get(i).name);
            }
            textY += 10;


            customerPhone1TextArray[i] = new TextView(context);
            tabs[i].addView(customerPhone1TextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) customerPhone1TextArray[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            customerPhone1TextArray[i].setLayoutParams(relativeParams);
            customerPhone1TextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            customerPhone1TextArray[i].setTypeface(null, Typeface.BOLD);
            customerPhone1TextArray[i].setTextColor(Color.BLACK);
            customerPhone1TextArray[i].setGravity(Gravity.CENTER);
            if (itemSelectedId == 0) {
                if (customersNotificationTasksModel.get(i).phone1.length() > 0 && customersNotificationTasksModel.get(i).phone2.length() > 0)
                    customerPhone1TextArray[i].setText(customersNotificationTasksModel.get(i).phone1 + " - " + customersNotificationTasksModel.get(i).phone2);
                else if (customersNotificationTasksModel.get(i).phone1.length() > 0)
                    customerPhone1TextArray[i].setText(customersNotificationTasksModel.get(i).phone1);
                else
                    customerPhone1TextArray[i].setText(customersNotificationTasksModel.get(i).phone2);
            } else {
                if (itemSelectedId == R.id.nav_tasks_tomorrow) {
                    if (customersTasksTomorrowModel.get(i).phone1.length() > 0 && customersTasksTomorrowModel.get(i).phone2.length() > 0)
                        customerPhone1TextArray[i].setText(customersTasksTomorrowModel.get(i).phone1 + " - " + customersTasksTomorrowModel.get(i).phone2);
                    else if (customersTasksTomorrowModel.get(i).phone1.length() > 0)
                        customerPhone1TextArray[i].setText(customersTasksTomorrowModel.get(i).phone1);
                    else
                        customerPhone1TextArray[i].setText(customersTasksTomorrowModel.get(i).phone2);
                } else if (itemSelectedId == R.id.nav_tasks_today) {
                    if (customersTasksTodayModel.get(i).phone1.length() > 0 && customersTasksTodayModel.get(i).phone2.length() > 0)
                        customerPhone1TextArray[i].setText(customersTasksTodayModel.get(i).phone1 + " - " + customersTasksTodayModel.get(i).phone2);
                    else if (customersTasksTodayModel.get(i).phone1.length() > 0)
                        customerPhone1TextArray[i].setText(customersTasksTodayModel.get(i).phone1);
                    else
                        customerPhone1TextArray[i].setText(customersTasksTodayModel.get(i).phone2);
                } else {
                    if (customersPendingTasksModel.get(i).phone1.length() > 0 && customersPendingTasksModel.get(i).phone2.length() > 0)
                        customerPhone1TextArray[i].setText(customersPendingTasksModel.get(i).phone1 + " - " + customersPendingTasksModel.get(i).phone2);
                    else if (customersPendingTasksModel.get(i).phone1.length() > 0)
                        customerPhone1TextArray[i].setText(customersPendingTasksModel.get(i).phone1);
                    else
                        customerPhone1TextArray[i].setText(customersPendingTasksModel.get(i).phone2);
                }
            }
            textY += 10;


            customerCityTextArray[i] = new TextView(context);
            tabs[i].addView(customerCityTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) customerCityTextArray[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            customerCityTextArray[i].setLayoutParams(relativeParams);
            customerCityTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            customerCityTextArray[i].setTypeface(null, Typeface.BOLD);
            customerCityTextArray[i].setTextColor(Color.BLACK);
            customerCityTextArray[i].setGravity(Gravity.CENTER);
            if (itemSelectedId == 0)
                customerCityTextArray[i].setText(customersNotificationTasksModel.get(i).city);
            else {
                if (itemSelectedId == R.id.nav_tasks_tomorrow)
                    customerCityTextArray[i].setText(customersTasksTomorrowModel.get(i).city);
                else if (itemSelectedId == R.id.nav_tasks_today)
                    customerCityTextArray[i].setText(customersTasksTodayModel.get(i).city);
                else
                    customerCityTextArray[i].setText(customersPendingTasksModel.get(i).city);
            }
            textY += 10;


            customerAddressTextArray[i] = new TextView(context);
            tabs[i].addView(customerAddressTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) customerAddressTextArray[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            customerAddressTextArray[i].setLayoutParams(relativeParams);
            customerAddressTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            customerAddressTextArray[i].setTypeface(null, Typeface.BOLD);
            customerAddressTextArray[i].setTextColor(Color.BLACK);
            customerAddressTextArray[i].setGravity(Gravity.CENTER);
            if (itemSelectedId == 0)
                customerAddressTextArray[i].setText(customersNotificationTasksModel.get(i).address);
            else {
                if (itemSelectedId == R.id.nav_tasks_tomorrow)
                    customerAddressTextArray[i].setText(customersTasksTomorrowModel.get(i).address);
                else if (itemSelectedId == R.id.nav_tasks_today)
                    customerAddressTextArray[i].setText(customersTasksTodayModel.get(i).address);
                else
                    customerAddressTextArray[i].setText(customersPendingTasksModel.get(i).address);
            }
            textY += 10;


            customersDetailsText[i] = new TextView(context);
            tabs[i].addView(customersDetailsText[i]);
            relativeParams = (RelativeLayout.LayoutParams) customersDetailsText[i].getLayoutParams();
            relativeParams.setMargins(w * 2 / 100, h * (textY + 5) / 100, 0, h * 5 / 100);
            relativeParams.width = w * 86 / 100;
            relativeParams.height = h * 40 / 100;
            customersDetailsText[i].setLayoutParams(relativeParams);
            customersDetailsText[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            customersDetailsText[i].setTypeface(null, Typeface.BOLD);
            customersDetailsText[i].setSingleLine(false);
            customersDetailsText[i].setGravity(Gravity.TOP);
            customersDetailsText[i].setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            customersDetailsText[i].setMaxLines(h * 40 / 100);
            customersDetailsText[i].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            customersDetailsText[i].setVerticalScrollBarEnabled(true);
            customersDetailsText[i].setHorizontalScrollBarEnabled(true);
            customersDetailsText[i].setMovementMethod(new ScrollingMovementMethod());
            customersDetailsText[i].setTextColor(Color.BLACK);
            customersDetailsText[i].setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
            customersDetailsText[i].setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(final View v, final MotionEvent motionEvent) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                customersDetailsText[i].setBackgroundDrawable(detailsTextViewBorderGd);
            else
                customersDetailsText[i].setBackground(detailsTextViewBorderGd);
            if (itemSelectedId == 0)
                customersDetailsText[i].setText(customersNotificationTasksModel.get(i).details);
            else {
                if (itemSelectedId == R.id.nav_tasks_tomorrow)
                    customersDetailsText[i].setText(customersTasksTomorrowModel.get(i).details);
                else if (itemSelectedId == R.id.nav_tasks_today)
                    customersDetailsText[i].setText(customersTasksTodayModel.get(i).details);
                else
                    customersDetailsText[i].setText(customersPendingTasksModel.get(i).details);
            }
            if (itemSelectedId == R.id.nav_pending_tasks)
                y += 110;
            else
                y += 100;
        }
    }

    public static void customerDeletedGuarantees() {
        if (!horizontalRelativeLayout.isShown()) {
            horizontalScrollView.setY(h * 10 / 100);
            horizontalScrollView.setX(0);
            horizontalScrollView.getLayoutParams().height = h * 97 / 100;
            horizontalScrollView.getLayoutParams().width = w * 96 / 100;
        }
        scrollViewTable.setY(0);
        scrollViewTable.setX(0);
        scrollViewTable.getLayoutParams().height = h * 90 / 100;
        scrollViewTable.fullScroll(ScrollView.FOCUS_UP);
        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        customersDeletedGuaranteeModel = new ArrayList<CustomersGuaranteeExtendedModel>();
        customersDeletedGuaranteeModel = mydb.getCustomerDeletedGuarantees();
        dataLength = customersDeletedGuaranteeModel.size();
        imageViewDelete = new ImageView[dataLength];
        customerNameTextArray = new TextView[dataLength];
        TextView[] customersDateText = new TextView[dataLength];
        TextView[] customersDetailsText = new TextView[dataLength];
        RelativeLayout[] tabs = new RelativeLayout[dataLength];
        int y = 0, textX = 0, textY;
        for (int i = 0; i < dataLength; i++) {
            tabs[i] = new RelativeLayout(context);
            verticalRelativeLayout.addView(tabs[i]);
            relativeParams = (RelativeLayout.LayoutParams) tabs[i].getLayoutParams();
            relativeParams.width = w * 89 / 100;
            relativeParams.height = h * 72 / 100;
            relativeParams.setMargins(0, h * y / 100, 0, h * 9 / 100);
            tabs[i].setLayoutParams(relativeParams);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                tabs[i].setBackgroundDrawable(gd);
            else
                tabs[i].setBackground(gd);

            imageViewDelete[i] = new ImageView(context);
            tabs[i].addView(imageViewDelete[i]);
            relativeParams = (RelativeLayout.LayoutParams) imageViewDelete[i].getLayoutParams();
            relativeParams.width = w * 12 / 100;
            relativeParams.height = h * 7 / 100;
            relativeParams.setMargins(w * 75 / 100, h * 2 / 100, 0, 0);
            imageViewDelete[i].setLayoutParams(relativeParams);
            imageViewDelete[i].setBackgroundResource(R.drawable.ic_customer_delete);
            imageViewDelete[i].setTag(i);
            imageViewDelete[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    customerIndex = (int) v.getTag();
                    resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle(res.getString(resourceID));
                    builder.setCancelable(false);
                    resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                    builder.setMessage(res.getString(resourceID));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int row = customersDeletedGuaranteeModel.get(customerIndex).guaranteeRow;
                            customerId = customersDeletedGuaranteeModel.get(customerIndex).id;
                            deletedGuarantees--;
                            MenuItem deletedCustomerGuarantees = menu.findItem(R.id.nav_customer_deleted_guarantees);
                            deletedCustomerGuarantees.setTitle(historySubmenuXML[0] + " (" + deletedGuarantees + ")");
                            mainActivity.setTitle(deletedCustomerGuarantees.getTitle().toString());
                            mydb.deleteCustomerDeletedGuaranteeRow(customerId, row);
                            mydb.updateCustomerDeletedGuaranteeRows(customerId);
                            resourceID = mainActivity.getResources().getIdentifier("deleteSuccess", "string", packageName);
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                            navigateMe();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });


            textY = 9;
            customerNameTextArray[i] = new TextView(context);
            tabs[i].addView(customerNameTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) customerNameTextArray[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            customerNameTextArray[i].setLayoutParams(relativeParams);
            customerNameTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            customerNameTextArray[i].setTypeface(null, Typeface.BOLD);
            customerNameTextArray[i].setTextColor(Color.BLACK);
            customerNameTextArray[i].setGravity(Gravity.CENTER);
            customerNameTextArray[i].setText(customersDeletedGuaranteeModel.get(i).name);
            textY += 10;


            customersDateText[i] = new TextView(context);
            tabs[i].addView(customersDateText[i]);
            relativeParams = (RelativeLayout.LayoutParams) customersDateText[i].getLayoutParams();
            relativeParams.setMargins(w * textX / 100, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            customersDateText[i].setLayoutParams(relativeParams);
            customersDateText[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            customersDateText[i].setTypeface(null, Typeface.BOLD);
            customersDateText[i].setTextColor(Color.BLACK);
            customersDateText[i].setGravity(Gravity.CENTER);
            customersDateText[i].setText(customersDeletedGuaranteeModel.get(i).date);
            textY += 10;


            customersDetailsText[i] = new TextView(context);
            tabs[i].addView(customersDetailsText[i]);
            relativeParams = (RelativeLayout.LayoutParams) customersDetailsText[i].getLayoutParams();
            relativeParams.setMargins(w * 2 / 100, h * (textY + 5) / 100, 0, h * 5 / 100);
            relativeParams.width = w * 86 / 100;
            relativeParams.height = h * 40 / 100;
            customersDetailsText[i].setLayoutParams(relativeParams);
            customersDetailsText[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            customersDetailsText[i].setTypeface(null, Typeface.BOLD);
            customersDetailsText[i].setSingleLine(false);
            customersDetailsText[i].setGravity(Gravity.TOP);
            customersDetailsText[i].setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            customersDetailsText[i].setMaxLines(h * 40 / 100);
            customersDetailsText[i].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            customersDetailsText[i].setVerticalScrollBarEnabled(true);
            customersDetailsText[i].setHorizontalScrollBarEnabled(true);
            customersDetailsText[i].setMovementMethod(new ScrollingMovementMethod());
            customersDetailsText[i].setTextColor(Color.BLACK);
            customersDetailsText[i].setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
            customersDetailsText[i].setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(final View v, final MotionEvent motionEvent) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                customersDetailsText[i].setBackgroundDrawable(detailsTextViewBorderGd);
            else
                customersDetailsText[i].setBackground(detailsTextViewBorderGd);
            customersDetailsText[i].setText(customersDeletedGuaranteeModel.get(i).details);
            y += 80;
        }
    }

    public static void customerPaymentHistory() {
        if (!horizontalRelativeLayout.isShown()) {
            horizontalScrollView.setY(h * 25 / 100);
            horizontalScrollView.setX(w * 2 / 100);
            horizontalScrollView.getLayoutParams().height = h * 97 / 100;
            horizontalScrollView.getLayoutParams().width = w * 96 / 100;
        }
        scrollViewTable.setY(h * 10 / 100);
        scrollViewTable.setX(w * -1 / 100);
        scrollViewTable.getLayoutParams().height = h * 65 / 100;
        scrollViewTable.fullScroll(ScrollView.FOCUS_UP);
        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        headerΤextViewArray = new TextView[2];
        int i, x = 0, y = 0;
        for (i = 0; i < headerΤextViewArray.length; i++) {
            headerΤextViewArray[i] = new TextView(context);
            horizontalRelativeLayout.addView(headerΤextViewArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) headerΤextViewArray[i].getLayoutParams();
            relativeParams.width = w * 40 / 100;
            relativeParams.height = h * 7 / 100;
            relativeParams.setMargins(w * x / 100, 0, 0, 0);
            headerΤextViewArray[i].setLayoutParams(relativeParams);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                headerΤextViewArray[i].setBackgroundDrawable(gd);
            else
                headerΤextViewArray[i].setBackground(gd);
            headerΤextViewArray[i].setGravity(Gravity.CENTER);
            headerΤextViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            headerΤextViewArray[i].setTextColor(Color.BLACK);
            headerΤextViewArray[i].setTypeface(null, Typeface.BOLD);
            resourceID = mainActivity.getResources().getIdentifier(header[i], "string", packageName);
            headerΤextViewArray[i].setText(res.getString(resourceID));
            x += 45;
        }
        customerNameTextArray = new TextView[customersCreditDebtModel.size()];
        customerSurnameTextArray = new TextView[customersCreditDebtModel.size()];
        ImageView[] imageViewDetails = new ImageView[customersCreditDebtModel.size()];
        imageViewDelete = new ImageView[customersCreditDebtModel.size()];
        ArrayList<String> names = new ArrayList<String>();
        for (i = 0; i < customersCreditDebtModel.size(); i++) {
            if (((customersCreditDebtModel.get(i).name.trim().toLowerCase().startsWith(searchText.trim().toLowerCase()) && spinnerSelectedItem == 0) || (customersCreditDebtModel.get(i).surname.trim().toLowerCase().startsWith(searchText.trim().toLowerCase()) && spinnerSelectedItem == 1)) && !names.contains(customersCreditDebtModel.get(i).name + " " + customersCreditDebtModel.get(i).surname)) {
                x = 0;
                names.add(customersCreditDebtModel.get(i).name + " " + customersCreditDebtModel.get(i).surname);
                customerNameTextArray[i] = new TextView(context);
                customerSurnameTextArray[i] = new TextView(context);
                imageViewDetails[i] = new ImageView(context);
                imageViewDelete[i] = new ImageView(context);
                verticalRelativeLayout.addView(customerNameTextArray[i]);
                verticalRelativeLayout.addView(customerSurnameTextArray[i]);
                verticalRelativeLayout.addView(imageViewDetails[i]);
                verticalRelativeLayout.addView(imageViewDelete[i]);
                relativeParams = (RelativeLayout.LayoutParams) customerNameTextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 0, h * 9 / 100);
                customerNameTextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerNameTextArray[i].setBackgroundDrawable(gd);
                else
                    customerNameTextArray[i].setBackground(gd);
                customerNameTextArray[i].setGravity(Gravity.CENTER);
                customerNameTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerNameTextArray[i].setTypeface(null, Typeface.BOLD);
                customerNameTextArray[i].setTextColor(Color.BLACK);
                customerNameTextArray[i].setText(customersCreditDebtModel.get(i).name);
                x += 45;


                relativeParams = (RelativeLayout.LayoutParams) customerSurnameTextArray[i].getLayoutParams();
                relativeParams.width = w * 40 / 100;
                relativeParams.height = h * 7 / 100;
                relativeParams.setMargins(w * x / 100, h * y / 100, 100, h * 9 / 100);
                customerSurnameTextArray[i].setLayoutParams(relativeParams);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                    customerSurnameTextArray[i].setBackgroundDrawable(gd);
                else
                    customerSurnameTextArray[i].setBackground(gd);
                customerSurnameTextArray[i].setGravity(Gravity.CENTER);
                customerSurnameTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
                customerSurnameTextArray[i].setTypeface(null, Typeface.BOLD);
                customerSurnameTextArray[i].setTextColor(Color.BLACK);
                customerSurnameTextArray[i].setText(customersCreditDebtModel.get(i).surname);
                x += 45;


                relativeParams = (RelativeLayout.LayoutParams) imageViewDetails[i].getLayoutParams();
                relativeParams.width = w * 8 / 100;
                relativeParams.height = h * 5 / 100;
                relativeParams.setMargins(w * x / 100, h * (y + 1) / 100, w * 5 / 100, h * 9 / 100);
                imageViewDetails[i].setLayoutParams(relativeParams);
                imageViewDetails[i].setBackgroundResource(R.drawable.ic_customer_details);
                imageViewDetails[i].setTag(i);
                imageViewDetails[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customerIndex = (int) v.getTag();
                        resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                        builder.setTitle(res.getString(resourceID));
                        builder.setCancelable(false);
                        resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                        builder.setMessage(res.getString(resourceID));
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if (mainActivity.getCurrentFocus() != null) {
                                    InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(mainActivity.getCurrentFocus().getWindowToken(), 0);
                                }
                                customerId = customersCreditDebtModel.get(customerIndex).customerId;
                                removeSomeViews();
                                seeCreditDebtHistory();
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                x += 15;


                relativeParams = (RelativeLayout.LayoutParams) imageViewDelete[i].getLayoutParams();
                relativeParams.width = w * 8 / 100;
                relativeParams.height = h * 5 / 100;
                relativeParams.setMargins(w * x / 100, h * (y + 1) / 100, h * 3 / 100, 0);
                imageViewDelete[i].setLayoutParams(relativeParams);
                imageViewDelete[i].setBackgroundResource(R.drawable.ic_customer_delete);
                imageViewDelete[i].setTag(i);
                imageViewDelete[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        customerIndex = (int) v.getTag();
                        resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                        builder.setTitle(res.getString(resourceID));
                        builder.setCancelable(false);
                        resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                        builder.setMessage(res.getString(resourceID));
                        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mydb.deleteCustomerCreditDebt(customersCreditDebtModel.get(customerIndex).customerId, -1);
                                customersCreditDebtModel = new ArrayList<CustomersCreditDebtModel>();
                                customersCreditDebtModel = mydb.getAllCustomersCreditDebt();
                                resourceID = mainActivity.getResources().getIdentifier("deleteSuccess", "string", packageName);
                                toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                                toastMessage.setText(res.getString(resourceID));
                                toast.setView(toastMessage);
                                toast.show();
                                navigateMe();
                            }
                        });
                        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();
                    }
                });
                y += 8;
            }
        }
    }

    public static void getPersonalNotes() {
        if (!horizontalRelativeLayout.isShown()) {
            horizontalScrollView.setY(h * 10 / 100);
            horizontalScrollView.setX(0);
            horizontalScrollView.getLayoutParams().height = h * 97 / 100;
            horizontalScrollView.getLayoutParams().width = w * 96 / 100;
        }
        scrollViewTable.setY(0);
        scrollViewTable.setX(0);
        scrollViewTable.getLayoutParams().height = h * 90 / 100;
        scrollViewTable.fullScroll(ScrollView.FOCUS_UP);
        horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
        int y = 0;
        btns = new Button[personalNotesModel.size()];
        editTextArray = new EditText[personalNotesModel.size()];
        imageViewDelete = new ImageView[personalNotesModel.size()];
        RelativeLayout[] tabs = new RelativeLayout[personalNotesModel.size()];
        for (int i = 0; i < personalNotesModel.size(); i++) {
            tabs[i] = new RelativeLayout(context);
            verticalRelativeLayout.addView(tabs[i]);
            relativeParams = (RelativeLayout.LayoutParams) tabs[i].getLayoutParams();
            relativeParams.width = w * 89 / 100;
            relativeParams.height = h * 47 / 100;
            relativeParams.setMargins(0, h * y / 100, 0, h * 9 / 100);
            tabs[i].setLayoutParams(relativeParams);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                tabs[i].setBackgroundDrawable(gd);
            else
                tabs[i].setBackground(gd);

            imageViewDelete[i] = new ImageView(context);
            tabs[i].addView(imageViewDelete[i]);
            relativeParams = (RelativeLayout.LayoutParams) imageViewDelete[i].getLayoutParams();
            relativeParams.width = w * 12 / 100;
            relativeParams.height = h * 7 / 100;
            relativeParams.setMargins(w * 75 / 100, h * 2 / 100, 0, 0);
            imageViewDelete[i].setLayoutParams(relativeParams);
            imageViewDelete[i].setBackgroundResource(R.drawable.ic_customer_delete);
            imageViewDelete[i].setTag(i);
            imageViewDelete[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemIndex = (int) v.getTag();
                    resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle(res.getString(resourceID));
                    builder.setCancelable(false);
                    resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                    builder.setMessage(res.getString(resourceID));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int noteId = personalNotesModel.get(itemIndex).id;
                            mydb.deletePersonalNotes(noteId);
                            personalNotesModel.remove(itemIndex);
                            resourceID = mainActivity.getResources().getIdentifier("deleteSuccess", "string", packageName);
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                            navigateMe();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });


            editTextArray[i] = new EditText(context);
            tabs[i].addView(editTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) editTextArray[i].getLayoutParams();
            relativeParams.setMargins(w * 2 / 100, h * 11 / 100, 0, h * 5 / 100);
            relativeParams.width = w * 86 / 100;
            relativeParams.height = h * 15 / 100;
            editTextArray[i].setLayoutParams(relativeParams);
            editTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 5 / 100);
            editTextArray[i].setTextColor(Color.BLACK);
            editTextArray[i].setTypeface(null, Typeface.BOLD);
            editTextArray[i].setSingleLine(false);
            editTextArray[i].setGravity(Gravity.TOP);
            editTextArray[i].setImeOptions(EditorInfo.IME_FLAG_NO_ENTER_ACTION);
            editTextArray[i].setMaxLines(h * 15 / 100);
            editTextArray[i].setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_MULTI_LINE);
            editTextArray[i].setVerticalScrollBarEnabled(true);
            editTextArray[i].setHorizontalScrollBarEnabled(true);
            editTextArray[i].setMovementMethod(new ScrollingMovementMethod());
            editTextArray[i].getBackground().mutate().setColorFilter(mainActivity.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            try {
                Field f = TextView.class.getDeclaredField("mCursorDrawableRes");
                f.setAccessible(true);
                f.set(editTextArray[i], R.drawable.cursor);
            } catch (Exception ignored) {
            }
            editTextArray[i].setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
            editTextArray[i].setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(final View v, final MotionEvent motionEvent) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                    switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                        case MotionEvent.ACTION_UP:
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                            break;
                    }
                    return false;
                }
            });
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                editTextArray[i].setBackgroundDrawable(detailsTextViewBorderGd);
            else
                editTextArray[i].setBackground(detailsTextViewBorderGd);
            editTextArray[i].setText(personalNotesModel.get(i).note);

            btns[i] = new Button(context);
            tabs[i].addView(btns[i]);
            relativeParams = (RelativeLayout.LayoutParams) btns[i].getLayoutParams();
            relativeParams.setMargins(w * 28 / 100, h * 32 / 100, 0, h * 2 / 100);
            relativeParams.width = w * 33 / 100;
            relativeParams.height = h * 10 / 100;
            btns[i].setLayoutParams(relativeParams);
            btns[i].setText("ΑΛΛΑΓΗ");
            btns[i].setTag(i);
            btns[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 4 / 100);
            btns[i].setTextColor(Color.BLACK);
            btns[i].setTypeface(null, Typeface.BOLD);
            btns[i].setBackgroundColor(Color.parseColor("#D6D7D7"));
            btns[i].setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    itemIndex = (int) v.getTag();
                    resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle(res.getString(resourceID));
                    builder.setCancelable(false);
                    resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                    builder.setMessage(res.getString(resourceID));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int noteId = personalNotesModel.get(itemIndex).id;
                            PersonalNotesModel model = new PersonalNotesModel();
                            model.id = noteId;
                            model.note = editTextArray[itemIndex].getText().toString();
                            mydb.updatePersonalNotes(noteId, model);
                            personalNotesModel.set(itemIndex, model);
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            resourceID = mainActivity.getResources().getIdentifier("changeSuccess", "string", packageName);
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });
            y += 50;
        }
    }

    public static void seeCreditDebtHistory() {
        functionNow = "seeCreditDebtHistory";
        tempCustomersCreditDebtModel = mydb.getCustomersCreditDebt(customerId);
        dataLength = tempCustomersCreditDebtModel.size();
        ImageView imageViewBack = new ImageView(context);
        relativeLayout.addView(imageViewBack);
        relativeParams = (RelativeLayout.LayoutParams) imageViewBack.getLayoutParams();
        relativeParams.width = w * 12 / 100;
        relativeParams.height = h * 7 / 100;
        relativeParams.setMargins(w * 2 / 100, h * 8 / 100, 0, 0);
        imageViewBack.setLayoutParams(relativeParams);
        imageViewBack.setBackgroundResource(R.drawable.ic_arrow_back);
        imageViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateMe();
            }
        });
        int y = 15, textY = 9;
        TextView customerNameSurname = new TextView(context);
        relativeLayout.addView(customerNameSurname);
        relativeParams = (RelativeLayout.LayoutParams) customerNameSurname.getLayoutParams();
        relativeParams.setMargins(0, h * y / 100, 0, 0);
        relativeParams.width = w;
        customerNameSurname.setLayoutParams(relativeParams);
        customerNameSurname.setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
        customerNameSurname.setTypeface(null, Typeface.BOLD);
        customerNameSurname.setTextColor(Color.BLACK);
        customerNameSurname.setGravity(Gravity.CENTER);
        customerNameSurname.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
        customerNameSurname.setText(customerNameTextArray[customerIndex].getText() + " " + customerSurnameTextArray[customerIndex].getText());
        y += 7;
        imageViewDelete = new ImageView[dataLength];
        TextView[] dateTextArray = new TextView[dataLength];
        oldDebtTextArray = new TextView[dataLength];
        creditDebtTextArray = new TextView[dataLength];
        currentDebtTextArray = new TextView[dataLength];
        RelativeLayout[] tabs = new RelativeLayout[dataLength];
        for (int i = 0; i < dataLength; i++) {
            tabs[i] = new RelativeLayout(context);
            relativeLayout.addView(tabs[i]);
            relativeParams = (RelativeLayout.LayoutParams) tabs[i].getLayoutParams();
            relativeParams.width = w * 89 / 100;
            relativeParams.height = h * 65 / 100;
            relativeParams.setMargins(0, h * y / 100, 0, h * 9 / 100);
            tabs[i].setLayoutParams(relativeParams);
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN)
                tabs[i].setBackgroundDrawable(gd);
            else
                tabs[i].setBackground(gd);
            textY = 15;

            imageViewDelete[i] = new ImageView(context);
            tabs[i].addView(imageViewDelete[i]);
            relativeParams = (RelativeLayout.LayoutParams) imageViewDelete[i].getLayoutParams();
            relativeParams.width = w * 12 / 100;
            relativeParams.height = h * 7 / 100;
            relativeParams.setMargins(w * 75 / 100, h * 2 / 100, 0, 0);
            imageViewDelete[i].setLayoutParams(relativeParams);
            imageViewDelete[i].setBackgroundResource(R.drawable.ic_customer_delete);
            imageViewDelete[i].setTag(i);
            imageViewDelete[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemIndex = (int) v.getTag();
                    resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle(res.getString(resourceID));
                    builder.setCancelable(false);
                    resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                    builder.setMessage(res.getString(resourceID));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            int i;
                            int row = tempCustomersCreditDebtModel.get(itemIndex).creditDebtRow;
                            for (i = 0; i < tempCustomersCreditDebtModel.size(); i++) {
                                if (tempCustomersCreditDebtModel.get(i).customerId == customerId && tempCustomersCreditDebtModel.get(i).creditDebtRow == row) {
                                    tempCustomersCreditDebtModel.remove(i);
                                    break;
                                }
                            }
                            for (i = 0; i < customersCreditDebtModel.size(); i++) {
                                if (customersCreditDebtModel.get(i).customerId == customerId && customersCreditDebtModel.get(i).creditDebtRow == row) {
                                    customersCreditDebtModel.remove(i);
                                    break;
                                }
                            }
                            mydb.deleteCustomerCreditDebt(customerId, row);
                            resourceID = mainActivity.getResources().getIdentifier("deleteSuccess", "string", packageName);
                            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                            toastMessage.setText(res.getString(resourceID));
                            toast.setView(toastMessage);
                            toast.show();
                            if (tempCustomersCreditDebtModel.size() == 0)
                                navigateMe();
                            else {
                                relativeLayout.removeAllViews();
                                seeCreditDebtHistory();
                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            });


            dateTextArray[i] = new TextView(context);
            tabs[i].addView(dateTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) dateTextArray[i].getLayoutParams();
            relativeParams.setMargins(0, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            dateTextArray[i].setLayoutParams(relativeParams);
            dateTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            dateTextArray[i].setTypeface(null, Typeface.BOLD);
            dateTextArray[i].setTextColor(Color.BLACK);
            dateTextArray[i].setGravity(Gravity.CENTER);
            dateTextArray[i].setText(tempCustomersCreditDebtModel.get(i).date);
            textY += 10;


            oldDebtTextArray[i] = new TextView(context);
            tabs[i].addView(oldDebtTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) oldDebtTextArray[i].getLayoutParams();
            relativeParams.setMargins(0, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            oldDebtTextArray[i].setLayoutParams(relativeParams);
            oldDebtTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            oldDebtTextArray[i].setTypeface(null, Typeface.BOLD);
            oldDebtTextArray[i].setTextColor(Color.BLACK);
            oldDebtTextArray[i].setGravity(Gravity.CENTER);
            debtDoubleParse = Double.valueOf(tempCustomersCreditDebtModel.get(i).oldDebt);
            debtFormatted = decimalFormat.format(debtDoubleParse);
            resourceID = mainActivity.getResources().getIdentifier("totalDebt", "string", packageName);
            oldDebtTextArray[i].setText(res.getString(resourceID) + " " + debtFormatted);
            textY += 10;


            creditDebtTextArray[i] = new TextView(context);
            tabs[i].addView(creditDebtTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) creditDebtTextArray[i].getLayoutParams();
            relativeParams.setMargins(0, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            creditDebtTextArray[i].setLayoutParams(relativeParams);
            creditDebtTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            creditDebtTextArray[i].setTypeface(null, Typeface.BOLD);
            creditDebtTextArray[i].setTextColor(Color.BLACK);
            creditDebtTextArray[i].setGravity(Gravity.CENTER);
            debtDoubleParse = Double.valueOf(tempCustomersCreditDebtModel.get(i).creditDebt);
            debtFormatted = decimalFormat.format(debtDoubleParse);
            resourceID = mainActivity.getResources().getIdentifier("credit", "string", packageName);
            creditDebtTextArray[i].setText(res.getString(resourceID) + " : " + debtFormatted);
            textY += 10;


            currentDebtTextArray[i] = new TextView(context);
            tabs[i].addView(currentDebtTextArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) currentDebtTextArray[i].getLayoutParams();
            relativeParams.setMargins(0, h * textY / 100, 0, h * 5 / 100);
            relativeParams.width = w;
            currentDebtTextArray[i].setLayoutParams(relativeParams);
            currentDebtTextArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * 6 / 100);
            currentDebtTextArray[i].setTypeface(null, Typeface.BOLD);
            currentDebtTextArray[i].setTextColor(Color.BLACK);
            currentDebtTextArray[i].setGravity(Gravity.CENTER);
            debtDoubleParse = Double.valueOf(tempCustomersCreditDebtModel.get(i).currentDebt);
            debtFormatted = decimalFormat.format(debtDoubleParse);
            resourceID = mainActivity.getResources().getIdentifier("newDebt", "string", packageName);
            currentDebtTextArray[i].setText(res.getString(resourceID) + " " + debtFormatted);
            y += 73;
        }
    }

    public void home() {
        btn = new Button(this);
        RelativeLayout[] tabs = new RelativeLayout[3];
        textViewArray = new TextView[3];
        int y = 13, textSize;
        for (int i = 0; i < textViewArray.length; i++) {
            if (i == 0) {
                textSize = 7;
            } else {
                textSize = 4;
            }
            textViewArray[i] = new TextView(this);
            tabs[i] = new RelativeLayout(this);
            relativeLayout.addView(tabs[i]);
            relativeParams = (RelativeLayout.LayoutParams) tabs[i].getLayoutParams();
            relativeParams.setMargins(0, h * y / 100, 0, 0);
            relativeParams.width = w;
            relativeParams.height = h * 18 / 100;
            tabs[i].setLayoutParams(relativeParams);
            tabs[i].addView(textViewArray[i]);
            relativeParams = (RelativeLayout.LayoutParams) textViewArray[i].getLayoutParams();
            relativeParams.setMargins(0, 0, 0, 0);
            relativeParams.width = w;
            textViewArray[i].setLayoutParams(relativeParams);
            textViewArray[i].setGravity(Gravity.CENTER);
            if (i == 0) {
                textViewArray[i].setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);
                textViewArray[i].setTypeface(null, Typeface.BOLD_ITALIC);
                resourceID = mainActivity.getResources().getIdentifier("welcome", "string", packageName);
            } else if (i == 1) {
                textViewArray[i].setTypeface(null, Typeface.BOLD);
                resourceID = mainActivity.getResources().getIdentifier("createdBy", "string", packageName);
            } else {
                textViewArray[i].setTypeface(null, Typeface.BOLD);
                resourceID = mainActivity.getResources().getIdentifier("sendEmail", "string", packageName);
            }
            textViewArray[i].setText(res.getString(resourceID));
            textViewArray[i].setTextSize(TypedValue.COMPLEX_UNIT_PX, w * textSize / 100);
            textViewArray[i].setTextColor(Color.BLACK);
            if (i == 0)
                y += 27;
            else
                y += 11;
        }
        String html = "<html><body style=\"background-color:#ADAAAA;\"><form action=\"https://www.paypal.com/cgi-bin/webscr\" method=\"post\" target=\"_top\">\n" +
                "<input type=\"hidden\" name=\"cmd\" value=\"_s-xclick\">\n" +
                "<input type=\"hidden\" name=\"hosted_button_id\" value=\"K99TE8ZQFMUW6\">\n" +
                "<input type=\"image\" src=\"file:///android_res/drawable/donate.png\" style=\"width: 80%\" border=\"0\" name=\"submit\" alt=\"Donate\">\n" +
                "</form>\n</body></html>";
        String mime = "text/html";
        String encoding = "utf-8";
        relativeParams = (RelativeLayout.LayoutParams) browser.getLayoutParams();
        relativeParams.setMargins(w * 21 / 100, h * (y + 4) / 100, 0, 0);
        relativeParams.width = w * 60 / 100;
        relativeParams.height = h * 25 / 100;
        browser.setLayoutParams(relativeParams);
        browser.setScrollContainer(false);
        browser.getSettings().setJavaScriptEnabled(true);
        browser.setBackgroundColor(Color.TRANSPARENT);
        browser.loadDataWithBaseURL(null, html, mime, encoding, null);
    }

    public static void createImageViewEdit(int i, int x, int y) {
        relativeParams = (RelativeLayout.LayoutParams) imageViewEdit[i].getLayoutParams();
        relativeParams.width = w * 8 / 100;
        relativeParams.height = h * 5 / 100;
        relativeParams.setMargins(w * x / 100, h * (y + 1) / 100, w * 5 / 100, h * 9 / 100);
        imageViewEdit[i].setLayoutParams(relativeParams);
        imageViewEdit[i].setBackgroundResource(R.drawable.ic_edit);
        imageViewEdit[i].setTag(i);
        imageViewEdit[i].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customerIndex = (int) v.getTag();
                if (itemSelectedId == R.id.nav_customer_payment && customersModel.get(customerIndex).debt == 0.00) {
                    toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                    resourceID = mainActivity.getResources().getIdentifier("noDebt", "string", packageName);
                    toastMessage.setText(res.getString(resourceID).replace("1", "'"));
                    toast.setView(toastMessage);
                    toast.show();

                } else {
                    resourceID = mainActivity.getResources().getIdentifier("confirm", "string", packageName);
                    AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                    builder.setTitle(res.getString(resourceID));
                    builder.setCancelable(false);
                    resourceID = mainActivity.getResources().getIdentifier("confirmMessage", "string", packageName);
                    builder.setMessage(res.getString(resourceID));
                    builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            customerId = customersModel.get(customerIndex).id;
                            customerTextArray = new ArrayList<String>();

                            if (itemSelectedId == R.id.nav_customer_guarantee || itemSelectedId == R.id.nav_customer_task) {
                                customerTextArray.add(textViewArray[customerIndex].getText().toString());
                                updateGuaranteeAndTask();
                            } else {
                                customerTextArray.add(textViewArray[customerIndex].getText().toString().replace(".", "").replace(",", "."));
                                totalDebt = customersModel.get(customerIndex).debt;
                                newCustomerDebt = customersModel.get(customerIndex).debt;
                                updateCustomersAndDebt();
                            }
                        }
                    });
                    builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {

                            dialog.dismiss();
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }
            }
        });
    }

    public static void removeSomeViews() {
        relativeLayout.removeAllViews();
        horizontalRelativeLayout.removeAllViews();
        verticalRelativeLayout.removeAllViews();
    }

    public static void navigateMe() {
        if (mainActivity.getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) mainActivity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(mainActivity.getCurrentFocus().getWindowToken(), 0);
        }
        functionNow = "";
        navigateMeFunction = 1;
        removeSomeViews();
        new loadingTask().execute();
    }

    public static void getNotificationTasks() {
        functionNow = "getNotificationTasks";
        Intent activityIntent = new Intent(context, MainActivity.class);
        activityIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        mainActivity.startActivity(activityIntent);
        mainActivity.setTitle(menuXML[2] + " (" + notificationTaskCounter + ")");
        for (int i = 0; i < menu.size(); i++) {
            if (i == 0)
                menu.getItem(i).setChecked(false);
            else {
                for (int j = 0; j < menu.getItem(i).getSubMenu().size(); j++)
                    menu.getItem(i).getSubMenu().getItem(j).setChecked(false);
            }
        }
        itemSelectedId = 0;
        removeSomeViews();
        new loadingTask().execute();
    }

    public static void createSearchViewAndSpinner() {
        searchView = new SearchView(mainActivity);
        relativeLayout.addView(searchView);
        relativeParams = (RelativeLayout.LayoutParams) searchView.getLayoutParams();
        relativeParams.setMargins(w * -2 / 100, h * 10 / 100, 0, h * 5 / 100);
        relativeParams.width = w * 60 / 100;
        searchView.setLayoutParams(relativeParams);
        searchView.setIconifiedByDefault(false);
        resourceID = mainActivity.getResources().getIdentifier("search", "string", packageName);
        searchView.setQueryHint(res.getString(resourceID));
        searchView.setQuery(searchText, false);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                searchText = newText;
                if (newText.length() == 0)
                    navigateMe();
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchText = query;
                navigateMe();
                return false;
            }


        });

        relativeLayout.addView(spinner);
        relativeParams = (RelativeLayout.LayoutParams) spinner.getLayoutParams();
        relativeParams.setMargins(w * 60 / 100, h * 11 / 100, 0, 0);
        relativeParams.width = w;
        relativeParams.height = h * 6 / 100;
        spinner.setLayoutParams(relativeParams);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < spinnerXML.length; i++)
            list.add(spinnerXML[i]);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(mainActivity,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(dataAdapter);
        if (navigateMeFunction == 0) {
            if (spinner.getSelectedItemPosition() == 1) {
                spinner.setSelection(0);
            }
        } else
            spinner.setSelection(spinnerSelectedItem);
        spinnerFirstCall = true;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // TODO Auto-generated method stub
                if (!spinnerFirstCall && spinnerSelectedItem != position) {
                    spinnerSelectedItem = position;
                    navigateMe();
                }
                spinnerFirstCall = false;
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub
            }
        });
    }

    public static void dateDialog() {
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(mainActivity, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                if (dateTimePicker == "addItem")
                    editTextArray[0].setText(dateFormat.format(newDate.getTime()));
                else
                    customerDateEdit[customerIndex].setText(dateFormat.format(newDate.getTime()));
            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));
    }

    public static void timeDialog() {
        Calendar mcurrentTime = Calendar.getInstance();
        int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
        int minute = mcurrentTime.get(Calendar.MINUTE);
        timePickerDialog = new TimePickerDialog(mainActivity, new TimePickerDialog.OnTimeSetListener() {

            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                if (dateTimePicker == "addItem") {
                    if (selectedMinute < 10)
                        editTextArray[1].setText(selectedHour + ":0" + selectedMinute);
                    else
                        editTextArray[1].setText(selectedHour + ":" + selectedMinute);
                } else {
                    if (selectedMinute < 10)
                        customerTimeEdit[customerIndex].setText(selectedHour + ":0" + selectedMinute);
                    else
                        customerTimeEdit[customerIndex].setText(selectedHour + ":" + selectedMinute);
                }
            }
        }, hour, minute, true);
    }

    public static boolean checkRequiredFields() {
        if (itemSelectedId == R.id.nav_customer_edit || itemSelectedId == R.id.nav_customer_add) {
            for (int i = 0; i < 2; i++)
                editTextArray[i].getBackground().mutate().setColorFilter(mainActivity.getResources().getColor(R.color.colorAccent), PorterDuff.Mode.SRC_ATOP);
            int indexError = -1;
            if (editTextArray[0].getText().toString().trim().length() == 0) {
                resourceID = mainActivity.getResources().getIdentifier("fieldReguiredError", "string", packageName);
                indexError = 0;
            } else if (editTextArray[1].getText().toString().trim().length() == 0) {
                resourceID = mainActivity.getResources().getIdentifier("fieldReguiredError", "string", packageName);
                indexError = 1;
            }
            if (indexError > -1) {
                toastMessage.setText(res.getString(resourceID));
                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                toast.setView(toastMessage);
                toast.show();
                scrollView.scrollTo(0, (int) textViewArray[indexError].getY() - (h * 13 / 100));
                editTextArray[indexError].getBackground().mutate().setColorFilter(mainActivity.getResources().getColor(R.color.colorError), PorterDuff.Mode.SRC_ATOP);
                editTextArray[indexError].requestFocus();
                return false;
            }
        } else {
            if (textViewArray[0].getText().toString().trim().length() == 0) {
                resourceID = mainActivity.getResources().getIdentifier("fieldReguiredError", "string", packageName);
                toastMessage.setText(res.getString(resourceID));
                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                toast.setView(toastMessage);
                toast.show();
                return false;
            }
        }
        return true;
    }

    public void checkCustomersDetails() {
        Calendar calTomorrow = Calendar.getInstance();
        calTomorrow.add(Calendar.DATE, +1);
        String tomorrow = dateFormat.format(calTomorrow.getTime());
        String today = dateFormat.format(new Date());
        double tempProgress = 20 / (double) customersGuaranteeModel.size();
        String format = String.format("%f", tempProgress);
        tempProgress = Double.parseDouble(format.replace(",", "."));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resourceID = getResources().getIdentifier("checkGuaranteesExpiration", "string", packageName);
                SpannableString ss = new SpannableString(res.getString(resourceID));
                ss.setSpan(new RelativeSizeSpan(w * 0.08f / 100), 0, ss.length(), 0);
                progressDialog.setTitle(ss);
            }
        });
        if (customersGuaranteeModel.size() == 0) {
            progress += 20;
            progressDialog.setProgress((int) progress);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < customersGuaranteeModel.size(); i++) {
            try {
                strDate = dateFormat.parse(customersGuaranteeModel.get(i).date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (strDate != null) {
                if (new Date().after(strDate) && !(customersGuaranteeModel.get(i).date.equals(today))) {
                    customerId = customersGuaranteeModel.get(i).id;
                    CustomersModel model = mydb.getCustomerById(customerId);
                    CustomersGuaranteeExtendedModel temp = new CustomersGuaranteeExtendedModel();
                    temp.id = customerId;
                    temp.guaranteeRow = customersGuaranteeModel.get(i).guaranteeRow;
                    temp.name = model.name + " " + model.surname;
                    temp.date = customersGuaranteeModel.get(i).date;
                    temp.details = customersGuaranteeModel.get(i).details;
                    mydb.insertCustomerDeletedGuarantee(temp);
                    model.guaranteeCount -= 1;
                    mydb.updateCustomers(model);
                    int row = customersGuaranteeModel.get(i).guaranteeRow;
                    mydb.deleteCustomerGuaranteeRow(customerId, row);
                    int found = 0;
                    for (int j = 0; j < customerIds.size(); j++)
                        if (customerIds.get(j) == customerId) {
                            found = 1;
                            break;
                        }
                    if (found == 0)
                        customerIds.add(customerId);
                }
            }
            progress += tempProgress;
            try {
                Thread.sleep(1);
                progressDialog.setProgress((int) progress);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < customerIds.size(); i++)
            mydb.updateCustomerGuaranteeRows(customerIds.get(i));
        deletedGuarantees = mydb.getCustomerDeletedGuaranteesRows();
        tempProgress = 20 / (double) customersTasksModel.size();
        format = String.format("%f", tempProgress);
        tempProgress = Double.parseDouble(format.replace(",", "."));
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                resourceID = getResources().getIdentifier("checkTasks", "string", packageName);
                SpannableString ss = new SpannableString(res.getString(resourceID));
                ss.setSpan(new RelativeSizeSpan(w * 0.08f / 100), 0, ss.length(), 0);
                progressDialog.setTitle(ss);
            }
        });
        if (customersTasksModel.size() == 0) {
            progress += 20;
            progressDialog.setProgress((int) progress);
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < customersTasksModel.size(); i++) {
            try {
                strDate = dateFormat.parse(customersTasksModel.get(i).date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (strDate != null) {
                if (customersTasksModel.get(i).date.equals(today)) {
                    String time;
                    Calendar mcurrentTime = Calendar.getInstance();
                    int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                    final int minute = mcurrentTime.get(Calendar.MINUTE);
                    if (minute < 10)
                        time = hour + ":0" + minute;
                    else
                        time = hour + ":" + minute;
                    currentTimeFormatted = Integer.parseInt(time.replace(":", ""));
                    if (customersTasksModel.get(i).time.trim().length() == 0)
                        taskTime = 99999;
                    else
                        taskTime = Integer.parseInt(customersTasksModel.get(i).time.replace(":", ""));
                }
                if (customersTasksModel.get(i).date.equals(today) && currentTimeFormatted <= taskTime) {
                    todayTasks++;
                    CustomersModel model = mydb.getCustomerById(customersTasksModel.get(i).id);
                    CustomersTasksExtendedModel temp = new CustomersTasksExtendedModel();
                    temp.id = customersTasksModel.get(i).id;
                    temp.name = model.name + " " + model.surname;
                    temp.phone1 = model.phone1;
                    temp.phone2 = model.phone2;
                    temp.city = model.city;
                    temp.address = model.address;
                    temp.taskRow = customersTasksModel.get(i).taskRow;
                    temp.date = customersTasksModel.get(i).date;
                    temp.time = customersTasksModel.get(i).time;
                    temp.details = customersTasksModel.get(i).details;
                    customersTasksTodayModel.add(temp);
                } else if (customersTasksModel.get(i).date.equals(tomorrow)) {
                    tomorrowTasks++;
                    CustomersModel model = mydb.getCustomerById(customersTasksModel.get(i).id);
                    CustomersTasksExtendedModel temp = new CustomersTasksExtendedModel();
                    temp.id = customersTasksModel.get(i).id;
                    temp.name = model.name + " " + model.surname;
                    temp.phone1 = model.phone1;
                    temp.phone2 = model.phone2;
                    temp.city = model.city;
                    temp.address = model.address;
                    temp.taskRow = customersTasksModel.get(i).taskRow;
                    temp.date = customersTasksModel.get(i).date;
                    temp.time = customersTasksModel.get(i).time;
                    temp.details = customersTasksModel.get(i).details;
                    customersTasksTomorrowModel.add(temp);
                } else if ((strDate.before(new Date()) && !(customersTasksModel.get(i).date.equals(today))) || (customersTasksModel.get(i).date.equals(today) && currentTimeFormatted > taskTime)) {
                    if (mydb.checkIfCustomerPendingTaskExist(customersTasksModel.get(i).id, customersTasksModel.get(i).taskRow) == 0) {
                        CustomersModel model = mydb.getCustomerById(customersTasksModel.get(i).id);
                        CustomersTasksExtendedModel temp = new CustomersTasksExtendedModel();
                        temp.id = customersTasksModel.get(i).id;
                        temp.name = model.name + " " + model.surname;
                        temp.phone1 = model.phone1;
                        temp.phone2 = model.phone2;
                        temp.city = model.city;
                        temp.address = model.address;
                        temp.taskRow = customersTasksModel.get(i).taskRow;
                        temp.date = customersTasksModel.get(i).date;
                        temp.time = customersTasksModel.get(i).time;
                        temp.details = customersTasksModel.get(i).details;
                        mydb.insertCustomerPendingTask(temp);
                    }
                }
                progress += tempProgress;
                try {
                    Thread.sleep(1);
                    progressDialog.setProgress((int) progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        pendingTasks = mydb.getCustomerPendingTaskRows();
    }

    public void permission_denied() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
        builder.setCancelable(false);
        builder.setTitle("Alert!");
        builder.setMessage("You must accept so that you get the appropriate permission to use the application!");
        builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission
                                .WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                        1);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CustomersDataOnStartup dbData = new CustomersDataOnStartup(mainActivity);
                    dbData.getCustomersData();
                } else {
                    permission_denied();
                }
                break;
        }
    }

    public class CustomersDataOnStartup extends SQLiteOpenHelper {

        public static final String DATABASE_NAME = "CustomersDB.db";

        public CustomersDataOnStartup(Context context) {
            super(context, DATABASE_NAME, null, 1);
        }

        public void getCustomersData() {
            db = this.getReadableDatabase();
            mydb.onCreate(db);
            progress = 0;
            new startUpTask().execute();
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }

    public class startUpTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                progressDialog.setProgress(0);
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            //customersGuaranteeModel Data
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resourceID = getResources().getIdentifier("checkCustomersGuarantees", "string", packageName);
                    SpannableString ss = new SpannableString(res.getString(resourceID).replace("1", "'"));
                    ss.setSpan(new RelativeSizeSpan(w * 0.08f / 100), 0, ss.length(), 0);
                    progressDialog.setTitle(ss);
                }
            });
            Cursor cursor = db.rawQuery("select * from customersGuarantee", null);
            long tableRows = DatabaseUtils.queryNumEntries(db, "customersGuarantee");
            double tempProgress = 20 / (double) tableRows;
            String format = String.format("%f", tempProgress);
            tempProgress = Double.parseDouble(format.replace(",", "."));
            if (tableRows == 0) {
                progress += 20;
                progressDialog.setProgress((int) progress);
                try {
                    Thread.sleep(1);
                    progressDialog.setProgress((int) progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                CustomersGuaranteeModel temp = new CustomersGuaranteeModel();
                temp.id = cursor.getInt(cursor.getColumnIndex("id"));
                temp.guaranteeRow = cursor.getInt(cursor.getColumnIndex("guaranteeRow"));
                temp.date = cursor.getString(cursor.getColumnIndex("date"));
                customersGuaranteeModel.add(temp);
                progress += tempProgress;
                try {
                    Thread.sleep(1);
                    progressDialog.setProgress((int) progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cursor.moveToNext();
            }

            //customersCreditDebtModel Data
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resourceID = getResources().getIdentifier("checkCustomersCreditDebts", "string", packageName);
                    SpannableString ss = new SpannableString(res.getString(resourceID).replace("1", "'"));
                    ss.setSpan(new RelativeSizeSpan(w * 0.08f / 100), 0, ss.length(), 0);
                    progressDialog.setTitle(ss);
                }
            });
            cursor = db.rawQuery("select * from customersCreditDebts", null);
            tableRows = DatabaseUtils.queryNumEntries(db, "customersCreditDebts");
            tempProgress = 20 / (double) tableRows;
            format = String.format("%f", tempProgress);
            tempProgress = Double.parseDouble(format.replace(",", "."));
            if (tableRows == 0) {
                progress += 20;
                progressDialog.setProgress((int) progress);
                try {
                    Thread.sleep(1);
                    progressDialog.setProgress((int) progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                CustomersCreditDebtModel temp = new CustomersCreditDebtModel();
                temp.id = cursor.getInt(cursor.getColumnIndex("id"));
                temp.creditDebtRow = cursor.getInt(cursor.getColumnIndex("creditDebtRow"));
                temp.name = cursor.getString(cursor.getColumnIndex("name"));
                temp.surname = cursor.getString(cursor.getColumnIndex("surname"));
                temp.date = cursor.getString(cursor.getColumnIndex("date"));
                temp.oldDebt = cursor.getDouble(cursor.getColumnIndex("oldDebt"));
                temp.creditDebt = cursor.getDouble(cursor.getColumnIndex("creditDebt"));
                temp.currentDebt = cursor.getDouble(cursor.getColumnIndex("currentDebt"));
                temp.customerId = cursor.getInt(cursor.getColumnIndex("customerId"));
                customersCreditDebtModel.add(temp);
                progress += tempProgress;
                try {
                    Thread.sleep(1);
                    progressDialog.setProgress((int) progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cursor.moveToNext();
            }

            //customersTasksModel Data
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resourceID = getResources().getIdentifier("checkCustomersTasks", "string", packageName);
                    SpannableString ss = new SpannableString(res.getString(resourceID).replace("1", "'"));
                    ss.setSpan(new RelativeSizeSpan(w * 0.08f / 100), 0, ss.length(), 0);
                    progressDialog.setTitle(ss);
                }
            });
            tableRows = DatabaseUtils.queryNumEntries(db, "customersTask");
            tempProgress = 20 / (double) tableRows;
            format = String.format("%f", tempProgress);
            tempProgress = Double.parseDouble(format.replace(",", "."));
            cursor = db.rawQuery("select * from customersTask", null);
            if (tableRows == 0) {
                progress += 20;
                progressDialog.setProgress((int) progress);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            cursor.moveToFirst();
            while (cursor.isAfterLast() == false) {
                CustomersTasksModel temp = new CustomersTasksModel();
                temp.id = cursor.getInt(cursor.getColumnIndex("id"));
                temp.taskRow = cursor.getInt(cursor.getColumnIndex("taskRow"));
                temp.date = cursor.getString(cursor.getColumnIndex("date"));
                temp.time = cursor.getString(cursor.getColumnIndex("time"));
                temp.details = cursor.getString(cursor.getColumnIndex("details"));
                customersTasksModel.add(temp);
                progress += tempProgress;
                try {
                    Thread.sleep(1);
                    progressDialog.setProgress((int) progress);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cursor.moveToNext();
            }
            checkCustomersDetails();

            //customersModel Data
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    resourceID = getResources().getIdentifier("checkCustomersData", "string", packageName);
                    SpannableString ss = new SpannableString(res.getString(resourceID).replace("1", "'"));
                    ss.setSpan(new RelativeSizeSpan(w * 0.08f / 100), 0, ss.length(), 0);
                    progressDialog.setTitle(ss);
                }
            });
            tableRows = DatabaseUtils.queryNumEntries(db, "personalNotes");
            if (tableRows == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            personalNotesModel = mydb.getPersonalNotes();
            tableRows = DatabaseUtils.queryNumEntries(db, "customers");
            if (tableRows == 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            customersModel = mydb.getCustomers();

            progressDialog.setProgress(100);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {
            final Handler handler = new Handler();
            Timer t = new Timer();
            timerTask = new TimerTask() {
                public void run() {
                    handler.post(new Runnable() {
                        public void run() {
                            getNotifications();
                        }
                    });
                }
            };

            // public void schedule (TimerTask task, long delay, long period)
            t.schedule(timerTask, 0, 3000);  //
            translateMenu();
            progressDialog.dismiss();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    public static class loadingTask extends AsyncTask<Integer, Integer, String> {
        @Override
        protected String doInBackground(Integer... params) {
            try {
                Thread.sleep(200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            scrollViewTable.fullScroll(ScrollView.FOCUS_UP);
            horizontalScrollView.fullScroll(HorizontalScrollView.FOCUS_LEFT);
            if (itemSelectedId == 0)
                getCustomersTasks();
            else {
                if (itemSelectedId == R.id.nav_customer_search || itemSelectedId == R.id.nav_customer_edit) {
                    searchEditCustomer();
                } else if (itemSelectedId == R.id.nav_tasks_today || itemSelectedId == R.id.nav_tasks_tomorrow || itemSelectedId == R.id.nav_pending_tasks) {
                    getCustomersTasks();
                } else if (itemSelectedId == R.id.nav_customer_deleted_guarantees) {
                    customerDeletedGuarantees();
                } else if (itemSelectedId == R.id.nav_customer_payment_history) {
                    customerPaymentHistory();
                } else if (itemSelectedId == R.id.nav_notes) {
                    getPersonalNotes();
                } else {
                    customerDataSearch();
                }
            }
            return "Task Completed.";
        }

        @Override
        protected void onPostExecute(String result) {
            if (!(itemSelectedId == R.id.nav_customer_deleted_guarantees || itemSelectedId == R.id.nav_tasks_today || itemSelectedId == R.id.nav_tasks_tomorrow || itemSelectedId == R.id.nav_pending_tasks || itemSelectedId == R.id.nav_notes || itemSelectedId == 0)) {
                createSearchViewAndSpinner();
            }
            progressBar.setVisibility(View.INVISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            relativeLayout.addView(horizontalScrollView);
        }

        @Override
        protected void onPreExecute() {
            horizontalRelativeLayout.addView(scrollViewTable);
            relativeLayout.addView(progressBar);
            progressBar.setVisibility(View.VISIBLE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {

        }
    }

    public void getNotifications() {
        if (!dayStarted.equals(dateFormat.format(new Date()))) {
            if (timerTask != null) {
                timerTask.cancel();
                timerTask = null;
                AlertDialog.Builder builder = new AlertDialog.Builder(mainActivity);
                builder.setCancelable(false);
                resourceID = mainActivity.getResources().getIdentifier("restart", "string", packageName);
                builder.setMessage(res.getString(resourceID));
                builder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent mStartActivity = new Intent(MainActivity.this, SplashActivity.class);
                        int mPendingIntentId = 123456;
                        PendingIntent mPendingIntent = PendingIntent.getActivity(MainActivity.this, mPendingIntentId, mStartActivity,
                                PendingIntent.FLAG_CANCEL_CURRENT);
                        AlarmManager mgr = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);
                        System.exit(0);
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        } else {
            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> services = activityManager
                    .getRunningTasks(Integer.MAX_VALUE);
            boolean isActivityFound = false;

            if (services.get(0).topActivity.getPackageName().toString()
                    .equalsIgnoreCase(getApplicationContext().getPackageName().toString())) {
                isActivityFound = true;
            }
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if (!isActivityFound || !isScreenOn) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                String time;
                if (minute < 10)
                    time = hour + ":0" + minute;
                else
                    time = hour + ":" + minute;
                for (int i = 0; i < customersTasksTodayModel.size(); i++) {
                    if (customersTasksTodayModel.get(i).time.equals(time) && !prevTime.equals(time)) {
                        notificationTaskCounterTemp++;
                        mBuilder =
                                new NotificationCompat.Builder(mainActivity);
                        NotificationCompat.InboxStyle inboxStyle =
                                new NotificationCompat.InboxStyle();
                        int id = customersTasksTodayModel.get(i).id;
                        CustomersModel model = mydb.getCustomerById(id);
                        CustomersTasksExtendedModel temp = new CustomersTasksExtendedModel();
                        temp.id = customersTasksTodayModel.get(i).id;
                        temp.name = model.name + " " + model.surname;
                        temp.phone1 = model.phone1;
                        temp.phone2 = model.phone2;
                        temp.city = model.city;
                        temp.address = model.address;
                        temp.taskRow = customersTasksTodayModel.get(i).taskRow;
                        temp.time = customersTasksTodayModel.get(i).time;
                        temp.details = customersTasksTodayModel.get(i).details;
                        customersNotificationTasksTempModel.add(temp);
                        events.add(model.name + " " + model.surname);
                        for (int j = 0; j < events.size(); j++) {
                            inboxStyle.addLine(events.get(j));
                        }
                        Intent intent = new Intent(context, NotificationReceiver.class);
                        intent.setAction("click");
                        PendingIntent pendingIntent =
                                PendingIntent.getBroadcast(context.getApplicationContext(),
                                        547, intent, PendingIntent.FLAG_ONE_SHOT);
                        mBuilder.setSmallIcon(R.drawable.ic_menu_customer_task);
                        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getApplicationContext().getResources(),
                                R.mipmap.ic_launcher));
                        resourceID = mainActivity.getResources().getIdentifier("tasks", "string", packageName);
                        mBuilder.setContentTitle(res.getString(resourceID) + " (" + notificationTaskCounterTemp + ")");
                        mBuilder.setContentText(model.name + " " + model.surname);
                        mBuilder.setStyle(inboxStyle);
                        mBuilder.setContentIntent(pendingIntent);
                        mBuilder.setDeleteIntent(createOnDismissedIntent(context, 547));
                        mNotificationManager =

                                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

                        mNotificationManager.notify(547, mBuilder.build());
                        prevTime = time;
                    }
                }
            }
        }
    }

    public PendingIntent createOnDismissedIntent(Context context, int notificationId) {
        Intent intent = new Intent(context, NotificationReceiver.class);
        intent.setAction("swipe");
        PendingIntent pendingIntent =
                PendingIntent.getBroadcast(context.getApplicationContext(),
                        notificationId, intent, PendingIntent.FLAG_ONE_SHOT);
        return pendingIntent;
    }

    public static class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action_name = intent.getAction();
            events = new ArrayList<String>();
            notificationTaskCounter = notificationTaskCounterTemp;
            notificationTaskCounterTemp = 0;
            if (action_name.equals("click")) {
                customersNotificationTasksModel = customersNotificationTasksTempModel;
                customersNotificationTasksTempModel = new ArrayList<CustomersTasksExtendedModel>();
                mNotificationManager.cancelAll();
                getNotificationTasks();
            } else
                customersNotificationTasksTempModel = new ArrayList<CustomersTasksExtendedModel>();
        }
    }

    public void translateMenu() {
        for (int i = 0; i < menu.size(); i++) {
            if (i == 0) {
                menu.getItem(i).setTitle(res.getString(R.string.home));
            } else {
                menu.getItem(i).setTitle(menuXML[i - 1]);
                for (int j = 0; j < menu.getItem(i).getSubMenu().size(); j++) {
                    if (i == 1)
                        menu.getItem(i).getSubMenu().getItem(j).setTitle(addSearchSubmenuXML[j]);
                    else if (i == 2)
                        menu.getItem(i).getSubMenu().getItem(j).setTitle(editSubmenuXML[j]);
                    else if (i == 3) {
                        if (j == 0)
                            menu.getItem(i).getSubMenu().getItem(j).setTitle(notificationsSubmenuXML[j] + " (" + todayTasks + ")");
                        else if (j == 1)
                            menu.getItem(i).getSubMenu().getItem(j).setTitle(notificationsSubmenuXML[j] + " (" + tomorrowTasks + ")");
                        else if (j == 2)
                            menu.getItem(i).getSubMenu().getItem(j).setTitle(notificationsSubmenuXML[j] + " (" + pendingTasks + ")");
                        else
                            menu.getItem(i).getSubMenu().getItem(j).setTitle(notificationsSubmenuXML[j]);
                    } else {
                        if (j == 0)
                            menu.getItem(i).getSubMenu().getItem(j).setTitle(historySubmenuXML[j] + " (" + deletedGuarantees + ")");
                        else
                            menu.getItem(i).getSubMenu().getItem(j).setTitle(historySubmenuXML[j]);
                    }
                }
            }
        }
    }
}
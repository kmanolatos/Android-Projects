package com.manolatostech.apppromoter;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;

import java.util.Timer;
import java.util.TimerTask;

public class LogOutActivity extends AppCompatActivity {
    TextView toastMessage, text;
    Toast toast;
    EditText user, pass;
    ProgressBar progressBar;
    int h, w;
    LogOutActivity logOutActivity;
    RelativeLayout relativeLayout;
    TimerTask timerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logout_activity);
        getWindow().getDecorView().setBackgroundColor(Color.parseColor("#0193D7"));
        logOutActivity = this;
        MobileAds.initialize(this, getString(R.string.admob_app_id));
        Display display = getWindowManager().getDefaultDisplay();
        w = display.getWidth();
        h = display.getHeight();
        relativeLayout = (RelativeLayout) findViewById(R.id.rl);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.getLayoutParams().height = h * 95 / 100;
        progressBar.setVisibility(View.VISIBLE);
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
        if (!Helper.isOnline(getApplicationContext())) {
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
                            if (Helper.isOnline(getApplicationContext())) {
                                timerTask.cancel();
                                timerTask = null;
                                MyDbHelper dbData = new MyDbHelper(logOutActivity);
                                dbData.check();
                            }
                        }
                    });
                }
            };

            // public void schedule (TimerTask task, long delay, long period)
            t.schedule(timerTask, 0, 1000);  //
        } else {
            MyDbHelper dbData = new MyDbHelper(logOutActivity);
            dbData.check();
        }
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
                cursor.moveToNext();
            }
            new LogOutNow(logOutActivity, toastMessage, h, progressBar, relativeLayout).execute(String.valueOf(id));
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {

        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }
    }
}

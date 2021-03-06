package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 12/1/2018.
 */

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class LogOut extends AsyncTask<String, Void, String> {
    TextView toastMessage;
    Toast toast;
    HomeActivity homeActivity;
    int h, mode;
    long userId;
    ProgressBar progressBar;
    DrawerLayout drawer;
    RelativeLayout relativeLayout;

    public LogOut(HomeActivity homeActivity, TextView toastMessage, int h, ProgressBar progressBar, RelativeLayout relativeLayout, DrawerLayout drawer, int mode) {
        this.homeActivity = homeActivity;
        this.h = h;
        this.mode = mode;
        this.toastMessage = toastMessage;
        this.progressBar = progressBar;
        this.drawer = drawer;
        this.relativeLayout = relativeLayout;
    }

    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
        toast = Toast.makeText(homeActivity, null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String id = (String) arg0[0];
            userId = Long.valueOf(id);
            String link = "http://manolatostechapppromoter.atwebpages.com/logOut.php";
            String data = URLEncoder.encode("id", "UTF-8") + "=" +
                    URLEncoder.encode(id, "UTF-8");
            URL url = new URL(link);
            URLConnection conn = url.openConnection();

            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

            wr.write(data);
            wr.flush();

            BufferedReader reader = new BufferedReader(new
                    InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                break;
            }

            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        progressBar.setVisibility(View.INVISIBLE);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        relativeLayout.removeAllViews();
        if (!result.equals("ok")) {
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("An error occured!");
        } else {
            DBHelper mydb = new DBHelper(homeActivity);
            mydb.delete(userId);
            if (mode == 0) {
                toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
                toastMessage.setText("You have been logout successfully!");
            } else {
                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                toastMessage.setText("Only one user can login to each account!");
            }
            Intent intent = new Intent(homeActivity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            homeActivity.startActivity(intent);
        }
        toast.setView(toastMessage);
        toast.show();
    }
}
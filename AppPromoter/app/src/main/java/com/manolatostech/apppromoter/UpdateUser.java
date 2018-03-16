package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 12/1/2018.
 */

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.Menu;
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

public class UpdateUser extends AsyncTask<String, Void, String> {
    TextView toastMessage;
    Toast toast;
    HomeActivity homeActivity;
    int h, mode;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    RelativeLayout[] tabs;
    String userId, name, surname, email, emailText, pass;
    UserModel userModel;
    Menu menu;
    DrawerLayout drawer;

    public UpdateUser(HomeActivity homeActivity, TextView toastMessage, int h, ProgressBar progressBar, UserModel userModel, RelativeLayout relativeLayout, RelativeLayout[] tabs, Menu menu, String emailText, DrawerLayout drawer) {
        this.homeActivity = homeActivity;
        this.h = h;
        this.toastMessage = toastMessage;
        this.progressBar = progressBar;
        this.userModel = userModel;
        this.relativeLayout = relativeLayout;
        this.tabs = tabs;
        this.menu = menu;
        this.emailText = emailText;
        this.drawer = drawer;
    }

    protected void onPreExecute() {
        toast = Toast.makeText(homeActivity, null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        progressBar.setVisibility(View.VISIBLE);
        tabs[0].setVisibility(View.GONE);
        tabs[1].setVisibility(View.GONE);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String link = "", data;
            userId = (String) arg0[0];
            if (arg0.length == 4) {
                mode = 0;
                name = (String) arg0[1];
                surname = (String) arg0[2];
                email = (String) arg0[3];
                if (emailText.equals(email))
                    email = "0";
                link = "http://manolatostechapppromoter.atwebpages.com/UpdateUser.php";
                data = URLEncoder.encode("userId", "UTF-8") + "=" +
                        URLEncoder.encode(userId, "UTF-8");
                data += "&" + URLEncoder.encode("name", "UTF-8") + "=" +
                        URLEncoder.encode(name, "UTF-8");
                data += "&" + URLEncoder.encode("surname", "UTF-8") + "=" +
                        URLEncoder.encode(surname, "UTF-8");
                data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                        URLEncoder.encode(email, "UTF-8");
            } else {
                mode = 1;
                pass = (String) arg0[1];
                link = "http://manolatostechapppromoter.atwebpages.com/changePassword.php";
                data = URLEncoder.encode("userId", "UTF-8") + "=" +
                        URLEncoder.encode(userId, "UTF-8");
                data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" +
                        URLEncoder.encode(pass, "UTF-8");
            }
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
        String currentEmailText = null;
        relativeLayout.removeAllViews();
        relativeLayout.addView(progressBar);
        progressBar.setVisibility(View.INVISIBLE);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if (mode == 0) {
            userModel.name = name;
            userModel.surname = surname;
        }
        if (result.equals("Exists")) {
            currentEmailText = emailText;
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("Email already exists!");
        } else if (result.equals("Error")) {
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("An error occured!");
        } else {
            if (mode == 0) {
                userModel.email = emailText;
                String userName = userModel.name + " " + userModel.surname;
                menu.getItem(0).setTitle(userName);
            }
            else
            {
                DBHelper mydb = new DBHelper(homeActivity);
                mydb.updatePass(Long.parseLong(userId), pass);
            }
            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
            toastMessage.setText("Changed successfully!");
        }
        if (email != "0" && mode == 0) {
            userModel.email = email;
        }
        homeActivity.changeInformation(currentEmailText);
        toast.setView(toastMessage);
        toast.show();
    }

}
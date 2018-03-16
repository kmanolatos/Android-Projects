package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 11/1/2018.
 */

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;

public class InsertApplication extends AsyncTask<String, Void, String> {
    TextView toastMessage;
    Toast toast;
    EditText link, name;
    Button btn;
    int h;
    ProgressBar progressBar;
    ArrayList<ApplicationModel> myApps;
    HomeActivity homeActivity;
    String appPackage;
    DrawerLayout drawer;

    public InsertApplication(HomeActivity homeActivity, TextView toastMessage, int h, ProgressBar progressBar, ArrayList<ApplicationModel> myApps, EditText name, EditText link, Button btn, DrawerLayout drawer) {
        this.homeActivity = homeActivity;
        this.h = h;
        this.toastMessage = toastMessage;
        this.progressBar = progressBar;
        this.link = link;
        this.name = name;
        this.btn = btn;
        this.myApps = myApps;
        this.drawer = drawer;
    }

    protected void onPreExecute() {
        toast = Toast.makeText(homeActivity, null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        progressBar.setVisibility(View.VISIBLE);
        link.setVisibility(View.INVISIBLE);
        name.setVisibility(View.INVISIBLE);
        btn.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String appName = (String) arg0[0];
            String appLink = (String) arg0[1];
            String userId = (String) arg0[2];
            appPackage = link.getText().toString().substring(link.getText().toString().indexOf("=")).replace("=", "");
            String link = "http://manolatostechapppromoter.atwebpages.com/insertApplication.php";
            String data = URLEncoder.encode("appName", "UTF-8") + "=" +
                    URLEncoder.encode(appName, "UTF-8");
            data += "&" + URLEncoder.encode("link", "UTF-8") + "=" +
                    URLEncoder.encode(appLink, "UTF-8");
            data += "&" + URLEncoder.encode("package", "UTF-8") + "=" +
                    URLEncoder.encode(appPackage, "UTF-8");
            data += "&" + URLEncoder.encode("userId", "UTF-8") + "=" +
                    URLEncoder.encode(userId, "UTF-8");

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
        link.setVisibility(View.VISIBLE);
        name.setVisibility(View.VISIBLE);
        btn.setVisibility(View.VISIBLE);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        if (result.equals("App is already published!")) {
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("App is already published!");
        } else if (result.equals("error")) {
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("An error occured!");
        } else {
            ApplicationModel temp = new ApplicationModel();
            temp.appId = Long.parseLong(result);
            temp.appLink = link.getText().toString();
            temp.appName = name.getText().toString();
            myApps.add(temp);
            name.requestFocus();
            link.setText("");
            name.setText("");
            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
            toastMessage.setText("Uploaded successfully!");
        }
        toast.setView(toastMessage);
        toast.show();
    }
}

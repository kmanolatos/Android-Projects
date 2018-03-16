package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 12/1/2018.
 */

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.ArrayList;

public class UpdateApplication extends AsyncTask<String, Void, String> {
    TextView toastMessage;
    Toast toast;
    HomeActivity homeActivity;
    int h, index;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    ArrayList<ApplicationModel> myApps;
    ImageView[] imageViewLink, imageViewEdit, imageViewDelete;
    EditText[] editTextArray;
    String appName;
    DrawerLayout drawer;

    public UpdateApplication(HomeActivity homeActivity, TextView toastMessage, int h, ProgressBar progressBar, ArrayList<ApplicationModel> myApps, int index, RelativeLayout relativeLayout, EditText[] editTextArray, ImageView[] imageViewLink, ImageView[] imageViewEdit, ImageView[] imageViewDelete, DrawerLayout drawer) {
        this.homeActivity = homeActivity;
        this.h = h;
        this.toastMessage = toastMessage;
        this.progressBar = progressBar;
        this.myApps = myApps;
        this.index = index;
        this.relativeLayout = relativeLayout;
        this.editTextArray = editTextArray;
        this.imageViewLink = imageViewLink;
        this.imageViewEdit = imageViewEdit;
        this.imageViewDelete = imageViewDelete;
        this.drawer = drawer;
    }

    protected void onPreExecute() {
        toast = Toast.makeText(homeActivity, null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        progressBar.setVisibility(View.VISIBLE);
        for (int i = 0; i < editTextArray.length; i++) {
            editTextArray[i].setVisibility(View.INVISIBLE);
            imageViewLink[i].setVisibility(View.INVISIBLE);
            imageViewEdit[i].setVisibility(View.INVISIBLE);
            imageViewDelete[i].setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String id = (String) arg0[0];
            appName = (String) arg0[1];
            String link = "http://manolatostechapppromoter.atwebpages.com/updateApplication.php";
            String data = URLEncoder.encode("id", "UTF-8") + "=" +
                    URLEncoder.encode(id, "UTF-8");
            data += "&" + URLEncoder.encode("appName", "UTF-8") + "=" +
                    URLEncoder.encode(appName, "UTF-8");


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
        if (!result.equals("ok")) {
            for (int i = 0; i < editTextArray.length; i++) {
                editTextArray[i].setVisibility(View.VISIBLE);
                imageViewLink[i].setVisibility(View.VISIBLE);
                imageViewEdit[i].setVisibility(View.VISIBLE);
                imageViewDelete[i].setVisibility(View.VISIBLE);
            }
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("An error occured!");
        } else {
            myApps.get(index).appName = appName;
            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
            toastMessage.setText("App name changed successfully!");
            relativeLayout.removeAllViews();
            relativeLayout.addView(progressBar);
            new HomeActivity.loadingTask().execute();
        }
        toast.setView(toastMessage);
        toast.show();
    }
}
package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 10/1/2018.
 */

import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.ProgressBar;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class GetStatistics extends AsyncTask<String, Void, String> {
    ProgressBar progressBar;
    ArrayList<ApplicationModel> statistics;
    HomeActivity homeActivity;
    DrawerLayout drawer;
    public GetStatistics(HomeActivity homeActivity, ProgressBar progressBar, ArrayList<ApplicationModel> statistics, DrawerLayout drawer) {
        this.progressBar = progressBar;
        this.statistics = statistics;
        this.homeActivity = homeActivity;
        this.drawer = drawer;
    }

    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String id = (String) arg0[0];
            String link = "http://manolatostechapppromoter.atwebpages.com/getStatistics.php?userId=" + id;

            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(link));
            HttpResponse response = client.execute(request);
            BufferedReader in = new BufferedReader(new
                    InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onPostExecute(String result) {
        if (result.trim().length() != 0) {
            String[] parts = result.split(Pattern.quote("$"));
            for (int i = 1; i < parts.length; i = i + 3) {
                ApplicationModel temp = new ApplicationModel();
                temp.appName = parts[i];
                temp.downloads = Long.parseLong(parts[i + 1]);
                temp.appLink = parts[i + 2];
                statistics.add(temp);
            }
        }
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        progressBar.setVisibility(View.INVISIBLE);
        homeActivity.getStatistics();
    }

}
package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 10/1/2018.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GetApplicationsByUserId extends AsyncTask<String, Void, String> {
    Long userId;
    ProgressBar progressBar;
    Context context;
    ArrayList<ApplicationModel> apps;
    UserModel userModel;
    TextView toastMessage, text;
    int h, mode;
    EditText user, pass;
    Button logIn, register, forgot, logInFb;

    public GetApplicationsByUserId(Context context, UserModel userModel, int h, ProgressBar progressBar, TextView toastMessage, EditText user, EditText pass, Button logIn, Button register, Button forgot, TextView text, Button logInFb, ArrayList<ApplicationModel> apps, int mode) {
        this.userModel = userModel;
        this.userId = userModel.id;
        this.progressBar = progressBar;
        this.context = context;
        this.apps = apps;
        this.toastMessage = toastMessage;
        this.h = h;
        this.mode = mode;
        this.user = user;
        this.pass = pass;
        this.logIn = logIn;
        this.register = register;
        this.forgot = forgot;
        this.logInFb = logInFb;
        this.text = text;
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String id = (String) arg0[0];
            String link = "http://manolatostechapppromoter.atwebpages.com/getApplicationsByUserId.php?userId=" + id;

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
        ArrayList<ApplicationModel> myApps = new ArrayList<ApplicationModel>();
        if (result.trim().length() != 0) {
            String[] parts = result.split(Pattern.quote("$"));
            for (int i = 1; i < parts.length; i = i + 3) {
                ApplicationModel temp = new ApplicationModel();
                temp.userId = userId;
                temp.appId = Long.parseLong(parts[i]);
                temp.appName = parts[i + 1];
                temp.appLink = parts[i + 2];
                myApps.add(temp);
            }
        }
            new LogIn(context, userModel, h, progressBar, toastMessage, user, pass, logIn, register, forgot, text, logInFb, apps, myApps, mode).execute(userId.toString());
    }
}
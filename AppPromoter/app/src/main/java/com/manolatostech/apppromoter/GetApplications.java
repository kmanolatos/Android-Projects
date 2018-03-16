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
import android.os.AsyncTask;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

public class GetApplications extends AsyncTask<String, Void, String> {
    Long userId;
    ProgressBar progressBar;
    Context context;
    UserModel userModel;
    TextView toastMessage, text;
    int h, mode;
    EditText user, pass;
    Button logIn, register, forgot, logInFb;
    public GetApplications(Context context, UserModel userModel, int h, ProgressBar progressBar, TextView toastMessage, EditText user, EditText pass, Button logIn, Button register, Button forgot, TextView text, Button logInFb, int mode) {
        this.userModel = userModel;
        this.userId = userModel.id;
        this.progressBar = progressBar;
        this.context = context;
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
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String id = (String) arg0[0];
            String link = "http://manolatostechapppromoter.atwebpages.com/getApplications.php?userId=" + id;

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
        ArrayList<ApplicationModel> apps = new ArrayList<ApplicationModel>();
        if (result.trim().length() != 0) {
            String[] parts = result.split(Pattern.quote("$"));
            for (int i = 1; i < parts.length; i = i + 5) {
                ApplicationModel temp = new ApplicationModel();
                temp.userId = Long.parseLong(parts[i]);
                temp.appId = Long.parseLong(parts[i + 1]);
                temp.appName = parts[i + 2];
                temp.email = parts[i + 3];
                temp.appLink = parts[i + 4];
                apps.add(temp);
            }
        }
        new GetApplicationsByUserId(context, userModel, h, progressBar, toastMessage, user, pass, logIn, register, forgot, text, logInFb, apps, mode).execute(userId.toString());
    }

}
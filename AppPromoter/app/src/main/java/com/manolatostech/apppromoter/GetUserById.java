package com.manolatostech.apppromoter;


/**
 * Created by kmanolatos on 10/1/2018.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
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

public class GetUserById extends AsyncTask<String, Void, String> {
    ProgressBar progressBar;
    Context context;
    TextView toastMessage, text;
    int h, mode;
    EditText user, pass;
    Button logIn, register, forgot, logInFb;
    String id, currentVersion;

    public GetUserById(Context context, int h, ProgressBar progressBar, TextView toastMessage, EditText user, EditText pass, Button logIn, Button register, Button forgot, TextView text,Button logInFb, int mode, String currentVersion) {
        this.context = context;
        this.h = h;
        this.mode = mode;
        this.progressBar = progressBar;
        this.toastMessage = toastMessage;
        this.user = user;
        this.pass = pass;
        this.logIn = logIn;
        this.register = register;
        this.forgot = forgot;
        this.logInFb = logInFb;
        this.text = text;
        this.currentVersion = currentVersion;
    }

    protected void onPreExecute() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            id = (String) arg0[0];
            String link = "http://manolatostechapppromoter.atwebpages.com/getUserById.php?id=" + id;
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
        String[] parts = result.split(Pattern.quote("$"));
        UserModel userModel = new UserModel();
        userModel.id = Long.parseLong(id);
        userModel.name = parts[0];
        userModel.surname = parts[1];
        userModel.email = parts[2];
        userModel.points = Long.parseLong(parts[3]);
        userModel.user = user.getText().toString();
        userModel.pass = pass.getText().toString();
        userModel.version = currentVersion;
        new GetApplications(context, userModel, h, progressBar, toastMessage, user, pass, logIn, register, forgot, text, logInFb, mode).execute(id.toString());
    }

}
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
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class SigningActivity extends AsyncTask<String, Void, String> {
    TextView toastMessage, text;
    Toast toast;
    Context context;
    EditText user, pass;
    Button logIn, register, forgot, logInFb;
    int h, mode;
    ProgressBar progressBar;
    String id, currentVersion;

    public SigningActivity(Context context, TextView toastMessage, int h, ProgressBar progressBar, EditText user, EditText pass, Button logIn, Button register, Button forgot, TextView text, Button logInFb, int mode, String currentVersion) {
        this.context = context;
        this.h = h;
        this.mode = mode;
        this.toastMessage = toastMessage;
        this.progressBar = progressBar;
        this.user = user;
        this.pass = pass;
        this.logIn = logIn;
        this.register = register;
        this.forgot = forgot;
        this.text = text;
        this.logInFb = logInFb;
        this.currentVersion = currentVersion;
    }

    protected void onPreExecute() {
        toast = Toast.makeText(context, null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        progressBar.setVisibility(View.VISIBLE);
        user.setVisibility(View.INVISIBLE);
        pass.setVisibility(View.INVISIBLE);
        logIn.setVisibility(View.INVISIBLE);
        register.setVisibility(View.INVISIBLE);
        forgot.setVisibility(View.INVISIBLE);
        text.setVisibility(View.INVISIBLE);
        logInFb.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String doInBackground(String... arg0) {
        if (mode == 0) {
            try {
                String username = (String) arg0[0];
                String password = (String) arg0[1];
                String link = "http://manolatostechapppromoter.atwebpages.com/checkCred.php?user=" + username + "&pass=" + password;

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
        } else
            return "ok";
    }

    @Override
    protected void onPostExecute(String result) {
        if (mode == 0) {
            if (result.equals("not found")) {
                progressBar.setVisibility(View.INVISIBLE);
                user.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);
                logIn.setVisibility(View.VISIBLE);
                register.setVisibility(View.VISIBLE);
                forgot.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                logInFb.setVisibility(View.VISIBLE);
                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                toastMessage.setText("Username or Password is wrong!");
                toast.setView(toastMessage);
                toast.show();
            } else if (result.equals("error")) {
                progressBar.setVisibility(View.INVISIBLE);
                user.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);
                logIn.setVisibility(View.VISIBLE);
                register.setVisibility(View.VISIBLE);
                forgot.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                logInFb.setVisibility(View.VISIBLE);
                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                toastMessage.setText("An error occured!");
                toast.setView(toastMessage);
                toast.show();
            } else if (result.equals("already logged")) {
                progressBar.setVisibility(View.INVISIBLE);
                user.setVisibility(View.VISIBLE);
                pass.setVisibility(View.VISIBLE);
                logIn.setVisibility(View.VISIBLE);
                register.setVisibility(View.VISIBLE);
                forgot.setVisibility(View.VISIBLE);
                text.setVisibility(View.VISIBLE);
                logInFb.setVisibility(View.VISIBLE);
                toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
                toastMessage.setText("Only one user can log in to each account!");
                toast.setView(toastMessage);
                toast.show();
            } else {
                id = result;
                new GetUserById(context, h, progressBar, toastMessage, user, pass, logIn, register, forgot, text, logInFb, mode, currentVersion).execute(id);
            }
        } else {
            id = String.valueOf(mode);
            new GetUserById(context, h, progressBar, toastMessage, user, pass, logIn, register, forgot, text, logInFb, mode, currentVersion).execute(id);
        }
    }
}
package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 12/1/2018.
 */

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import android.widget.Button;
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

public class LogIn extends AsyncTask<String, Void, String> {
    Toast toast;
    ProgressBar progressBar;
    Context context;
    ArrayList<ApplicationModel> apps, myApps;
    UserModel userModel;
    TextView toastMessage, text;
    int h, mode;
    EditText user, pass;
    Button logIn, register, forgot, logInFb;

    public LogIn(Context context, UserModel userModel, int h, ProgressBar progressBar, TextView toastMessage, EditText user, EditText pass, Button logIn, Button register, Button forgot, TextView text, Button logInFb, ArrayList<ApplicationModel> apps, ArrayList<ApplicationModel> myApps, int mode) {
        this.context = context;
        this.userModel = userModel;
        this.apps = apps;
        this.progressBar = progressBar;
        this.myApps = myApps;
        this.myApps = myApps;
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
        toast = Toast.makeText(context, null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String id = (String) arg0[0];
            String link = "";
            if (mode == 0)
                link = "http://manolatostechapppromoter.atwebpages.com/logIn.php";
            else
                link = "http://manolatostechapppromoter.atwebpages.com/getLogIn.php";
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
        if (result.equals("Error")) {
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
        } else {
            Intent intent = new Intent(context, HomeActivity.class);
            Bundle b = new Bundle();
            b.putSerializable("userModel", userModel);
            b.putSerializable("myApps", myApps);
            b.putSerializable("apps", apps);
            intent.putExtras(b);
            intent.putExtra("logIn", Integer.parseInt(result));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }
    }
}

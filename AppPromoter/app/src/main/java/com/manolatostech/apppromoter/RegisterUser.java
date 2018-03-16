package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 12/1/2018.
 */

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
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

public class RegisterUser extends AsyncTask<String, Void, String> {
    TextView toastMessage;
    Toast toast;
    RegisterActivity registerActivity;
    ProgressBar progressBar;
    int h;
    EditText name, surname, user, pass, confPass, email;
    Button register;
    public RegisterUser(RegisterActivity registerActivity, TextView toastMessage, int h, ProgressBar progressBar, EditText name, EditText surname, EditText user, EditText pass, EditText confPass, EditText email, Button register) {
        this.registerActivity = registerActivity;
        this.progressBar = progressBar;
        this.h = h;
        this.toastMessage = toastMessage;
        this.name = name;
        this.surname = surname;
        this.user = user;
        this.pass = pass;
        this.confPass = confPass;
        this.email = email;
        this.register = register;
    }

    protected void onPreExecute() {
        toast = Toast.makeText(registerActivity, null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        progressBar.setVisibility(View.VISIBLE);
        name.setVisibility(View.INVISIBLE);
        surname.setVisibility(View.INVISIBLE);
        user.setVisibility(View.INVISIBLE);
        pass.setVisibility(View.INVISIBLE);
        confPass.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);
        register.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String name = (String) arg0[0];
            String surname = (String) arg0[1];
            String user = (String) arg0[2];
            String pass = (String) arg0[3];
            String email = (String) arg0[4];
            String androidId = (String) arg0[5];
            String link = "http://manolatostechapppromoter.atwebpages.com/registerUser.php";
            String data = URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("surname", "UTF-8") + "=" +
                    URLEncoder.encode(surname, "UTF-8");
            data += "&" + URLEncoder.encode("user", "UTF-8") + "=" +
                    URLEncoder.encode(user, "UTF-8");
            data += "&" + URLEncoder.encode("pass", "UTF-8") + "=" +
                    URLEncoder.encode(pass, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("androidId", "UTF-8") + "=" +
                    URLEncoder.encode(androidId, "UTF-8");

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
        name.setVisibility(View.VISIBLE);
        surname.setVisibility(View.VISIBLE);
        user.setVisibility(View.VISIBLE);
        pass.setVisibility(View.VISIBLE);
        confPass.setVisibility(View.VISIBLE);
        email.setVisibility(View.VISIBLE);
        register.setVisibility(View.VISIBLE);
        if (!result.equals("ok")) {
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText(result);
        } else {
            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
            toastMessage.setText("Registered successfully!");
            Intent intent = new Intent(registerActivity, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            registerActivity.startActivity(intent);
        }
        toast.setView(toastMessage);
        toast.show();
    }
}
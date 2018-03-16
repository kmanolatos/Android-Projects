package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 10/1/2018.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
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

public class LoginUserFacebook extends AsyncTask<String, Void, String> {
    TextView toastMessage, text;
    Toast toast;
    Context context;
    EditText user, pass;
    Button logIn, register, forgot, logInFb;
    int h, mode;
    ProgressBar progressBar;
    String id, currentVersion;

    public LoginUserFacebook(Context context, TextView toastMessage, int h, ProgressBar progressBar, EditText user, EditText pass, Button logIn, Button register, Button forgot, TextView text, Button logInFb, int mode, String currentVersion) {
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
        try {
            String name = (String) arg0[0];
            String surname = (String) arg0[1];
            String email = (String) arg0[2];
            String fbId = (String) arg0[3];
            String link = "http://manolatostechapppromoter.atwebpages.com/loginUserFacebook.php";
            String data = URLEncoder.encode("name", "UTF-8") + "=" +
                    URLEncoder.encode(name, "UTF-8");
            data += "&" + URLEncoder.encode("surname", "UTF-8") + "=" +
                    URLEncoder.encode(surname, "UTF-8");
            data += "&" + URLEncoder.encode("email", "UTF-8") + "=" +
                    URLEncoder.encode(email, "UTF-8");
            data += "&" + URLEncoder.encode("fbId", "UTF-8") + "=" +
                    URLEncoder.encode(fbId, "UTF-8");

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
        if (result.equals("Error")) {
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
        } else {
            id = result;
            new GetUserById(context, h, progressBar, toastMessage, user, pass, logIn, register, forgot, text, logInFb, mode, currentVersion).execute(id);
        }
    }
}
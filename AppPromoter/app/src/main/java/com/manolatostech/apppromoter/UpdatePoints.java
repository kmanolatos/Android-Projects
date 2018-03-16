package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 12/1/2018.
 */

import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.View;

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
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class UpdatePoints extends AsyncTask<String, Void, String> {
    TextView toastMessage;
    Toast toast;
    HomeActivity homeActivity;
    int h, index;
    ProgressBar progressBar;
    RelativeLayout relativeLayout;
    ArrayList<ApplicationModel> apps;
    String userId;
    UserModel userModel;
    DrawerLayout drawer;

    public UpdatePoints(HomeActivity homeActivity, TextView toastMessage, int h, ProgressBar progressBar, ArrayList<ApplicationModel> apps, UserModel userModel, int index, RelativeLayout relativeLayout, DrawerLayout drawer) {
        this.homeActivity = homeActivity;
        this.h = h;
        this.toastMessage = toastMessage;
        this.progressBar = progressBar;
        this.apps = apps;
        this.userModel = userModel;
        this.index = index;
        this.relativeLayout = relativeLayout;
        this.drawer = drawer;
    }

    protected void onPreExecute() {
        toast = Toast.makeText(homeActivity, null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        relativeLayout.removeAllViews();
        relativeLayout.addView(progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            userId = (String) arg0[0];
            String appId = (String) arg0[1];
            String link = "http://manolatostechapppromoter.atwebpages.com/updatePoints.php";
            String data = URLEncoder.encode("userId", "UTF-8") + "=" +
                    URLEncoder.encode(userId, "UTF-8");
            data += "&" + URLEncoder.encode("appId", "UTF-8") + "=" +
                    URLEncoder.encode(appId, "UTF-8");
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
        if (!result.equals("ok")) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
            progressBar.setVisibility(View.INVISIBLE);
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("An error occured!");
            toast.setView(toastMessage);
            toast.show();
        } else {
            if (apps.get(index).email.toString().trim().length() == 0)
                goToHomeActivity();
            else
                new SendEmail().execute("");
        }
    }

    private class SendEmail extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... params) {
            String host = "smtp.gmail.com";
            String port = "587";
            String mailFrom = "manolatostechnoreply@gmail.com";
            String password = "manolatostech123";

            // outgoing message information
            String mailTo = apps.get(index).email;
            String subject = "App Promoter - Application Downloaded";
            String message = "<p>Your application: <a href='" + apps.get(index).appLink + "'>" + apps.get(index).appName + "</a> has been downloaded!</p>";
            message += "<p>To increase your application downloads invite other people by sending them this link:</p>";
            message += "<a href='https://play.google.com/store/apps/details?id=com.manolatostech.apppromoter'>https://play.google.com/store/apps/details?id=com.manolatostech.apppromoter</a>";
            HtmlEmailSender mailer = new HtmlEmailSender();

            try {
                mailer.sendHtmlEmail(host, port, mailFrom, password, mailTo,
                        subject, message);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            goToHomeActivity();
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }

    public class HtmlEmailSender {

        public void sendHtmlEmail(String host, String port,
                                  final String userName, final String password, String toAddress,
                                  String subject, String message) throws AddressException,
                MessagingException {

            // sets SMTP server properties
            Properties properties = new Properties();
            properties.put("mail.smtp.host", host);
            properties.put("mail.smtp.port", port);
            properties.put("mail.smtp.auth", "true");
            properties.put("mail.smtp.starttls.enable", "true");

            // creates a new session with an authenticator
            Authenticator auth = new Authenticator() {
                public PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(userName, password);
                }
            };

            Session session = Session.getInstance(properties, auth);

            // creates a new e-mail message
            Message msg = new MimeMessage(session);

            msg.setFrom(new InternetAddress("no-reply@gmail.com"));
            InternetAddress[] toAddresses = {new InternetAddress(toAddress)};
            msg.setRecipients(Message.RecipientType.TO, toAddresses);
            msg.setSubject(subject);
            msg.setContent(message, "text/html; charset=utf-8");

            // sends the e-mail
            Transport.send(msg);

        }
    }

    public void goToHomeActivity() {
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        apps.remove(index);
        relativeLayout.removeAllViews();
        userModel.points += 1;
        progressBar.setVisibility(View.INVISIBLE);
        toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
        toastMessage.setText("+1 Point for your account!");
        homeActivity.setTitle("Points: " + userModel.points);
        relativeLayout.removeAllViews();
        relativeLayout.addView(progressBar);
        toast.setView(toastMessage);
        toast.show();
        new HomeActivity.loadingTask().execute();
    }
}
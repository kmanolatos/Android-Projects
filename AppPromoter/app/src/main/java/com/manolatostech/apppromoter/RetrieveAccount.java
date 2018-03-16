package com.manolatostech.apppromoter;

/**
 * Created by kmanolatos on 10/1/2018.
 */

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Properties;
import java.util.regex.Pattern;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.graphics.Color;
import android.os.AsyncTask;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class RetrieveAccount extends AsyncTask<String, Void, String> {
    ForgotActivity forgotActivity;
    ProgressBar progressBar;
    TextView toastMessage;
    Toast toast;
    int h;
    EditText email;
    Button retrieve;
    String[] parts;

    public RetrieveAccount(ForgotActivity forgotActivity, TextView toastMessage, int h, ProgressBar progressBar, EditText email, Button retrieve) {
        this.forgotActivity = forgotActivity;
        this.h = h;
        this.toastMessage = toastMessage;
        this.progressBar = progressBar;
        this.email = email;
        this.retrieve = retrieve;
    }

    protected void onPreExecute() {
        toast = Toast.makeText(forgotActivity, null,
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, h * 3 / 100);
        progressBar.setVisibility(View.VISIBLE);
        email.setVisibility(View.INVISIBLE);
        retrieve.setVisibility(View.INVISIBLE);
    }

    @Override
    protected String doInBackground(String... arg0) {
        try {
            String email = (String) arg0[0];
            String link = "http://manolatostechapppromoter.atwebpages.com/retrieveAccount.php?email=" + email;

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

        if (result.equals("not found")) {
            progressBar.setVisibility(View.INVISIBLE);
            email.setVisibility(View.VISIBLE);
            retrieve.setVisibility(View.VISIBLE);
            toastMessage.setBackgroundColor(Color.parseColor("#B81102"));
            toastMessage.setText("Your email is wrong!");
            toast.setView(toastMessage);
            toast.show();
        } else {
            parts = result.split(Pattern.quote("$"));
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
            String mailTo = email.getText().toString();
            String subject = "App Promoter - Retrieve Account";
            String message = "<p>Username: " + parts[0] + "</p>";
            message += "<p>Password: " + parts[1] + "</p>";
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
            progressBar.setVisibility(View.INVISIBLE);
            email.setVisibility(View.VISIBLE);
            retrieve.setVisibility(View.VISIBLE);
            email.setText("");
            toastMessage.setBackgroundColor(Color.parseColor("#038E18"));
            toastMessage.setText("Check your email!");
            toast.setView(toastMessage);
            toast.show();
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
}
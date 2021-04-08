package services;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

import edu.ncc.nest.nestapp.async.BackgroundTask;

/*
All Rest API Functionality is here.
Currently this only has headers and implementations to send Notifications with Rest API Post
requests through Firebase service.
 */

public class RestAPI extends BackgroundTask<Float, String> {
    /*This should be the Key associated to your Firebase's Project file.
    To get your API Key, go to Project Directory, Press the COG Icon on the left (next to project
     overview) and Click on the Cloud Messaging Tab.
    */
    private static String APIKEY = "AAAA-FL3foQ:APA91bEXvWDKpfEp0jFXo8TzlirdKw0o0bYDuf-qVhyJaoNkg_kJmVdDu11" +
            "UuIgiifQPiHntDl-djwKO6JCdkfWHGbEXJHFf8ukL9zg2znamN4-Ra0hLkjhUKi9T1fPmG73vgZtXODQ6";

     private static  String API_URL = "https://fcm.googleapis.com/fcm/send";


    /*This method may not work.
    Method may be used before sending any requests, checks to see if there is currently
    network connectivity
     */
public boolean checkNetworkConnection(Context context) {
    ConnectivityManager connec
            =(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);

    NetworkInfo networkInfo = connec.getActiveNetworkInfo();
    boolean isConnected = false;
    if (networkInfo != null)
        isConnected = networkInfo.isConnected();

    return isConnected;
}

/**
 * //TODO
 * Placeholder Send POST REST API Function.
 * You may want to create a new Activity that will act as a console to send notifications.
 * Have a Notification Title/Body Text Box and pass those values onto the REST API Post Request.
 * For now, this is a sample.
 *
 * May also need to use doInBackground to avoid possible UI freezes.
 *
 */
public static String makeRequest(String id) throws JSONException {
    HttpURLConnection urlConnection;
    JSONObject json = new JSONObject();
    JSONObject info = new JSONObject();
    info.put("title", "Notification Title");   // Notification title
    info.put("body", "Notification body"); // Notification body
    json.put("notification", info);

    String data = json.toString();
    String result = null;
    try {
        //Connect
        urlConnection = (HttpURLConnection) ((new URL(API_URL).openConnection()));
        urlConnection.setDoOutput(true);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Authorization", APIKEY);
        urlConnection.setRequestMethod("POST");
        urlConnection.connect();

        //Write
        OutputStream outputStream = urlConnection.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        writer.write(data);
        writer.close();
        outputStream.close();

        //Read
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }

        bufferedReader.close();
        result = sb.toString();

    } catch (UnsupportedEncodingException e) {
        e.printStackTrace();
    } catch (IOException e) {
        e.printStackTrace();
    }
    return result;
}

    @Override
    protected String doInBackground() {
        return null;
    }


}

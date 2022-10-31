//Name: Jennifer Chen
//AndrewId: yuc3
package edu.cmu.mydictionaryforproject4;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.annotation.RequiresApi;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Project4 Task1 and Task2
 * Some of the code is reference from 95702 Lab 8 Android Application Lab
 *
 * @author Jennifer Chen (yuc3@andrew.cmu.edu)
 */
public class GetInfo {
    MyDictionary md = null;   // for callback
    String searchTerm = null;
    String definition = null;
    Bitmap picture = null;

    public void search(String searchTerm, Activity activity, MyDictionary md) {
        this.md = md;
        this.searchTerm = searchTerm;
        new BackgroundTask(activity).execute();
    }

    private class BackgroundTask {

        private Activity activity; // The UI thread

        public BackgroundTask(Activity activity) {
            this.activity = activity;
        }

        private void startBackground() {
            new Thread(new Runnable() {
                public void run() {

                    doInBackground();
                    // This is magic: activity should be set to MainActivity.this
                    //    then this method uses the UI thread
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            onPostExecute();
                        }
                    });
                }
            }).start();
        }

        private void execute() {
            // There could be more setup here, which is why
            //    startBackground is not called directly
            startBackground();
        }

        // doInBackground( ) implements whatever you need to do on
        //    the background thread.
        // Implement this method to suit your needs
        private void doInBackground() {
            String jsonString = getRemoteJson(searchTerm); //get def and pic
            getInfo(jsonString);
        }

        // onPostExecute( ) will run on the UI thread after the background
        //    thread completes.
        // Implement this method to suit your needs
        public void onPostExecute() {
            md.infoReady(picture, definition);
        }

        /**
         * https://stackoverflow.com/questions/12916169/how-to-consume-rest-in-java
         *
         * @param searchTerm
         * @return
         */
        private String getRemoteJson(String searchTerm) {
            StringBuilder res = new StringBuilder();
            try {
                //task1's web api link
                //URL url = new URL("https://still-refuge-70928.herokuapp.com/getWordInfo?word=" + searchTerm);
                //local host for testing
                //URL url = new URL("http://10.0.2.2:8080/Project4WebServiceTask2-1.0-SNAPSHOT/getWordInfo?word=" + searchTerm);
                URL url = new URL("https://polar-cove-78505.herokuapp.com/getWordInfo?word=" + searchTerm);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                conn.setRequestProperty("Accept", "application/json");
                if (conn.getResponseCode() != 200) {
                    throw new RuntimeException("Failed : HTTP Error code : "
                            + conn.getResponseCode());
                }
                InputStreamReader in = new InputStreamReader(conn.getInputStream());
                BufferedReader br = new BufferedReader(in);
                String output;
                while ((output = br.readLine()) != null) {
                    res.append(output);
                }
                conn.disconnect();

            } catch (Exception e) {
                System.out.println("Exception in NetClientGet:- " + e);
            }

            return res.toString();
        }

        /**
         * Use Jackson library to get info from json.
         *
         * @param jsonString
         */
        private void getInfo(String jsonString) {
            try {
                JSONObject obj = new JSONObject(jsonString);
                //def
                definition = obj.get("definition").toString();

                //pic
                String picUrl = obj.get("picUrl").toString();
                URL u = new URL(picUrl);
                picture = getImage(u);

            } catch (JSONException | MalformedURLException e) {
                e.printStackTrace();
            }

        }

        /*
         * Given a URL referring to an image, return a bitmap of that image
         */
        @RequiresApi(api = Build.VERSION_CODES.P)
        private Bitmap getImage(final URL url) {
            try {
                final URLConnection conn = url.openConnection();
                conn.connect();
                BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
                return BitmapFactory.decodeStream(bis);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

    }


}

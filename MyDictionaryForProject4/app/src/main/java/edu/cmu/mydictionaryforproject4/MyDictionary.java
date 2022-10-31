//Name: Jennifer Chen
//AndrewId: yuc3
package edu.cmu.mydictionaryforproject4;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

/**
 * Project4 Task1 and Task2
 * Some of the code is reference from 95702 Lab 8 Android Application Lab
 *
 * @author Jennifer Chen (yuc3@andrew.cmu.edu)
 */
public class MyDictionary extends AppCompatActivity {

    MyDictionary me = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final MyDictionary ma = this;

        /*
         * Find the "submit" button, and add a listener to it
         */
        Button submitButton = (Button) findViewById(R.id.submit);


        // Add a listener to the send button
        submitButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View viewParam) {
                String searchTerm = ((EditText) findViewById(R.id.searchTerm)).getText().toString().trim();
                if (searchTerm.trim().equals("")) {
                    isEmpty();
                    return;
                }
                System.out.println("searchTerm = " + searchTerm);
                GetInfo getInfo = new GetInfo();
                getInfo.search(searchTerm, me, ma); // Done asynchronously in another thread.
            }
        });
    }

    /**
     * Call this method when the background thread is ready.
     *
     * @param picture
     * @param def
     */
    @SuppressLint("SetTextI18n")
    public void infoReady(Bitmap picture, String def) {
        ImageView pictureView = (ImageView) findViewById(R.id.wordPicture);
        TextView searchView = (EditText) findViewById(R.id.searchTerm);
        TextView definitionView = (TextView) findViewById(R.id.definition);
        TextView feedbackView = (TextView) findViewById(R.id.feedback);
        if (def != null) {
            pictureView.setImageBitmap(picture);

            feedbackView.setText("Here is the information of " + searchView.getText() + " :");
            feedbackView.setVisibility(View.VISIBLE);

            definitionView.setText("Definition: " + def);
            definitionView.setVisibility(View.VISIBLE);
            pictureView.setVisibility(View.VISIBLE);

        } else {
            pictureView.setImageResource(R.mipmap.ic_launcher);
            //System.out.println("No picture");
            pictureView.setVisibility(View.INVISIBLE);
            definitionView.setVisibility(View.INVISIBLE);
            feedbackView.setText("Sorry, no information of " + searchView.getText());
            feedbackView.setVisibility(View.VISIBLE);
        }
        searchView.setText("");
        pictureView.invalidate();
    }

    /**
     * If the user input is empty, show feedback to user
     * and do not need to send api to my web server.
     */
    @SuppressLint("SetTextI18n")
    public void isEmpty() {
        ImageView pictureView = (ImageView) findViewById(R.id.wordPicture);
        TextView definitionView = (TextView) findViewById(R.id.definition);
        TextView feedbackView = (TextView) findViewById(R.id.feedback);

        feedbackView.setText("You input nothing!");
        feedbackView.setVisibility(View.VISIBLE);

        pictureView.setVisibility(View.INVISIBLE);
        definitionView.setVisibility(View.INVISIBLE);
    }
}

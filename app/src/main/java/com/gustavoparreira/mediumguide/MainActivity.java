package com.gustavoparreira.mediumguide;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class MainActivity extends AppCompatActivity {

    SubscriptionHandler subscription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ParseUser user = new ParseUser();

        user.setUsername("my name");

        user.setPassword("my pass");

        user.setEmail("email@example.com");

        // other fields can be set just like with ParseObject

        user.put("phone", "650–555–0000");

        user.signUpInBackground(new SignUpCallback() {

            public void done(ParseException e) {

                if (e == null) {

                    // Hooray! Let them use the app now.

                } else {

                    // Sign up didn’t succeed. Look at the ParseException

                    // to figure out what went wrong

                }

            }

        });

        subscription = new SubscriptionHandler(this);

        Button subscribeButton = (Button) findViewById(R.id.subscribeButton);
        Button messageButton = (Button) findViewById(R.id.messageButton);

        subscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subscribeOrtc();
            }
        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageOrtc();
            }
        });
    }

    private void subscribeOrtc() {
        if (subscription.isOrtcConnected()) {
            subscription.subscribeChannel("general");
        } else {
            Toast.makeText(this, "Ortc not connected yet, try again in a few seconds.",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void messageOrtc() {
        if (subscription.isOrtcConnected()) {
            if (subscription.isOrtcSubscribed("general")) {
                subscription.sendMessage("general", "Hello World!");
            } else {
                Toast.makeText(this, "Ortc not subscribed. Subscribe before trying to send a message",
                        Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Ortc not connected yet, try again in a few seconds.",
                    Toast.LENGTH_SHORT).show();
        }
    }
}

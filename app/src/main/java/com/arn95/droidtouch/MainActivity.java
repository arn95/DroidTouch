package com.arn95.droidtouch;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class MainActivity extends AppCompatActivity{

    DroidTouch droidTouch;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);

        droidTouch = new DroidTouch(this,getSupportFragmentManager());
        if (button != null) {
            droidTouch.addDialog("login_dialog", button, R.layout.activity_login, new Intent(MainActivity.this, LoginActivity.class));
            button.setOnTouchListener(droidTouch);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        //true if mother activity resumed
        droidTouch.setCurrentActivityState("login_dialog", true);

    }

    @Override
    protected void onStop() {
        //false if mother activity stopped
        droidTouch.setCurrentActivityState("login_dialog", false);
        super.onStop();
    }
}



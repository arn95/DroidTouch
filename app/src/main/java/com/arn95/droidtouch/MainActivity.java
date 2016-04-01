package com.arn95.droidtouch;

import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity{

    DroidTouch droidTouch;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = (Button) findViewById(R.id.button);

        droidTouch = new DroidTouch(this,getSupportFragmentManager());
        if (button != null) {
            droidTouch.createDialog("login_dialog",button,R.layout.activity_login,new Intent(MainActivity.this,LoginActivity.class));
            button.setOnTouchListener(droidTouch);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        droidTouch.setActivityState("login_dialog", false);

    }

    @Override
    protected void onStop() {
        droidTouch.setActivityState("login_dialog", true);
        super.onStop();
    }
}



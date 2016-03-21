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

public class MainActivity extends AppCompatActivity implements View.OnTouchListener{

    private boolean activity_started;
    TextView textView;
    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.textView);

        Button button = (Button) findViewById(R.id.button);
        //startActivity(new Intent(MainActivity.this, LoginActivity.class));
        if (button != null) {
            button.setOnTouchListener(this);
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        textView.setText("" + event.getPressure());
        Vibrator vibrator = (Vibrator) MainActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        DroidTouch droidTouch = new DroidTouch(getSupportFragmentManager());
        if (!droidTouch.isShown("login_dialog")) {
            if (event.getPressure() > 0.7 && event.getPressure() < 1.0) {
                //show dialog
                droidTouch.createDialog("login_dialog",R.layout.activity_login,1000,1000);
                droidTouch.showDialog("login_dialog");
                vibrator.vibrate(30);
            }
        } else {
            if (event.getPressure() >= 1) {
                if (!activity_started) {
                    //open activity fullscreen
                    droidTouch.dismissDialog("login_dialog");
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    vibrator.vibrate(30);
                    activity_started = true;
                }
            } else if (event.getPressure() < 0.7 || event.getAction() == MotionEvent.ACTION_UP) {
                //dismiss dialog
                droidTouch.dismissDialog("login_dialog");
            }
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        activity_started = false;
    }
}



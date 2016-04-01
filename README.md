# DroidTouch
Simulates force touch on Android by using MotionEvent's pressure method.

It uses MethodEvent which measures the area of the finger and gets back with a pressure value from 0.0 to 1.0 (or higher).
This is not the actual force touch. It does not measure force, it measures area of the finger that is pressing the screen.
A pressure higher than 0.74 but lower than 1.0 makes the dialog pop up with an activity layout. 
Pressure higher than 1.0 starts the activity itself.
Play with it.

#Usage

On the mother activity where the view to be clicked on is displayed initialize DroidTouch:

    DroidTouch droidTouch; //field because activity state needs to be tracked

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        //Can be any view that takes a touch listener
        Button button = (Button) findViewById(R.id.button);

        droidTouch = new DroidTouch(this,getSupportFragmentManager());
        if (button != null) {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            droidTouch.addDialog("login_dialog",button,R.layout.activity_login,intent);
            button.setOnTouchListener(droidTouch);
        }
    }

Update activity state

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

# DroidTouch
Simulates force touch on Android by using MotionEvent's pressure method.

It uses MethodEvent which measures the area of the finger and gets back with a pressure value from 0.0 to 1.0 (or higher).
This is not the actual force touch. It does not measure force, it measures area of the finger that is pressing the screen.
A pressure higher than 0.74 but lower than 1.0 makes the dialog pop up with an activity layout. 
Pressure higher than 1.0 starts the activity itself.
Play with it.


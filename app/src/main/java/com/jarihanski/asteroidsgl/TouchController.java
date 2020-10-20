package com.jarihanski.asteroidsgl;

import android.view.MotionEvent;
import android.view.View;

public class TouchController extends InputManager implements View.OnTouchListener{
    public TouchController(View view){
        view.findViewById(R.id.keypad_left).setOnTouchListener(this);
        view.findViewById(R.id.keypad_right).setOnTouchListener(this);
        view.findViewById(R.id.keypad_a).setOnTouchListener(this);
        view.findViewById(R.id.keypad_b).setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(final View v, final MotionEvent event) {
        final int action = event.getActionMasked();
        final int id = v.getId();
        if(action == MotionEvent.ACTION_DOWN){
            // User started pressing a key
            if (id == R.id.keypad_left) {
                _horizontalFactor -= 1;
            } else if(id == R.id.keypad_right) {
                _horizontalFactor += 1;
            }
            if (id == R.id.keypad_a) {
                _pressingA = true;
            }
            if (id == R.id.keypad_b) {
                _pressingB = true;
            }
        } else if(action == MotionEvent.ACTION_UP) {
            // User released a key
            if (id == R.id.keypad_left) {
                _horizontalFactor += 1;
            } else if (id == R.id.keypad_right) {
                _horizontalFactor -= 1;
            }
            if (id == R.id.keypad_a) {
                _pressingA = false;
            }
            if (id == R.id.keypad_b) {
                _pressingB = false;
            }
        }
        return false;
    }
}

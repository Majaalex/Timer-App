package com.example.timer;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


public class Timer extends Fragment{

    private EditText mEditTextTimer;
    private TextView mTextViewCountdown;
    private CountDownTimer mCountDownTimer;

    private boolean mTimerRunning;

    private long mStartTimeInMillis;
    private long mTimeLeftInMillis = mStartTimeInMillis;
    private long mEndTime;

    Button timerSetButton;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_timer, container, false);
        mEditTextTimer = v.findViewById(R.id.timer_input);
        mTextViewCountdown = v.findViewById(R.id.text_timer);
        Button timerStartButton = v.findViewById(R.id.button_timerStart);
        Button timerStopButton = v.findViewById(R.id.button_timerStop);
        Button timerResetButton = v.findViewById(R.id.button_timerReset);
        timerSetButton = v.findViewById(R.id.button_timerSet);


        // Button listener activations
        timerSetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setTimer(v);
            }
        });
        stopTimer(timerStopButton);
        resetTimer();
        updateCountdownTimerText();
        timerResetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTimer();
            }
        });
        timerStartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!mTimerRunning){
                    startTimer();
                }
            }
        });
        return v;
    }

    // Set the timer to a value based on user input
    private void setTimer(View v) {
        String input = mEditTextTimer.getText().toString();
        // Check that field isn't empty
        if (input.length() == 0){
            Toast.makeText(getContext(), "Field can't be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check that field isn't 0
        long millisInput = Long.parseLong(input) * 60000;
        if (millisInput == 0){
            Toast.makeText(getContext(), "Please enter a positive number", Toast.LENGTH_SHORT).show();
            return;
        }
        // Set the time
        mStartTimeInMillis = millisInput;
        resetTimer();
        mEditTextTimer.setText("");
        closeKeyboard(v);
    }

    // Hide user input while the timer is running, and show when timer is stopped
    private void updateWatchInterface(){
        if (mTimerRunning){
            mEditTextTimer.setVisibility(View.INVISIBLE);
            timerSetButton.setVisibility(View.INVISIBLE);
        } else {
            mEditTextTimer.setVisibility(View.VISIBLE);
            timerSetButton.setVisibility(View.VISIBLE);
        }
    }

    // Reset the timer
    private void resetTimer() {
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        mTimeLeftInMillis = mStartTimeInMillis;
        updateCountdownTimerText();
        mTimerRunning = false;
        updateWatchInterface();
    }

    // stop the timer if it is running
    private void stopTimer(Button timerStopButton) {
        timerStopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimerRunning){
                    mCountDownTimer.cancel();
                    mTimerRunning = false;
                    updateWatchInterface();
                }
            }
        });
    }

    // Start the timer if it is not running
    private void startTimer() {
        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;

            mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
                @Override
                public void onTick(long millisUntilFinished) {
                    mTimeLeftInMillis = millisUntilFinished;
                    updateCountdownTimerText();
                }

                @Override
                public void onFinish() {
                    mTimerRunning = false;

                }
            }.start();

            mTimerRunning = true;
            updateWatchInterface();


    }

    private void updateCountdownTimerText() {
        int hours = (int) (mTimeLeftInMillis / 1000) / 3600;
        int minutes = (int) ((mTimeLeftInMillis / 1000) % 3600) / 60;
        int seconds = (int) (mTimeLeftInMillis / 1000) % 60;
        String timeLeftFormatted;
        if (hours > 0){
            timeLeftFormatted = String.format(Locale.getDefault(),"%d:%02d:%02d",hours, minutes, seconds);
        } else {
            timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        }
        mTextViewCountdown.setText(timeLeftFormatted);
    }

    @Override
    // Get the values stored while the app is closed to continue timer as if it never got turned off
    public void onStart() {
        super.onStart();
        Context context = getContext();
        assert context != null;
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);

        mTimeLeftInMillis = prefs.getLong("millisLeft", mStartTimeInMillis);
        mTimerRunning = prefs.getBoolean("timerRunning", false);
        mStartTimeInMillis = prefs.getLong("startTimeInMillis", 600000);

        //updateCountdownTimerText();

        if (mTimerRunning) {
            mEndTime = prefs.getLong("endTime", 0);
            mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

            if (mTimeLeftInMillis < 0) {
                mTimeLeftInMillis = 0;
                mTimerRunning = false;
                updateCountdownTimerText();
            } else {
                startTimer();
            }
        }
    }

    private void closeKeyboard(View v){
        if (v != null){
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
    }
    @Override
    // Store the timer on exit, so that the app can continue the timer with the correct time when resumed
    public void onStop() {
        super.onStop();
        Context context = getContext();
        assert context != null;
        SharedPreferences prefs = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("startTimeInMillis", mStartTimeInMillis);
        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);

        editor.apply();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}

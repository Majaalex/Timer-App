package com.example.timer;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

public class Stopwatch extends Fragment {
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;

    private Button mButtonStopwatchStart;
    private Button mButtonStopwatchStop;
    private Button mButtonStopwatchReset;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_stopwatch, container, false);
        chronometer = v.findViewById(R.id.chronometer_stopwatch);
        mButtonStopwatchStart = v.findViewById(R.id.button_stopwatchStart);
        mButtonStopwatchStop = v.findViewById(R.id.button_stopwatchStop);
        mButtonStopwatchReset = v.findViewById(R.id.button_stopwatchReset);
        startStopwatch();
        stopStopwatch();
        resetStopwatch();
        return v;
    }

    private void resetStopwatch() {
        mButtonStopwatchReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
            }
        });
    }
    private void stopStopwatch() {
        mButtonStopwatchStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running){
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
            }
        });
    }

    private void startStopwatch() {
        mButtonStopwatchStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running){
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }
            }
        });
    }
}

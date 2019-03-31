package com.example.timer;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import java.util.ArrayList;

public class Stopwatch extends Fragment {
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;
    private long seconds;
    private long minutes;

    private Button mButtonStopwatchStart;
    private Button mButtonStopwatchStop;
    private Button mButtonStopwatchReset;
    private Button mButtonStopwatchLap;

    // RecyclerView waypoints
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<ListItem> mItemList;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.fragment_stopwatch, container, false);
        chronometer = v.findViewById(R.id.chronometer_stopwatch);
        // Get the button listeners up and running
        initButtons(v);
        mItemList = new ArrayList<>();
        buildRecyclerView(v);
        return v;
    }

    // Input the lapped time to the bottom of the list for the recyclerView
    public void insertItemAtEnd(){
        seconds = (SystemClock.elapsedRealtime() - chronometer.getBase()) / 1000;
        minutes = seconds /  60;
        seconds = seconds % 60;
        mItemList.add(mItemList.size(),new ListItem(String.format("%02d", minutes)+ ":" + String.format("%02d", seconds)));
        mAdapter.notifyDataSetChanged();
    }


    private void initButtons(View v) {
        mButtonStopwatchStart = v.findViewById(R.id.button_stopwatchStart);
        mButtonStopwatchStop = v.findViewById(R.id.button_stopwatchStop);
        mButtonStopwatchReset = v.findViewById(R.id.button_stopwatchReset);
        mButtonStopwatchLap = v.findViewById(R.id.button_stopwatchLap);
        startStopwatch();
        stopStopwatch();
        resetStopwatch();
        lapStopwatch();
    }

    private void lapStopwatch() {
        mButtonStopwatchLap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running){
                    insertItemAtEnd();
                }
            }
        });
    }

    private void resetStopwatch() {
        mButtonStopwatchReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
                mItemList.clear();
                mAdapter.notifyDataSetChanged();
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
    private void buildRecyclerView(View v) {
        mRecyclerView = v.findViewById(R.id.recyclerView);
        mLayoutManager = new LinearLayoutManager(v.getContext());
        mAdapter = new RecyclerViewAdapter(mItemList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}

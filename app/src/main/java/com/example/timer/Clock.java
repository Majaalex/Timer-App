package com.example.timer;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class Clock extends Fragment {
    Spinner spinner;
    TextClock timezoneTime;
    TextView timezoneDate;
    TextView timezoneSelected;
    TextView textViewDate;

    ArrayAdapter<String> idAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_clock, container, false);
        drawCalendar(v);
        // Init all the different objects on the view
        spinner = v.findViewById(R.id.spinner);
        timezoneTime = v.findViewById(R.id.clock_selectedTime);
        timezoneDate = v.findViewById(R.id.clock_selectedDate);
        timezoneSelected = v.findViewById(R.id.clock_selectedTimezone);

        // Array for all timezones
        String[] idArray = TimeZone.getAvailableIDs();

        // Setup for spinner
        idAdapter = new ArrayAdapter<>(v.getContext(), android.R.layout.simple_spinner_item, idArray);
        idAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(idAdapter);
        trackSpinner();
        return v;
    }

    private void trackSpinner() {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // Get the selected id from the spinner
                String selectedId = (String) parent.getItemAtPosition(position);
                // Get the timezone from the ID
                TimeZone tz = TimeZone.getTimeZone(selectedId);
                timezoneSelected.setText(tz.getDisplayName());

                // Create a calendar, date and update the values on the screen
                Calendar selected = Calendar.getInstance(tz);
                String tzDate = DateFormat.getDateInstance().format(selected.getTime());
                timezoneTime.setTimeZone(tz.getID());
                timezoneDate.setText(tzDate);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    // Draws the calendar showing the locale time
    private void drawCalendar(View v) {
        Calendar current = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(current.getTime());
        textViewDate = v.findViewById(R.id.clock_date);
        textViewDate.setText(currentDate);
    }
}

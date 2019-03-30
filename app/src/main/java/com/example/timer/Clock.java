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
        spinner = v.findViewById(R.id.spinner);
        timezoneTime = v.findViewById(R.id.clock_selectedTime);
        timezoneDate = v.findViewById(R.id.clock_selectedDate);
        timezoneSelected = v.findViewById(R.id.clock_selectedTimezone);

        String[] idArray = TimeZone.getAvailableIDs();

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
                System.out.println("hello");
                setNewTimezone(parent, position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void setNewTimezone(AdapterView<?> parent, int position) {
        String selectedId = (String) parent.getItemAtPosition(position);
        TimeZone tz = TimeZone.getTimeZone(selectedId);
        timezoneSelected.setText(tz.getDisplayName());

        Calendar selected = Calendar.getInstance(tz);
        String tzDate = DateFormat.getDateInstance().format(selected.getTime());
        //TODO: neither of the following 2 work
        timezoneTime.setTimeZone(tz.getDisplayName());
        timezoneDate.setText(tzDate);
    }

    private void drawCalendar(View v) {
        Calendar current = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance().format(current.getTime());
        textViewDate = v.findViewById(R.id.clock_date);
        textViewDate.setText(currentDate);
    }
}

package com.example.myruns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class Manual extends AppCompatActivity {

    public int activity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        RecyclerView options = findViewById(R.id.options);

        options.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        options.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(options.getContext(),
                layoutManager.getOrientation());
        options.addItemDecoration(mDividerItemDecoration);

        OptionsAdapter optionsAdapter = new OptionsAdapter(getResources().getStringArray(R.array.manual_array), this);
        options.setAdapter(optionsAdapter);

        activity = getIntent().getIntExtra("activity", 0);
    }

    public void save (View v){
        ExerciseEntryDbHelper dbHelper = new ExerciseEntryDbHelper(this);
        ExerciseEntry entry = new ExerciseEntry();
        SharedPreferences prefs = getSharedPreferences("manualEntry", MODE_PRIVATE);
        SharedPreferences settingsPrefs = getSharedPreferences(getString(R.string.settingsPref), MODE_PRIVATE);

        entry.setInputType(0);
        entry.setActivityType(activity);
        Date date = new Date();
        LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int hour = prefs.getInt("hour", date.getHours());
        int minute = prefs.getInt("minute", date.getMinutes());
        int seconds = 0;
        if(minute == date.getMinutes() && hour == date.getHours()){
            seconds = date.getSeconds();
        }
        int year = prefs.getInt("year", localDate.getYear());
        int month = prefs.getInt("month", localDate.getMonth().getValue()-1);
        int day = prefs.getInt("day", localDate.getDayOfMonth());
        Date calDate = new Date(year-1900, month, day, hour, minute, seconds);
        Calendar cal = Calendar.getInstance();
        cal.setTime(calDate);
        entry.setDateTime(cal);
        entry.setDuration(Integer.parseInt(prefs.getString("Duration", "0")));
        float dist = Float.parseFloat(prefs.getString("Distance", "0"));
        final float constant = (float) 0.621371;
        if(settingsPrefs.getInt("unit", 1) == 0){
            entry.setDistance(dist*constant);
        }
        else{
            entry.setDistance(dist);
        }
        entry.setAvgPace(0);
        entry.setAvgSpeed(0);
        entry.setCalorie(Integer.parseInt(prefs.getString("Calories", "0")));
        entry.setClimb(0);
        entry.setHeartRate(Integer.parseInt(prefs.getString("Heart Rate", "0")));
        entry.setComment(prefs.getString("Comment", ""));
        entry.setPrivacy(settingsPrefs.getBoolean("privacy", false) ? 1 : 0);
        entry.setLocationList(null);
        long id = dbHelper.insertEntry(entry);
        Toast.makeText(this, "Entry #" + id + " saved.", Toast.LENGTH_SHORT).show();
        prefs.edit().clear().apply();
        finish();
    }

    public void cancel(View v){
        SharedPreferences prefs = getSharedPreferences("manualEntry", MODE_PRIVATE);
        prefs.edit().clear().apply();
        finish();
    }
}

package com.example.myruns;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class DisplayEntryActivity extends AppCompatActivity {

    long rowID;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_entry);
        rowID = getIntent().getLongExtra("id", -1);
        if(rowID == -1){
            finish();
            return;
        }

        new getEntryTask().execute(rowID);

    }

    private class getEntryTask extends AsyncTask<Long, Void, ExerciseEntry> {
        protected ExerciseEntry doInBackground(Long... id) {
            return new ExerciseEntryDbHelper(getApplicationContext()).fetchEntryByIndex(id[0]);

        }

        protected void onPostExecute(ExerciseEntry result) {
            setInfo(result);
        }
    }

    private class deleteEntryTask extends AsyncTask<Long, Void, Void> {
        protected Void doInBackground(Long... id) {
            new ExerciseEntryDbHelper(getApplicationContext()).removeEntry(id[0]);
            return null;
        }
    }

    public void setInfo(ExerciseEntry entry){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd YYYY");
        ((EditText)findViewById(R.id.inputType1)).setText(getResources().getStringArray(R.array.input_array)[entry.getInputType()]);
        ((EditText)findViewById(R.id.activityType1)).setText(getResources().getStringArray(R.array.activity_array)[entry.getActivityType()]);
        ((EditText)findViewById(R.id.dateTime1)).setText(sdf.format(entry.getDateTime().getTime()));
        SharedPreferences settingsPrefs = getSharedPreferences(getString(R.string.settingsPref), MODE_PRIVATE);
        if(settingsPrefs.getInt("unit", 1) == 0){
            ((EditText)findViewById(R.id.distance1)).setText(String.valueOf(entry.getDistance() * 1.60934));
        }
        else{
            ((EditText)findViewById(R.id.distance1)).setText(String.valueOf(entry.getDistance()));
        }
        ((EditText)findViewById(R.id.duration1)).setText(String.valueOf(entry.getDuration()));
        ((EditText)findViewById(R.id.calories1)).setText((String.valueOf(entry.getCalorie())));
        ((EditText)findViewById(R.id.heartRate1)).setText((String.valueOf(entry.getHeartRate())));
    }


    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        getMenuInflater().inflate(R.menu.mymenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    // handle button activities
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.mybutton) {
            new deleteEntryTask().execute(rowID);
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

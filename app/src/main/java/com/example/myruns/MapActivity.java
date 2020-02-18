package com.example.myruns;

import android.app.FragmentManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener, ServiceConnection {

    boolean start = false;
    boolean reset = false;
    GoogleMap map;
    PolylineOptions polylineOptions;
    Marker finalMarker;
    boolean isBound = false;
    TextView typeText, avgSpeedText, curSpeedText, climbText, calText, distText;
    int activityInt;
    String activity;
    boolean history;
    long rowID;
    double startAlt;
    long startEpoch;
    LatLng startLatLng;
    LatLng lastLatLng;
    double totalDist;
    int inputType = 1;
    double currCal, currClimb, curSpeed, currDur;
    int standingCount, walkingCount, runningCount;
    Calendar begin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        setTitle("Map");
        history = getIntent().getBooleanExtra("history", false);
        activityInt = getIntent().getIntExtra("activity", -1);
        if(activityInt == -1){
            activity = "Unknown";
            inputType = 2;
        }
        else{
            activity = getResources().getStringArray(R.array.activity_array)[activityInt];
        }
        rowID = getIntent().getLongExtra("rowID", -1);

        FragmentManager myFragmentManager = getFragmentManager();
        MapFragment mapFragment
                = (MapFragment)myFragmentManager.findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        polylineOptions = new PolylineOptions();
        polylineOptions.color(Color.BLUE);

        typeText = findViewById(R.id.type_stats);
        avgSpeedText = findViewById(R.id.avgSpeed_stats);
        curSpeedText = findViewById(R.id.curSpeed_stats);
        climbText = findViewById(R.id.climb_stats);
        calText = findViewById(R.id.calorie_stats);
        distText = findViewById(R.id.distance_stats);

        if(savedInstanceState != null && !history){
            start = true;
            reset = true;
            startAlt = savedInstanceState.getDouble("startAlt");
            startEpoch = savedInstanceState.getLong("startEpoch");
            startLatLng = new LatLng(savedInstanceState.getDouble("startLat"), savedInstanceState.getDouble("startLng"));
            lastLatLng = startLatLng;
            totalDist = savedInstanceState.getDouble("totalDist");
            begin = Calendar.getInstance();
            Gson gson = new Gson();
            Type type = new TypeToken<List<LatLng>>() {}.getType();
            List<LatLng> locs = gson.fromJson(savedInstanceState.getString("locs"), type);
            polylineOptions.addAll(locs);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if(!history) {
            outState.putDouble("startAlt", startAlt);
            outState.putLong("startEpoch", startEpoch);
            outState.putDouble("startLat", startLatLng.latitude);
            outState.putDouble("startLng", startLatLng.longitude);
            outState.putDouble("totalDist", totalDist);
            Gson gson = new Gson();
            String inputString = gson.toJson(polylineOptions.getPoints());
            outState.putString("locs", inputString);
        }

        super.onSaveInstanceState(outState);
    }

    public void onDestroy(){
        super.onDestroy();
        if(isBound) {
            unbindService(this);
        }
        isBound = false;
    }

    public void killActivity(View v){
        finish();
    }

    public void updateMap(LatLng latLng, double speed, double alt, long time) {
        boolean metric;
        String speedSuffix, distSuffix;
        SharedPreferences settingsPrefs = getSharedPreferences(getString(R.string.settingsPref), MODE_PRIVATE);
        if(settingsPrefs.getInt("unit", 1) == 0){
            metric = true;
            speedSuffix = " km/h";
            distSuffix = " Kilometers";
        }
        else{
            metric = false;
            speedSuffix = " mi/h";
            distSuffix = " Miles";
        }

        if(metric){
            speed = speed * 3.6;
            alt = alt / 1000;
        }
        else{
            speed = speed * 2.23694;
            alt = alt / 1609;
        }

        polylineOptions.add(latLng);
        map.addPolyline(polylineOptions);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);

        if(reset){
            reset = false;
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(startLatLng, 17);
            map.animateCamera(cameraUpdate);
            MarkerOptions markerOptions1 = new MarkerOptions();
            markerOptions1.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            markerOptions1.position(polylineOptions.getPoints().get(0));
            map.addMarker(markerOptions1);

        }
        if(!start){
            markerOptions.icon(BitmapDescriptorFactory
                    .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
            map.addMarker(markerOptions);
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 17);
            map.animateCamera(cameraUpdate);
            start = true;
            startAlt = alt;
            startEpoch = time;
            startLatLng = latLng;
            lastLatLng = latLng;
            totalDist = 0;
            speed = 0;
            begin = Calendar.getInstance();
        }
        else{
            if(finalMarker != null){
                finalMarker.remove();
            }
            finalMarker = map.addMarker(markerOptions);
        }
        DecimalFormat df = new DecimalFormat("#.##");
        typeText.setText("Type: " + activity);
        if((time - startEpoch) != 0) {
            double timeElap = (time - startEpoch) / 3.6e+12;
            float[] distRes = new float[1];
            Location.distanceBetween(latLng.latitude, latLng.longitude, lastLatLng.latitude, lastLatLng.longitude, distRes);
            if(metric){distRes[0] = distRes[0] / 1000;}
            else{distRes[0] = distRes[0] / 1609;}
            totalDist = distRes[0] + totalDist;
            avgSpeedText.setText("Avg Speed: " + df.format((totalDist/timeElap)) + speedSuffix);
            distText.setText("Distance: " + df.format(totalDist) + distSuffix);
            int calories;
            if(metric){
                calories = (int) ((0.035 * 137) + ((((totalDist/timeElap) / 3.6) * ((totalDist/timeElap) / 3.6)) / 1.65) * (0.029) * (137));
            }
            else{
                calories = (int) ((0.035 * 137) + ((((totalDist/timeElap) / 2.237) * ((totalDist/timeElap) / 2.237)) / 1.65) * (0.029) * (137));
            }
            calText.setText("Calorie: " + calories);
            currCal = calories;
            currClimb = (alt-startAlt);
            curSpeed = (totalDist/timeElap);
            currDur = (time - startEpoch) / 6e+10;
        }
        else{
            avgSpeedText.setText("Avg Speed: " + 0 + speedSuffix);
            distText.setText("Distance: " + 0 + distSuffix);
            calText.setText("Calorie: " + 0);
        }

        curSpeedText.setText("Cur Speed: " + df.format(speed) + speedSuffix);
        climbText.setText("Climb: " + df.format((alt-startAlt)) + distSuffix);

        lastLatLng = latLng;



    }

    public void displayHistory(ExerciseEntry entry){
        if(entry == null){
            finish();
            return;
        }
        String speedSuffix, distSuffix;
        SharedPreferences settingsPrefs = getSharedPreferences(getString(R.string.settingsPref), MODE_PRIVATE);
        if(entry.getActivityType() != -1) {
            typeText.setText("Type: " + getResources().getStringArray(R.array.activity_array)[entry.getActivityType()]);
        }
        else{
            typeText.setText("Type: " + "Unknown");
        }
        DecimalFormat df = new DecimalFormat("#.##");

        if(settingsPrefs.getInt("unit", 1) == 0){
            speedSuffix = " km/h";
            distSuffix = " Kilometers";

            avgSpeedText.setText("Avg Speed: " + df.format(entry.getAvgSpeed() * 1.609) + speedSuffix);
            curSpeedText.setText("n/a");
            climbText.setText("Climb: " + df.format(entry.getClimb() * 1.609) + distSuffix);
            calText.setText("Calorie: " + df.format(entry.getCalorie()));
            distText.setText("Distance: " + df.format(entry.getDistance() * 1.609) + distSuffix);

        }
        else{
            speedSuffix = " mi/h";
            distSuffix = " Miles";

            avgSpeedText.setText("Avg Speed: " + df.format(entry.getAvgSpeed()) + speedSuffix);
            curSpeedText.setText("n/a");
            climbText.setText("Climb: " + df.format(entry.getClimb()) + distSuffix);
            calText.setText("Calorie: " + df.format(entry.getCalorie()));
            distText.setText("Distance: " + df.format(entry.getDistance()) + distSuffix);

        }

        polylineOptions.addAll(entry.getLocationList());
        map.addPolyline(polylineOptions);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
        markerOptions.position(entry.getLocationList().get(0));
        map.addMarker(markerOptions);

        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(entry.getLocationList().get(0), 17);
        map.animateCamera(cameraUpdate);

        MarkerOptions markerOptions1 = new MarkerOptions();
        markerOptions1.position(entry.getLocationList().get(entry.getLocationList().size() - 1));
        map.addMarker(markerOptions1);

        Button save = findViewById(R.id.saveButtonMap);
        Button cancel = findViewById(R.id.cancelButtonMap);
        save.setVisibility(View.GONE);
        cancel.setVisibility(View.GONE);
    }


    public void saveEntry(View view) {
        SharedPreferences settingsPrefs = getSharedPreferences(getString(R.string.settingsPref), MODE_PRIVATE);
        ExerciseEntry entry = new ExerciseEntry();
        if(settingsPrefs.getInt("unit", 1) == 0){
            entry.setContext(this);
            entry.setDistance((float) (totalDist/1.609));
            entry.setLocationList(new ArrayList<LatLng>(polylineOptions.getPoints()));
            entry.setPrivacy(settingsPrefs.getBoolean("privacy", false) ? 1 : 0);
            entry.setInputType(inputType);
            entry.setHeartRate(0);
            entry.setCalorie((int)currCal);
            entry.setClimb((float) (currClimb/1.609));
            entry.setActivityType(activityInt);
            entry.setAvgPace((float) (curSpeed/1.609));
            entry.setAvgSpeed((float) (curSpeed/1.609));
            entry.setDuration((int) currDur);
            entry.setDateTime(begin);
        }
        else {
            entry.setContext(this);
            entry.setDistance((float) totalDist);
            entry.setLocationList(new ArrayList<LatLng>(polylineOptions.getPoints()));
            entry.setPrivacy(settingsPrefs.getBoolean("privacy", false) ? 1 : 0);
            entry.setInputType(inputType);
            entry.setHeartRate(0);
            entry.setClimb((float) currClimb);
            entry.setCalorie((int)currCal);
            entry.setActivityType(activityInt);
            entry.setAvgPace((float) curSpeed);
            entry.setAvgSpeed((float) curSpeed);
            entry.setDuration((int) currDur);
            entry.setDateTime(begin);
        }
        new saveEntryTask().execute(entry);
        finish();
    }

    private class saveEntryTask extends AsyncTask<ExerciseEntry, Void, Void> {
        protected Void doInBackground(ExerciseEntry... entry) {
            new ExerciseEntryDbHelper(getApplicationContext()).insertEntry(entry[0]);
            return null;
        }
    }

    private class getEntryTask extends AsyncTask<Long, Void, ExerciseEntry> {
        protected ExerciseEntry doInBackground(Long... id) {
            return new ExerciseEntryDbHelper(getApplicationContext()).fetchEntryByIndex(id[0]);

        }

        protected void onPostExecute(ExerciseEntry result) {
            displayHistory(result);
        }
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMapLongClick(LatLng latLng) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);

        if(!isBound && !history) {
            Intent intent = new Intent(this, LocationUpdateService.class);
            bindService(intent, this, Context.BIND_AUTO_CREATE);
            isBound = true;
        }
        else if(history){
            new getEntryTask().execute(rowID);
        }
    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        LocationUpdateService.locationBinder locationBinder = (LocationUpdateService.locationBinder) service;
        LocationHandler locationHandler = new LocationHandler();
        locationBinder.setMessageHandler(locationHandler);
        if (inputType == 2) {
            TypeHandler typeHandler = new TypeHandler();
            locationBinder.setTypeHandler(typeHandler);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        isBound = false;
    }

    public class LocationHandler extends Handler {
        public void handleMessage(Message message){
            Bundle bundle = message.getData();
            double lat = bundle.getDouble(LocationUpdateService.LAT_KEY);
            double lng = bundle.getDouble(LocationUpdateService.LNG_KEY);
            double speed = bundle.getDouble(LocationUpdateService.SPEED_KEY);
            double alt = bundle.getDouble(LocationUpdateService.ALT_KEY);
            long time = bundle.getLong(LocationUpdateService.TIME_KEY);
            updateMap(new LatLng(lat, lng), speed, alt, time);
        }
    }

    public class TypeHandler extends Handler {
        public void handleMessage(Message message) {
            Bundle bundle = message.getData();
            int type = bundle.getInt(LocationUpdateService.TYPE_KEY);
            if (type == 0) {
                standingCount++;
            }
            if (type == 1) {
                walkingCount++;
            }
            if (type == 2) {
                runningCount++;
            }

            if (standingCount > walkingCount && standingCount > runningCount) {
                activityInt = 0;
            }
            if (walkingCount > standingCount && walkingCount > runningCount) {
                activityInt = 1;
            }
            if (runningCount > walkingCount && runningCount > standingCount) {
                activityInt = 2;
            }
            activity = getResources().getStringArray(R.array.activity_array)[activityInt];
            typeText.setText("Type: " + activity);
        }
    }

    // create an action bar button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // R.menu.mymenu is a reference to an xml file named mymenu.xml which should be inside your res/menu directory.
        // If you don't have res/menu, just create a directory named "menu" inside res
        if(history) {
            getMenuInflater().inflate(R.menu.mymenu, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    private class deleteEntryTask extends AsyncTask<Long, Void, Void> {
        protected Void doInBackground(Long... id) {
            new ExerciseEntryDbHelper(getApplicationContext()).removeEntry(id[0]);
            return null;
        }
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

package com.example.myruns;

import android.content.Context;

import com.google.android.gms.maps.model.LatLng;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ExerciseEntry {
    private Long id;
    private int inputType;  // Manual, GPS or automatic
    private int activityType;     // Running, cycling etc.
    private Calendar dateTime;    // When does this entry happen
    private int duration;         // Exercise duration in seconds
    private float distance;      // Distance traveled. Either in meters or feet.
    private float avgPace;       // Average pace
    private float avgSpeed;     // Average speed
    private int calorie;        // Calories burnt
    private float climb;         // Climb. Either in meters or feet.
    private int heartRate;       // Heart rate
    private int privacy;
    private String comment;       // Comments
    private ArrayList<LatLng> locationList; // Location list
    private Context context;


    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getInputType() {
        return inputType;
    }

    public void setInputType(int inputType) {
        this.inputType = inputType;
    }

    public int getActivityType() {
        return activityType;
    }

    public void setActivityType(int activityType) {
        this.activityType = activityType;
    }

    public Calendar getDateTime() {
        return dateTime;
    }

    public String getDateTimeSQL() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(dateTime.getTime());
    }

    public void setDateTimeSQL(String dateTimeString){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = sdf.parse(dateTimeString);
            dateTime = Calendar.getInstance();
            dateTime.setTime(date);
        }
        catch (Exception e){
            dateTime = Calendar.getInstance();
        }
    }

    public void setDateTime(Calendar dateTime) {
        this.dateTime = dateTime;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public float getDistance() {
        return distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getAvgPace() {
        return avgPace;
    }

    public void setAvgPace(float avgPace) {
        this.avgPace = avgPace;
    }

    public float getAvgSpeed() {
        return avgSpeed;
    }

    public void setAvgSpeed(float avgSpeed) {
        this.avgSpeed = avgSpeed;
    }

    public int getCalorie() {
        return calorie;
    }

    public void setCalorie(int calorie) {
        this.calorie = calorie;
    }

    public float getClimb() {
        return climb;
    }

    public void setClimb(float climb) {
        this.climb = climb;
    }

    public int getHeartRate() {
        return heartRate;
    }

    public void setHeartRate(int heartRate) {
        this.heartRate = heartRate;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public ArrayList<LatLng> getLocationList() {
        return locationList;
    }

    public void setLocationList(ArrayList<LatLng> locationList) {
        this.locationList = locationList;
    }

    public int getPrivacy() {
        return privacy;
    }

    public void setPrivacy(int privacy) {
        this.privacy = privacy;
    }

    public String stringLine1(){
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss MMM dd YYYY");
        if(activityType != -1) {
            return context.getResources().getStringArray(R.array.input_array)[inputType] + ": " + context.getResources().getStringArray(R.array.activity_array)[activityType]
                    + ", " + sdf.format(dateTime.getTime());
        }
        else{
            return context.getResources().getStringArray(R.array.input_array)[inputType] + ": " + "Unknown"
                    + ", " + sdf.format(dateTime.getTime());
        }
    }

    public String stringLine2Miles(){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        if(duration != 0) {
            return df.format(distance) + " Miles, " + duration + "mins 0secs";
        }
        else{
            return df.format(distance) + " Miles, 0secs";
        }
    }

    public String stringLine2KM(){
        DecimalFormat df = new DecimalFormat("#.##");
        df.setRoundingMode(RoundingMode.CEILING);
        if(duration != 0) {
            return df.format((distance * 1.60934)) + " KM, " + duration + "mins 0secs";
        }
        else{
            return df.format((distance * 1.60934)) + " KM, 0secs";
        }
    }
}

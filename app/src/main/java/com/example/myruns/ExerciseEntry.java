package com.example.myruns;

import com.google.android.gms.maps.model.LatLng;

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
}

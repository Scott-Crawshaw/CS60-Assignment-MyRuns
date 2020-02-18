package com.example.myruns;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.util.concurrent.ArrayBlockingQueue;

public class LocationUpdateService extends Service implements LocationListener, SensorEventListener {

    NotificationManager notificationManager;
    LocationManager locationManager;
    MapActivity.LocationHandler locationHandler;
    public static final String TYPE_KEY = "type";
    public static final String CHANNEL_ID = "notification channel";
    public static final int NOTIFY_ID = 11;
    public static final String LAT_KEY = "lat";
    public static final String LNG_KEY = "lng";
    public static final String SPEED_KEY = "spd";
    public static final String ALT_KEY = "alt";
    public static final String TIME_KEY = "time";
    public static final int ACCELEROMETER_BUFFER_CAPACITY = 2048;
    public static final int ACCELEROMETER_BLOCK_CAPACITY = 64;
    private static ArrayBlockingQueue<Double> mAccBuffer;
    MapActivity.TypeHandler typeHandler;
    private Sensor accelerometer;
    private SensorManager sensorManager;
    private UpdateLabelTask asyncTask;


    public void onCreate(){
        super.onCreate();
        notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        showNotification();
        mAccBuffer = new ArrayBlockingQueue<Double>(ACCELEROMETER_BUFFER_CAPACITY);

    }

    public void onDestroy(){
        super.onDestroy();
        if(locationManager != null) {
            locationManager.removeUpdates(this);
        }
        if (notificationManager != null) {
            notificationManager.cancel(NOTIFY_ID);
        }
        if (asyncTask != null) {
            asyncTask.cancel(true);
        }
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new locationBinder();
    }

    @Override
    public void onLocationChanged(Location location) {
        if(location == null){
            return;
        }
        double lat = location.getLatitude();
        double lng = location.getLongitude();
        double speed = location.getSpeed();
        double alt = location.getAltitude();
        long time = location.getElapsedRealtimeNanos();

        Bundle bundle = new Bundle();
        bundle.putDouble(LAT_KEY, lat);
        bundle.putDouble(LNG_KEY, lng);
        bundle.putDouble(SPEED_KEY, speed);
        bundle.putDouble(ALT_KEY, alt);
        bundle.putLong(TIME_KEY, time);
        Message message = locationHandler.obtainMessage();
        message.setData(bundle);
        locationHandler.sendMessage(message);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public int onStartCommand(Intent intent, int flags, int startId){
        return START_NOT_STICKY;
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION) {
            double m = Math.sqrt(event.values[0] * event.values[0] + event.values[1] * event.values[1] + event.values[2] * event.values[2]);
            try {
                mAccBuffer.add(new Double(m));
            } catch (IllegalStateException e) {
                ArrayBlockingQueue<Double> newBuf = new ArrayBlockingQueue<Double>(mAccBuffer.size() * 2);
                mAccBuffer.drainTo(newBuf);
                mAccBuffer = newBuf;
                mAccBuffer.add(new Double(m));
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void initTypeListener() {
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometer = sensorManager
                .getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

        sensorManager.registerListener(this, accelerometer,
                SensorManager.SENSOR_DELAY_FASTEST);
        asyncTask = new UpdateLabelTask();
        asyncTask.execute();
    }

    private class UpdateLabelTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            int blockSize = 0;
            FFT fft = new FFT(ACCELEROMETER_BLOCK_CAPACITY);
            double[] accBlock = new double[ACCELEROMETER_BLOCK_CAPACITY];
            double[] re = accBlock;
            double[] im = new double[ACCELEROMETER_BLOCK_CAPACITY];
            double max;

            while (true) {
                try {
                    if (isCancelled() == true) {
                        return null;
                    }
                    Double[] featVect = new Double[ACCELEROMETER_BLOCK_CAPACITY + 1];
                    int featVectCounter = 0;
                    accBlock[blockSize++] = mAccBuffer.take().doubleValue();

                    if (blockSize == ACCELEROMETER_BLOCK_CAPACITY) {
                        blockSize = 0;
                        max = .0;
                        for (double val : accBlock) {
                            if (max < val) {
                                max = val;
                            }
                        }

                        fft.fft(re, im);

                        for (int i = 0; i < re.length; i++) {
                            double mag = Math.sqrt(re[i] * re[i] + im[i]
                                    * im[i]);
                            featVect[featVectCounter] = Double.valueOf(mag);
                            featVectCounter++;
                            im[i] = .0;
                        }
                        featVect[featVectCounter] = Double.valueOf(max);
                        int option = (int) WekaClassifier.classify(featVect);
                        Bundle bundle = new Bundle();
                        bundle.putInt(TYPE_KEY, option);
                        Message message = typeHandler.obtainMessage();
                        message.setData(bundle);
                        if (typeHandler != null) {
                            typeHandler.sendMessage(message);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public class locationBinder extends Binder {
        public void setMessageHandler(MapActivity.LocationHandler locHandler){
            locationHandler = locHandler;
            initLocationManager();
        }

        public void setTypeHandler(MapActivity.TypeHandler tHandler) {
            typeHandler = tHandler;
            initTypeListener();
        }
    }

    public void initLocationManager(){
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            criteria.setAccuracy(Criteria.ACCURACY_FINE);
            String provider = locationManager.getBestProvider(criteria, true);
            Location location = locationManager.getLastKnownLocation(provider);
            onLocationChanged(location);
            locationManager.requestLocationUpdates(provider, 0, 0, this);
        }catch (SecurityException e){}
    }

    private void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "channel";
            String description = "myruns channel";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    public void showNotification(){
        createNotificationChannel();
        Intent intent = new Intent(this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                intent, 0);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        notificationBuilder.setContentTitle("MyRuns Location Tracker");
        notificationBuilder.setContentText("Tap here to see your progress");
        notificationBuilder.setContentIntent(pendingIntent);
        notificationBuilder.setAutoCancel(false);
        notificationBuilder.setSmallIcon(R.drawable.running);
        Notification notification = notificationBuilder.build();

        notificationManager.notify(NOTIFY_ID, notification);
    }
}

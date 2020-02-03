package com.example.myruns;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class ExerciseEntryDbHelper extends SQLiteOpenHelper {

    public static final String TABLE_EXERCISE = "exercise";
    private static final String DATABASE_NAME = "exercise.db";
    private static final int DATABASE_VERSION = 1;

    private final String CREATE_TABLE_ENTRIES = "CREATE TABLE IF NOT EXISTS "+TABLE_EXERCISE+" ("+
            "_id INTEGER PRIMARY KEY AUTOINCREMENT,"+
            "input_type INTEGER NOT NULL,"+
            "activity_type INTEGER NOT NULL,"+
            "date_time DATETIME NOT NULL,"+
            "duration INTEGER NOT NULL,"+
            "distance REAL,"+
            "avg_pace REAL,"+
            "avg_speed REAL,"+
            "calories INTEGER,"+
            "climb REAL,"+
            "heartrate INTEGER,"+
            "comment TEXT,"+
            "privacy INTEGER,"+
            "gps_data BLOB );";

    public ExerciseEntryDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EXERCISE);
        onCreate(db);
    }

    // Insert a item given each column value
    public long insertEntry(ExerciseEntry entry) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("input_type", entry.getInputType());
        contentValues.put("activity_type", entry.getActivityType());
        contentValues.put("date_time", entry.getDateTimeSQL());
        contentValues.put("duration", entry.getDuration());
        contentValues.put("distance", entry.getDistance());
        contentValues.put("avg_pace", entry.getAvgPace());
        contentValues.put("avg_speed", entry.getAvgSpeed());
        contentValues.put("calories", entry.getCalorie());
        contentValues.put("climb", entry.getClimb());
        contentValues.put("heartrate", entry.getHeartRate());
        contentValues.put("comment", entry.getComment());
        contentValues.put("privacy", entry.getPrivacy());
        contentValues.put("gps_data", new byte[0]);

        long rowID = db.insert(TABLE_EXERCISE, null, contentValues);
        db.close();
        return rowID;
    }

    // Remove an entry by giving its index
    public void removeEntry(long rowIndex) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_EXERCISE, "_id =" + rowIndex, null);
        db.close();
    }

    // Query a specific entry by its index.
    public ExerciseEntry fetchEntryByIndex(long rowId) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_EXERCISE + " where _id = " + String.valueOf(rowId), null);
        ExerciseEntry entry = null;
        if (cursor.moveToFirst()){
            entry = sqlToObject(cursor);
        }
        db.close();
        return entry;
    }

    private ExerciseEntry sqlToObject(Cursor sql){
        ExerciseEntry entry = new ExerciseEntry();
        entry.setId(sql.getLong(0));
        entry.setInputType(sql.getInt(1));
        entry.setActivityType(sql.getInt(2));
        entry.setDateTimeSQL(sql.getString(3));
        entry.setDuration(sql.getInt(4));
        entry.setDistance(sql.getInt(5));
        entry.setAvgPace(sql.getFloat(6));
        entry.setAvgSpeed(sql.getFloat(7));
        entry.setCalorie(sql.getInt(8));
        entry.setClimb(sql.getFloat(9));
        entry.setHeartRate(sql.getInt(10));
        entry.setComment(sql.getString(11));
        entry.setPrivacy(sql.getInt(12));
        entry.setLocationList(null);
        return entry;
    }

    // Query the entire table, return all rows
    public ArrayList<ExerciseEntry> fetchEntries() {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from " + TABLE_EXERCISE, null);
        ArrayList<ExerciseEntry> entries = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            entries.add(sqlToObject(cursor));
            cursor.moveToNext();
        }
        db.close();
        return entries;
    }

}

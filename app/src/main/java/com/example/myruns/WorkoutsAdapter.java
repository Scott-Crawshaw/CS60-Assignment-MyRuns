package com.example.myruns;


import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class WorkoutsAdapter extends RecyclerView.Adapter<WorkoutsAdapter.ViewHolder> {
    public Context context;
    public ArrayList<ExerciseEntry> data;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public Context context;
        public View view;

        public ViewHolder(View v, Context c) {
            super(v);
            view = v;
            context = c;
        }

        @Override
        public void onClick(View view){

        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public WorkoutsAdapter(Context c) {
        context = c;
        data = new ExerciseEntryDbHelper(c).fetchEntries();

    }

    public void updateData(){
        data = new ExerciseEntryDbHelper(context).fetchEntries();
    }

    // Create new views (invoked by the layout manager)
    @Override
    public WorkoutsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_cell, parent, false);
        WorkoutsAdapter.ViewHolder vh = new WorkoutsAdapter.ViewHolder(v, context);
        return vh;

    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        Log.e("scott", "worked");
        ExerciseEntry entry = data.get(position);
        ((TextView)holder.view.findViewById(R.id.firstLine)).setText(entry.stringLine1());
        SharedPreferences settingsPrefs = context.getSharedPreferences(context.getString(R.string.settingsPref), MODE_PRIVATE);
        if(settingsPrefs.getInt("unit", 1) == 0){
            ((TextView)holder.view.findViewById(R.id.secondLine)).setText(entry.stringLine2KM());
        }
        else{
            ((TextView)holder.view.findViewById(R.id.secondLine)).setText(entry.stringLine2Miles());
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return data.size();
    }
}

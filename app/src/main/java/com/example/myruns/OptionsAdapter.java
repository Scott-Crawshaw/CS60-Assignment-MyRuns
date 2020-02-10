package com.example.myruns;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.text.InputType;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;


public class OptionsAdapter extends RecyclerView.Adapter<OptionsAdapter.ViewHolder> {
    private String[] mDataset;
    private Context context;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public TextView textView;
        public Context context;

        public ViewHolder(TextView v, Context c) {
            super(v);
            textView = v;
            context = c;
            textView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            final String chosenOption = textView.getText().toString();
            int hour, minute, year, month, day;
            String content;
            SharedPreferences prefs = context.getSharedPreferences("manualEntry", Context.MODE_PRIVATE);
            Date date = new Date();
            LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            hour = prefs.getInt("hour", date.getHours());
            minute = prefs.getInt("minute", date.getMinutes());
            year = prefs.getInt("year", localDate.getYear());
            month = prefs.getInt("month", localDate.getMonth().getValue()-1);
            day = prefs.getInt("day", localDate.getDayOfMonth());
            content = prefs.getString(chosenOption, "");

            final DatePickerDialog datePicker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    SharedPreferences prefs = context.getSharedPreferences("manualEntry", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("year", year);
                    editor.putInt("month", month);
                    editor.putInt("day", dayOfMonth);
                    editor.commit();
                }
            }, year, month, day);
            final TimePickerDialog timePicker = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    SharedPreferences prefs = context.getSharedPreferences("manualEntry", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putInt("hour", hourOfDay);
                    editor.putInt("minute", minute);
                    editor.commit();
                }
            }, hour, minute, DateFormat.is24HourFormat(context));
            final EditText input = new EditText(context);
            if(chosenOption.equals("Date")){
                datePicker.show();
            }
            else if(chosenOption.equals("Time")){
                timePicker.show();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(textView.getText());
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                if (chosenOption.equals("Comment")) {
                    input.setHint("How did it go? Notes here.");
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setLines(3);
                } else {
                    input.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
                }
                input.setText(content);
                builder.setView(input);


                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        SharedPreferences prefs = context.getSharedPreferences("manualEntry", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = prefs.edit();
                        String text = input.getText().toString();
                        if (text.equals("")){text=null;}
                        editor.putString(chosenOption, text);
                        editor.commit();

                        dialog.cancel();
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public OptionsAdapter(String[] myDataset, Context c) {
        mDataset = myDataset;
        context = c;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public OptionsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        TextView v = (TextView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view, parent, false);
        ViewHolder vh = new ViewHolder(v, context);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.textView.setText(mDataset[position]);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }
}

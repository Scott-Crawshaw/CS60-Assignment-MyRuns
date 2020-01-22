package com.example.myruns;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.recyclerview.widget.RecyclerView;

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
            String chosenOption = textView.getText().toString();

            if(chosenOption.equals("Date")){
                DatePickerDialog picker = new DatePickerDialog(context);
                picker.show();

            }
            else if(chosenOption.equals("Time")){
                TimePickerDialog picker = new TimePickerDialog(context,null,0,0,false);
                picker.show();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(textView.getText());
                EditText input = new EditText(context);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                if (chosenOption.equals("Comment")) {
                    input.setHint("How did it go? Notes here.");
                    input.setInputType(InputType.TYPE_CLASS_TEXT);
                    input.setLines(3);
                } else {
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                }
                builder.setView(input);


                builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
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

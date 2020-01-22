package com.example.myruns;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;


public class StartFragment extends Fragment {

    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_start, container, false);

        Spinner inputSpinner = view.findViewById(R.id.input_spinner);
        ArrayAdapter<CharSequence> inputAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.input_array, android.R.layout.simple_spinner_item);
        inputAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputSpinner.setAdapter(inputAdapter);

        Spinner activitySpinner = view.findViewById(R.id.activity_spinner);
        ArrayAdapter<CharSequence> activityAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.activity_array, android.R.layout.simple_spinner_item);
        activityAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activitySpinner.setAdapter(activityAdapter);

        return view;
    }

    public void entryMode(){
        Spinner inputSpinner = view.findViewById(R.id.input_spinner);
        if(inputSpinner.getSelectedItemPosition() == 0){
            Intent manualEntry = new Intent(getActivity(), Manual.class);
            startActivity(manualEntry);
        }
        else{
            Intent map = new Intent(getActivity(), MapActivity.class);
            startActivity(map);
        }
    }
}

package com.example.myruns;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class HistoryFragment extends Fragment {
    View view;
    WorkoutsAdapter workoutsAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);

        RecyclerView workouts = view.findViewById(R.id.workouts);

        workouts.setHasFixedSize(false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        workouts.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(workouts.getContext(),
                layoutManager.getOrientation());
        workouts.addItemDecoration(mDividerItemDecoration);

        workoutsAdapter = new WorkoutsAdapter(getContext());
        workouts.setAdapter(workoutsAdapter);
        return view;

    }

}

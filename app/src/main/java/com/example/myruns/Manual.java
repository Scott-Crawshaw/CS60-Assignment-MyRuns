package com.example.myruns;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

public class Manual extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual);
        RecyclerView options = findViewById(R.id.options);

        options.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        options.setLayoutManager(layoutManager);

        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(options.getContext(),
                layoutManager.getOrientation());
        options.addItemDecoration(mDividerItemDecoration);

        OptionsAdapter optionsAdapter = new OptionsAdapter(getResources().getStringArray(R.array.manual_array), this);
        options.setAdapter(optionsAdapter);
    }

    public void killActivity(View v){
        finish();
    }
}

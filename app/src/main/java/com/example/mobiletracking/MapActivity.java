package com.example.mobiletracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MapActivity extends AppCompatActivity {
    private MyMapFragment myMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        MapActivity oui = this;
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        this.myMapFragment = (MyMapFragment) fragmentManager.findFragmentById(R.id.fragment_map);

        final Button button = (Button) findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                myMapFragment.newPos(14.0583,108.2772);
                Toast.makeText(oui, "CLICK", Toast.LENGTH_LONG).show();
            }
        });
    }
}
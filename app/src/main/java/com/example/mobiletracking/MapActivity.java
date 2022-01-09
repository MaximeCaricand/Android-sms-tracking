package com.example.mobiletracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

public class MapActivity extends AppCompatActivity {
    private MyMapFragment myMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        this.myMapFragment = (MyMapFragment) fragmentManager.findFragmentById(R.id.fragment_map);
    }
}
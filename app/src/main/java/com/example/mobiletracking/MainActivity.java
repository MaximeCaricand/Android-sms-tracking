package com.example.mobiletracking;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.dbHandler = new DBHelper(getApplicationContext());
    }

    public void openPedometer(View view) {
        Intent settingsIntent = new Intent(this, PedometerActivity.class);
        startActivity(settingsIntent);
    }

    public void openTracker(View view) {
        Intent settingsIntent = new Intent(this, TrackerActivity.class);
        startActivity(settingsIntent);
    }

    public void openMap(View view) {
        Intent settingsIntent = new Intent(this, MapActivity.class);
        startActivity(settingsIntent);
    }

    public void clearDB(View view) {
        this.dbHandler.clearTable();
    }
}
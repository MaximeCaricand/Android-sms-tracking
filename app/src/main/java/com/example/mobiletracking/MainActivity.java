package com.example.mobiletracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHandler;
    private static final int PERMISSION_REQUEST_CODE = 2;
    private static final String[] PERMISSION_LIST = {
            Manifest.permission.SEND_SMS,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.INTERNET,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.dbHandler = new DBHelper(getApplicationContext());
        this.HandlePermissions();
    }

    public void openPedometer(View view) {
        Intent settingsIntent = new Intent(this, PedometerActivity.class);
        startActivity(settingsIntent);
    }

    public void openTracker(View view) {
        Intent settingsIntent = new Intent(this, TrackerActivity.class);
        startActivity(settingsIntent);
    }

    public void clearDB(View view) {
        this.dbHandler.clearTable();
    }

    @SuppressLint("NewApi")
    private void HandlePermissions() {
        if (android.os.Build.VERSION.SDK_INT >=  android.os.Build.VERSION_CODES.M) {

            boolean needPermission = Arrays.stream(PERMISSION_LIST)
                    .map((el) -> ActivityCompat.checkSelfPermission(this, el) != PackageManager.PERMISSION_GRANTED)
                    .reduce(false, (cur, acc) -> acc || cur);

            if (needPermission) {
                this.requestPermissions(PERMISSION_LIST, PERMISSION_REQUEST_CODE);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                Toast.makeText(
                        this,
                        "Permission " + ((grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) ? "granted" :"denied" + " !"),
                        Toast.LENGTH_LONG
                ).show();
                break;
            }
        }
    }
}
package com.example.mobiletracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    DBHelper dbHandler;
    private static final int MY_PERMISSION_REQUEST_CODE_SMS = 1;
    private static final String SMS_PERMISSION = Manifest.permission_group.SMS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.dbHandler = new DBHelper(getApplicationContext());
        this.askPermissionAndSendSMS();
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

    private void askPermissionAndSendSMS() {
        if (android.os.Build.VERSION.SDK_INT >=  android.os.Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(this, SMS_PERMISSION) != PackageManager.PERMISSION_GRANTED) {
                this.requestPermissions(new String[]{SMS_PERMISSION}, MY_PERMISSION_REQUEST_CODE_SMS);
                return;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_CODE_SMS: {
                String context = "";
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    context = "granted !";
                else
                    context = "denied !";
                Toast.makeText(this, "Permission " + context, Toast.LENGTH_LONG).show();
                break;
            }
        }
    }
}
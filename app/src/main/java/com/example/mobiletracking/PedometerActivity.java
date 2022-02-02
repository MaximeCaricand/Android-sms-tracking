package com.example.mobiletracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class PedometerActivity extends AppCompatActivity implements SensorEventListener {

    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 2;

    private static int startCounter = -1;
    private static int currentCounter = 0;

    private TextView tvPedometer;
    private SensorManager sensorManager;

    private Sensor pedometer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        tvPedometer = findViewById(R.id.tv_pedometer);
        updateTextView();

        checkPermission();
        sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        pedometer = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, pedometer, SensorManager.SENSOR_DELAY_FASTEST);
    }

    private boolean checkPermission() {

        if (ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            ActivityCompat.requestPermissions(this,
                new String[]{Manifest.permission.ACTIVITY_RECOGNITION},
                MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION);

            return ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACTIVITY_RECOGNITION
            ) == PackageManager.PERMISSION_GRANTED;
        }
    }

    private void updateTextView() {
        int tmp = currentCounter - startCounter;
        if(tmp < 0)
            tmp = 0;
        String msg = tmp + " steps";
        tvPedometer.setText(msg);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;
        float[] values = event.values;

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if(startCounter == -1)
                startCounter = (int) values[0];
            currentCounter = (int) values[0];
            updateTextView();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
}
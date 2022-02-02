package com.example.mobiletracking;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;

public class PedometerActivity extends AppCompatActivity implements SensorEventListener {

    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 2;

    private static int startCounter = -1;
    private static int currentCounter = 0;

    private TextView tvPedometer;
    private SensorManager sensorManager;

    private Sensor pedometer;

    //Variables pour gérer l'envoi de sms tous les 15s
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 15 * 1000;
    private boolean isFollowed;

    //Variables pour gérer la réception et l'envoi de sms
    private final String START_TRACKING_MESSAGE = "start_tracking";
    private final String STOP_TRACKING_MESSAGE = "stop_tracking";
    private final String SMS_BR_INTENT_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private final String NEW_POSTION_MESSAGE_SUFFIX = "newPos";
    private final String PDUS = "pdus";
    private String trackerPhoneNumber;
    private BroadcastReceiver smsReceiver;
    private LocationManager lm;
    private LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            lm.removeUpdates(this);
        }
        public void onProviderDisabled(String provider) {}
        public void onProviderEnabled(String provider) {}
        public void onStatusChanged(String provider, int status, Bundle extras) {}
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        tvPedometer = findViewById(R.id.tv_pedometer);
        updateTextView();

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        pedometer = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        sensorManager.registerListener(this, pedometer, SensorManager.SENSOR_DELAY_FASTEST);

        isFollowed = false;
        smsReceiver = new BroadcastReceiver() {
            @Override
            @SuppressLint("NewApi")
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                byte[][] pdus = (byte[][]) bundle.get(PDUS);
                if (pdus != null) {
                    for (byte[] pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu(pdu, bundle.getString("format"));

                        if (smsMessage.getMessageBody().equals(START_TRACKING_MESSAGE)) {//&& isFollowed == false) {
                            new AlertDialog.Builder(context)
                                    .setTitle("Someone want follow you !")
                                    .setMessage("If you accept, the number " + smsMessage.getOriginatingAddress() + " will be able to follow your movements.")
                                    .setPositiveButton("Agree", (dialog, which) -> {
                                        trackerPhoneNumber = smsMessage.getOriginatingAddress();
                                        isFollowed = true;
                                        Toast.makeText(context, "ACCEPT", Toast.LENGTH_SHORT).show();
                                        dialog.dismiss();
                                    })
                                    .setNegativeButton("Disagree", (dialog, which) -> dialog.dismiss())
                                    .show();
                        }
                        if (smsMessage.getMessageBody().equals(STOP_TRACKING_MESSAGE)) {
                            trackerPhoneNumber = "";
                            isFollowed = false;
                            Toast.makeText(context, "You are no longer followed.", Toast.LENGTH_LONG).show();
                        }

                        Toast.makeText(context, smsMessage.getMessageBody(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        };
        registerReceiver(smsReceiver, new IntentFilter(SMS_BR_INTENT_ACTION));
    }

    private void updateTextView() {
        int tmp = currentCounter - startCounter;
        if (tmp < 0)
            tmp = 0;
        String msg = tmp + " steps";
        tvPedometer.setText(msg);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        Sensor sensor = event.sensor;
        float[] values = event.values;

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (startCounter == -1)
                startCounter = (int) values[0];
            currentCounter = (int) values[0];
            updateTextView();
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {
    }

    @Override
    public void onResume() {
        //start handler as activity become visible
        handler.postDelayed(runnable = new Runnable() {
            public void run() {
                sendPositionToFollower();

                handler.postDelayed(runnable, delay);
            }
        }, delay);

        super.onResume();
    }

    @Override
    public void onPause() {
        handler.removeCallbacks(runnable); //stop handler when activity not visible

        super.onPause();
    }

    private void sendPositionToFollower() {
        try {
            String messPosition = "";
            messPosition += NEW_POSTION_MESSAGE_SUFFIX;

            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            if (lm != null) {
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    String coordonnees = String.format("Latitude : %f - Longitude : %f\n", location.getLatitude(), location.getLongitude());

                    Toast.makeText(getApplicationContext(), coordonnees, Toast.LENGTH_LONG).show();
                }
            }

            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(trackerPhoneNumber, null, messPosition, null, null);
            Toast.makeText(getApplicationContext(), "SMS envoyé à " + trackerPhoneNumber, Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Erreur d'envoi de SMS" + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
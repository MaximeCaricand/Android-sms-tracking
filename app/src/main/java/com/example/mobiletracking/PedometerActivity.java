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
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
//import com.google.android.gms.location.LocationResult;

public class PedometerActivity extends AppCompatActivity implements SensorEventListener {

    private static final int MY_PERMISSIONS_REQUEST_ACTIVITY_RECOGNITION = 2;

    private static int startCounter = -1;
    private static int currentCounter = 0;

    private DBHelper dbHelper;
    private int numberStep;
    private float currentTimeSpeed;
    private float distTraveled;
    private float avgTimeSpeed;

    private TextView tvPedometer;
    private TextView tvCurrSpeed;
    private TextView tvAvgSpeed;
    private TextView tvDistance;
    private SensorManager sensorManager;

    private Sensor pedometer;
    private Location location;

    //Variables pour gérer l'envoi de sms tous les 15s
    private Handler handler = new Handler();
    private Runnable runnable;
    private int delay = 7 * 1000;
    private Date now;
    private boolean isFollowed;

    //Variables pour gérer la réception et l'envoi de sms
    private String trackerPhoneNumber;
    private BroadcastReceiver smsReceiver;
    private LocationManager lm;
    private LocationListener locationListenerGps = new LocationListener() {
        public void onLocationChanged(Location location) {
            lm.removeUpdates(this);
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedometer);

        dbHelper = new DBHelper(getApplicationContext());

        tvPedometer = findViewById(R.id.tv_pedometer);
        tvCurrSpeed = findViewById(R.id.tv_curr_speed);
        tvAvgSpeed  = findViewById(R.id.tv_avg_speed);
        tvDistance  = findViewById(R.id.tv_distance);
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
            byte[][] pdus = (byte[][]) bundle.get(getString(R.string.PDUS));
            if (pdus != null) {
                for (byte[] pdu : pdus) {
                    SmsMessage smsMessage = SmsMessage.createFromPdu(pdu, bundle.getString("format"));

                    if (smsMessage.getMessageBody().equals(getString(R.string.START_TRACKING_MESSAGE))) {//&& isFollowed == false) {
                        new AlertDialog.Builder(context)
                                .setTitle(getString(R.string.alertTitle))
                                .setMessage(getString(R.string.messPart1) + smsMessage.getOriginatingAddress() + getString(R.string.messPart2))
                                .setPositiveButton(getString(R.string.agree), (dialog, which) -> {
                                    trackerPhoneNumber = smsMessage.getOriginatingAddress();
                                    isFollowed = true;
                                    Toast.makeText(context, getString(R.string.startFollow), Toast.LENGTH_SHORT).show();
                                    sendPositionToFollower();
                                    dialog.dismiss();
                                })
                                .setNegativeButton(getString(R.string.disagree), (dialog, which) -> dialog.dismiss())
                                .show();
                    }
                    if (smsMessage.getMessageBody().equals(getString(R.string.STOP_TRACKING_MESSAGE))) {
                        trackerPhoneNumber = "";
                        isFollowed = false;
                        Toast.makeText(context, getString(R.string.endFollow), Toast.LENGTH_LONG).show();
                    }

                    //Toast.makeText(context, smsMessage.getMessageBody(), Toast.LENGTH_LONG).show();
                }
            }
            }
        };

        registerReceiver(smsReceiver, new IntentFilter(getString(R.string.SMS_BR_INTENT_ACTION)));
        new Timer().scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                now = new Date();

                float tmpSpeed = 0;
                if(location != null) {
                    currentTimeSpeed = location.getSpeed();
                    tmpSpeed = currentTimeSpeed;
                }
                Walker walker = new Walker(numberStep, tmpSpeed, distTraveled, now.getTime());
                dbHelper.addWalker(walker);
                updateTextViews();
            }
        }, 1000, 1000);
    }

    private float getTotalDistance() {
        ArrayList<Position> positions = dbHelper.getAllPositions();
        float totalDistance = 0;
        if(positions.size() < 2)
            return totalDistance;
        Position lastP = positions.get(0);
        for(int i=1; i<positions.size(); i++) {
            Position newP = positions.get(i);
            totalDistance += calcDistance(lastP, newP);
            lastP = newP;
        }
        return totalDistance;
    }

    private float calcDistance(Position p1, Position p2) {
        double R = 6371e3;
        double lat1 = p1.getLat() * Math.PI/180;
        double lat2 = p1.getLat() * Math.PI/180;
        double deltaLat = (p2.getLat()-p1.getLat()) * Math.PI/180;
        double deltaLng = (p2.getLng()-p1.getLng()) * Math.PI/180;

        double a = Math.sin(deltaLat/2) * Math.sin(deltaLat/2) +
                    Math.cos(lat1) * Math.cos(lat2) *
                        Math.sin(deltaLng/2) * Math.sin(deltaLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return (float)(R * c);
    }

    private void updateTextView() {
        String msg = this.numberStep + " PAS";
        tvPedometer.setText(msg);
    }

    private void updateTextViews() {
        String msg = "actuel : " + this.currentTimeSpeed + " m/s";
        this.tvCurrSpeed.setText(msg);
        msg = "moyenne : " + 5 + " m/s";
        this.tvAvgSpeed.setText(msg);
        msg = Math.floor(this.distTraveled) + " m";
        this.tvDistance.setText(msg);
    }

    public void saveDataWalker(View view) {
        now = new Date();
        //add walker information
        Walker walker = new Walker((int)(Math.random()*100), (float)(Math.random()*20), (float)(Math.random()*100), now.getTime());
        dbHelper.addWalker(walker);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        float[] values = event.values;

        if (sensor.getType() == Sensor.TYPE_STEP_COUNTER) {
            if (startCounter == -1)
                startCounter = (int) values[0];
            currentCounter = (int) values[0];

            this.numberStep = currentCounter - startCounter;
            if(this.numberStep < 0)
                this.numberStep = 0;
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
                if (isFollowed) {
                    now = new Date();
                    //add walker information
                    Walker walker = new Walker(numberStep, currentTimeSpeed, distTraveled, now.getTime());
                    dbHelper.addWalker(walker);
                    //send sms to tracker
                    sendPositionToFollower();
                }
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
            messPosition += getString(R.string.NEW_POSITION_MESSAGE_PREFIX)+";";

            lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }

            String coordonnees = String.format("%f;%f;\n", 0., 0.);

            lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListenerGps);
            if (lm != null) {
                this.location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    coordonnees = String.format("%f;%f;\n", location.getLatitude(), location.getLongitude());
                }

                messPosition += coordonnees.replace(',','.') + now.getTime();

                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(trackerPhoneNumber, null, messPosition, null, null);
                Toast.makeText(getApplicationContext(), getString(R.string.sendSMS) + trackerPhoneNumber + " -> " + messPosition, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.errorLocalisation), Toast.LENGTH_LONG).show();
            }

        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), getString(R.string.errorSMS) + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
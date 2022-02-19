package com.example.mobiletracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class TrackerActivity extends AppCompatActivity {

    private EditText etPhoneNumber;
    private Button buttonSend;
    private String trackedPhoneNumber;
    private TextView lastUpdateTV;

    private BroadcastReceiver smsReceiver;
    private TrackerMapsFragment trackerMapsFragment;

    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        this.lastUpdateTV = this.findViewById(R.id.lastUpdate);
        this.etPhoneNumber = this.findViewById(R.id.editText_phoneNumber);
        this.buttonSend = this.findViewById(R.id.button_send);
        this.buttonSend.setOnClickListener((View v) -> this.updateTrackingState());
        this.updateButtonText();

        this.dbHelper = new DBHelper(getApplicationContext());

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        this.trackerMapsFragment = (TrackerMapsFragment) fragmentManager.findFragmentById(R.id.fragment_map);

        smsReceiver = new BroadcastReceiver() {
            @Override
            @SuppressLint("NewApi")
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                byte[][] pdus = (byte[][]) bundle.get(getString(R.string.PDUS));
                if (pdus != null) {
                    for (byte[] pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu(pdu, bundle.getString("format"));
                        if (trackedPhoneNumber != null && smsMessage.getOriginatingAddress().endsWith(trackedPhoneNumber)) {
                            String[] messageData = smsMessage.getMessageBody().split(";");
                            if (messageData.length == 4 && messageData[0].equals(getString(R.string.NEW_POSTION_MESSAGE_SUFFIX))) {
                                Position newPos = new Position(
                                        trackedPhoneNumber,
                                        Double.parseDouble(messageData[1]),
                                        Double.parseDouble(messageData[2]),
                                        Long.parseLong(messageData[3])
                                );
                                trackerMapsFragment.updateCurrentPosition(newPos.getLatLng());
                                lastUpdateTV.setText("(Last update: " + newPos.getHourFormat() + ")");
                                dbHelper.addPosition(newPos);
                            }
                        }
                    }
                }
            }
        };
    }

    @Override
    protected void onResume() {
        registerReceiver(smsReceiver, new IntentFilter(getString(R.string.SMS_BR_INTENT_ACTION)));
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(smsReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if(this.trackingON()) {
            Toast.makeText(getApplicationContext(), this.getString(R.string.askStopTracking), Toast.LENGTH_LONG).show();
            this.askStopTracking();
        }
        super.onDestroy();
    }

    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putString("trackedPhoneNumber", this.trackedPhoneNumber);
    }

    public void onRestoreInstanceState(Bundle bundle) {
        this.trackedPhoneNumber = bundle.getString("trackedPhoneNumber");
        this.etPhoneNumber.setText(this.trackedPhoneNumber);
        this.updateButtonText();
    }

    public void quit(View view) {
        this.finish();
    }

    private boolean trackingON() {
        System.out.println(this.trackedPhoneNumber);
        return this.trackedPhoneNumber != null;
    }

    private void updateTrackingState() {
        if (this.trackingON()) this.askStopTracking();
        else this.askStartTracking();
    }

    private void askStartTracking() {
        String phoneNumber = this.etPhoneNumber.getText().toString();
        if (!phoneNumber.equals("")) {
            this.trackedPhoneNumber = phoneNumber;
            this.sendSMS(phoneNumber, getString(R.string.START_TRACKING_MESSAGE));
        }
    }

    private void askStopTracking() {
        String lastPhoneNumber = this.trackedPhoneNumber;
        this.trackedPhoneNumber = null;
        this.sendSMS(lastPhoneNumber, getString(R.string.STOP_TRACKING_MESSAGE));
    }

    private void updateButtonText() {
        if (this.trackingON()) this.buttonSend.setText(this.getString(R.string.stopTracking));
        else this.buttonSend.setText(this.getString(R.string.askTracking));
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage("+33" + phoneNumber, null, message, null, null);
            this.updateButtonText();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Erreur d'envoi de SMS" + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
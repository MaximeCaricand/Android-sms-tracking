package com.example.mobiletracking;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TrackerActivity extends AppCompatActivity {

    // move this to android values
    private final String START_TRACKING_MESSAGE = "start_tracking";
    private final String STOP_TRACKING_MESSAGE = "stop_tracking";
    private final String SMS_BR_INTENT_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    private final String NEW_POSTION_MESSAGE_SUFFIX = "newPos";
    private final String PDUS = "newPos";


    private EditText etPhoneNumber;
    private Button buttonSend;
    private String trackedPhoneNumber;

    private BroadcastReceiver smsReceiver;
    private TrackerMapsFragment trackerMapsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        this.etPhoneNumber = this.findViewById(R.id.editText_phoneNumber);
        this.buttonSend = this.findViewById(R.id.button_send);
        this.buttonSend.setOnClickListener((View v) -> this.updateTrackingState());
        this.updateButtonText();

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        this.trackerMapsFragment = (TrackerMapsFragment) fragmentManager.findFragmentById(R.id.fragment_map);

        smsReceiver = new BroadcastReceiver() {
            @Override
            @SuppressLint("NewApi")
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                byte[][] pdus = (byte[][]) bundle.get(PDUS);
                if (pdus != null) {
                    for (byte[] pdu : pdus) {
                        SmsMessage smsMessage = SmsMessage.createFromPdu(pdu, bundle.getString("format"));
                        if (trackedPhoneNumber != null && smsMessage.getOriginatingAddress().endsWith(trackedPhoneNumber)) {
                            String[] messageData = smsMessage.getMessageBody().split(";");
                            if (messageData.length == 4 && messageData[0].equals(NEW_POSTION_MESSAGE_SUFFIX)) {
                                trackerMapsFragment.updateCurrentPosition(Double.parseDouble(messageData[1]), Double.parseDouble(messageData[2]));
                            }
                            Toast.makeText(context, smsMessage.getMessageBody(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        };
        registerReceiver(smsReceiver, new IntentFilter(SMS_BR_INTENT_ACTION));
    }

    @Override
    protected void onPause() {
        unregisterReceiver(smsReceiver);
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(this.trackingON()) {
            this.askStopTracking();
        }
    }

    protected void openHistory(View v) {

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
            this.sendSMS(phoneNumber, this.START_TRACKING_MESSAGE);
        }
    }

    private void askStopTracking() {
        String lastPhoneNumber = this.trackedPhoneNumber;
        this.trackedPhoneNumber = null;
        this.sendSMS(lastPhoneNumber, this.STOP_TRACKING_MESSAGE);
    }

    private void updateButtonText() {
        if (this.trackingON()) this.buttonSend.setText("Arrêter le suivi");
        else this.buttonSend.setText("Demander le suivi");
    }

    private void sendSMS(String phoneNumber, String message) {
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "SMS envoyé", Toast.LENGTH_LONG).show();
            this.updateButtonText();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Erreur d'envoi de SMS" + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
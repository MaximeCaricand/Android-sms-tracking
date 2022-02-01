package com.example.mobiletracking;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class TrackerActivity extends AppCompatActivity {

    private final String message = "Acceptez-vous de vous faire suivre ? \n Envoyez 'Oui' pour lancer le suivi.";
    private EditText editTextPhoneNumber;
    private Button buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        this.editTextPhoneNumber = this.findViewById(R.id.editText_phoneNumber);
        this.buttonSend = this.findViewById(R.id.button_send);
        this.buttonSend.setOnClickListener((View v) -> this.sendSMS_by_smsManager());
    }

    private void sendSMS_by_smsManager() {
        String phoneNumber = this.editTextPhoneNumber.getText().toString();
        try {
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phoneNumber, null, message, null, null);
            Toast.makeText(getApplicationContext(), "Your sms has successfully sent!", Toast.LENGTH_LONG).show();
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), "Your sms has failed... " + ex.getMessage(), Toast.LENGTH_LONG).show();
            ex.printStackTrace();
        }
    }
}
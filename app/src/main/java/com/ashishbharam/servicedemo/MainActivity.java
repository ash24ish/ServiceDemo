package com.ashishbharam.servicedemo;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
public class MainActivity extends AppCompatActivity {

    Button btnStartMyService, btnStopMyService;
    private Intent serviceIntent;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "if MainActivity thread ID: " + Thread.currentThread().getId());

        btnStartMyService = findViewById(R.id.btnStartService);
        btnStopMyService = findViewById(R.id.btnStopService);

        serviceIntent = new Intent(getApplicationContext(), MyService.class);

        btnStartMyService.setOnClickListener(v -> {
            Log.i("TAG", "Service Started: ");
            //startService(serviceIntent);
            serviceIntent.putExtra("starter","starter"+(++count));
            MyService.enqueueWork(this, serviceIntent);
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        });

        btnStopMyService.setOnClickListener(v -> {
            Log.i("TAG", "Service can not be Stopped by user. ");
            stopService(serviceIntent);
            Toast.makeText(this, "Service will stop when task is done", Toast.LENGTH_SHORT).show();
        });
    }
}
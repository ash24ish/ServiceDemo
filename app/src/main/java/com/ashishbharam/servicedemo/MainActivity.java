package com.ashishbharam.servicedemo;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
public class MainActivity extends AppCompatActivity {

    Button btnStartMyService, btnStopMyService;
    private Intent serviceIntent;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "if MainActivity thread ID is same as service: " + Thread.currentThread().getId());
        Log.i("TAG", "that means MainActivity & MyService is running on same thread ");

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
            Log.i("TAG", "Service Stopped: ");
            stopService(serviceIntent);
            Toast.makeText(this, "Service will stop when task is done", Toast.LENGTH_SHORT).show();
        });
    }
}
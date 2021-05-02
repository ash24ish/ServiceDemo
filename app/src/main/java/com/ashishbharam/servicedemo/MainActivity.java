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

public class MainActivity extends AppCompatActivity {

    MyService myService;
    Button btnStartMyService, btnStopMyService, btnUnbind, btnBind, btnShowNumber;
    TextView tvShowNumber;
    private Intent serviceIntent;

    private boolean isServiceBound;
    private ServiceConnection serviceConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "if MainActivity thread ID is same as service: " + Thread.currentThread().getId());
        Log.i("TAG", "that means MainActivity & MyService is running on same thread ");

        btnStartMyService = findViewById(R.id.btnStartService);
        btnStopMyService = findViewById(R.id.btnStopService);
        btnBind = findViewById(R.id.btnBindService);
        btnUnbind = findViewById(R.id.btnUnbindService);
        btnShowNumber = findViewById(R.id.btnShowNumber);
        tvShowNumber = findViewById(R.id.tvRandomNum);

        serviceIntent = new Intent(getApplicationContext(), MyService.class);

        btnStartMyService.setOnClickListener(v -> {
            Log.i("TAG", "Service Started: ");
            startService(serviceIntent);
        });

        btnStopMyService.setOnClickListener(v -> {
            Log.i("TAG", "Service Stopped: ");
            stopService(serviceIntent);
        });

        btnBind.setOnClickListener(v -> {
            if (serviceConnection == null) {
                serviceConnection = new ServiceConnection() {
                    @Override
                    public void onServiceConnected(ComponentName name, IBinder iBinder) {
                        MyService.MyServiceBinder myServiceBinder = (MyService.MyServiceBinder) iBinder;
                        myService = myServiceBinder.getService();
                        isServiceBound = true;
                    }

                    @Override
                    public void onServiceDisconnected(ComponentName name) {
                        isServiceBound = false;
                    }
                };
            }
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        });

        btnUnbind.setOnClickListener(v -> {
            if (isServiceBound) {
                unbindService(serviceConnection);
                isServiceBound = false;
            }
        });

        btnShowNumber.setOnClickListener(v -> {
            if (isServiceBound) {
                tvShowNumber.setText("" + myService.getRandomNumber());
            } else {
                tvShowNumber.setText("Service not Bound");
            }
        });
    }
}
package com.ashishbharam.servicedemo;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ComponentInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnStartMyService, btnStopMyService;
    private JobScheduler jobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.i("TAG", "if MainActivity thread ID: " + Thread.currentThread().getId());

        btnStartMyService = findViewById(R.id.btnStartService);
        btnStopMyService = findViewById(R.id.btnStopService);

        jobScheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);

        btnStartMyService.setOnClickListener(v -> {
            Log.i("TAG", "Service Started: ");
            startJob();
            Toast.makeText(this, "Service Started", Toast.LENGTH_SHORT).show();
        });

        btnStopMyService.setOnClickListener(v -> {
            Log.i("TAG", "Service can not be Stopped by user. ");
            stopJob();
            Toast.makeText(this, "Service will stop when task is done", Toast.LENGTH_SHORT).show();
        });
    }

    private void startJob() {
        ComponentName componentName = new ComponentName(this, MyService.class);
        JobInfo jobInfo = new JobInfo.Builder(123, componentName)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_CELLULAR)
                .setRequiresCharging(false)
                .setPeriodic(15 * 60 * 1000)
                .setPersisted(true)
                .build();
        if (jobScheduler.schedule(jobInfo) == JobScheduler.RESULT_SUCCESS) {
            Log.i("TAG", "Job Scheduled Successfully : MainActivity Thread ID: " + Thread.currentThread().getId());
        } else {
            Log.i("TAG", "Job NOT Scheduled : MainActivity Thread ID: " + Thread.currentThread().getId());
        }
    }

    private void stopJob() {
        jobScheduler.cancel(123);
    }
}
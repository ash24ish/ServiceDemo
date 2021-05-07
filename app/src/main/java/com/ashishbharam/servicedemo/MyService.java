package com.ashishbharam.servicedemo;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.util.Random;

public class MyService extends JobService {
    private int mRandomNumber;
    private boolean mRandomNumberOn;

    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("TAG", "onStartJob");
        doItOnBackgroundThread();
        //return true for long running task;
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        Log.i("TAG", "OnStopJob: ");
        return false;
        //return true to reschedule task
    }

    @Override
    public void onDestroy() {
        mRandomNumberOn = false;
        super.onDestroy();
        Log.i("TAG", "Service Destroyed Automatically");
    }

    private void doItOnBackgroundThread() {
        new Thread(() -> {
            mRandomNumberOn = true;
            startRandomNumberGenerator();
        }).start();
    }

    private void startRandomNumberGenerator() {
        while (mRandomNumberOn) {
            try {
                Thread.sleep(1000);
                mRandomNumber = new Random().nextInt(999 - 99) + 99;
                Log.i("TAG", "startRandomNumberGenerator Thread id: "
                        + Thread.currentThread().getId()
                        + " Random num:" + mRandomNumber);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.i("TAG", "Thread Interrupted :");

            }
        }
    }
}

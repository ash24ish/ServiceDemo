package com.ashishbharam.servicedemo;


import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;

import java.util.Random;

public class MyService extends JobIntentService {
    private int mRandomNumber;
    private boolean isRandomGeneratorOn;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TAG", "In onBind(): ");
        return super.onBind(intent);
    }

    static void enqueueWork(Context context, Intent intent){
        enqueueWork(context,MyService.class, 123, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        isRandomGeneratorOn = true;
        startRandomNumberGenerator();
        Log.i("TAG", "In onHandleWork, thread ID: " + Thread.currentThread().getId());
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.i("TAG", "onStopCurrentWork Thread id: " + Thread.currentThread().getId()
                + " Random num:" + mRandomNumber);
        return super.onStopCurrentWork();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "Service Destroyed");
        stopRandomNumberGenerator();
    }


    private void startRandomNumberGenerator() {
        while (isRandomGeneratorOn) {
            try {
                Thread.sleep(1000);
                if (isRandomGeneratorOn) {
                    mRandomNumber = new Random().nextInt(999 - 99) + 99;
                    Log.i("TAG", "startRandomNumberGenerator Thread id: " + Thread.currentThread().getId()
                            + " Random num:" + mRandomNumber);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("TAG", "Thread Interrupted :" + e.getLocalizedMessage());

            }
        }
    }

    public void stopRandomNumberGenerator() {
        isRandomGeneratorOn = false;
    }

}

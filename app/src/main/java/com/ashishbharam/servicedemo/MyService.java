package com.ashishbharam.servicedemo;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import java.util.Random;

//Self stopping service
public class MyService extends JobIntentService {
    private int mRandomNumber;

    static void enqueueWork(Context context, Intent intent) {
        enqueueWork(context, MyService.class, 123, intent);
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        startRandomNumberGenerator(intent.getStringExtra("starter"));
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
    }

    @Override
    public IBinder onBind(@NonNull Intent intent) {
        Log.i("TAG", "In onBind(): ");
        return super.onBind(intent);
    }

    private void startRandomNumberGenerator(String starter) {
        for (int i = 0; i < 5; i++) {
            try {
                if (isStopped()) {
                    Log.i("TAG", "JobScheduler is force stopping the Service stopped" + starter);
                   return;
                }
                Thread.sleep(1000);
                mRandomNumber = new Random().nextInt(999 - 99) + 99;
                Log.i("TAG", "startRandomNumberGenerator Thread id: "
                        + Thread.currentThread().getId()
                        + " Random num:" + mRandomNumber
                        + " " + starter);
            } catch (InterruptedException e) {
                e.printStackTrace();
                Log.d("TAG", "Thread Interrupted : starter" + starter);

            }
        }
        Log.i("TAG", "Service stopped" + starter);
        stopSelf();
    }

}

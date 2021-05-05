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
        Log.i("TAG", "In onBind(): Executes only once, Components can be bind to both started,stopped or unstarted service");
        return super.onBind(intent);
    }

    static void enqueueWork(Context context, Intent intent){
        enqueueWork(context,MyService.class, 123, intent);
    }
    /*@Override
    protected void onHandleIntent(@Nullable Intent intent) {
        //this method of IntentService class runs on background.
        //this class is deprecated in API level 30. Use JobIntentService class & its method onHandleWork().

    }*/

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        isRandomGeneratorOn = true;
        startRandomNumberGenerator();
        Log.i("TAG", "In onHandleIntent, thread ID: " + Thread.currentThread().getId());
    }

    @Override
    public boolean onStopCurrentWork() {
        Log.i("TAG", "onStopCurrentWork Thread id: " + Thread.currentThread().getId()
                + " Random num:" + mRandomNumber);
        return super.onStopCurrentWork();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.i("TAG", "In onUnbind(): Executes only once, you can not stop service if don't Unbind it.");
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("TAG", "Service Destroyed : if you call to stop a service before unbinding it, " +
                "it will stop automatically after all components from the service are unbound ");
        stopRandomNumberGenerator();
    }

    //No need of this method
   /* @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TAG", "In onStartCommand, MyService thread ID: " + Thread.currentThread().getId());
        //here we are running function on separate thread bcz service->onStartCommand() runs on main thread.
        //if we don't use separate thread it will block the UI and lead to ANR.

        //We are using IntentService class here so the function/logic which needs to run on background
        //thread is moved from here to onHandleIntent().

        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }*/

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

    public int getRandomNumber() {
        return mRandomNumber;
    }
}

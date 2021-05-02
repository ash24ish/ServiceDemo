package com.ashishbharam.servicedemo;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Random;

public class MyService extends Service {
    private int mRandomNumber;
    private boolean isRandomGeneratorOn;

    class MyServiceBinder extends Binder {
        public MyService getService() {
            return MyService.this;
        }
    }

    private IBinder iBinder = new MyServiceBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("TAG", "In onBind(): Executes only once, Components can be bind to both started,stopped or unstarted service");
        return iBinder;
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

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("TAG", "In onStartCommand, MyService thread ID: " + Thread.currentThread().getId());
        isRandomGeneratorOn = true;
        //here we are running function on separate thread bcz service->onStartCommand() runs on main thread.
        //if we don't use separate thread it will block the UI and lead to ANR.
        new Thread(() -> {
            startRandomNumberGenerator();
        }).start();

        //return super.onStartCommand(intent, flags, startId);
        return START_STICKY;
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

    public int getRandomNumber() {
        return mRandomNumber;
    }
}

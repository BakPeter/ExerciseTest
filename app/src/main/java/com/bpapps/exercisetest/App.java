package com.bpapps.exercisetest;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import com.bpapps.exercisetest.repository.Repository;

public class App extends Application {
    public static final String CHANNEL_ID = "com.bpapps.exercisetest.CHANNEL_ID";
    public static final String CHANNEL_NAME = "com.bpapps.exercisetest.CHANNNEL_NAME";

    private static App sInstance;

    public static Context getAppContext() {
        return App.sInstance.getApplicationContext();
    }

    private static void setInstance(App sInstance) {
        App.sInstance = sInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        setInstance(this);

        createNotificationChanel();

        Repository.getInstance().loadData();
    }

    private void createNotificationChanel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);
            channel.setSound(null, null);

            NotificationManager nm = getSystemService(NotificationManager.class);
            if (nm != null) {
                nm.createNotificationChannel(channel);
            }
        }
    }
}

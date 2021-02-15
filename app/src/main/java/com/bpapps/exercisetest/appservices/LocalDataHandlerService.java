package com.bpapps.exercisetest.appservices;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import com.bpapps.exercisetest.App;
import com.bpapps.exercisetest.MainActivity;
import com.bpapps.exercisetest.R;
import com.bpapps.exercisetest.repository.model.datamodel.Result;

public class LocalDataHandlerService extends Service {
    private static final String TAG = "TAG.LocalDataHandlerService";

    private LocalDataBinder mBinder = new LocalDataBinder();
    private boolean mIsServiceRunning = true;
    private boolean mIsFinishedSaving = false;

    @Override
    public void onDestroy() {
        stopTheService();
        super.onDestroy();
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        stopTheService();
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        setServiceToForeground();

        return START_NOT_STICKY;
    }

    private void setServiceToForeground() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
        Notification notification = new Notification.Builder(this, App.CHANNEL_ID)
                .setContentTitle(getString(R.string.notification_saving_data_title))
                .setContentText(getString(R.string.notification_saving_data_content_text))
                .setSmallIcon(R.drawable.ic_saving_data)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_saving_data_large_icon))
                .setContentIntent(pendingIntent)
                .build();

        startForeground(2357, notification);
    }

    public void saveData(Result result) {
        if (mIsServiceRunning) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i = 0; i < 20 && mIsServiceRunning; i++) {
                        Log.d(TAG, "run: " + i);
                    }

                    mIsFinishedSaving = true;
                }
            }).start();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        if (mIsFinishedSaving) {
            stopTheService();
        }
        return super.onUnbind(intent);
    }

    public void stopTheService() {
        mIsServiceRunning = false;
    }

    public class LocalDataBinder extends Binder {
        public LocalDataHandlerService getService() {
            return LocalDataHandlerService.this;
        }
    }
}

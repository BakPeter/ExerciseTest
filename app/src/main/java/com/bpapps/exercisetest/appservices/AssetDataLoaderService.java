package com.bpapps.exercisetest.appservices;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bpapps.exercisetest.App;
import com.bpapps.exercisetest.repository.localdatasource.assets.AssetDataHandler;
import com.bpapps.exercisetest.repository.model.datamodel.Result;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class AssetDataLoaderService extends Service {
    private static final String TAG = "TAG.AssetDataLoaderBindService";

    private IBinder mBinder = new AssetServiceBinder();
    private boolean mIsServiceRunning = true;
    private String mJsonFileName;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        mJsonFileName = intent.getStringExtra(AssetDataHandler.EXTRA_JSON_FILE_NAME);
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind");

        stopTheService();

        return super.onUnbind(intent);
    }


    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        stopTheService();
    }

    public void fetchData(@NonNull final IOnDataFetchedListener callback) {
        if (mIsServiceRunning) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream inputStream = App.getAppContext().getAssets().open(mJsonFileName);
                        String json = new BufferedReader(new InputStreamReader(inputStream))
                                .lines()
                                .parallel()
                                .collect(Collectors.joining("\n"));

                        Gson gson = new Gson();
                        final Result data = gson.fromJson(json, Result.class);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onDataFetchSuccess(data);
                            }
                        });
                    } catch (final IOException e) {
                        e.printStackTrace();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onDataFetchFailure(e);
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private void stopTheService() {
        mIsServiceRunning = false;
        stopSelf();
    }

    public interface IOnDataFetchedListener {
        void onDataFetchSuccess(Result data);

        void onDataFetchFailure(Exception error);
    }

    public class AssetServiceBinder extends Binder {
        public AssetDataLoaderService getService() {
            return AssetDataLoaderService.this;
        }
    }
}
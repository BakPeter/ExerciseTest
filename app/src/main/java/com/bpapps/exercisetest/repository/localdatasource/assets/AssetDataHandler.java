package com.bpapps.exercisetest.repository.localdatasource.assets;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bpapps.exercisetest.App;
import com.bpapps.exercisetest.appservices.AssetDataLoaderService;
import com.bpapps.exercisetest.repository.model.datamodel.Result;

public class AssetDataHandler {
    private static final String TAG = "TAG.AssetDataHandler";
    public static final String EXTRA_JSON_FILE_NAME = "com.bpapps.exercisetest.extra_json_file_name";

    private static final String JSON_FILE_NAME = "jsonObject.json";

    private AssetDataLoaderService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            AssetDataLoaderService.AssetServiceBinder binder = (AssetDataLoaderService.AssetServiceBinder) service;
            mService = binder.getService();
            mService.fetchData(new AssetDataLoaderService.IOnDataFetchedListener() {
                @Override
                public void onDataFetchSuccess(Result data) {
                    if (mCallback != null) {
                        mCallback.onLoadSuccess(data);
                    }
                    unBindService();
                }

                @Override
                public void onDataFetchFailure(Exception error) {
                    if (mCallback != null) {
                        mCallback.onLoadFailure(error);
                    }
                    unBindService();
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    private IDataLoadListener mCallback = null;

    public void loadData(@NonNull IDataLoadListener callback) {
        mCallback = callback;
        bindService();
    }

    private void bindService() {
        Intent intent = new Intent(App.getAppContext(), AssetDataLoaderService.class);
        intent.putExtra(EXTRA_JSON_FILE_NAME, JSON_FILE_NAME);
        App.getAppContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unBindService() {
        App.getAppContext().unbindService(mServiceConnection);
    }

    public interface IDataLoadListener {
        void onLoadSuccess(Result data);

        void onLoadFailure(Exception error);
    }
}
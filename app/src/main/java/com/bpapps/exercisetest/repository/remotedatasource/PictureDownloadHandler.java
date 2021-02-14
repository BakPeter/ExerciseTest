package com.bpapps.exercisetest.repository.remotedatasource;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.bpapps.exercisetest.App;
import com.bpapps.exercisetest.appservices.PicturesDownloaderService;
import com.bpapps.exercisetest.repository.model.datamodel.Result;

public class PictureDownloadHandler {
    private static final String TAG = "TAG.PictureDownloadHandler";

    private Result mData;
    private IOnDataDownloadListener mDataLoadCallback;
    private IOnDataDownloadStatusUpdateListener mDownloadStatusCallback;

    private PicturesDownloaderService mService;
    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            PicturesDownloaderService.PicturesDownloadBinder binder = (PicturesDownloaderService.PicturesDownloadBinder) service;
            mService = binder.getService();
            mService.downloadPictures(
                    mData,
                    new PicturesDownloaderService.IOnDataDownloadListener() {
                        @Override
                        public void onLoadSuccess(Result data) {
                            mDataLoadCallback.onLoadSuccess(data);

                            unBindService();
                        }

                        @Override
                        public void onFailure(Exception error) {
                            mDataLoadCallback.onFailure(error);
                        }
                    },
                    new PicturesDownloaderService.IOnDataDownloadStatusUpdateListener() {
                        @Override
                        public void onStatusUpdated(int numberOfItems, int downloadedCount) {
                            mDownloadStatusCallback.onStatusUpdated(numberOfItems, downloadedCount);
                        }
                    }
            );
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            Log.d(TAG, "onServiceDisconnected");
        }
    };

    public void downLoadPictures(@NonNull Result data, @NonNull IOnDataDownloadListener dataLoadCallback, IOnDataDownloadStatusUpdateListener downloadStatusCallback) {
        Log.d(TAG, "downLoadPictures");
        bindService();
        mData = data;
        mDataLoadCallback = dataLoadCallback;
        mDownloadStatusCallback = downloadStatusCallback;
    }

    private void bindService() {
        Intent intent = new Intent(App.getAppContext(), PicturesDownloaderService.class);
        App.getAppContext().bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    private void unBindService() {
        Log.d(TAG, "unBindService");
        App.getAppContext().unbindService(mServiceConnection);
    }

    public interface IOnDataDownloadStatusUpdateListener {
        void onStatusUpdated(int numberOfItems, int downloadedCount);
    }

    public interface IOnDataDownloadListener {
        void onLoadSuccess(Result data);

        void onFailure(Exception error);
    }
}

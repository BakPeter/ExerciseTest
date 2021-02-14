package com.bpapps.exercisetest.appservices;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bpapps.exercisetest.repository.model.datamodel.DataListObject;
import com.bpapps.exercisetest.repository.model.datamodel.Result;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class PicturesDownloaderService extends Service {
    private static final String TAG = "TAG.PicturesDownloaderService";

    private PicturesDownloadBinder mBinder = new PicturesDownloadBinder();
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private boolean mServiceRunning = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopTheService();
        return super.onUnbind(intent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);

        stopTheService();
    }

    public void downloadPictures(@NonNull final Result data,
                                 @NonNull final IOnDataDownloadListener downloadListenerCallback,
                                 final IOnDataDownloadStatusUpdateListener dataDownloadStatusCallback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                final List<DataListObject> items = data.getDataObject().getDataListObject();
                for (int i = 0; i < items.size() && mServiceRunning; i++) {
                    final int itemNumber = i + 1;
                    try {
                        URL url = new URL(items.get(i).getImageUrl());
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();
                        InputStream input = connection.getInputStream();
                        final Bitmap bitmap = BitmapFactory.decodeStream(input);

                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                data.getDataObject().getDataListObject().get(itemNumber).setImgBitmap(bitmap);
                                if (dataDownloadStatusCallback != null) {
                                    dataDownloadStatusCallback.onStatusUpdated(items.size(), itemNumber);
                                }
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        new Handler(Looper.getMainLooper()).post(new Runnable() {
                            @Override
                            public void run() {
                                if (dataDownloadStatusCallback != null) {
                                    dataDownloadStatusCallback.onStatusUpdated(items.size(),itemNumber);
                                }
                            }
                        });
                    }

                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                downloadListenerCallback.onLoadSuccess(data);
            }
        }).start();
    }

    public void stopTheService() {
        mServiceRunning = false;
        stopSelf();
    }

    public interface IOnDataDownloadStatusUpdateListener {
        void onStatusUpdated(int numberOfItems, int downloadedCount);
    }

    public interface IOnDataDownloadListener {
        void onLoadSuccess(Result data);

        void onFailure(Exception error);
    }

    public class PicturesDownloadBinder extends Binder {
        public PicturesDownloaderService getService() {
            return PicturesDownloaderService.this;
        }
    }
}

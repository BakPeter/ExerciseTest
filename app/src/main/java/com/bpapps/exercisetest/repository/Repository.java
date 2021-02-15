package com.bpapps.exercisetest.repository;

import com.bpapps.exercisetest.repository.localdatasource.assets.AssetDataHandler;
import com.bpapps.exercisetest.repository.localdatasource.sql.LocalDataHandler;
import com.bpapps.exercisetest.repository.model.datamodel.Result;
import com.bpapps.exercisetest.repository.remotedatasource.PictureDownloadHandler;

public class Repository {
    private static final String TAG = "TAG.Repository";

    private static Repository sInstance = null;

    private IOnDataDownloadStatusUpdateListener mDownloadStatusUpdateCallback;
    private IOnDataDownloadListener mDownloadCallback;

    public static Repository getInstance() {
        if (sInstance == null) {
            sInstance = new Repository();
        }

        return sInstance;
    }

    private Repository() {
    }


    public void loadData() {
        new AssetDataHandler().loadData(new AssetDataHandler.IDataLoadListener() {
            @Override
            public void onLoadSuccess(Result data) {
//                Log.d(TAG, "onLoadSuccess: " + data.toString());
                new PictureDownloadHandler().downLoadPictures(
                        data,
                        new PictureDownloadHandler.IOnDataDownloadListener() {
                            @Override
                            public void onLoadSuccess(Result data) {
                                if (mDownloadCallback != null) {
                                    mDownloadCallback.onLoadSuccess(data);
                                }

                                new LocalDataHandler().saveData(data);
                            }

                            @Override
                            public void onFailure(Exception error) {
                                if (mDownloadCallback != null) {
                                    mDownloadCallback.onFailure(error);
                                }
                            }
                        },
                        new PictureDownloadHandler.IOnDataDownloadStatusUpdateListener() {
                            @Override
                            public void onStatusUpdated(int numberOfItems, int downloadedCount) {
                                mDownloadStatusUpdateCallback.onStatusUpdated(numberOfItems, downloadedCount);
                            }
                        }
                );
            }

            @Override
            public void onLoadFailure(Exception error) {
                error.printStackTrace();
                System.exit(1);
            }
        });
    }

    public void registerDownloadStatusListener(IOnDataDownloadStatusUpdateListener callback) {
        mDownloadStatusUpdateCallback = callback;
    }

    public void unRegisterDownloadStatusListener() {
        mDownloadStatusUpdateCallback = null;
    }

    public void registerOnDataDownloadListener(IOnDataDownloadListener callback) {
        mDownloadCallback = callback;
    }

    public void unRegisterOnDataDownloadListener() {
        mDownloadCallback = null;
    }

    public interface IOnDataDownloadStatusUpdateListener {
        void onStatusUpdated(int numberOfItems, int downloadedCount);
    }

    public interface IOnDataDownloadListener {
        void onLoadSuccess(Result data);

        void onFailure(Exception error);
    }
}
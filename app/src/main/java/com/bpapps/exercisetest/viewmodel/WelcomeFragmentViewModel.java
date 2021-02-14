package com.bpapps.exercisetest.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bpapps.exercisetest.repository.Repository;
import com.bpapps.exercisetest.repository.model.datamodel.Result;

public class WelcomeFragmentViewModel extends ViewModel
        implements Repository.IOnDataDownloadStatusUpdateListener, Repository.IOnDataDownloadListener {
    private static final String TAG = "TAG.WelcomeFragmentViewMode";

    private Repository mRepository = Repository.getInstance();

    private MutableLiveData<Integer> mDownloadProgress = new MutableLiveData<>(0);

    public WelcomeFragmentViewModel() {
        super();

        mRepository.registerDownloadStatusListener(this);
        mRepository.registerOnDataDownloadListener(this);
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        mRepository.unRegisterDownloadStatusListener();
        mRepository.unRegisterOnDataDownloadListener();
    }

    public LiveData<Integer> getDownloadProgress() {
        return mDownloadProgress;
    }

    @Override
    public void onStatusUpdated(int numberOfItems, int downloadedCount) {
        Log.d(TAG, numberOfItems + " : " + downloadedCount);
        mDownloadProgress.postValue((int) (100 * ((double) downloadedCount / (double) numberOfItems)));
    }

    @Override
    public void onLoadSuccess(Result data) {
        Log.d(TAG, "onLoadSuccess");
    }

    @Override
    public void onFailure(Exception error) {
        Log.d(TAG, "onFailure");
    }
}
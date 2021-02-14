package com.bpapps.exercisetest.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.bpapps.exercisetest.apputils.ConnectivityManagement;

public class MainActivityViewModel extends ViewModel implements ConnectivityManagement.IConnectivityChangedListener {

    private ConnectivityManagement mConnectivityManagement = null;
    private MutableLiveData<Boolean> mConnectivityStatus = new MutableLiveData<>();

    @Override
    protected void onCleared() {
        mConnectivityManagement.unRegisterForOnLineChangedListener();
        super.onCleared();
    }

    public LiveData<Boolean> getConnectivityStatus() {
        if (mConnectivityManagement == null) {
            throw new IllegalStateException("ConnectivityManager is not initialized. Invoke MainActivityViewModel.initConnectivityManagement(Context) first.");
        }

        return mConnectivityStatus;
    }

    public void initConnectivityManagement(Context context) {
        mConnectivityManagement = new ConnectivityManagement(context);
        mConnectivityStatus.setValue(mConnectivityManagement.getCurrentConnectivityStatus());
        mConnectivityManagement.registerForOnLineChangedListener(this);
    }

    @Override
    public void onConnectivityStatusChanged(boolean isConnected) {
        mConnectivityStatus.postValue(isConnected);
    }
}

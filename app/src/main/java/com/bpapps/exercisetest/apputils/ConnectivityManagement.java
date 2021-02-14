package com.bpapps.exercisetest.apputils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

public class ConnectivityManagement {
    private static final String TAG = "TAG.ConnectivityManagement";

    private ConnectivityManager mCM;

    private boolean mCurrentConnectivityStatus = false;
    private boolean mHasCellularTransporter = false;
    private boolean mHasWifiTransporter = false;

    private IConnectivityChangedListener mConnectivityChangedListenerCallback = null;
    private ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(@NonNull Network network) {
//            Log.d(TAG, "The default network is now: " + network);

            updateTransports();

            if (!mCurrentConnectivityStatus) {
                setCurrentConnectivityStatus();

                if (mConnectivityChangedListenerCallback != null) {
                    mConnectivityChangedListenerCallback.onConnectivityStatusChanged(getCurrentConnectivityStatus());
                }
            }
        }

        @Override
        public void onLost(@NonNull Network network) {
//            Log.d(TAG, "The application no longer has a default network. The last default network was " + network);

            updateTransports();
            setCurrentConnectivityStatus();

            int numberOfTransports = getNumberOfTransports();

            if (numberOfTransports == 0) {
                if (mConnectivityChangedListenerCallback != null) {
                    mConnectivityChangedListenerCallback.onConnectivityStatusChanged(getCurrentConnectivityStatus());
                }
            }
        }
    };

    private int getNumberOfTransports() {
        if (getCurrentConnectivityStatus()) {
            int numberOfTransports = 0;

            if (hasCellularTransporter()) {
                numberOfTransports++;
            }
            if (hasWifiTransporter()) {
                numberOfTransports++;
            }

            return numberOfTransports;
        } else {
            return 0;
        }
    }

    public ConnectivityManagement(@NonNull Context context) {
        this.mCM = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        updateTransports();
    }

    public boolean hasCellularTransporter() {
        return mHasCellularTransporter;
    }

    private void setHasCellularTransporter(boolean hasCellularTransporter) {
        this.mHasCellularTransporter = hasCellularTransporter;
    }

    public boolean hasWifiTransporter() {
        return mHasWifiTransporter;
    }

    private void setHasWifiTransporter(boolean hasWifiTransporter) {
        this.mHasWifiTransporter = hasWifiTransporter;
    }

    public boolean getCurrentConnectivityStatus() {
        return mCurrentConnectivityStatus;
    }

    private void setCurrentConnectivityStatus() {
        this.mCurrentConnectivityStatus = hasCellularTransporter() || hasWifiTransporter();
    }

    private void updateTransports() {
        setHasCellularTransporter(false);
        setHasWifiTransporter(false);

        for (Network network : mCM.getAllNetworks()) {
            NetworkCapabilities nc = mCM.getNetworkCapabilities(network);
            if (nc != null) {
                if (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                    setHasCellularTransporter(true);
                }

                if (nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                    setHasWifiTransporter(true);
                }
            }
        }
    }

    public void registerForOnLineChangedListener(IConnectivityChangedListener callback) {
        mConnectivityChangedListenerCallback = callback;

        mCM.registerNetworkCallback(new NetworkRequest.Builder().build(), mNetworkCallback);
    }

    public void unRegisterForOnLineChangedListener() {
        mCM.unregisterNetworkCallback(mNetworkCallback);
        mConnectivityChangedListenerCallback = null;
    }

    public interface IConnectivityChangedListener {
        void onConnectivityStatusChanged(boolean isConnected);
    }
}
package com.bpapps.exercisetest.repository.localdatasource.sql;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.bpapps.exercisetest.App;
import com.bpapps.exercisetest.appservices.AssetDataLoaderService;
import com.bpapps.exercisetest.appservices.LocalDataHandlerService;
import com.bpapps.exercisetest.repository.model.datamodel.Result;

public class LocalDataHandler {
    public void saveData(final Result data) {
        Intent intent = new Intent(App.getAppContext(), LocalDataHandlerService.class);
        App.getAppContext().startService(intent);

        ServiceConnection serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LocalDataHandlerService.LocalDataBinder binder = (LocalDataHandlerService.LocalDataBinder) service;
                LocalDataHandlerService localDataHandlerService = binder.getService();
                localDataHandlerService.saveData(data);
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
        App.getAppContext().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
//        App.getAppContext().unbindService(serviceConnection);
    }
}

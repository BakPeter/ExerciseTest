package com.bpapps.exercisetest;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bpapps.exercisetest.view.fragments.WelcomeFragment;
import com.bpapps.exercisetest.viewmodel.MainActivityViewModel;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "TAG.MainActivity";

    public static final int REQUEST_CONNECTIVITY_SETTINGS = 17;

    private MainActivityViewModel mViewModel;

    private androidx.appcompat.app.AlertDialog mDialogNoConnectivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);
        mViewModel.initConnectivityManagement(this);
        mViewModel.getConnectivityStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean newConnectivityStatus) {
                Log.d(TAG, "connectivity status changed: " + newConnectivityStatus);
                connectivityStatusChanged(newConnectivityStatus);
            }
        });

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container_view, WelcomeFragment.newInstance())
                .commit();
    }

    private void connectivityStatusChanged(Boolean newConnectivityStatus) {
        if (newConnectivityStatus) {
            appConnected();
        } else {
            appDisconnected();
        }
    }


    private void appDisconnected() {
        Log.d(TAG, "appDisconnected");
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle(R.string.no_connection_dialog_title)
                .setMessage(R.string.no_connection_dialog_msg)
                .setCancelable(false)
                .setPositiveButton(R.string.no_connection_dialog_positive_btn_txt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                            Log.d(TAG, "go to internet settings");
                        startActivityForResult(new Intent(Settings.ACTION_WIRELESS_SETTINGS), REQUEST_CONNECTIVITY_SETTINGS);
                    }
                })
                .setNegativeButton(R.string.no_connection_dialog_negative_btn_txt, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finishAndRemoveTask();
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
//                            requireActivity().finishAndRemoveTask();
                        finishAndRemoveTask();
                        System.exit(0);
                    }
                });

        mDialogNoConnectivity = builder.create();
        mDialogNoConnectivity.show();
    }

    private void appConnected() {
        Log.d(TAG, "appConnected");
        if (mDialogNoConnectivity != null) {
            mDialogNoConnectivity.dismiss();
            mDialogNoConnectivity = null;
        }
    }
}
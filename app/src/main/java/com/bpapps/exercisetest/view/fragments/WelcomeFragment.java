package com.bpapps.exercisetest.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bpapps.exercisetest.R;
import com.bpapps.exercisetest.viewmodel.WelcomeFragmentViewModel;

public class WelcomeFragment extends Fragment {

    private WelcomeFragmentViewModel mViewModel;

    private ProgressBar mPbDownloadProgress;
    private AppCompatTextView mTvLoadPercentageCompleted;

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_presntation_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(WelcomeFragmentViewModel.class);

        mPbDownloadProgress = view.findViewById(R.id.pbDownloadProgress);
        mTvLoadPercentageCompleted = view.findViewById(R.id.tvLoadPercentageCompleted);

        mViewModel.getDownloadProgress().observe(getViewLifecycleOwner(), new Observer<Integer>() {
            @Override
            public void onChanged(Integer newProgress) {
                dataProgressUpdated(newProgress);
            }
        });
    }

    private void dataProgressUpdated(Integer newProgress) {
        mPbDownloadProgress.setProgress(newProgress);
        mTvLoadPercentageCompleted.setText(newProgress + "%");
    }
}
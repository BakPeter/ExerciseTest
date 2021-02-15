package com.bpapps.exercisetest.repository.model.datamodel;

import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Result implements Serializable {
    @SerializedName("DataObject")
    @Expose
    private DataObject dataObject;

    public DataObject getDataObject() {
        return dataObject;
    }

    @Override
    public String toString() {
        return "Result{" +
                "dataObject=" + dataObject +
                '}';
    }
}

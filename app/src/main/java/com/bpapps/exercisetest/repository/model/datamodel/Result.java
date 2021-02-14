package com.bpapps.exercisetest.repository.model.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Result {
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

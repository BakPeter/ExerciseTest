package com.bpapps.exercisetest.repository.model.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DataObject {
    @SerializedName("DataListObject")
    @Expose
    private List<DataListObject> dataListObject;

    @SerializedName("DataListCat")
    @Expose
    List<DataListCat> dataListCat;

    public List<DataListObject> getDataListObject() {
        return dataListObject;
    }

    public List<DataListCat> getDataListCat() {
        return dataListCat;
    }

    @Override
    public String toString() {
        return "DataObject{" +
                "dataListObject=" + dataListObject +
                ", dataListCat=" + dataListCat +
                '}';
    }
}

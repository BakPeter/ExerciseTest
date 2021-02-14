package com.bpapps.exercisetest.repository.model.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DataListAddr {

    @SerializedName("Addr")
    @Expose
    private String adrr = null;


    @SerializedName("DAd")
    @Expose
    private String dAd = null;

    public String getAdrr() {
        return adrr;
    }

    public String getdAd() {
        return dAd;
    }

    @Override
    public String toString() {
        return "DataListAddr{" +
                "adrr='" + adrr + '\'' +
                ", dAd='" + dAd + '\'' +
                '}';
    }
}

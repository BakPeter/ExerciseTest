package com.bpapps.exercisetest.repository.model.datamodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DataListCat implements Serializable {
    @SerializedName("CatId")
    @Expose
    private Integer catId;

    @SerializedName("CTitle")
    @Expose
    private String categoryTitle;

    public Integer getCatId() {
        return catId;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    @Override
    public String toString() {
        return "DataListCat{" +
                "catId=" + catId +
                ", categoryTitle='" + categoryTitle + '\'' +
                '}';
    }
}

package com.bpapps.exercisetest.repository.model.datamodel;

import android.graphics.Bitmap;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class DataListObject implements Serializable {

    @SerializedName("CatId")
    @Expose
    private Integer catId;

    @SerializedName("Title")
    @Expose
    private String title;

    @SerializedName("STitle")
    @Expose
    private String sTitle;

    @SerializedName("Imag")
    @Expose
    private String imageUrl;

    @SerializedName("Id")
    @Expose
    private Integer id;

    @SerializedName("DataListAddr")
    @Expose
    private List<DataListAddr> dataListAddr;

    @SerializedName("NOV")
    @Expose
    private String nOV;

    @SerializedName("ClET")
    @Expose
    private Boolean clET;

    private Bitmap imgBitmap;


    public Integer getCatId() {
        return catId;
    }

    public String getTitle() {
        return title;
    }

    public String getsTitle() {
        return sTitle;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public Integer getId() {
        return id;
    }

    public List<DataListAddr> getDataListAddr() {
        return dataListAddr;
    }

    public String getnOV() {
        return nOV;
    }

    public Boolean getClET() {
        return clET;
    }

    public Bitmap getImgBitmap() {
        return imgBitmap;
    }

    public void setImgBitmap(Bitmap imgBitmap) {
        this.imgBitmap = imgBitmap;
    }

    @Override
    public String toString() {
        return "DataListObject{" +
                "catId=" + catId +
                ", title='" + title + '\'' +
                ", sTitle='" + sTitle + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", id=" + id +
                ", dataListAddr=" + dataListAddr +
                ", nOV='" + nOV + '\'' +
                ", clET=" + clET +
                ", imgBitmap=" + imgBitmap +
                '}';
    }
}

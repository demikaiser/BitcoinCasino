package com.comp3617.bitcoinCasino;


import java.util.Date;

public class ListView_Asset {

    private int assetImage;

    private String assetCode;

    private String assetName;

    private Date currentTime;

    private double assetPrice;

    private double assetAmount;

    public int getAssetImage() {
        return assetImage;
    }

    public void setAssetImage(int assetImage) {
        this.assetImage = assetImage;
    }

    public String getAssetCode() {
        return assetCode;
    }

    public void setAssetCode(String assetCode) {
        this.assetCode = assetCode;
    }

    public String getAssetName() {
        return assetName;
    }

    public void setAssetName(String assetName) {
        this.assetName = assetName;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public double getAssetPrice() {
        return assetPrice;
    }

    public void setAssetPrice(double assetPrice) {
        this.assetPrice = assetPrice;
    }

    public double getAssetAmount() {
        return assetAmount;
    }

    public void setAssetAmount(double assetAmount) {
        this.assetAmount = assetAmount;
    }

}

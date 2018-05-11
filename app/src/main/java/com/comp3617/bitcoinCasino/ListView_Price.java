package com.comp3617.bitcoinCasino;

import java.util.Date;


public class ListView_Price {

    private int currencyImage;

    private String currencyCode;

    private String currencyName;

    private Date currentTime;

    private double currentPrice;

    private double fluctuatingAmount;

    private int fluctuatingAmountImage;

    public int getFluctuatingAmountImage() {
        return fluctuatingAmountImage;
    }

    public void setFluctuatingAmountImage(int fluctuatingAmountImage) {
        this.fluctuatingAmountImage = fluctuatingAmountImage;
    }

    public int getCurrencyImage() {
        return currencyImage;
    }

    public void setCurrencyImage(int currencyImage) {
        this.currencyImage = currencyImage;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public Date getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(Date currentTime) {
        this.currentTime = currentTime;
    }

    public double getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(double currentPrice) {
        this.currentPrice = currentPrice;
    }

    public double getFluctuatingAmount() {
        return fluctuatingAmount;
    }

    public void setFluctuatingAmount(double fluctuatingAmount) {
        this.fluctuatingAmount = fluctuatingAmount;
    }
}

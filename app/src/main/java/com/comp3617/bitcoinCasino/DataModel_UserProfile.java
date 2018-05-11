package com.comp3617.bitcoinCasino;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class DataModel_UserProfile extends RealmObject {

    public static final long UNIQUE_ID_THE_USER = 7777777;
    public static final double STARTING_CASH = 100000.00;

    @PrimaryKey
    private long uniqueId;

    private double cash;

    private double coinBtc;

    private double coinLtc;

    private double coinEth;

    private double coinZec;

    private double coinXrp;

    public DataModel_UserProfile() {}

    public DataModel_UserProfile(long uniqueId) { this.uniqueId = uniqueId; }

    public double getCash() {
        return cash;
    }

    public void setCash(double cash) {
        this.cash = cash;
    }

    public double getCoinBtc() {
        return coinBtc;
    }

    public void setCoinBtc(double coinBtc) {
        this.coinBtc = coinBtc;
    }

    public double getCoinLtc() {
        return coinLtc;
    }

    public void setCoinLtc(double coinLtc) {
        this.coinLtc = coinLtc;
    }

    public double getCoinEth() {
        return coinEth;
    }

    public void setCoinEth(double coinEth) {
        this.coinEth = coinEth;
    }

    public double getCoinZec() {
        return coinZec;
    }

    public void setCoinZec(double coinZec) {
        this.coinZec = coinZec;
    }

    public double getCoinXrp() {
        return coinXrp;
    }

    public void setCoinXrp(double coinXrp) {
        this.coinXrp = coinXrp;
    }

    public long getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void addCoinBtc(double coinsToAdd) {
        this.coinBtc = this.coinBtc + coinsToAdd;
    }

    public void addCoinLtc(double coinsToAdd) {
        this.coinLtc = this.coinLtc + coinsToAdd;
    }

    public void addCoinEth(double coinsToAdd) {
        this.coinEth = this.coinEth + coinsToAdd;
    }

    public void addCoinZec(double coinsToAdd) {
        this.coinZec = this.coinZec + coinsToAdd;
    }

    public void addCoinXrp(double coinsToAdd) {
        this.coinXrp = this.coinXrp + coinsToAdd;
    }

    public void subtractCoinBtc(double coinsToSubtract) {
        this.coinBtc = this.coinBtc - coinsToSubtract;
    }

    public void subtractCoinLtc(double coinsToSubtract) {
        this.coinLtc = this.coinLtc - coinsToSubtract;
    }

    public void subtractCoinEth(double coinsToSubtract) {
        this.coinEth = this.coinEth - coinsToSubtract;
    }

    public void subtractCoinZec(double coinsToSubtract) {
        this.coinZec = this.coinZec - coinsToSubtract;
    }

    public void subtractCoinXrp(double coinsToSubtract) {
        this.coinXrp = this.coinXrp - coinsToSubtract;
    }
}

package com.comp3617.bitcoinCasino;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


public class DataModel_PriceIndicesNow extends RealmObject {

    public static final long UNIQUE_ID_CURRENT = 123456789;
    public static final long UNIQUE_ID_PREVIOUS = 987654321;

    @PrimaryKey
    private long uniqueId;

    private long time;

    private double btc;

    private double ltc;

    private double eth;

    private double zec;

    private double xrp;

    public DataModel_PriceIndicesNow() {}

    public DataModel_PriceIndicesNow(long uniqueId) {
        this.uniqueId = uniqueId;
    }

    public long getUniqueId() { return uniqueId; }

    public void setUniqueId(long uniqueId) { this.uniqueId = uniqueId; }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public double getBtc() {
        return btc;
    }

    public void setBtc(double btc) {
        this.btc = btc;
    }

    public double getLtc() {
        return ltc;
    }

    public void setLtc(double ltc) {
        this.ltc = ltc;
    }

    public double getEth() {
        return eth;
    }

    public void setEth(double eth) {
        this.eth = eth;
    }

    public double getZec() {
        return zec;
    }

    public void setZec(double zec) {
        this.zec = zec;
    }

    public double getXrp() {
        return xrp;
    }

    public void setXrp(double xrp) {
        this.xrp = xrp;
    }

    public String toString() {
        String summary = "";

        summary += "TIME: " + time + "\n";
        summary += "BTC: " + btc + "\n";
        summary += "LTC: " + ltc + "\n";
        summary += "ETH: " + eth + "\n";
        summary += "ZEC: " + zec + "\n";
        summary += "XRP: " + xrp + "\n";

        return summary;
    }


}

package com.comp3617.bitcoinCasino;


import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class ListView_Charts extends RealmObject {

    @PrimaryKey
    private long id;

    private String chart;
    private String coin;
    private String period;
    private String limit;
    private RealmList<DataModel_PriceHistory> arrayListForPriceHistories;

    public ListView_Charts() {}

    public ListView_Charts(long id, String chart, String coin, String period, String limit,
                           RealmList<DataModel_PriceHistory> arrayListForPriceHistories) {
        this.id = id;
        this.chart = chart;
        this.coin = coin;
        this.period = period;
        this.limit = limit;
        this.arrayListForPriceHistories = arrayListForPriceHistories;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getChart() { return chart; }

    public void setChart(String chart) {
        this.chart = chart;
    }

    public String getCoin() {
        return coin;
    }

    public void setCoin(String coin) {
        this.coin = coin;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public String getLimit() {
        return limit;
    }

    public void setLimit(String limit) {
        this.limit = limit;
    }

    public RealmList<DataModel_PriceHistory> getArrayListForPriceHistories() {
        return arrayListForPriceHistories;
    }

    public void setArrayListForPriceHistories(RealmList<DataModel_PriceHistory> arrayListForPriceHistories) {
        this.arrayListForPriceHistories = arrayListForPriceHistories;
    }
}

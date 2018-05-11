package com.comp3617.bitcoinCasino;


public class DataModel_CryptoCurrencyNews {

    private String imageURL;

    private String title;

    private String description;

    private String link;

    private String pubDate;

    DataModel_CryptoCurrencyNews() {}

    DataModel_CryptoCurrencyNews(String imageURL, String title,
                                 String description, String link, String pubDate) {
        this.imageURL = imageURL;
        this.title = title;
        this.description = description;
        this.link = link;
        this.pubDate = pubDate;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

}

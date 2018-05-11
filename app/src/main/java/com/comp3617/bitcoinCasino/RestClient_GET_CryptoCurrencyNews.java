package com.comp3617.bitcoinCasino;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class RestClient_GET_CryptoCurrencyNews {

    private static RestClient_GET_CryptoCurrencyNews instance = null;

    private String url;
    private InputStreamReader responseBodyReader = null;

    private RestClient_GET_CryptoCurrencyNews(String url) {
        this.url = url;
    }

    public static RestClient_GET_CryptoCurrencyNews getInstanceWithUrl(String url) {
        if(instance == null) {
            instance = new RestClient_GET_CryptoCurrencyNews(url);
        }
        return instance;
    }

    // Process the http restful call to get news.
    public ArrayList<DataModel_CryptoCurrencyNews> run() {
        ArrayList<DataModel_CryptoCurrencyNews> result = new ArrayList<>();

        try {
            // Set an endpoint.
            URL endpoint = new URL(url);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) endpoint.openConnection();

            // Set headers.
            httpsURLConnection.setRequestProperty("User-Agent", "Realtime Bitcoin Casino App");

            // Handles response codes.
            if (httpsURLConnection.getResponseCode() == 200) {
                InputStream responseBody = httpsURLConnection.getInputStream();

                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser parser = xmlPullParserFactory.newPullParser();

                parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(responseBody, null);

                result = parseCoinTelegraphRSSNewsXml(parser);
            } else {
                Logger.error(this, "The server returns other than the correct code (200).");
            }
            // Close and end the connection.
            httpsURLConnection.disconnect();
        } catch (Exception exception) {
            Logger.error(this, "RestClient_GET_CryptoCurrencyNews.run() throws an exception.");
            exception.printStackTrace();
        }

        if (result != null) {
            return result;
        } else {
            return null;
        }
    }

    private ArrayList<DataModel_CryptoCurrencyNews> parseCoinTelegraphRSSNewsXml(XmlPullParser parser)
            throws XmlPullParserException,IOException {

        ArrayList<DataModel_CryptoCurrencyNews> newsArrayList = null;
        int eventType = parser.getEventType();
        DataModel_CryptoCurrencyNews news = null;

        while (eventType != XmlPullParser.END_DOCUMENT){
            String name;
            switch (eventType) {

                case XmlPullParser.START_DOCUMENT:
                    newsArrayList = new ArrayList();
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();

                    if (name.equals("item")) {
                        news = new DataModel_CryptoCurrencyNews();
                    } else if (news != null) {
                        if (name.equals("media:content")){
                            news.setImageURL(parser.getAttributeValue(null,"url"));
                        } else if (name.equals("title")){
                            news.setTitle(parser.nextText());
                        } else if (name.equals("description")){
                            news.setDescription(processDescription(parser.nextText()));
                        } else if (name.equals("link")){
                            news.setLink(parser.nextText());
                        } else if (name.equals("pubDate")){
                            news.setPubDate(processPubDate(parser.nextText()));
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if (name.equalsIgnoreCase("item") && news != null) {
                        newsArrayList.add(news);
                    }
            }
            eventType = parser.next();
        }
        return newsArrayList;
    }

    public String processDescription(String description) {
        String result = description.substring(
                description.indexOf("<p>") + "<p>".length(), description.indexOf("</p>") );
        return result;
    }

    public String processPubDate(String pubDate) {
        String result = pubDate.replace("+0000", "");
        return result;
    }
}

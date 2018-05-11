package com.comp3617.bitcoinCasino;


import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class RestClient_GET_PriceHistoy {

    public static final String MODE_MINUTE = "minute";
    public static final String MODE_HOUR = "hour";
    public static final String MODE_DAY = "day";

    private InputStreamReader responseBodyReader = null;

    private String requestUrl;

    /* Mode can be: minute, hour, day */
    private String mode;
    private String symbol;
    private int limit;

    RestClient_GET_PriceHistoy(String mode, String symbol, int limit) {
        this.requestUrl = "https://min-api.cryptocompare.com/data/histo" + mode + "?fsym="
                + symbol + "&tsym=USD&limit=" + limit + "&aggregate=1&e=CCCAGG";
        this.mode = mode;
        this.symbol = symbol;
        this.limit = limit;
    }

    // Process the http restful call to get price indices.
    public ArrayList<DataModel_PriceHistory> run() {
        ArrayList<DataModel_PriceHistory> result = new ArrayList<>();

        try {
            // Set an endpoint.
            URL endpoint = new URL(requestUrl);
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) endpoint.openConnection();

            // Set headers.
            httpsURLConnection.setRequestProperty("User-Agent", "Realtime Bitcoin Casino App");

            // Handles response codes.
            if (httpsURLConnection.getResponseCode() == 200) {
                InputStream responseBody = httpsURLConnection.getInputStream();
                responseBodyReader = new InputStreamReader(responseBody, "UTF-8");
                JsonReader jsonReader = new JsonReader(responseBodyReader);
                jsonReader.beginObject();

                while (jsonReader.hasNext()) {
                    String key = jsonReader.nextName();
                    if (key.equals("Data")) {
                        jsonReader.beginArray();
                        while (jsonReader.hasNext())
                        {
                            result.add(readDataModel_PriceHistory(jsonReader));
                        }
                        jsonReader.endArray();
                    } else {
                        jsonReader.skipValue();
                    }
                }
                jsonReader.close();

            } else {
                Logger.error(this, "The server returns other than the correct code (200).");
            }

            // Close and end the connection.
            httpsURLConnection.disconnect();
        } catch (Exception exception) {
            Logger.error(this, "RestClient_GET_PriceIndicesNow.run() throws an exception.");
            exception.printStackTrace();
        }
        return result;
    }

    // Read a price for each currency.
    public DataModel_PriceHistory readDataModel_PriceHistory(JsonReader jsonReader)
            throws IOException {
        DataModel_PriceHistory result = null;

        long time = 0;
        double close = 0;
        double high = 0;
        double low = 0;
        double open = 0;
        double volumeFrom = 0;
        double volumeTo = 0;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals("time")) {
                time = Long.parseLong(jsonReader.nextString());
            } else if (name.equals("close")) {
                close = Double.parseDouble(jsonReader.nextString());
            } else if (name.equals("high")) {
                high = Double.parseDouble(jsonReader.nextString());
            } else if (name.equals("low")) {
                low = Double.parseDouble(jsonReader.nextString());
            } else if (name.equals("open")) {
                open = Double.parseDouble(jsonReader.nextString());
            } else if (name.equals("volumefrom")) {
                volumeFrom = Double.parseDouble(jsonReader.nextString());
            } else if (name.equals("volumeto")) {
                volumeTo = Double.parseDouble(jsonReader.nextString());
            }
        }
        jsonReader.endObject();

        result = new DataModel_PriceHistory(time, close, high, low, open, volumeFrom, volumeTo);

        return result;
    }

}

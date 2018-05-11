package com.comp3617.bitcoinCasino;

import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Date;
import javax.net.ssl.HttpsURLConnection;

/**
 *
 */
public class RestClient_GET_PriceIndicesNow {

    private static RestClient_GET_PriceIndicesNow instance = null;

    private String url;
    private InputStreamReader responseBodyReader = null;

    private RestClient_GET_PriceIndicesNow(String url) {
        this.url = url;
    }

    public static RestClient_GET_PriceIndicesNow getInstanceWithUrl(String url) {
        if(instance == null) {
            instance = new RestClient_GET_PriceIndicesNow(url);
        }
        return instance;
    }

    // Process the http restful call to get price indices.
    public DataModel_PriceIndicesNow run() {
        DataModel_PriceIndicesNow result = new DataModel_PriceIndicesNow();
        result.setTime((new Date()).getTime());

        try {
            // Set an endpoint.
            URL endpoint = new URL(url);
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

                    if (key.equals("BTC")) {
                        result.setBtc(readPrice(jsonReader));
                    } else if (key.equals("LTC")) {
                        result.setLtc(readPrice(jsonReader));
                    } else if (key.equals("ETH")) {
                        result.setEth(readPrice(jsonReader));
                    } else if (key.equals("ZEC")) {
                        result.setZec(readPrice(jsonReader));
                    } else if (key.equals("XRP")) {
                        result.setXrp(readPrice(jsonReader));
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

        // Check if there is a real value, return null if there is no real values.
        if ((result.getBtc() != 0)
                && (result.getLtc() != 0)
                && (result.getEth() != 0)
                && (result.getZec() != 0)
                && (result.getXrp() != 0)) {
            return result;
        } else {
            return null;
        }
    }

    // Read a price for each currency.
    public Double readPrice(JsonReader jsonReader) throws IOException {
        Double result = null;

        jsonReader.beginObject();
        while (jsonReader.hasNext()) {
            String name = jsonReader.nextName();
            if (name.equals("USD")) {
                result = jsonReader.nextDouble();
            } else {
                jsonReader.skipValue();
            }
        }
        jsonReader.endObject();
        return result;
    }

}

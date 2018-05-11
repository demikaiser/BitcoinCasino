package com.comp3617.bitcoinCasino;

import android.app.Application;
import android.os.AsyncTask;

import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

public class RealtimeBitcoinCasinoApp extends Application {

    /* Data Refresh Rate for the Entire Application */
    public static final int REFRESH_RATE_DATA = 10000;

    @Override
    public void onCreate() {
        Logger.debug(this, "onCreate()");
        super.onCreate();

        // Initiate the Realm database connection.
        Realm.init(this);

        // Refresh the data in the periodically.
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                new updatePriceIndices().execute();
            }
        }, 0, RealtimeBitcoinCasinoApp.REFRESH_RATE_DATA);

    }

    @Override
    public void onTerminate() {
        Logger.debug(this, "onTerminate()");
        super.onTerminate();
    }

    private class updatePriceIndices extends AsyncTask<Void, Void, Void> {

        // This is a job running background periodically.
        @Override
        protected Void doInBackground(Void... params) {
            try {
                // Get price indices in the object "dataModel_priceIndicesNow".
                Thread.sleep(500);
                DataModel_PriceIndicesNow dataModel_priceIndicesNow = RestClient_GET_PriceIndicesNow
                        .getInstanceWithUrl("https://min-api.cryptocompare.com/" +
                                "data/pricemulti?fsyms=BTC,LTC,ETH,ZEC,XRP&tsyms=USD")
                        .run();

                // Update database only if there is real data.
                if (dataModel_priceIndicesNow != null) {
                    Logger.debug(this, dataModel_priceIndicesNow.toString());
                    Realm realm = Realm.getDefaultInstance();

                    // Get the current data.
                    DataModel_PriceIndicesNow current
                            = DatabaseController.getTPriceIndicesFromRealmDatabase(
                            realm,
                            DataModel_PriceIndicesNow.UNIQUE_ID_CURRENT);

                    if (current != null) {
                        // Set the current data to previous data.
                        DatabaseController.setPriceIndicesToRealmDatabase(
                                realm,
                                DataModel_PriceIndicesNow.UNIQUE_ID_PREVIOUS, current);
                    }

                    // Set the most recent data to the current data.
                    DatabaseController.setPriceIndicesToRealmDatabase(
                            realm,
                            DataModel_PriceIndicesNow.UNIQUE_ID_CURRENT,
                            dataModel_priceIndicesNow);

                    Logger.debug(this, "Periodic Realm Transaction Completed Successfully!");
                }
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Logger.debug(this, "onPostExecute() - Getting Cryptocurrencies Data");
            super.onPostExecute(aVoid);
        }
    }
}

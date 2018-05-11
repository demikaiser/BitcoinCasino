package com.comp3617.bitcoinCasino;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.comp3617.finalproject.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;

public class TabFragment_Coins extends Fragment {

    ListView listViewForPrice;
    ListView_Price_Adapter listView_Price_Adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_coins, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        /* List View for Price */

        // Set an adapter for the ListView to inflate the custom lists.
        listViewForPrice = (ListView) getActivity().findViewById(R.id.listViewForPrice);
        listView_Price_Adapter = new ListView_Price_Adapter(getContext(),
                populateFromRealmDatabase());
        listViewForPrice.setAdapter(listView_Price_Adapter);

        // Register a listener for short-clicking a coin.
        listViewForPrice.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                // Get information from the selected item.
                List<ListView_Price> list = populateFromRealmDatabase();
                ListView_Price listView_price = list.get(index);
                String currencyCode = listView_price.getCurrencyCode();
                double currentPrice = listView_price.getCurrentPrice();

                // Buy coins by starting a dialog fragment with arguments.
                TabFragment_Coins_Buy dialogFragment
                        = TabFragment_Coins_Buy.newInstance(currencyCode, currentPrice);
                // Set up the custom listener for call back mechanism.
                dialogFragment.setOnBuyDialogListener(new TabFragment_Coins_Buy.OnBuyDialogListener() {
                    @Override
                    public void onBuyingPerformed(boolean isBought, String currencyCode, double coinBoughtAmount) {
                        if (isBought) {
                            updateListView();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Bought " + currencyCode + " "
                                            + String.format("%.2f", coinBoughtAmount),
                                    Toast.LENGTH_SHORT).show();
                            requestAssetFragmentRefresh();
                        }
                    }
                });
                dialogFragment.show(getFragmentManager(), null);
            }
        });

        // Register a listener for long-clicking a coin.
        listViewForPrice.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get information from the selected item.
                List<ListView_Price> list = populateFromRealmDatabase();
                ListView_Price listView_price = list.get(position);
                String currencyCode = listView_price.getCurrencyCode();

                // Buy all coins selected.
                double coinBoughtAmount = DatabaseController.buyMaximumCoins(
                        Realm.getDefaultInstance(),
                        currencyCode,
                        listView_price.getCurrentPrice());

                // Clenp up and show messages.
                requestAssetFragmentRefresh();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Bought All " + currencyCode + " "
                                + String.format("%.2f", coinBoughtAmount),
                        Toast.LENGTH_SHORT).show();

                return true;
            }
        });

        // Refresh the data in the ListView periodically.
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateListView();
                    }
                });
            }
        }, 0, RealtimeBitcoinCasinoApp.REFRESH_RATE_DATA);
    }

    public void updateListView() {
        listView_Price_Adapter.clear();
        listView_Price_Adapter.addAll(populateFromRealmDatabase());
        listView_Price_Adapter.notifyDataSetChanged();
    }

    public List<ListView_Price> populateFromRealmDatabase() {
        // Get the most recent data from realm.
        Realm realm = Realm.getDefaultInstance();

        DataModel_PriceIndicesNow current
                = DatabaseController.getTPriceIndicesFromRealmDatabase(
                        realm, DataModel_PriceIndicesNow.UNIQUE_ID_CURRENT);
        DataModel_PriceIndicesNow previous
                = DatabaseController.getTPriceIndicesFromRealmDatabase(
                        realm, DataModel_PriceIndicesNow.UNIQUE_ID_PREVIOUS);

        // Safety net for the initial condition.
        if (current == null) {
            current = new DataModel_PriceIndicesNow(DataModel_PriceIndicesNow.UNIQUE_ID_CURRENT);
        }
        if (previous == null) { previous = current; }

        // Make a list for the adapter.
        List<ListView_Price> priceIndices = new ArrayList<ListView_Price>();

        // BTC
        ListView_Price btc = new ListView_Price();
        btc.setCurrencyImage(R.drawable.bitcoin);
        btc.setCurrencyCode("BTC");
        btc.setCurrencyName("Bitcoin");
        btc.setCurrentTime(new Date(current.getTime()));
        btc.setCurrentPrice(current.getBtc());

        double fluctuatingAmountBtc = (current.getBtc() - previous.getBtc());
        btc.setFluctuatingAmount(fluctuatingAmountBtc);
        if (0.009 < fluctuatingAmountBtc) {
            btc.setFluctuatingAmountImage(R.mipmap.price_up);
        } else if (fluctuatingAmountBtc < -0.009) {
            btc.setFluctuatingAmountImage(R.mipmap.price_down);
        } else {
            btc.setFluctuatingAmountImage(R.mipmap.price_stay);
        }

        // LTC
        ListView_Price ltc = new ListView_Price();
        ltc.setCurrencyImage(R.drawable.litecoin);
        ltc.setCurrencyCode("LTC");
        ltc.setCurrencyName("Litecoin");
        ltc.setCurrentTime(new Date(current.getTime()));
        ltc.setCurrentPrice(current.getLtc());

        double fluctuatingAmountLtc = (current.getLtc() - previous.getLtc());
        ltc.setFluctuatingAmount(fluctuatingAmountLtc);
        if (0.009 < fluctuatingAmountLtc) {
            ltc.setFluctuatingAmountImage(R.mipmap.price_up);
        } else if (fluctuatingAmountBtc < -0.009) {
            ltc.setFluctuatingAmountImage(R.mipmap.price_down);
        } else {
            ltc.setFluctuatingAmountImage(R.mipmap.price_stay);
        }

        // ETH
        ListView_Price eth = new ListView_Price();
        eth.setCurrencyImage(R.drawable.ethereum);
        eth.setCurrencyCode("ETH");
        eth.setCurrencyName("Ethereum");
        eth.setCurrentTime(new Date(current.getTime()));
        eth.setCurrentPrice(current.getEth());

        double fluctuatingAmountEth = (current.getEth() - previous.getEth());
        eth.setFluctuatingAmount(fluctuatingAmountEth);
        if (0.009 < fluctuatingAmountEth) {
            eth.setFluctuatingAmountImage(R.mipmap.price_up);
        } else if (fluctuatingAmountBtc < -0.009) {
            eth.setFluctuatingAmountImage(R.mipmap.price_down);
        } else {
            eth.setFluctuatingAmountImage(R.mipmap.price_stay);
        }

        // ZEC
        ListView_Price zec = new ListView_Price();
        zec.setCurrencyImage(R.drawable.zcash);
        zec.setCurrencyCode("ZEC");
        zec.setCurrencyName("Zcash");
        zec.setCurrentTime(new Date(current.getTime()));
        zec.setCurrentPrice(current.getZec());

        double fluctuatingAmountZec = (current.getZec() - previous.getZec());
        zec.setFluctuatingAmount(fluctuatingAmountZec);
        if (0.009 < fluctuatingAmountZec) {
            zec.setFluctuatingAmountImage(R.mipmap.price_up);
        } else if (fluctuatingAmountBtc < -0.009) {
            zec.setFluctuatingAmountImage(R.mipmap.price_down);
        } else {
            zec.setFluctuatingAmountImage(R.mipmap.price_stay);
        }

        // XRP
        ListView_Price xrp = new ListView_Price();
        xrp.setCurrencyImage(R.drawable.ripple);
        xrp.setCurrencyCode("XRP");
        xrp.setCurrencyName("Ripple");
        xrp.setCurrentTime(new Date(current.getTime()));
        xrp.setCurrentPrice(current.getXrp());

        double fluctuatingAmountXrp = (current.getXrp() - previous.getXrp());
        xrp.setFluctuatingAmount(fluctuatingAmountXrp);
        if (0.009 < fluctuatingAmountXrp) {
            xrp.setFluctuatingAmountImage(R.mipmap.price_up);
        } else if (fluctuatingAmountBtc < -0.009) {
            xrp.setFluctuatingAmountImage(R.mipmap.price_down);
        } else {
            xrp.setFluctuatingAmountImage(R.mipmap.price_stay);
        }

        // Add all coins to the list.
        priceIndices.add(btc);
        priceIndices.add(ltc);
        priceIndices.add(eth);
        priceIndices.add(zec);
        priceIndices.add(xrp);

        return priceIndices;
    }

    private void requestAssetFragmentRefresh() {
        Intent intent = new Intent();
        intent.setAction("com.comp3617.finalproject." + TabFragment_Asset.REFRESH_ASSET_FRAGMENT);
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES);
        getActivity().sendBroadcast(intent);
    }

}

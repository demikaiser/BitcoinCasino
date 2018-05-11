package com.comp3617.bitcoinCasino;


import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3617.finalproject.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import io.realm.Realm;


public class TabFragment_Asset extends Fragment {

    private Realm realm;

    BroadcastReceiver broadcastReceiver;
    public static final String REFRESH_ASSET_FRAGMENT = "REFRESH_ASSET_FRAGMENT";

    TextView totalAsset;
    TextView cashAmount;
    TextView productAmount;
    TextView resetAll;

    ListView listViewForAsset;
    ListView_Asset_Adapter listView_Asset_Adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_asset, container, false);

        // Setup the broadcast receiver.
        setBroadcastReceiver();

        return view;
    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();

        // Unregister the broadcast receiver.
        getActivity().getApplicationContext().unregisterReceiver(broadcastReceiver);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        realm = Realm.getDefaultInstance();

        totalAsset = (TextView) getActivity().findViewById(R.id.totalAsset);
        cashAmount = (TextView) getActivity().findViewById(R.id.cashAmount);
        productAmount = (TextView) getActivity().findViewById(R.id.productAmount);

        resetAll = (TextView) getActivity().findViewById(R.id.resetAll);
        resetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Reset the user profile for starting over.
                // Make a new user profile
                ResetUserProfileDialogFragment dialogFragment = new ResetUserProfileDialogFragment();
                dialogFragment.show(getActivity().getFragmentManager(), "Dialog_Delete_Fragment");

            }
        });

        /* List View for Asset */
        // Set an adapter for the ListView to inflate the custom lists.
        listViewForAsset = (ListView) getActivity().findViewById(R.id.listViewForAsset);
        listView_Asset_Adapter = new ListView_Asset_Adapter(getContext(),
                populateFromRealmDatabase());
        listViewForAsset.setAdapter(listView_Asset_Adapter);

        // Register a listener for short-clicking a coin.
        listViewForAsset.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                // Get information from the selected item.
                List<ListView_Asset> list = populateFromRealmDatabase();
                ListView_Asset listView_asset = list.get(index);
                String assetCode = listView_asset.getAssetCode();
                double assetPrice = listView_asset.getAssetPrice();
                double assetAmount = listView_asset.getAssetAmount();

                // Buy coins by starting a dialog fragment with arguments.
                TabFragment_Asset_Sell dialogFragment
                        = TabFragment_Asset_Sell.newInstance(assetCode, assetPrice, assetAmount);
                // Set up the custom listener for call back mechanism.
                dialogFragment.setOnSellDialogListener(new TabFragment_Asset_Sell.OnSellDialogListener() {
                    @Override
                    public void onSellingPerformed(boolean isSold, String currencyCode, double coinSoldAmount) {
                        if (isSold) {
                            updateView();
                            updateListView();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Sold " + currencyCode + " "
                                            + String.format("%.2f", coinSoldAmount),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dialogFragment.show(getFragmentManager(), null);
            }
        });

        // Register a listener for long-clicking a coin.
        listViewForAsset.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Get information from the selected item.
                List<ListView_Asset> list = populateFromRealmDatabase();
                ListView_Asset listView_asset = list.get(position);
                String currencyCode = listView_asset.getAssetCode();

                // Sell all coins selected.
                double coinSoldAmount = DatabaseController.sellMaximumCoins(
                        Realm.getDefaultInstance(),
                        currencyCode,
                        listView_asset.getAssetPrice());

                // Clenp up and show messages.
                updateView();
                updateListView();
                Toast.makeText(getActivity().getApplicationContext(),
                        "Sold All " + currencyCode + " "
                                + String.format("%.2f", coinSoldAmount),
                        Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        // Refresh the data in the fragment periodically.
        Timer timer = new Timer();
        timer.schedule(new TimerTask(){
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateView();
                        updateListView();
                    }
                });
            }
        }, 0, RealtimeBitcoinCasinoApp.REFRESH_RATE_DATA);
    }

    public void updateView() {
        // Get the most recent data from realm.
        Realm realm = Realm.getDefaultInstance();

        DataModel_UserProfile userProfile
                = DatabaseController.getTUserProfileFromRealmDatabase(
                realm, DataModel_UserProfile.UNIQUE_ID_THE_USER);

        // If there is no user data, make new one with a default setting.
        if (userProfile == null) {
            Logger.error(this, "A user profile has been created for the first time.");
            userProfile = new DataModel_UserProfile(DataModel_UserProfile.UNIQUE_ID_THE_USER);
            userProfile.setCash(DataModel_UserProfile.STARTING_CASH);
            DatabaseController.setUserProfileToRealmDatabase(realm,
                    DataModel_UserProfile.UNIQUE_ID_THE_USER, userProfile);
        }

        // Start evaluation.
        double totalAssetEvaluated = 0;
        double cashAmountEvaluated = userProfile.getCash();
        double productAmountEvaluated = 0;

        // Calculate productAmount.
        DataModel_PriceIndicesNow currentPrice
                = DatabaseController.getTPriceIndicesFromRealmDatabase(
                realm, DataModel_PriceIndicesNow.UNIQUE_ID_CURRENT);

        if (currentPrice == null) {
            currentPrice = new DataModel_PriceIndicesNow(DataModel_PriceIndicesNow.UNIQUE_ID_CURRENT);
        }

        productAmountEvaluated = userProfile.getCoinBtc() * currentPrice.getBtc()
                + userProfile.getCoinLtc() * currentPrice.getLtc()
                + userProfile.getCoinEth() * currentPrice.getEth()
                + userProfile.getCoinZec() * currentPrice.getZec()
                + userProfile.getCoinXrp() * currentPrice.getXrp();

        // Calculate totalAsset.
        totalAssetEvaluated = cashAmountEvaluated + productAmountEvaluated;

        // Update values.
        totalAsset.setText(String.format("%.2f", totalAssetEvaluated));
        cashAmount.setText(String.format("%.2f", cashAmountEvaluated));
        productAmount.setText(String.format("%.2f", productAmountEvaluated));

    }

    public void updateListView() {
        listView_Asset_Adapter.clear();
        listView_Asset_Adapter.addAll(populateFromRealmDatabase());
        listView_Asset_Adapter.notifyDataSetChanged();
    }

    public List<ListView_Asset> populateFromRealmDatabase() {
        // Get the most recent data from realm.
        Realm realm = Realm.getDefaultInstance();

        DataModel_UserProfile userProfile
                = DatabaseController.getTUserProfileFromRealmDatabase(
                realm, DataModel_UserProfile.UNIQUE_ID_THE_USER);

        DataModel_PriceIndicesNow currentPrice
                = DatabaseController.getTPriceIndicesFromRealmDatabase(
                realm, DataModel_PriceIndicesNow.UNIQUE_ID_CURRENT);

        // Make a list for the adapter.
        List<ListView_Asset> assets = new ArrayList<ListView_Asset>();
        // Safety net for the initial condition by returning an empty asset list.
        if ((userProfile == null) || (currentPrice == null)) {
            return assets;
        }

        // BTC
        if (userProfile.getCoinBtc() != 0) {
            ListView_Asset btc = new ListView_Asset();
            btc.setAssetImage(R.drawable.bitcoin);
            btc.setAssetCode("BTC");
            btc.setAssetName("Bitcoin");
            btc.setCurrentTime(new Date(currentPrice.getTime()));
            btc.setAssetPrice(currentPrice.getBtc());
            btc.setAssetAmount(userProfile.getCoinBtc());

            assets.add(btc);
        }

        // LTC
        if (userProfile.getCoinLtc() != 0) {
            ListView_Asset ltc = new ListView_Asset();
            ltc.setAssetImage(R.drawable.litecoin);
            ltc.setAssetCode("LTC");
            ltc.setAssetName("Litecoin");
            ltc.setCurrentTime(new Date(currentPrice.getTime()));
            ltc.setAssetPrice(currentPrice.getLtc());
            ltc.setAssetAmount(userProfile.getCoinLtc());

            assets.add(ltc);
        }

        // ETH
        if (userProfile.getCoinEth() != 0) {
            ListView_Asset eth = new ListView_Asset();
            eth.setAssetImage(R.drawable.ethereum);
            eth.setAssetCode("ETH");
            eth.setAssetName("Ethereum");
            eth.setCurrentTime(new Date(currentPrice.getTime()));
            eth.setAssetPrice(currentPrice.getEth());
            eth.setAssetAmount(userProfile.getCoinEth());

            assets.add(eth);
        }

        // ZEC
        if (userProfile.getCoinZec() != 0) {
            ListView_Asset zec = new ListView_Asset();
            zec.setAssetImage(R.drawable.zcash);
            zec.setAssetCode("ZEC");
            zec.setAssetName("Zcash");
            zec.setCurrentTime(new Date(currentPrice.getTime()));
            zec.setAssetPrice(currentPrice.getZec());
            zec.setAssetAmount(userProfile.getCoinZec());

            assets.add(zec);
        }

        // XRP
        if (userProfile.getCoinXrp() != 0) {
            ListView_Asset xrp = new ListView_Asset();
            xrp.setAssetImage(R.drawable.ripple);
            xrp.setAssetCode("XRP");
            xrp.setAssetName("Ripple");
            xrp.setCurrentTime(new Date(currentPrice.getTime()));
            xrp.setAssetPrice(currentPrice.getXrp());
            xrp.setAssetAmount(userProfile.getCoinXrp());

            assets.add(xrp);
        }

        return assets;
    }

    private void setBroadcastReceiver() {
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent i) {
                updateView();
                updateListView();
            }
        };
        getActivity().getApplicationContext()
                .registerReceiver(broadcastReceiver,
                        new IntentFilter("com.comp3617.finalproject." + REFRESH_ASSET_FRAGMENT));

    }

    public static class ResetUserProfileDialogFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogForReset);
            builder.setMessage(R.string.dialog_reset_message)
                    .setPositiveButton(R.string.dialog_reset_reset, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Realm realm = Realm.getDefaultInstance();
                            DataModel_UserProfile userProfile
                                    = new DataModel_UserProfile(DataModel_UserProfile.UNIQUE_ID_THE_USER);
                            userProfile.setCash(DataModel_UserProfile.STARTING_CASH);
                            DatabaseController.setUserProfileToRealmDatabase(realm,
                                    DataModel_UserProfile.UNIQUE_ID_THE_USER, userProfile);
                            realm.close();
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Profile Reset Success", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton(R.string.dialog_reset_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "Profile Reset Cancelled", Toast.LENGTH_SHORT).show();
                        }
                    });
            return builder.create();
        }
    }
}

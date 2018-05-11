package com.comp3617.bitcoinCasino;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.comp3617.finalproject.R;

import java.util.Date;

import io.realm.Realm;

public class TabFragment_Asset_Sell extends DialogFragment {

    private static final String COIN_NAME = "COIN_NAME";
    private static final String COIN_PRICE = "COIN_PRICE";
    private static final String COIN_AMOUNT = "COIN_AMOUNT";

    private Realm realm;
    private String currencyCode;

    OnSellDialogListener onSellDialogListener;

    public interface OnSellDialogListener {
        void onSellingPerformed(boolean isSold, String currencyCode, double coinSoldAmount);
    }

    public void setOnSellDialogListener(OnSellDialogListener onSellDialogListener) {
        this.onSellDialogListener = onSellDialogListener;
    }

    private DataModel_UserProfile userProfile;
    private double userCash;
    private double totalAmount;

    ImageView coinImage;
    TextView currentTime;
    TextView coinPrice;

    SeekBar coinSellSeekBar;
    TextView transactionMinValue;
    TextView transactionMaxValue;

    EditText coinSellEnterValue;

    TextView coinSellAmount;
    TextView coinSellTotalValue;

    TextView coinCancel;
    TextView coinSell;

    public static TabFragment_Asset_Sell newInstance(String coinName, double coinPrice, double coinAmount) {

        Bundle args = new Bundle();
        args.putString(COIN_NAME, coinName);
        args.putDouble(COIN_PRICE, coinPrice);
        args.putDouble(COIN_AMOUNT, coinAmount);

        TabFragment_Asset_Sell fragment = new TabFragment_Asset_Sell();
        fragment.setArguments(args);
        return fragment;
    }

    public TabFragment_Asset_Sell() {
        // Empty public constructor is required.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_asset_sell, container, false);

        // Get passed arguments.
        Bundle arguments = getArguments();

        currencyCode = arguments.getString(COIN_NAME);
        final double assetPrice = arguments.getDouble(COIN_PRICE);
        final double assetAmount = arguments.getDouble(COIN_AMOUNT);

        totalAmount = assetPrice * assetAmount;

        // Get user's profile information.
        realm = Realm.getDefaultInstance();

        userProfile = DatabaseController.getTUserProfileFromRealmDatabase(
                realm, DataModel_UserProfile.UNIQUE_ID_THE_USER);

        userCash = userProfile.getCash();

        // UI Controllers.
        coinImage = (ImageView) rootView.findViewById(R.id.coinImage);
        if (currencyCode.equalsIgnoreCase("BTC")) {
            coinImage.setImageResource(R.drawable.bitcoin);
        } else if (currencyCode.equalsIgnoreCase("LTC")) {
            coinImage.setImageResource(R.drawable.litecoin);
        } else if (currencyCode.equalsIgnoreCase("ETH")) {
            coinImage.setImageResource(R.drawable.ethereum);
        } else if (currencyCode.equalsIgnoreCase("ZEC")) {
            coinImage.setImageResource(R.drawable.zcash);
        } else if (currencyCode.equalsIgnoreCase("XRP")) {
            coinImage.setImageResource(R.drawable.ripple);
        }

        currentTime = (TextView) rootView.findViewById(R.id.currentTime);
        currentTime.setText((new Date().toString()));

        coinPrice = (TextView) rootView.findViewById(R.id.coinPrice);
        coinPrice.setText(String.format("%.2f", assetPrice));

        coinSellSeekBar = (SeekBar) rootView.findViewById(R.id.coinBuySeekBar);
        coinSellSeekBar.setProgress(0);
        coinSellSeekBar.setMax((int) totalAmount);
        coinSellSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double sellAmount = progress / assetPrice;

                coinSellEnterValue.setText("" + progress);
                coinSellAmount.setText(String.format("%.2f", sellAmount));
                coinSellTotalValue.setText("" + progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        transactionMinValue = (TextView) rootView.findViewById(R.id.transactionMinValue);
        transactionMinValue.setText("Min: 0");
        transactionMaxValue = (TextView) rootView.findViewById(R.id.transactionMaxValue);
        transactionMaxValue.setText("Max: " + (int)totalAmount);

        coinSellEnterValue = (EditText) rootView.findViewById(R.id.coinBuyEnterValue);
        coinSellEnterValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        coinSellEnterValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!s.toString().isEmpty()) {
                        coinSellSeekBar.setProgress(Integer.parseInt(s.toString()));
                        coinSellEnterValue.setSelection(coinSellEnterValue.getText().length());
                    }
                } catch (Exception exception) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        coinSellAmount = (TextView) rootView.findViewById(R.id.coinBuyAmount);
        coinSellAmount.setText("0");

        coinSellTotalValue = (TextView) rootView.findViewById(R.id.coinBuyTotalValue);
        coinSellTotalValue.setText("0");

        coinCancel = (TextView) rootView.findViewById(R.id.coinBuyCancel);
        coinCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Callback to the parent fragment.
                onSellDialogListener.onSellingPerformed(false, null, 0);

                // Close and clean up.
                realm.close();
                dismiss();
            }
        });

        coinSell = (TextView) rootView.findViewById(R.id.coinBuyBuy);
        coinSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buy the coin from the cash selected.
                double cashGained;

                if (coinSellSeekBar.getProgress() == ((int) totalAmount)) {
                    cashGained = totalAmount;
                } else {
                    cashGained = Double.parseDouble(coinSellTotalValue.getText().toString());
                }

                double coinSoldAmount = cashGained / assetPrice;
                double cashLeft = userCash + cashGained;

                // Make a new user profile
                DataModel_UserProfile newUserProfile = new DataModel_UserProfile();
                newUserProfile.setCoinBtc(userProfile.getCoinBtc());
                newUserProfile.setCoinLtc(userProfile.getCoinLtc());
                newUserProfile.setCoinEth(userProfile.getCoinEth());
                newUserProfile.setCoinZec(userProfile.getCoinZec());
                newUserProfile.setCoinXrp(userProfile.getCoinXrp());

                // Update new cash amount.
                newUserProfile.setCash(cashLeft);

                // Update new coin amount.
                if (currencyCode.equalsIgnoreCase("BTC")) {
                    newUserProfile.subtractCoinBtc(coinSoldAmount);
                } else if (currencyCode.equalsIgnoreCase("LTC")) {
                    newUserProfile.subtractCoinLtc(coinSoldAmount);
                } else if (currencyCode.equalsIgnoreCase("ETH")) {
                    newUserProfile.subtractCoinEth(coinSoldAmount);
                } else if (currencyCode.equalsIgnoreCase("ZEC")) {
                    newUserProfile.subtractCoinZec(coinSoldAmount);
                } else if (currencyCode.equalsIgnoreCase("XRP")) {
                    newUserProfile.subtractCoinXrp(coinSoldAmount);
                }

                // Save to the database.
                DatabaseController.setUserProfileToRealmDatabase(realm,
                        DataModel_UserProfile.UNIQUE_ID_THE_USER, newUserProfile);

                // Callback to the parent fragment.
                onSellDialogListener.onSellingPerformed(true, currencyCode, coinSoldAmount);

                // Dismiss.
                realm.close();
                dismiss();
            }
        });

        return rootView;
    }
}

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

public class TabFragment_Coins_Buy extends DialogFragment {

    private static final String COIN_NAME = "COIN_NAME";
    private static final String COIN_PRICE = "COIN_PRICE";

    private Realm realm;
    private String currencyCode;

    OnBuyDialogListener onBuyDialogListener;

    public interface OnBuyDialogListener {
        void onBuyingPerformed(boolean isBought, String currencyCode, double coinBoughtAmount);
    }

    public void setOnBuyDialogListener(OnBuyDialogListener onBuyDialogListener) {
        this.onBuyDialogListener = onBuyDialogListener;
    }

    private DataModel_UserProfile userProfile;
    private double userCash;

    ImageView coinImage;
    TextView currentTime;
    TextView coinPrice;

    SeekBar coinBuySeekBar;
    TextView transactionMinValue;
    TextView transactionMaxValue;

    EditText coinBuyEnterValue;

    TextView coinBuyAmount;
    TextView coinBuyTotalValue;

    TextView coinCancel;
    TextView coinBuy;

    public static TabFragment_Coins_Buy newInstance(String coinName, double coinPrice) {

        Bundle args = new Bundle();
        args.putString(COIN_NAME, coinName);
        args.putDouble(COIN_PRICE, coinPrice);

        TabFragment_Coins_Buy fragment = new TabFragment_Coins_Buy();
        fragment.setArguments(args);
        return fragment;
    }

    public TabFragment_Coins_Buy() {
        // Empty public constructor is required.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.tab_fragment_coins_buy, container, false);

        // Get passed arguments.
        Bundle arguments = getArguments();

        currencyCode = arguments.getString(COIN_NAME);
        final double currentPrice = arguments.getDouble(COIN_PRICE);

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
        coinPrice.setText(String.format("%.2f", currentPrice));

        coinBuySeekBar = (SeekBar) rootView.findViewById(R.id.coinBuySeekBar);
        coinBuySeekBar.setProgress(0);
        coinBuySeekBar.setMax((int)userCash);
        coinBuySeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                double buyAmount = progress / currentPrice;

                coinBuyEnterValue.setText("" + progress);
                coinBuyAmount.setText(String.format("%.2f", buyAmount));
                coinBuyTotalValue.setText("" + progress);
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
        transactionMaxValue.setText("Max: " + (int)userCash);

        coinBuyEnterValue = (EditText) rootView.findViewById(R.id.coinBuyEnterValue);
        coinBuyEnterValue.setInputType(InputType.TYPE_CLASS_NUMBER);
        coinBuyEnterValue.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    if (!s.toString().isEmpty()) {
                        coinBuySeekBar.setProgress(Integer.parseInt(s.toString()));
                        coinBuyEnterValue.setSelection(coinBuyEnterValue.getText().length());
                    }
                } catch (Exception exception) {

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        coinBuyAmount = (TextView) rootView.findViewById(R.id.coinBuyAmount);
        coinBuyAmount.setText("0");

        coinBuyTotalValue = (TextView) rootView.findViewById(R.id.coinBuyTotalValue);
        coinBuyTotalValue.setText("0");

        coinCancel = (TextView) rootView.findViewById(R.id.coinBuyCancel);
        coinCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Callback to the parent fragment.
                onBuyDialogListener.onBuyingPerformed(false, null, 0);

                // Close and clean up.
                realm.close();
                dismiss();
            }
        });

        coinBuy = (TextView) rootView.findViewById(R.id.coinBuyBuy);
        coinBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buy the coin from the cash selected.
                double cashSpent = Double.parseDouble(coinBuyTotalValue.getText().toString());
                double coinBoughtAmount = cashSpent / currentPrice;
                double cashLeft = userCash - cashSpent;

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
                    newUserProfile.addCoinBtc(coinBoughtAmount);
                } else if (currencyCode.equalsIgnoreCase("LTC")) {
                    newUserProfile.addCoinLtc(coinBoughtAmount);
                } else if (currencyCode.equalsIgnoreCase("ETH")) {
                    newUserProfile.addCoinEth(coinBoughtAmount);
                } else if (currencyCode.equalsIgnoreCase("ZEC")) {
                    newUserProfile.addCoinZec(coinBoughtAmount);
                } else if (currencyCode.equalsIgnoreCase("XRP")) {
                    newUserProfile.addCoinXrp(coinBoughtAmount);
                }

                // Save to the database.
                DatabaseController.setUserProfileToRealmDatabase(realm,
                        DataModel_UserProfile.UNIQUE_ID_THE_USER, newUserProfile);

                // Callback to the parent fragment.
                onBuyDialogListener.onBuyingPerformed(true, currencyCode, coinBoughtAmount);

                // Dismiss.
                realm.close();
                dismiss();
            }
        });



        return rootView;
    }
}

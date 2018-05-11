package com.comp3617.bitcoinCasino;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comp3617.finalproject.R;

import java.util.List;


public class ListView_Price_Adapter extends ArrayAdapter<ListView_Price> {
    private final Context context;
    private List<ListView_Price> priceIndices;

    public ListView_Price_Adapter(Context context, List<ListView_Price> priceIndices) {
        super(context, 0, priceIndices);
        this.context = context;
        this.priceIndices = priceIndices;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;

        if (convertView == null) {
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_view_coins, parent, false);
        } else {
            rowView = convertView;
        }

        ImageView currencyImage = (ImageView) rowView.findViewById(R.id.imageViewCurrencyImage);
        TextView currencyCode = (TextView) rowView.findViewById(R.id.textViewCurrencyCode);
        TextView currencyName = (TextView) rowView.findViewById(R.id.textViewCurrencyName);
        TextView currencyTime = (TextView) rowView.findViewById(R.id.textViewCurrencyTime);
        TextView currencyPrice = (TextView) rowView.findViewById(R.id.textViewCurrencyPrice);
        TextView currencyFluctuation = (TextView) rowView.findViewById(R.id.textViewCurrencyFluctuation);
        ImageView currencyFluctuationImage = (ImageView) rowView.findViewById(R.id.imageViewCurrencyFluctuation);


        ListView_Price priceIndex = priceIndices.get(position);

        currencyImage.setImageResource(priceIndex.getCurrencyImage());
        currencyCode.setText(priceIndex.getCurrencyCode());
        currencyName.setText(priceIndex.getCurrencyName());
        currencyTime.setText(priceIndex.getCurrentTime().toString());
        currencyPrice.setText(String.valueOf(priceIndex.getCurrentPrice()));
        currencyFluctuation.setText(String.format("%.2f", priceIndex.getFluctuatingAmount()));
        currencyFluctuationImage.setImageResource(priceIndex.getFluctuatingAmountImage());

        return rowView;
    }
}

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

public class ListView_Asset_Adapter extends ArrayAdapter<ListView_Asset> {
    private final Context context;
    private List<ListView_Asset> assets;

    public ListView_Asset_Adapter(Context context, List<ListView_Asset> assets) {
        super(context, 0, assets);
        this.context = context;
        this.assets = assets;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;

        if (convertView == null) {
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_view_assets, parent, false);
        } else {
            rowView = convertView;
        }

        ImageView assetImage = (ImageView) rowView.findViewById(R.id.imageViewAssetImage);
        TextView assetCode = (TextView) rowView.findViewById(R.id.textViewAssetCode);
        TextView assetName = (TextView) rowView.findViewById(R.id.textViewAssetName);
        TextView assetTime = (TextView) rowView.findViewById(R.id.textViewAssetTime);
        TextView assetPrice = (TextView) rowView.findViewById(R.id.textViewAssetPrice);
        TextView assetCalculation = (TextView) rowView.findViewById(R.id.textViewAssetAmount);

        ListView_Asset asset = assets.get(position);

        assetImage.setImageResource(asset.getAssetImage());
        assetCode.setText(asset.getAssetCode());
        assetName.setText(asset.getAssetName());
        assetTime.setText(asset.getCurrentTime().toString());
        assetPrice.setText(String.valueOf(asset.getAssetPrice()));

        String calculation = " x " + String.format("%.2f", asset.getAssetAmount()) + " = "
                + String.format("%.2f", asset.getAssetPrice() * asset.getAssetAmount());
        assetCalculation.setText(calculation);

        return rowView;
    }



}

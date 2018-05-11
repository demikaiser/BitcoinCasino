package com.comp3617.bitcoinCasino;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.comp3617.finalproject.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ListView_News_Adapter extends ArrayAdapter<ListView_News> {

    private final Context context;
    private List<ListView_News> newsList;

    public ListView_News_Adapter(Context context, List<ListView_News> newsList) {
        super(context, 0, newsList);
        this.context = context;
        this.newsList = newsList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = null;

        if (convertView == null) {
            LayoutInflater inflater
                    = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.list_view_news, parent, false);
        } else {
            rowView = convertView;
        }

        ImageView newsImage = (ImageView) rowView.findViewById(R.id.imageViewNewsImage);
        TextView newsTitle = (TextView) rowView.findViewById(R.id.textViewNewsTitle);
        TextView newsDescription = (TextView) rowView.findViewById(R.id.textViewNewsDescription);
        TextView newsTime = (TextView) rowView.findViewById(R.id.textViewNewsTime);

        ListView_News news = newsList.get(position);

        // Download the image to display with Picasso.
        Picasso.with(context).load(news.getImageURL()).into(newsImage);
        newsTitle.setText(news.getTitle());
        newsDescription.setText(news.getDescription());
        newsTime.setText(news.getPubDate());


        return rowView;
    }
}

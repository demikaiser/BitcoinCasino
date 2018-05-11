package com.comp3617.bitcoinCasino;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
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
import java.util.List;

public class TabFragment_News extends Fragment {

    TextView refreshNews;

    ListView listViewForNews;
    ListView_News_Adapter listView_news_adapter;

    List<ListView_News> newsArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_news, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        refreshNews = (TextView) getActivity().findViewById(R.id.refresh_news);
        refreshNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Refresh the news.
                (new GetNewsListFromCoinTelegraph()).execute();
            }
        });

         /* List View for News */

        // Set an adapter for the ListView to inflate the custom lists.
        listViewForNews = (ListView) getActivity().findViewById(R.id.listViewForNews);
        listView_news_adapter = new ListView_News_Adapter(getContext(), newsArrayList);
        listViewForNews.setAdapter(listView_news_adapter);

        // Register a listener for short-clicking a news.
        listViewForNews.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int index, long id) {
                // Follow the link to view the full article.
                ListView_News listView_news = newsArrayList.get(index);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(listView_news.getLink()));
                startActivity(intent);
            }
        });
    }

    private class GetNewsListFromCoinTelegraph extends AsyncTask<Void, String, Void> {

        private static final String COIN_TELEGRAPH_NEWS_URL = "https://cointelegraph.com/rss";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            publishProgress("Fetching Data...");
            ArrayList<DataModel_CryptoCurrencyNews> newsList =
                    RestClient_GET_CryptoCurrencyNews
                            .getInstanceWithUrl(COIN_TELEGRAPH_NEWS_URL).run();

            List<ListView_News> tempNewsList = new ArrayList<>();

            publishProgress("Loading Data...");
            for (DataModel_CryptoCurrencyNews news : newsList) {

                ListView_News newsToAdd = new ListView_News(
                        news.getImageURL(),
                        news.getTitle(),
                        news.getDescription(),
                        news.getLink(),
                        news.getPubDate());

                tempNewsList.add(newsToAdd);
            }

            newsArrayList = tempNewsList;
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            refreshNews.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            updateListView();
            refreshNews.setText("REFRESH NEWS");

            Toast.makeText(getActivity().getApplicationContext(),
                    "News Updated", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateListView() {
        listView_news_adapter.clear();
        listView_news_adapter.addAll(newsArrayList);
        listView_news_adapter.notifyDataSetChanged();
    }

}

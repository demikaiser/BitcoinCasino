package com.comp3617.bitcoinCasino;

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
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmList;


public class TabFragment_Charts extends Fragment {

    TextView selectChart;

    ListView listViewForCharts;
    ListView_Charts_Adapter listView_Charts_adapter;

    List<ListView_Charts> chartsArrayList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_fragment_charts, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        selectChart = (TextView) getActivity().findViewById(R.id.selectChart);
        selectChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Buy coins by starting a dialog fragment with arguments.
                TabFragment_Charts_Select dialogFragment
                        = TabFragment_Charts_Select.newInstance();
                // Set up the custom listener for call back mechanism.
                dialogFragment.setOnSelectChartListener(new TabFragment_Charts_Select.OnSelectChartListener() {
                    @Override
                    public void onSelectChartPerformed(String chart, String coin,
                                                       String period, String limit) {
                        (new GetPriceHistoryDataForChart()).execute(chart, coin, period, limit);
                    }
                });
                dialogFragment.show(getFragmentManager(), null);
            }
        });

        /* List View for Charts */

        // Set an adapter for the ListView to inflate the custom lists.
        listViewForCharts = (ListView) getActivity().findViewById(R.id.listViewForCharts);
        listView_Charts_adapter = new ListView_Charts_Adapter(getContext(), chartsArrayList);
        listViewForCharts.setAdapter(listView_Charts_adapter);

        // Register a listener for short-clicking a chart.
        listViewForCharts.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /* Pop a chart from the list view. */
                Toast.makeText(getActivity().getApplicationContext(),
                        "Chart Deleted", Toast.LENGTH_SHORT).show();

                /* 1. Get rid of the selected chart. */
                Realm realmForDeletion = Realm.getDefaultInstance();
                ListView_Charts listView_charts = chartsArrayList.get(position);
                DatabaseController.deleteListView_ChartsToRealmDatabase(realmForDeletion,
                        listView_charts.getId());

                /* 2. Update the data and the list view.*/
                updateListViewData();
                updateListView();
                return true;
            }
        });

        updateListViewData();
        updateListView();
    }

    private class GetPriceHistoryDataForChart extends AsyncTask<String, String, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(String... params) {

            /* Insert a chart to the list view. */
            String chart = params[0];
            String coin = params[1];
            String period = params[2];
            String limit = params[3];

            ArrayList<DataModel_PriceHistory> priceHistories = new ArrayList<>();

            /* 1. Get the data through the Restful call. */
            publishProgress("Fetching Data...");

            switch (period) {
                case "MIN":
                    priceHistories = (new RestClient_GET_PriceHistoy(
                            RestClient_GET_PriceHistoy.MODE_MINUTE, coin, Integer.parseInt(limit))).run();
                    break;
                case "HOUR":
                    priceHistories = (new RestClient_GET_PriceHistoy(
                            RestClient_GET_PriceHistoy.MODE_HOUR, coin, Integer.parseInt(limit))).run();
                    break;
                case "DAY":
                    priceHistories = (new RestClient_GET_PriceHistoy(
                            RestClient_GET_PriceHistoy.MODE_DAY, coin, Integer.parseInt(limit))).run();
                    break;
            }

            Realm realmInBackgroundThread = Realm.getDefaultInstance();

            /* 2. Put the made chart to the chartsArrayList. */
            ListView_Charts listView_charts
                    = new ListView_Charts(new Date().getTime(), chart, coin, period, limit,
                    realmListAdapterFromArrayListToRealm(priceHistories));
            DatabaseController.setListView_ChartsToRealmDatabase(realmInBackgroundThread, listView_charts);

            /* 3. Updating the list view must be here with a delay to prevent some race conditions. */
            publishProgress("Loading Data.");
            try {
                Thread.sleep(300);
                publishProgress("Loading Data..");
                Thread.sleep(300);
                publishProgress("Loading Data...");
                Thread.sleep(300);
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            getActivity().runOnUiThread(new Thread(new Runnable() {
                public void run() {
                    updateListViewData();
                    updateListView();
                }
            }));

            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            selectChart.setText(values[0]);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            selectChart.setText("SELECT A CHART");

            Toast.makeText(getActivity().getApplicationContext(),
                    "Chart Updated", Toast.LENGTH_SHORT).show();
        }
    }

    public void updateListViewData() {

        Realm realmForUpdateListViewData = Realm.getDefaultInstance();

        /* Get data from Realm and put those into the chartsArrayList */
        List<ListView_Charts> chartsArrayListTemp
                = DatabaseController.getAllListView_ChartsFromRealmDatabase(realmForUpdateListViewData);
        List<ListView_Charts> chartsArrayListTarget = new ArrayList<>();

        /* Reverse the order for the fluent user experience. */
        for (ListView_Charts chart : chartsArrayListTemp) {
            /* Some price lists are 0, so it must be pre-validated before getting
            * inflated to the chart view otherwise it will crashes. Implement all
            * validation logic here. */
            if (chart.getArrayListForPriceHistories().size() != 0) {
                chartsArrayListTarget.add(0, chart);
            }
        }
        chartsArrayList = chartsArrayListTarget;
    }

    public void updateListView() {
        /* Standard procedure for updating list view. */
        listView_Charts_adapter.clear();
        listView_Charts_adapter.addAll(chartsArrayList);
        listView_Charts_adapter.notifyDataSetChanged();
    }

    public RealmList<DataModel_PriceHistory> realmListAdapterFromArrayListToRealm(
            ArrayList<DataModel_PriceHistory> arrayList) {
        RealmList<DataModel_PriceHistory> newHistory = new RealmList<>();

        for (DataModel_PriceHistory history : arrayList) {
            newHistory.add(history);
        }

        return newHistory;
    }
}








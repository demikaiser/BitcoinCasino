package com.comp3617.bitcoinCasino;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.comp3617.finalproject.R;
import com.github.mikephil.charting.charts.CandleStickChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.CandleData;
import com.github.mikephil.charting.data.CandleDataSet;
import com.github.mikephil.charting.data.CandleEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListView_Charts_Adapter extends ArrayAdapter<ListView_Charts> {
    private final Context context;
    private List<ListView_Charts> charts;

    public ListView_Charts_Adapter(Context context, List<ListView_Charts> charts) {
        super(context, 0, charts);
        this.context = context;
        this.charts = charts;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ListView_Charts chart = charts.get(position);

        String titleString = chart.getChart() + " (" + chart.getCoin() + "): "
                + chart.getLimit() + " (" + chart.getPeriod() + ")";

        LayoutInflater inflater
                = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (chart.getChart().equalsIgnoreCase("Simple Line Chart")) {
            convertView = inflater.inflate(R.layout.list_view_charts_for_line_chart, parent, false);

            /* Set title. */
            TextView textViewTitle
                    = (TextView) convertView.findViewById(R.id.textViewTitleForListViewForLineChart);
            textViewTitle.setText(titleString);

            /* Simple Line Chart */
            LineChart viewportForLineChart
                    = (LineChart) convertView.findViewById(R.id.viewportForLineChart);

            /* Set description. */
            Description description = new Description();
            description.setText("");
            viewportForLineChart.setDescription(description);

            /* Add entires. */
            List<Entry> entries = new ArrayList<>();
            int dataNumber = 1;
            for (DataModel_PriceHistory data : chart.getArrayListForPriceHistories()) {

                entries.add(new Entry(dataNumber, (float) data.getClose()));
                dataNumber++;
            }

            /* Add entries to the dataset and style it. */
            LineDataSet dataSet = new LineDataSet(entries, chart.getCoin());

            dataSet.setCircleRadius(2.0f);
            dataSet.setCircleColor(Color.rgb(199, 165, 0));
            dataSet.setColor(Color.rgb(199, 165, 0));

            /* Make line data and refresh the viewport. */
            LineData lineData = new LineData(dataSet);
            viewportForLineChart.setData(lineData);
            viewportForLineChart.invalidate(); // refresh

            TextView textViewChartTime
                    = (TextView) convertView.findViewById(R.id.textViewChartTimeForLineChart);
            textViewChartTime.setText(new Date(chart.getId()).toString());

        } else if (chart.getChart().equalsIgnoreCase("Candle Stick Chart")) {
            convertView = inflater.inflate(R.layout.list_view_charts_for_candle_stick_chart, parent, false);

            /* Set title. */
            TextView textViewTitle
                    = (TextView) convertView.findViewById(R.id.textViewTitleForListViewForCandleStickChart);
            textViewTitle.setText(titleString);

            /* Candle Stick Chart */
            CandleStickChart viewportForCandleStickChart
                    = (CandleStickChart) convertView.findViewById(R.id.viewportForCandleStickChart);

            /* Set description. */
            Description description = new Description();
            description.setText("");
            viewportForCandleStickChart.setDescription(description);

            /* Add entires. */
            List<CandleEntry> entriesForCandleStickChart = new ArrayList<>();
            int dataNumber = 1;
            for (DataModel_PriceHistory data : chart.getArrayListForPriceHistories()) {
                entriesForCandleStickChart.add(
                        new CandleEntry(dataNumber,
                                (float) data.getHigh(),
                                (float) data.getLow(),
                                (float) data.getClose(),
                                (float) data.getOpen()));
                dataNumber++;
            }

            /* Add entries to the dataset and style it. */
            CandleDataSet dataSetForCandleStickChart
                    = new CandleDataSet(entriesForCandleStickChart, chart.getCoin());

            dataSetForCandleStickChart.setColors(ColorTemplate.COLORFUL_COLORS, 150);
            dataSetForCandleStickChart.setColor(Color.rgb(80, 80, 80));
            dataSetForCandleStickChart.setShadowColor(Color.DKGRAY);
            dataSetForCandleStickChart.setShadowWidth(1);

            dataSetForCandleStickChart.setDecreasingColor(Color.RED);
            dataSetForCandleStickChart.setDecreasingPaintStyle(Paint.Style.FILL);

            dataSetForCandleStickChart.setIncreasingColor(Color.BLUE);
            dataSetForCandleStickChart.setIncreasingPaintStyle(Paint.Style.FILL);

            dataSetForCandleStickChart.setNeutralColor(Color.BLUE);
            dataSetForCandleStickChart.setValueTextColor(Color.RED);
            dataSetForCandleStickChart.setDrawValues(false);

            /* Make line data and refresh the viewport. */
            CandleData dataForCandleStickChart = new CandleData(dataSetForCandleStickChart);
            viewportForCandleStickChart.setData(dataForCandleStickChart);
            viewportForCandleStickChart.invalidate(); // refresh

            TextView textViewChartTime
                    = (TextView) convertView.findViewById(R.id.textViewChartTimeForCandleStickChart);
            textViewChartTime.setText(new Date(chart.getId()).toString());
        }

        return convertView;

    }


}

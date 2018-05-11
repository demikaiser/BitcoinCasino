package com.comp3617.bitcoinCasino;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.comp3617.finalproject.R;

public class TabFragment_Charts_Select extends DialogFragment {

    OnSelectChartListener onSelectChartListener;

    Spinner spinnerChartSelection;
    Spinner spinnerCoinSelection;
    Spinner spinnerPeriodSelection;
    Spinner spinnerLimitSelection;

    TextView coinChartCancel;
    TextView coinChartShow;

    public interface OnSelectChartListener {
        void onSelectChartPerformed(String chart, String coin, String period, String limit);
    }

    public void setOnSelectChartListener(OnSelectChartListener onSelectChartListener) {
        this.onSelectChartListener = onSelectChartListener;
    }

    public static TabFragment_Charts_Select newInstance() {

        Bundle args = new Bundle();
        args.putString("", "");

        TabFragment_Charts_Select fragment = new TabFragment_Charts_Select();
        fragment.setArguments(args);
        return fragment;
    }

    public TabFragment_Charts_Select() {
        // Empty public constructor is required.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.tab_fragment_charts_select, container, false);

        // Get passed arguments.
        Bundle arguments = getArguments();

        // UI Controllers.
        spinnerChartSelection = (Spinner) rootView.findViewById(R.id.spinnerChartSelection);
        ArrayAdapter<CharSequence> adapterForSpinnerChartSelection = ArrayAdapter.createFromResource(
                rootView.getContext(), R.array.string_array_select_chart_class,
                R.layout.custom_spinner_chart_item);
        adapterForSpinnerChartSelection.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerChartSelection.setAdapter(adapterForSpinnerChartSelection);

        spinnerCoinSelection = (Spinner) rootView.findViewById(R.id.spinnerCoinSelection);
        ArrayAdapter<CharSequence> adapterForSpinnerCoinSelection = ArrayAdapter.createFromResource(
                rootView.getContext(), R.array.string_array_select_chart_coin,
                R.layout.custom_spinner_chart_item);
        adapterForSpinnerCoinSelection.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerCoinSelection.setAdapter(adapterForSpinnerCoinSelection);

        spinnerPeriodSelection = (Spinner) rootView.findViewById(R.id.spinnerPeriodSelection);
        ArrayAdapter<CharSequence> adapterForSpinnerPeriodSelection = ArrayAdapter.createFromResource(
                rootView.getContext(), R.array.string_array_select_chart_period,
                R.layout.custom_spinner_chart_item);
        adapterForSpinnerPeriodSelection.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerPeriodSelection.setAdapter(adapterForSpinnerPeriodSelection);

        spinnerLimitSelection = (Spinner) rootView.findViewById(R.id.spinnerLimitSelection);
        ArrayAdapter<CharSequence> adapterForSpinnerLimitSelection = ArrayAdapter.createFromResource(
                rootView.getContext(), R.array.string_array_select_chart_limit,
                R.layout.custom_spinner_chart_item);
        adapterForSpinnerLimitSelection.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        spinnerLimitSelection.setAdapter(adapterForSpinnerLimitSelection);

        coinChartCancel = (TextView) rootView.findViewById(R.id.coinChartCancel);
        coinChartCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(rootView.getContext(), "Chart Selection Cancelled", Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });

        coinChartShow = (TextView) rootView.findViewById(R.id.coinChartShow);
        coinChartShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String chart = spinnerChartSelection.getSelectedItem().toString();

                /* Coin String Processing */
                String coinTemp = spinnerCoinSelection.getSelectedItem().toString();
                String coin = "";
                if (coinTemp.contains("BTC")) {
                    coin = "BTC";
                } else if (coinTemp.contains("LTC")) {
                    coin = "LTC";
                } else if (coinTemp.contains("ETH")) {
                    coin = "ETH";
                } else if (coinTemp.contains("ZEC")) {
                    coin = "ZEC";
                } else if (coinTemp.contains("XRP")) {
                    coin = "XRP";
                }

                String period = spinnerPeriodSelection.getSelectedItem().toString();
                String limit = spinnerLimitSelection.getSelectedItem().toString();

                onSelectChartListener.onSelectChartPerformed(chart, coin, period, limit);
                dismiss();
            }
        });

        return rootView;
    }
}

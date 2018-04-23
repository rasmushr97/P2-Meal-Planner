package com.example.rasmus.p2app.frontend.ui.fragments;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rasmus.p2app.R;
import com.example.rasmus.p2app.frontend.other.Storage;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChartFragment extends android.app.Fragment {

    private LineChart mChart;
    static Storage storage = new Storage();

    public ChartFragment() {
        // Required empty public constructor
    }

    static public ChartFragment newInstance(){
        return new ChartFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_chart, container, false);


        mChart = view.findViewById(R.id.weightChart);
        mChart.getDescription().setEnabled(false);
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);

        LineDataSet userSet = new LineDataSet(storage.userWeight,"Your weight");
        LineDataSet goalSet = new LineDataSet(storage.goalWeight,"Goal weight");


        mChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        YAxis rightYaxis = mChart.getAxisRight();
        rightYaxis.setEnabled(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();

        userSet.setColor(Color.RED);
        userSet.setCircleColor(Color.RED);
        dataSets.add(userSet);
        dataSets.add(goalSet);
        LineData data = new LineData(dataSets);

        mChart.setData(data);

        return view;
    }

}

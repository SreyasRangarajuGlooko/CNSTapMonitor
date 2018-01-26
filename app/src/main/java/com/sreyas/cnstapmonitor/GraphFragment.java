package com.sreyas.cnstapmonitor;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

/**
 * Created by Sreyas on 1/26/2018.
 */

public class GraphFragment extends Fragment {
    GraphView graph;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_graph, container, false);
        graph = view.findViewById(R.id.graph);
        drawGraph();
        return  view;
    }

    public void drawGraph(){
        if(TapData.getTapData(getActivity()).size() > 0){
            DataPoint[] points = new DataPoint[TapData.getTapData(getActivity()).size()];
            for (int i = 0; i < points.length; i++) {
                points[i] = new DataPoint(TapData.getTapData(getActivity()).get(i).getTimeStamp() * 60000L, TapData.getTapData(getActivity()).get(i).getNumTaps());
            }
            LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);
            series.setDrawDataPoints(true);
            graph.addSeries(series);
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity()));
            graph.getGridLabelRenderer().setHumanRounding(false);
            graph.getViewport().setMinX(TapData.getTapData(getActivity()).get(0).getTimeStamp() * 60000L);
            graph.getViewport().setMaxX(TapData.getTapData(getActivity()).get(TapData.getTapData(getActivity()).size() - 1).getTimeStamp() * 60000L + 100000);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setScalable(true);
            graph.getViewport().setScalableY(true);
            graph.getGridLabelRenderer().setVerticalAxisTitle(getResources().getString(R.string.YAxisLabel));
            graph.getGridLabelRenderer().setHorizontalAxisTitle(getResources().getString(R.string.XAxisLabel));
        }
    }
}

package com.sreyas.cnstapmonitor.Graph;

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
import com.sreyas.cnstapmonitor.Models.TapData;
import com.sreyas.cnstapmonitor.R;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by Sreyas on 1/26/2018.
 */

public class GraphFragment extends Fragment {
    GraphView graph;
    LineGraphSeries<DataPoint> series;
    View rootView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_graph, container, false);
        graph = rootView.findViewById(R.id.graph);
        series = new LineGraphSeries<>(getGraphData());
        drawGraph();
        return  rootView;
    }

    public void drawGraph(){
        if(TapData.getTapData(getActivity()).size() > 0){
            series.setDrawDataPoints(true);
            graph.addSeries(series);
            graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(getActivity(), new SimpleDateFormat("MM/dd", Locale.getDefault())));
            graph.getGridLabelRenderer().setHumanRounding(false);
            graph.getViewport().setMinX(TapData.getTapData(getActivity()).get(0).getTimeStamp() * 60000L);
            graph.getViewport().setMaxX(TapData.getTapData(getActivity()).get(TapData.getTapData(getActivity()).size() - 1).getTimeStamp() * 60000L);
            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinY(0);
            graph.getViewport().setMaxY(TapData.getMax(getActivity()) + 5);
            graph.getViewport().setYAxisBoundsManual(true);
            graph.setTitle(getResources().getString(R.string.GraphTitle));
            graph.getGridLabelRenderer().setPadding(32);
        }
    }

    public DataPoint[] getGraphData(){
        DataPoint[] points = new DataPoint[TapData.getTapData(getActivity()).size()];
        for (int i = 0; i < points.length; i++) {
            points[i] = new DataPoint(TapData.getTapData(getActivity()).get(i).getTimeStamp() * 60000L, TapData.getTapData(getActivity()).get(i).getNumTaps());
        }
        return points;
    }
}

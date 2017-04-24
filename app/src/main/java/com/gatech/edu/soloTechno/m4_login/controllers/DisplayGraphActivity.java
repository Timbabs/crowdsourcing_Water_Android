package com.gatech.edu.soloTechno.m4_login.controllers;


import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;

import com.gatech.edu.soloTechno.m4_login.R;
import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GridLabelRenderer;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by timothybaba on 4/4/17.
 */

public class DisplayGraphActivity extends FragmentActivity {

    private  LineGraphSeries<DataPoint> series;
    private BarGraphSeries<DataPoint> barSeries;
    private PointsGraphSeries<DataPoint> pointSeries;
    GraphView graph;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historical_report);
        graph = (GraphView) findViewById(R.id.graph);
        graph.setBackgroundColor(Color.argb(50, 50, 0, 200));



        if(HistoricalReportActivity.data1!=null && HistoricalReportActivity.data2!=null
                && HistoricalReportActivity.data3 !=null) {
            plot(determineSelection(HistoricalReportActivity.data1),
                    determineSelection(HistoricalReportActivity.data2));
            plot2(determineSelection(HistoricalReportActivity.data1),
                    determineSelection(HistoricalReportActivity.data3));
            graph.getGridLabelRenderer().setVerticalLabelsColor(Color.BLUE);

            graph.getGridLabelRenderer().setHorizontalAxisTitle(HistoricalReportActivity.data1);
          graph.getGridLabelRenderer().setVerticalAxisTitle(HistoricalReportActivity.data2);
            graph.getGridLabelRenderer().setVerticalAxisTitleColor(Color.BLUE);
            graph.setTitle("Graph of " + HistoricalReportActivity.data1 + " against " +
                    HistoricalReportActivity.data2 + " and " + HistoricalReportActivity.data3);
            graph.getSecondScale().setVerticalAxisTitle(HistoricalReportActivity.data3);
            graph.getSecondScale().setVerticalAxisTitleColor(Color.RED);





        } else if(HistoricalReportActivity.data1!=null && HistoricalReportActivity.data2!=null) {
            plot(determineSelection(HistoricalReportActivity.data1),
                    determineSelection(HistoricalReportActivity.data2));
            plotPoint(determineSelection(HistoricalReportActivity.data1),
                    determineSelection(HistoricalReportActivity.data2));
            plotBarChart(determineSelection(HistoricalReportActivity.data1),
                    determineSelection(HistoricalReportActivity.data2));

            graph.getGridLabelRenderer().setHorizontalAxisTitle(HistoricalReportActivity.data1);
            graph.getGridLabelRenderer().setVerticalAxisTitle(HistoricalReportActivity.data2);
            graph.setTitle("Graph of " + HistoricalReportActivity.data1 + " against " + HistoricalReportActivity.data2);


        } else if(HistoricalReportActivity.data2!=null && HistoricalReportActivity.data3!=null) {
            plot(determineSelection(HistoricalReportActivity.data2),
                    determineSelection(HistoricalReportActivity.data3));
            plotBarChart(determineSelection(HistoricalReportActivity.data2),
                    determineSelection(HistoricalReportActivity.data3));
           plotPoint(determineSelection(HistoricalReportActivity.data2),
                    determineSelection(HistoricalReportActivity.data3));

            graph.getGridLabelRenderer().setHorizontalAxisTitle(HistoricalReportActivity.data2);
            graph.getGridLabelRenderer().setVerticalAxisTitle(HistoricalReportActivity.data3);
            graph.setTitle("Graph of " + HistoricalReportActivity.data2 + " against " + HistoricalReportActivity.data3);



        } else if(HistoricalReportActivity.data1!=null && HistoricalReportActivity.data3!=null){
            plot(determineSelection(HistoricalReportActivity.data1),
                    determineSelection(HistoricalReportActivity.data3));
            plotBarChart(determineSelection(HistoricalReportActivity.data1),
                    determineSelection(HistoricalReportActivity.data3));

            plotPoint(determineSelection(HistoricalReportActivity.data1),
                    determineSelection(HistoricalReportActivity.data3));
            graph.getGridLabelRenderer().setHorizontalAxisTitle(HistoricalReportActivity.data1);
            graph.getGridLabelRenderer().setVerticalAxisTitle(HistoricalReportActivity.data3);
            graph.setTitle("Graph of " + HistoricalReportActivity.data1 + " against " + HistoricalReportActivity.data3);


        }
    }

    void plot (ArrayList list1, ArrayList list2) {
        series = new LineGraphSeries<>();

        int length = list2.size();
        Collections.sort(list1);
        Collections.sort(list2);
        for(int i = 0; i< length; i++) {
            series.appendData(new DataPoint(Double.parseDouble((String)list1.get(i)), Double.parseDouble((String)list2.get(i))), true, length);
        }

        graph.addSeries(series);
    }
    void plotPoint (ArrayList list1, ArrayList list2) {
        pointSeries = new PointsGraphSeries<>();

        int length = list2.size();
        Collections.sort(list1);
        Collections.sort(list2);
        for(int i = 0; i< length; i++) {
            pointSeries.appendData(new DataPoint(Double.parseDouble((String)list1.get(i)), Double.parseDouble((String)list2.get(i))), true, length);
        }

        graph.getSecondScale().addSeries(pointSeries);
        // the y bounds are always manual for second scale
        graph.getSecondScale().setMinY(Double.parseDouble((String)list2.get(0)));
        graph.getSecondScale().setMaxY(Double.parseDouble((String)list2.get(list1.size() - 1)));
       // series.setColor(Color.RED);
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.BLACK);


    }
    void plot2 (ArrayList list1, ArrayList list2) {
        series = new LineGraphSeries<>();

        int length = list2.size();
        Collections.sort(list1);
        Collections.sort(list2);
        for(int i = 0; i< length; i++) {
            series.appendData(new DataPoint(Double.parseDouble((String)list1.get(i)), Double.parseDouble((String)list2.get(i))), true, length);
        }
        graph.getSecondScale().addSeries(series);
        // the y bounds are always manual for second scale
        graph.getSecondScale().setMinY(Double.parseDouble((String)list2.get(0)));
        graph.getSecondScale().setMaxY(Double.parseDouble((String)list2.get(list1.size() - 1)));
        series.setColor(Color.RED);
        graph.getGridLabelRenderer().setVerticalLabelsSecondScaleColor(Color.RED);
    }
    void plotBarChart (ArrayList list2, ArrayList list1) {
        barSeries = new BarGraphSeries<>();

        int length = list2.size();
        Collections.sort(list1);
        Collections.sort(list2);
        for(int i = 0; i< length; i++) {
            barSeries.appendData(new DataPoint(Double.parseDouble((String)list2.get(i)), Double.parseDouble((String)list1.get(i))), true, length);
        }


        barSeries.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
            }
        });

        barSeries.setSpacing(50);

        // draw values on top
        barSeries.setDrawValuesOnTop(true);
        barSeries.setValuesOnTopColor(Color.RED);

        graph.addSeries(barSeries);

    }

    ArrayList determineSelection(String selected) {
        ArrayList answer = new ArrayList();
        switch (selected) {
            /*case "Location": answer = HistoricalReportActivity.locations;
                break;*/
            case "Year": answer = HistoricalReportActivity.years;
                break;
            case "Virus": answer = HistoricalReportActivity.virus;
                break;
            case "Contaminants": answer = HistoricalReportActivity.contaminants;
                break;
        }
        return answer;
    }

}

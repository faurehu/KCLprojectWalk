package com.example.amosmadalinneculau.googlechartapiexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class HelloChart extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_line_chart);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }
    }

    /**
     * A fragment containing a line chart.
     */
    public static class PlaceholderFragment extends Fragment {

        private LineChartView chart;
        private LineChartData data;
        private int maxNumberOfLines = 6;
        private int numberOfPoints = 50;

        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private boolean hasLines = true;
        private boolean hasPoints = true;
        private ValueShape shape = ValueShape.CIRCLE;
        private boolean isFilled = false;
        private boolean hasLabels = false;
        private boolean isCubic = false;
        private boolean hasLabelForSelected = false;
        private boolean pointsHaveDifferentColor;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout._fragment_line_chart, container, false);

            chart = (LineChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            BackgroundTask bt = new BackgroundTask();
            bt.execute("gb");

            return rootView;
        }

        private void resetViewport() {
            // Reset viewport height range to (0,100)
            final Viewport v = new Viewport(chart.getMaximumViewport());
            v.bottom = 0;
            v.top = 100;
            v.left = 0;
            v.right = numberOfPoints - 1;
            chart.setMaximumViewport(v);
            chart.setCurrentViewport(v);
        }

        protected void renderLines(HashMap exportsData, HashMap GDPData) {

            class stringComparator implements Comparator<Map.Entry<Integer, Float>> {
                @Override
                public int compare(Map.Entry<Integer, Float> lhs, Map.Entry<Integer, Float> rhs) {
                    if (lhs.getKey() > rhs.getKey()) {
                        return 1;
                    } else if ( lhs.getKey() == rhs.getKey()) {
                        return 0;
                    } else {
                        return -1;
                    }
                }
            }

            List<Line> lines = new ArrayList<Line>();

            List<PointValue> exportsValues = new ArrayList<PointValue>();
            Set<Map.Entry<Integer, Float>> exportsEntrySet = exportsData.entrySet();
            SortedSet<Map.Entry<Integer, Float>> sortedSet = new TreeSet<Map.Entry<Integer, Float>>(new stringComparator());
            Iterator it = exportsEntrySet.iterator();
            while(it.hasNext()) {
                sortedSet.add((Map.Entry)it.next());
            }
            it = sortedSet.iterator();
            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                exportsValues.add(new PointValue((Integer) entry.getKey(), (Float) entry.getValue()));
            }

            List<PointValue> GDPValues = new ArrayList<PointValue>();
            Set<Map.Entry<Integer, Float>> GDPEntrySet = exportsData.entrySet();
            sortedSet = new TreeSet<Map.Entry<Integer, Float>>(new stringComparator());
            it = GDPEntrySet.iterator();
            while(it.hasNext()) {
                sortedSet.add((Map.Entry)it.next());
            }
            it = sortedSet.iterator();
            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                GDPValues.add(new PointValue((Integer) entry.getKey(), (Float) entry.getValue()));
            }

            Line exportsLine = new Line(exportsValues);
            Line GDPLine = new Line(GDPValues);

            exportsLine.setColor(ChartUtils.COLOR_ORANGE);
            exportsLine.setShape(shape);
            exportsLine.setCubic(isCubic);
            exportsLine.setFilled(isFilled);
            exportsLine.setHasLabels(hasLabels);
            exportsLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
            exportsLine.setHasLines(hasLines);
            exportsLine.setHasPoints(hasPoints);

            GDPLine.setColor(ChartUtils.COLOR_BLUE);
            GDPLine.setShape(shape);
            GDPLine.setCubic(isCubic);
            GDPLine.setFilled(isFilled);
            GDPLine.setHasLabels(hasLabels);
            GDPLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
            GDPLine.setHasLines(hasLines);
            GDPLine.setHasPoints(hasPoints);

            lines.add(GDPLine);
            lines.add(exportsLine);

            data = new LineChartData(lines);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
                axisX.setLineColor(ChartUtils.DEFAULT_DARKEN_COLOR);
                axisY.setLineColor(ChartUtils.DEFAULT_DARKEN_COLOR);
                if (hasAxesNames) {
                    axisX.setName("Axis X");
                    axisY.setName("Axis Y");
                }
                data.setAxisXBottom(axisX);
                data.setAxisYLeft(axisY);
            } else {
                data.setAxisXBottom(null);
                data.setAxisYLeft(null);
            }

            data.setBaseValue(Float.NEGATIVE_INFINITY);
            chart.setLineChartData(data);
//            chart.setViewportCalculationEnabled(false);

//            resetViewport();
        }

        protected void parseData(String exportsJson, String GDPJson) {

            HashMap<Integer, Float> exportsData = new HashMap<Integer, Float>();
            HashMap<Integer, Float> GDPData = new HashMap<Integer, Float>();

            try {
                JSONArray exportsArray = new JSONArray(exportsJson);
                Log.i("exportsArray", exportsArray.toString());
                JSONArray GDPArray = new JSONArray(GDPJson);
                JSONArray exportsValues = (JSONArray) exportsArray.get(1);
                JSONArray GDPValues = (JSONArray) GDPArray.get(1);
                for(int i = 1; i<exportsValues.length(); ++i){
                    JSONObject exportsNode = (JSONObject) exportsValues.get(i);
                    JSONObject GDPNode = (JSONObject) GDPValues.get(i);
                    Log.i("date", exportsNode.get("date").getClass().toString());
                    Log.i("value", exportsNode.get("value").getClass().toString());
                    try {
                        exportsData.put(Integer.parseInt((String) exportsNode.get("date")), Float.parseFloat((String) exportsNode.get("value")));
                        GDPData.put(Integer.parseInt((String) GDPNode.get("date")), Float.parseFloat((String) GDPNode.get("value")));
                    } catch (ClassCastException e) {
                        Log.e("ClassCastException", e.toString());
                    }
                }
            } catch (JSONException e) {
                Log.e("JSON", e.toString());
            }

            renderLines(exportsData, GDPData);
        }

        public class BackgroundTask extends AsyncTask<String, Void, Void> {
            @Override
            protected Void doInBackground(String... country) {
                String GDPUrl = "http://api.worldbank.org/countries/" + country[0] + "/indicators/NE.EXP.GNFS.ZS/?date=1960:2010&format=json&per_page=51";
                String exportsUrl = "http://api.worldbank.org/countries/" + country[0] + "/indicators/FR.INR.RINR/?date=1960:2010&format=json&per_page=51";
                try {
                    String exportsResponse  = readData(exportsUrl);
                    String GDPResponse = readData(GDPUrl);
                    Log.i("exports response", exportsUrl);
                    parseData(exportsResponse, GDPResponse);
                } catch (IOException e) {
                    Log.e("ERROR", "IOException");
                }
                return null;
            }

            public String readData(String urlName) throws IOException {
                StringBuffer buffer = new StringBuffer();
                URL url = new URL(urlName);
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoInput(true);
                connection.connect();
                BufferedReader in;
                in = new BufferedReader(
                        new InputStreamReader(connection.getInputStream()));
                String inputLine = in.readLine();
                while (inputLine != null) {
                    buffer.append(inputLine);
                    inputLine = in.readLine();
                }
                in.close();
                connection.disconnect();
                return buffer.toString();
            }
        }

        private class ValueTouchListener implements LineChartOnValueSelectListener {

            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {
                // TODO Auto-generated method stub

            }

        }
    }
}
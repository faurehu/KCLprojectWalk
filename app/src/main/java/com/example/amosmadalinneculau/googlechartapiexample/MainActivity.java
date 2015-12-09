package com.example.amosmadalinneculau.googlechartapiexample;

import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


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

import lecho.lib.hellocharts.animation.ChartAnimationListener;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.Chart;
import lecho.lib.hellocharts.view.LineChartView;
import tyrantgit.explosionfield.ExplosionField;



public class MainActivity extends AppCompatActivity {

    /*Declaring some variables*/
    LinearLayout countries;
    LinearLayout mainLayout;
    Animation vertAnimeTra, horAnimeTra;
    ToggleButton bgMusik;
    MediaPlayer mediaPlayer;
    TextView ourClaim;


    private ExplosionField explosionTest;
    ImageView explosion;



    public static at.markushi.ui.CircleButton country1;
    public static at.markushi.ui.CircleButton country2;
    public static at.markushi.ui.CircleButton country3;
    public static at.markushi.ui.CircleButton country4;
    public static at.markushi.ui.CircleButton country5;
    public static at.markushi.ui.CircleButton country6;
    public boolean []countriesOnGraph;


    public static ButtonListeners listeners;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // HelloChart
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new PlaceholderFragment()).commit();
        }

        countriesOnGraph= new boolean[6];
        //UK
        countriesOnGraph[0] = false;
        //US
        countriesOnGraph[1] = false;
        //DE
        countriesOnGraph[2] = false;
        //AUS
        countriesOnGraph[3] = false;
        //IND
        countriesOnGraph[4] = false;
        //CN
        countriesOnGraph[5] = false;
        // HelloChart


        // Other Stuff
        vertAnimeTra = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_ver1);
        horAnimeTra = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_hor1);

        /*Intro Explaining Image (commented temporarily)*/
//        explosionTest = ExplosionField.attach2Window(this);
//        explosion = (ImageView) findViewById(R.id.imageViewExplTest);
//        View.OnClickListener ourOnclickListener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                explosionTest.explode(v);
//                v.setOnClickListener(null);
//            }
//        };
//        explosion.setOnClickListener(ourOnclickListener);

        initAnim();
        bgSound();
        choosingCountry();

        /*Font testing*/
        Typeface Font1 = Typeface.createFromAsset(getAssets(), "green_avocado.ttf");
        ourClaim = (TextView) findViewById(R.id.ourClaim);
        ourClaim.setTypeface(Font1);


        // Other Stuff
        listeners = new ButtonListeners(country1, country2, country3, country4, country5, country6);
        listeners.contry1Listener();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.check_UK:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    Log.i("Reached here", "UK deselect");
                    countriesOnGraph[0] = false;
                }
                else {
                    item.setChecked(true);
                    //add line
                    Log.i("Reached here", "UK select");
                    countriesOnGraph[0] = true;
                }
                return true;
            case R.id.check_US:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    countriesOnGraph[1] = false;
                }
                else {
                    item.setChecked(true);
                    //add line
                    countriesOnGraph[1] = true;
                }
                return true;
            case R.id.check_AUS:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    countriesOnGraph[3] = false;
                }
                else {
                    item.setChecked(true);
                    //add line
                    countriesOnGraph[3] = true;

                }
                return true;
            case R.id.check_CN:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    countriesOnGraph[5] = false;
                }
                else {
                    item.setChecked(true);
                    //add line
                    countriesOnGraph[5] = true;
                }
                return true;
            case R.id.check_DE:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    countriesOnGraph[2] = false;
                }
                else {
                    item.setChecked(true);
                    //add line
                    countriesOnGraph[2] = true;
                }
                return true;
            case R.id.check_IND:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    countriesOnGraph[4] = false;
                }
                else {
                    item.setChecked(true);
                    //add line
                    countriesOnGraph[4] = true;
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // HelloChart
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
        public BackgroundTask bt;
        MenuItem itm;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);
            chart = (LineChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            bt = new BackgroundTask();

            country1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bt = new BackgroundTask();
                    bt.execute("gb");

                }
            });
            country2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bt = new BackgroundTask();
                    bt.execute("us");
                }
            });

            country3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bt = new BackgroundTask();
                    bt.execute("de");
                }
            });
            country4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bt = new BackgroundTask();
                    bt.execute("aus");
                }
            });

            country5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bt = new BackgroundTask();
                    bt.execute("ind");
                }
            });
            country6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bt = new BackgroundTask();
                    bt.execute("cn");
                }
            });

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

        //protected void renderLines(HashMap exportsData, HashMap GDPData) {
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
            SortedSet<Map.Entry<Integer, Float>> sortedSet1 = new TreeSet<Map.Entry<Integer, Float>>(new stringComparator());
            Iterator it = exportsEntrySet.iterator();
            while(it.hasNext()) {
                sortedSet.add((Map.Entry)it.next());
            }
            it = sortedSet.iterator();
            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                exportsValues.add(new PointValue((Integer) entry.getKey(), (Float) entry.getValue()));
            }

            for(int i=0; i<exportsValues.size(); ++i){
                Log.i("FIRST LINE Value: ",""+exportsValues.get(i).toString());
            }


            List<PointValue> GDPValues = new ArrayList<PointValue>();
            Set<Map.Entry<Integer, Float>> GDPEntrySet = GDPData.entrySet();
            sortedSet1 = new TreeSet<Map.Entry<Integer, Float>>(new stringComparator());
            it = GDPEntrySet.iterator();
            while(it.hasNext()) {
                sortedSet1.add((Map.Entry)it.next());
            }
            it = sortedSet1.iterator();
            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                GDPValues.add(new PointValue((Integer) entry.getKey(), (Float) entry.getValue()));

            }

            for(int i=0; i<GDPValues.size(); ++i){
                Log.i("SECOND LINE Value: ",""+GDPValues.get(i).toString());
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
                    axisX.setName("Years");
                    axisY.setName("Percentage");
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
                String GDPUrl = "http://api.worldbank.org/countries/" + country[0] + "/indicators/NE.IMP.GNFS.ZS/?date=1960:2010&format=json&per_page=51";
                String exportsUrl = "http://api.worldbank.org/countries/" + country[0] + "/indicators/FR.INR.RINR/?date=1960:2010&format=json&per_page=51";
                //String GDPUrl = "http://api.worldbank.org/countries/" + "gb" + "/indicators/NE.IMP.GNFS.ZS/?date=1960:2010&format=json&per_page=51";
                //String exportsUrl = "http://api.worldbank.org/countries/" + "gb" + "/indicators/FR.INR.RINR/?date=1960:2010&format=json&per_page=51";
                try {
                    String exportsResponse  = readData(exportsUrl);
                    String GDPResponse = readData(GDPUrl);
                    Log.i("exports response", exportsUrl);
                    Log.i("gdp response", GDPUrl);
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
    //HelloChart

    //Other Stuff
    /*Intro*/
    public void initAnim() {

        mainLayout = (LinearLayout) findViewById(R.id.mainLayout);
        mainLayout.startAnimation(vertAnimeTra);

        bgMusik = (ToggleButton) findViewById(R.id.toggleMusik);
        bgMusik.startAnimation(vertAnimeTra);

    }

    public void bgSound() {
        mediaPlayer = MediaPlayer.create(this, R.raw.music_play);
        bgMusik.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bgMusik.isChecked()) {
                    mediaPlayer.start();
                    mediaPlayer.setLooping(true);
                } else {
                    mediaPlayer.pause();
                }
            }
        });

    }

    public void choosingCountry() {

        countries = (LinearLayout) findViewById(R.id.countries);
        countries.startAnimation(horAnimeTra);

        country1 = (at.markushi.ui.CircleButton) findViewById(R.id.country1);
        country2 = (at.markushi.ui.CircleButton) findViewById(R.id.country2);
        country3 = (at.markushi.ui.CircleButton) findViewById(R.id.country3);
        country4 = (at.markushi.ui.CircleButton) findViewById(R.id.country4);
        country5 = (at.markushi.ui.CircleButton) findViewById(R.id.country5);
        country6 = (at.markushi.ui.CircleButton) findViewById(R.id.country6);

    }
}

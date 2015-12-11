package com.example.amosmadalinneculau.googlechartapiexample;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
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

import at.markushi.ui.CircleButton;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;

public class MainActivity extends AppCompatActivity {

    /*Declaring some variables*/
    LinearLayout countries;
    RelativeLayout mainLayout;
    Animation vertAnimeTra, horAnimeTra;
    ToggleButton bgMusik;
    MediaPlayer mediaPlayer;
    TextView ourClaim;

    //Json Object in order to do the cacheing
    public static JSONObject cacheJSON;
    //for cacheing
    public static SharedPreferences sharedPref;

    /*
    * toggleArea - button that changes the look of the graph
    * square - button that changes the look of the graph
    * toggleLabels - button that changes the look og the graph
    * reset - button that reset the graph
    * infoButton - button that initialize the Pop class in order to initialize the pop-up
     */
    public static ImageButton toggleArea, square, toggleLabels, reset, infoButton;

    /*
    * HashMap exportsData - here we store the values that we get  from the WorldBank API
    * HashMap GDPData - here we store the values that we get from the WorldBank API
    *
    * IMPORTANT:
    *           *the variable exportsData stands for the following indicator: Interests
    *           *the variable GDPData stands for the following indicator: Inports
     */
    static HashMap<Integer, Float> exportsData = new HashMap<Integer, Float>();
    static HashMap<Integer, Float> GDPData = new HashMap<Integer, Float>();

    /*
    Buttons for countries we decided to include data in our project
    * country1 = UK
    * country2 = US
    * country3 = DE
    * country4 = AUS
    * country5 = IND
    * country6 = CN
     */
    public static at.markushi.ui.CircleButton country1;
    public static at.markushi.ui.CircleButton country2;
    public static at.markushi.ui.CircleButton country3;
    public static at.markushi.ui.CircleButton country4;
    public static at.markushi.ui.CircleButton country5;
    public static at.markushi.ui.CircleButton country6;

    //this array keeps track of what we choose to show on graph
    //Helper in order to add more countries on graph (something we wanted to do but we didn't manage to do because of the lack of time)
    /*
    6 slots:
        *countriesOnGraph[0] indicates UK
        * *countriesOnGraph[1] indicates US
        * *countriesOnGraph[2] indicates DE
        * *countriesOnGraph[3] indicates AUS
        * *countriesOnGraph[4] indicates IND
        * *countriesOnGraph[5] indicates CN

     */
    public static boolean []countriesOnGraph;

    //Helper in order to add more countries on graph (something we wanted to do but we didn't manage to do because of the lack of time)
    public ArrayList<String> cntToAdd;

    //Object of ButtonListeners class
    public static ButtonListeners listeners;

    //Print the countriesOnGraph array
    public void printState(){
        for(int i=0; i<countriesOnGraph.length; ++i)
            Log.i("Country["+i+"]: ",""+countriesOnGraph[i]);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        //INITIALIZATION OF OBJECTS
        square = (ImageButton) findViewById(R.id.button3);
        toggleLabels = (ImageButton) findViewById(R.id.button2);
        toggleArea = (ImageButton) findViewById(R.id.button);
        reset = (ImageButton) findViewById(R.id.button4);
        infoButton = (ImageButton) findViewById(R.id.info);
        cntToAdd = new ArrayList<String>();

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        String cacheString = MainActivity.sharedPref.getString("cache", "{}");

        //cacheing
        try {
            MainActivity.cacheJSON = new JSONObject(cacheString);
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

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
        //Animation objects
        vertAnimeTra = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_ver1);
        horAnimeTra = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_hor1);

        //initialization of animation
        initAnim();
        //initialization of background sound
        bgSound();
        //initialization of choosinCountry
        choosingCountry();

        /*Font testing*/
        Typeface Font1 = Typeface.createFromAsset(getAssets(), "green_avocado.ttf");
        ourClaim = (TextView) findViewById(R.id.ourClaim);
        ourClaim.setTypeface(Font1);


        // Other Stuff
        //Initialization of listeners object
        listeners = new ButtonListeners(country1, country2, country3, country4, country5, country6);
        listeners.contry1Listener();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    //MENU ITEMS
    // (Helpers in order to add more countries on graph (something we wanted to do but we didn't manage to do because of the lack of time)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.check_UK:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    Log.i("Reached here", "UK deselect");
                    countriesOnGraph[0] = false;
                    printState();

                }
                else {
                    item.setChecked(true);
                    //add line
                    Log.i("Reached here", "UK select");
                    countriesOnGraph[0] = true;
                    printState();
                }
                return true;
            case R.id.check_US:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    countriesOnGraph[1] = false;
                    printState();
                }
                else {
                    item.setChecked(true);
                    //add line
                    countriesOnGraph[1] = true;
                    printState();
                }
                return true;
            case R.id.check_AUS:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    countriesOnGraph[3] = false;
                    printState();
                }
                else {
                    item.setChecked(true);
                    //add line
                    countriesOnGraph[3] = true;
                    printState();

                }
                return true;
            case R.id.check_CN:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    countriesOnGraph[5] = false;
                    printState();
                }
                else {
                    item.setChecked(true);
                    //add line
                    countriesOnGraph[5] = true;
                    printState();
                }
                return true;
            case R.id.check_DE:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    countriesOnGraph[2] = false;
                    printState();
                }
                else {
                    item.setChecked(true);
                    //add line
                    countriesOnGraph[2] = true;
                    printState();
                }
                return true;
            case R.id.check_IND:
                if (item.isChecked()) {
                    item.setChecked(false);
                    //delete line
                    countriesOnGraph[4] = false;
                    printState();
                }
                else {
                    item.setChecked(true);
                    //add line
                    countriesOnGraph[4] = true;
                    printState();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    // HelloChart
    public class PlaceholderFragment extends Fragment {

        //Objects of HelloChart lib
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
        //BackGroundTask (this brings data on graph)
        public BackgroundTask bt;
        //helpers
        MenuItem itm;
        //last state = last country selected (updates every time)
        // (Helper in order to add more countries on graph (something we wanted to do but we didn't manage to do because of the lack of time)
        public String lastState;



        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            //initialize the graph
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);
            chart = (LineChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            //last state at start is empty
            lastState = "";
            //initialization of background task
            bt = new BackgroundTask();

            //infoButton listener (Pop-up)
            infoButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    startActivity(new Intent(MainActivity.this,Pop.class));
                }
            });


            //SQUARE BUTTON LISTENER
            square.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(shape == ValueShape.CIRCLE)
                    {
                        shape = ValueShape.SQUARE;
                        //renderLines(exportsData, GDPData);

                    }
                    else
                    {
                        shape = ValueShape.CIRCLE;
                        renderLines(exportsData, GDPData);

                    }

                }
            });
            //END OF SQUARE BUTTON LISTENER

            //TOGLE LABELS BUTTON LISTENER
            toggleLabels.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (hasLabels == false) {
                        hasLabels = true;
                        //renderLines(exportsData, GDPData);

                    }

                    else
                    {
                        hasLabels = false;
                        renderLines(exportsData, GDPData);

                    }

                }
            });
            //END OF TOGLE LABELS LISTENER

            //TOGGLE FILLED

            toggleArea.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isFilled == false)
                    {
                        isFilled = true;
                        //renderLines(exportsData, GDPData);


                    }
                    else
                    {
                        isFilled = false;
                        renderLines(exportsData, GDPData);


                    }

                }
            });

            //END OF TOGGLE FILLED

            //RESET BUTTON
            reset.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    resetViewport();
                    renderLines(exportsData, GDPData);

                }
            });
            //END OF RESET BUTTON



            //country1 listener (UK)
            country1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new background task
                    bt = new BackgroundTask();
                    //execute the country (UK)
                    bt.execute("gb");
                    //last state = the country selected
                    lastState = "gb";

                    //change the states of countriesOnGraph
                    for(int i=0; i<countriesOnGraph.length; ++i)
                        countriesOnGraph[i] = false;

                    countriesOnGraph[0] = true;
                }
            });
            //country2 listener (US)
            country2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new background task
                    bt = new BackgroundTask();
                    //execute the country (US)
                    bt.execute("us");
                    //last state = the country selected
                    lastState = "us";

                    //change the states of countriesOnGraph
                    for(int i=0; i<countriesOnGraph.length; ++i)
                        countriesOnGraph[i] = false;

                    countriesOnGraph[1] = true;
                }
            });
            //country3 listener (DE)
            country3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new background task
                    bt = new BackgroundTask();
                    //execute the country (DE)
                    bt.execute("de");
                    //last state = the country selected
                    lastState = "de";

                    //change the states of countriesOnGraph
                    for(int i=0; i<countriesOnGraph.length; ++i)
                        countriesOnGraph[i] = false;

                    countriesOnGraph[2] = true;
                }
            });
            //country4 listener (AUS)
            country4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new background task
                    bt = new BackgroundTask();
                    //execute the country (AUS)
                    bt.execute("aus");
                    //last state = the country selected
                    lastState = "aus";

                    //change the states of countriesOnGraph
                    for(int i=0; i<countriesOnGraph.length; ++i)
                        countriesOnGraph[i] = false;

                    countriesOnGraph[3] = true;
                }
            });
            //country5 listener (IND)
            country5.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new background task
                    bt = new BackgroundTask();
                    //execute the country (IND)
                    bt.execute("ind");
                    //last state = the country selected
                    lastState = "ind";

                    //change the states of countriesOnGraph
                    for(int i=0; i<countriesOnGraph.length; ++i)
                        countriesOnGraph[i] = false;

                    countriesOnGraph[4] = true;
                }
            });
            //country6 listener (CN)
            country6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new background task
                    bt = new BackgroundTask();
                    //execute the country (CN)
                    bt.execute("cn");
                    //last state = the country selected
                    lastState = "cn";

                    //change the states of countriesOnGraph
                    for(int i=0; i<countriesOnGraph.length; ++i)
                        countriesOnGraph[i] = false;

                    countriesOnGraph[5] = true;
                }
            });

            //return of rootView
            return rootView;
        }

        //reset of viewPort (RESET THE GRAPH)
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

        //LINES ON GRAPH(LINES OF INDICATORS WILL APPEAR ON GRAPH)
        public Line exportsLine;
        public Line GDPLine;


        //LINES RENDER
        protected void renderLines(HashMap exportsData, HashMap GDPData) {

            //COMPARATOR IN ORDER TO SORT THE DATA, IN ORDER TO BE CORRECTLY SHOW ON GRAPH
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


            //ARRAYLIST OF LINES(YOU CAN ADD AS MANY LINES AS YOU WANT TO BE SHOWN)
            //IN OUR CASE JUST 2 REPRESENTING THE INDICATORS
            List<Line> lines = new ArrayList<Line>();

            //Points of the first indicator
            //sort them
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


            //Points of the second indicator
            //sort them
            List<PointValue> GDPValues = new ArrayList<PointValue>();
            Set<Map.Entry<Integer, Float>> GDPEntrySet = GDPData.entrySet();
            sortedSet1 = new TreeSet<>(new stringComparator());
            it = GDPEntrySet.iterator();
            while(it.hasNext()) {
                sortedSet1.add((Map.Entry)it.next());
            }
            it = sortedSet1.iterator();
            while(it.hasNext()) {
                Map.Entry entry = (Map.Entry) it.next();
                GDPValues.add(new PointValue((Integer) entry.getKey(), (Float) entry.getValue()));
            }

            //add points of the first indicator on line
            exportsLine = new Line(exportsValues);
            //add points of the second indicator on line
            GDPLine = new Line(GDPValues);

            //coloring the lines as show in legend (KEY)
            setColors(cnt);

            //ui for the line
            //initialize the way it look
            exportsLine.setShape(shape);
            exportsLine.setCubic(isCubic);
            exportsLine.setFilled(isFilled);
            exportsLine.setHasLabels(hasLabels);
            exportsLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
            exportsLine.setHasLines(hasLines);
            exportsLine.setHasPoints(hasPoints);


            //ui for the line
            //initialize the way it look
            GDPLine.setShape(shape);
            GDPLine.setCubic(isCubic);
            GDPLine.setFilled(isFilled);
            GDPLine.setHasLabels(hasLabels);
            GDPLine.setHasLabelsOnlyForSelected(hasLabelForSelected);
            GDPLine.setHasLines(hasLines);
            GDPLine.setHasPoints(hasPoints);

            //add the two lines in the arraylist
            lines.add(GDPLine);
            lines.add(exportsLine);

            //give the lines to data in order to put them on graph (to show them)
            data = new LineChartData(lines);


            //axes, initialization, the way they look etc.
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

        }

        //coloring the lines (as shown on legend)
        public void setColors(String s){
            switch(s){
                case "gb":
                    GDPLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_RED);
                    exportsLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_BLUE);
                    break;
                case "us":
                    GDPLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_YELLOW);
                    exportsLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_GREEN);
                    break;
                case "de":
                    GDPLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_PINK);
                    exportsLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_ORANGE);
                    break;
                case "aus":
                    GDPLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_MAROON);
                    exportsLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_GRAY);
                    break;
                case "ind":
                    GDPLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_BROWN);
                    exportsLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_PURPLE);
                    break;
                case "cn":
                    GDPLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_WHITE);
                    exportsLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.COLOR_BLACK);
                    break;
                default:
                    GDPLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.DEFAULT_COLOR);
                    exportsLine.setColor(com.example.amosmadalinneculau.googlechartapiexample.ChartUtils.DEFAULT_COLOR);
            }
        }

        //JSON PARSER
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
                        Log.e("ClassCastException", e.getMessage());
                        Log.e("Null exports value", exportsNode.getString("date"));
                    }
                }
            } catch (JSONException e) {
                Log.e("JSON", e.toString());
            }

            renderLines(exportsData, GDPData);
        }

        //cnt = country of current state
        public String cnt;

        //background task where you can find
        //reader
        //cache
        //execute
        //fetch the data
        //http request
        public class BackgroundTask extends AsyncTask<String, Void, Void> {

            @Override
            protected Void doInBackground(String... country) {
                cnt = country[0];
                checkCache(cnt);
                return null;
            }

            public void checkCache(String country) {
                if (MainActivity.cacheJSON.has(country)) {
                    try {
                        JSONObject cacheCountry = (JSONObject) MainActivity.cacheJSON.get(country);
                        Log.i("cache country", cacheCountry.toString());
                        String imports = (String) cacheCountry.get("imports");
                        String interest = (String) cacheCountry.get("interests");
                        parseData(imports, interest);
                    } catch(JSONException e) {
                        Log.e("JSONException", e.getMessage());
                    }
                } else {
                    fetchData(country);
                }
            }

            public void fetchData(String countryName) {
                String interestsUrl = "http://api.worldbank.org/countries/" + countryName + "/indicators/FR.INR.RINR/?date=1960:2010&format=json&per_page=51";
                String importsUrl = "http://api.worldbank.org/countries/" + countryName + "/indicators/NE.IMP.GNFS.ZS/?date=1960:2010&format=json&per_page=51";
                try {
                    String interestsResponse  = httpRequest(interestsUrl);
                    String importsResponse = httpRequest(importsUrl);
                    JSONObject newCountry = new JSONObject();
                    newCountry.put("interests", interestsResponse);
                    newCountry.put("imports", importsResponse);
                    MainActivity.cacheJSON.put(countryName, newCountry);
                    SharedPreferences.Editor editor = MainActivity.sharedPref.edit();
                    editor.putString("cache", MainActivity.cacheJSON.toString());
                    editor.commit();
                    parseData(interestsResponse, importsResponse);
                } catch (IOException e) {
                    Log.e("ERROR", e.getMessage());
                } catch (JSONException e) {
                    Log.e("ERROR", e.getMessage());
                }
            }

            public String httpRequest(String urlName) throws IOException {
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
    //ANIMATION FUNCTION
    public void initAnim() {

        mainLayout = (RelativeLayout) findViewById(R.id.mainLayout);
        mainLayout.startAnimation(vertAnimeTra);

        bgMusik = (ToggleButton) findViewById(R.id.toggleMusik);
        bgMusik.startAnimation(vertAnimeTra);

    }

    //BACKGROUND SOUND FUNCTION
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

    //initialization of country buttons
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

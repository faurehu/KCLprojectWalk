package com.example.amosmadalinneculau.googlechartapiexample;

import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;

import java.util.ArrayList;
import java.util.List;

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
    LinearLayout mainLayout;
    LinearLayout linLayoutLeft;
    LinearLayout linLayoutRight;
    FrameLayout frameLayout;
    RelativeLayout relLayout;
    Animation vertAnimeTra, horAnimeTra;
    ToggleButton bgMusik;
    MediaPlayer mediaPlayer;
    ImageView explosion;

    private ExplosionField explosionTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // HelloChart
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.frameLayout, new PlaceholderFragment()).commit();
        }
        // HelloChart

        // Other Stuff
        vertAnimeTra = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_ver1);
        horAnimeTra = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.translate_hor1);

        /*Intro Explaining Image*/
        explosionTest = ExplosionField.attach2Window(this);
        explosion = (ImageView) findViewById(R.id.imageViewExplTest);
        View.OnClickListener ourOnclickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                explosionTest.explode(v);
                v.setOnClickListener(null);
            }
        };
        explosion.setOnClickListener(ourOnclickListener);

        initAnim();
        bgSound();
        choosingCountry();
        // Other Stuff



    }


    /**/


    // HelloChart
    public static class PlaceholderFragment extends Fragment {

        private LineChartView chart;
        private LineChartData data;
        private int numberOfLines = 1;
        private int maxNumberOfLines = 3;
        private int numberOfPoints = 12;

        float[][] randomNumbersTab = new float[maxNumberOfLines][numberOfPoints];

        private boolean hasAxes = true;
        private boolean hasAxesNames = true;
        private boolean hasLines = true;
        private boolean hasPoints = true;
        private ValueShape shape = ValueShape.CIRCLE;
        private boolean isFilled = false;
        private boolean hasLabels = false;
        private boolean isCubic = true;
        private boolean hasLabelForSelected = false;
        private boolean pointsHaveDifferentColor;

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);

            chart = (LineChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            // Disable viewport recalculations, see toggleCubic() method for more info.
            chart.setViewportCalculationEnabled(false);

            resetViewport();

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

        private void generateData() {

            List<Line> lines = new ArrayList<Line>();
            for (int i = 0; i < numberOfLines; ++i) {

                List<PointValue> values = new ArrayList<PointValue>();
                for (int j = 0; j < numberOfPoints; ++j) {
                    values.add(new PointValue(j, randomNumbersTab[i][j]));
                }

                Line line = new Line(values);
                line.setColor(ChartUtils.COLORS[i]);
                line.setShape(shape);
                line.setCubic(isCubic);
                line.setFilled(isFilled);
                line.setHasLabels(hasLabels);
                line.setHasLabelsOnlyForSelected(hasLabelForSelected);
                line.setHasLines(hasLines);
                line.setHasPoints(hasPoints);
                if (pointsHaveDifferentColor){
                    line.setPointColor(ChartUtils.COLORS[(i + 1) % ChartUtils.COLORS.length]);
                }
                lines.add(line);
            }

            data = new LineChartData(lines);

            if (hasAxes) {
                Axis axisX = new Axis();
                Axis axisY = new Axis().setHasLines(true);
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

        }

        private class ValueTouchListener implements LineChartOnValueSelectListener {

            @Override
            public void onValueSelected(int lineIndex, int pointIndex, PointValue value) {
                Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onValueDeselected() {}

        }
    }
    //HelloChart

    //Other Stuff
    /*Intro*/
    public void initAnim() {

        relLayout = (RelativeLayout) findViewById(R.id.relLayout);
        relLayout.startAnimation(vertAnimeTra);

        bgMusik = (ToggleButton) findViewById(R.id.toggleMusik);
        bgMusik.startAnimation(horAnimeTra);

        //ExplosionField explosionField =
        //explosionField.explode(view);


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

        /*FloatingActionsMenu 1 & its countries */
        FloatingActionButton country1 = new FloatingActionButton(getBaseContext());
        Drawable drawable = ContextCompat.getDrawable(getApplicationContext(), R.drawable.japan_flag);
        country1.setSize(FloatingActionButton.SIZE_MINI);
        country1.setBackground(drawable);
        FloatingActionButton country2 = new FloatingActionButton(getBaseContext());
        final FloatingActionsMenu menuMultipleActions = (FloatingActionsMenu) findViewById(R.id.multiple_actions);
        menuMultipleActions.startAnimation(vertAnimeTra);
        menuMultipleActions.addButton(country1);
        menuMultipleActions.addButton(country2);


        /*FloatingActionsMenu 2 & its countries */
        FloatingActionButton country3 = new FloatingActionButton(getBaseContext());
        FloatingActionButton country4 = new FloatingActionButton(getBaseContext());
        final FloatingActionsMenu menuMultipleActions2 = (FloatingActionsMenu) findViewById(R.id.multiple_actions2);
        menuMultipleActions2.startAnimation(vertAnimeTra);
        menuMultipleActions2.addButton(country3);
        menuMultipleActions2.addButton(country4);

        /*FloatingActionsMenu 3 & its countries */
        FloatingActionButton country5 = new FloatingActionButton(getBaseContext());
        FloatingActionButton country6 = new FloatingActionButton(getBaseContext());
        final FloatingActionsMenu menuMultipleActions3 = (FloatingActionsMenu) findViewById(R.id.multiple_actions3);
        menuMultipleActions3.startAnimation(vertAnimeTra);
        menuMultipleActions3.addButton(country5);
        menuMultipleActions3.addButton(country6);

    }
    //Other Stuff
}

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

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            setHasOptionsMenu(true);
            View rootView = inflater.inflate(R.layout.fragment_line_chart, container, false);

            chart = (LineChartView) rootView.findViewById(R.id.chart);
            chart.setOnValueTouchListener(new ValueTouchListener());

            // Generate some randome values.
            generateValues();

            generateData();

            // Disable viewpirt recalculations, see toggleCubic() method for more info.
            chart.setViewportCalculationEnabled(false);

            resetViewport();

            return rootView;
        }

//        // MENU
//        @Override
//        public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//            inflater.inflate(R.menu.line_chart, menu);
//        }
//
//        @Override
//        public boolean onOptionsItemSelected(MenuItem item) {
//            int id = item.getItemId();
//            if (id == R.id.action_reset) {
//                reset();
//                generateData();
//                return true;
//            }
//            if (id == R.id.action_add_line) {
//                addLineToData();
//                return true;
//            }
//            if (id == R.id.action_toggle_lines) {
//                toggleLines();
//                return true;
//            }
//            if (id == R.id.action_toggle_points) {
//                togglePoints();
//                return true;
//            }
//            if (id == R.id.action_toggle_cubic) {
//                toggleCubic();
//                return true;
//            }
//            if (id == R.id.action_toggle_area) {
//                toggleFilled();
//                return true;
//            }
//            if (id == R.id.action_point_color) {
//                togglePointColor();
//                return true;
//            }
//            if (id == R.id.action_shape_circles) {
//                setCircles();
//                return true;
//            }
//            if (id == R.id.action_shape_square) {
//                setSquares();
//                return true;
//            }
//            if (id == R.id.action_shape_diamond) {
//                setDiamonds();
//                return true;
//            }
//            if (id == R.id.action_toggle_labels) {
//                toggleLabels();
//                return true;
//            }
//            if (id == R.id.action_toggle_axes) {
//                toggleAxes();
//                return true;
//            }
//            if (id == R.id.action_toggle_axes_names) {
//                toggleAxesNames();
//                return true;
//            }
//            if (id == R.id.action_animate) {
//                prepareDataAnimation();
//                chart.startDataAnimation();
//                return true;
//            }
//            if (id == R.id.action_toggle_selection_mode) {
//                toggleLabelForSelected();
//
//                Toast.makeText(getActivity(),
//                        "Selection mode set to " + chart.isValueSelectionEnabled() + " select any point.",
//                        Toast.LENGTH_SHORT).show();
//                return true;
//            }
//            if (id == R.id.action_toggle_touch_zoom) {
//                chart.setZoomEnabled(!chart.isZoomEnabled());
//                Toast.makeText(getActivity(), "IsZoomEnabled " + chart.isZoomEnabled(), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//            if (id == R.id.action_zoom_both) {
//                chart.setZoomType(ZoomType.HORIZONTAL_AND_VERTICAL);
//                return true;
//            }
//            if (id == R.id.action_zoom_horizontal) {
//                chart.setZoomType(ZoomType.HORIZONTAL);
//                return true;
//            }
//            if (id == R.id.action_zoom_vertical) {
//                chart.setZoomType(ZoomType.VERTICAL);
//                return true;
//            }
//            return super.onOptionsItemSelected(item);
//        }

        private void generateValues() {
            for (int i = 0; i < maxNumberOfLines; ++i) {
                for (int j = 0; j < numberOfPoints; ++j) {
                    randomNumbersTab[i][j] = (float) Math.random() * 100f;
                }
            }
        }

        private void reset() {
            numberOfLines = 1;

            hasAxes = true;
            hasAxesNames = true;
            hasLines = true;
            hasPoints = true;
            shape = ValueShape.CIRCLE;
            isFilled = false;
            hasLabels = false;
            isCubic = false;
            hasLabelForSelected = false;
            pointsHaveDifferentColor = false;

            chart.setValueSelectionEnabled(hasLabelForSelected);
            resetViewport();
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

        /**
         * Adds lines to data, after that data should be set again with
         * {@link LineChartView#setLineChartData(LineChartData)}. Last 4th line has non-monotonically x values.
         */
        private void addLineToData() {
            if (data.getLines().size() >= maxNumberOfLines) {
                Toast.makeText(getActivity(), "Samples app uses max 4 lines!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                ++numberOfLines;
            }

            generateData();
        }

        private void toggleLines() {
            hasLines = !hasLines;

            generateData();
        }

        private void togglePoints() {
            hasPoints = !hasPoints;

            generateData();
        }

        private void toggleCubic() {
            isCubic = !isCubic;

            generateData();

            if (isCubic) {
                // It is good idea to manually set a little higher max viewport for cubic lines because sometimes line
                // go above or below max/min. To do that use Viewport.inest() method and pass negative value as dy
                // parameter or just set top and bottom values manually.
                // In this example I know that Y values are within (0,100) range so I set viewport height range manually
                // to (-5, 105).
                // To make this works during animations you should use Chart.setViewportCalculationEnabled(false) before
                // modifying viewport.
                // Remember to set viewport after you call setLineChartData().
                final Viewport v = new Viewport(chart.getMaximumViewport());
                v.bottom = -5;
                v.top = 105;
                // You have to set max and current viewports separately.
                chart.setMaximumViewport(v);
                // I changing current viewport with animation in this case.
                chart.setCurrentViewportWithAnimation(v);
            } else {
                // If not cubic restore viewport to (0,100) range.
                final Viewport v = new Viewport(chart.getMaximumViewport());
                v.bottom = 0;
                v.top = 100;

                // You have to set max and current viewports separately.
                // In this case, if I want animation I have to set current viewport first and use animation listener.
                // Max viewport will be set in onAnimationFinished method.
                chart.setViewportAnimationListener(new ChartAnimationListener() {

                    @Override
                    public void onAnimationStarted() {
                        // TODO Auto-generated method stub

                    }

                    @Override
                    public void onAnimationFinished() {
                        // Set max viewpirt and remove listener.
                        chart.setMaximumViewport(v);
                        chart.setViewportAnimationListener(null);

                    }
                });
                // Set current viewpirt with animation;
                chart.setCurrentViewportWithAnimation(v);
            }

        }

        private void toggleFilled() {
            isFilled = !isFilled;

            generateData();
        }

        private void togglePointColor() {
            pointsHaveDifferentColor = !pointsHaveDifferentColor;

            generateData();
        }

        private void setCircles() {
            shape = ValueShape.CIRCLE;

            generateData();
        }

        private void setSquares() {
            shape = ValueShape.SQUARE;

            generateData();
        }

        private void setDiamonds() {
            shape = ValueShape.DIAMOND;

            generateData();
        }

        private void toggleLabels() {
            hasLabels = !hasLabels;

            if (hasLabels) {
                hasLabelForSelected = false;
                chart.setValueSelectionEnabled(hasLabelForSelected);
            }

            generateData();
        }

        private void toggleLabelForSelected() {
            hasLabelForSelected = !hasLabelForSelected;

            chart.setValueSelectionEnabled(hasLabelForSelected);

            if (hasLabelForSelected) {
                hasLabels = false;
            }

            generateData();
        }

        private void toggleAxes() {
            hasAxes = !hasAxes;

            generateData();
        }

        private void toggleAxesNames() {
            hasAxesNames = !hasAxesNames;

            generateData();
        }

        /**
         * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
         * method(don't confuse with View.animate()). If you operate on data that was set before you don't have to call
         * {@link LineChartView#setLineChartData(LineChartData)} again.
         */
        private void prepareDataAnimation() {
            for (Line line : data.getLines()) {
                for (PointValue value : line.getValues()) {
                    // Here I modify target only for Y values but it is OK to modify X targets as well.
                    value.setTarget(value.getX(), (float) Math.random() * 100);
                }
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

package com.example.amosmadalinneculau.googlechartapiexample;

/**
 * Created by williamhawken on 09/12/2015.
 */


        import android.app.Activity;
        import android.os.Bundle;
        import android.util.DisplayMetrics;

/**
 * Created by Sidd on 09/12/2015.
 */
public class Pop extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*.8));
    }
}


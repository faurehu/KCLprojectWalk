package com.example.amosmadalinneculau.googlechartapiexample;

/**
 * Created by williamhawken on 09/12/2015.
 */


        import android.app.Activity;
        import android.content.Intent;
        import android.os.Bundle;
        import android.util.DisplayMetrics;
        import android.view.View;
        import android.widget.Button;

/**
 * Created by Sidd on 09/12/2015.
 */
public class Pop extends Activity {

    protected Button closeBtn;
    protected Intent i;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pop_up);

        i = new Intent(this, MainActivity.class);

        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        int width = dm.widthPixels;
        int height = dm.heightPixels;

        getWindow().setLayout((int)(width*0.8),(int)(height*.8));

        //closeBtn = (Button) findViewById(R.id.closeBtn);


        /*closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(i);
            }
        });*/

    }

}


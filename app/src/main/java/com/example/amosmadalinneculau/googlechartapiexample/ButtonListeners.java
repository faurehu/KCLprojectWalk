package com.example.amosmadalinneculau.googlechartapiexample;

import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Created by amosmadalinneculau on 06.12.2015.
 */
public class ButtonListeners{

//    private Button country1;
//    private Button country2;
//    private Button country3;
//    private Button country4;
//    private Button country5;
//    private Button country6;

    private at.markushi.ui.CircleButton country1;
    private at.markushi.ui.CircleButton country2;
    private at.markushi.ui.CircleButton country3;
    private at.markushi.ui.CircleButton country4;
    private at.markushi.ui.CircleButton country5;
    private at.markushi.ui.CircleButton country6;

    public ButtonListeners(at.markushi.ui.CircleButton country1,
                           at.markushi.ui.CircleButton country2,
                           at.markushi.ui.CircleButton country3,
                           at.markushi.ui.CircleButton country4,
                           at.markushi.ui.CircleButton country5,
                           at.markushi.ui.CircleButton country6){
        this.country1 = country1;
        this.country2 = country2;
        this.country3 = country3;
        this.country4 = country4;
        this.country5 = country5;
        this.country6 = country6;
    }

    public void contry1Listener(){
        country1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Successful:","button country1 onClickListener");

            }
        });
    }
}

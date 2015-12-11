package com.example.amosmadalinneculau.googlechartapiexample;

/**
 * Created by amosmadalinneculau on 28.11.2015.
 */

//helper class
    //a point has 2 values etc
public class Point {

    private String x;
    private String y;

    public Point(String x, String y){
        this.x = x;
        this.y = y;
    }

    public String getX(){
        return x;
    }

    public String getY(){
        return y;
    }

    public void setX(String newXValue){
        x = newXValue;
    }

    public void setY(String newYValue){
        y = newYValue;
    }

    public String toString(){
        return "[ " + x + " , " + y +" ]";
    }
}

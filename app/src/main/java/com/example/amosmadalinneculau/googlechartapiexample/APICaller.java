package com.example.amosmadalinneculau.googlechartapiexample;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by faure on 09/12/2015.
 */

//API CALLER AND DATA CACHEING
public class APICaller extends AsyncTask<String, Void, Void> {

    @Override
    protected Void doInBackground(String... country) {
        checkCache(country[0]);
        return null;
    }

    public void checkCache(String country) {
        if (MainActivity.cacheJSON.has(country)) {
            try {
                JSONObject cacheCountry = (JSONObject) MainActivity.cacheJSON.get(country);
                String imports = (String) cacheCountry.get("imports");
                String interest = (String) cacheCountry.get("interest");
                parseData(imports, interest);
            } catch(JSONException e) {
                Log.e("JSONException", e.getMessage());
            }
        } else {
            fetchData(country);
        }
    }

    public void fetchData(String countryName) {
        String interestsUrl = "http://api.worldbank.org/countries/" + countryName + "/indicators/NE.EXP.GNFS.ZS/?date=1960:2010&format=json&per_page=51";
        String importsUrl = "http://api.worldbank.org/countries/" + countryName + "/indicators/FR.INR.RINR/?date=1960:2010&format=json&per_page=51";
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

    protected void parseData(String importsJson, String interestsJson) {

        HashMap<Integer, Float> importsData = new HashMap<Integer, Float>();
        HashMap<Integer, Float> interestsData = new HashMap<Integer, Float>();

        try {
            JSONArray importsArray = new JSONArray(importsJson);
            JSONArray interestsArray = new JSONArray(interestsJson);
            JSONArray importsValues = (JSONArray) importsArray.get(1);
            JSONArray interestsValues = (JSONArray) interestsArray.get(1);
            for(int i = 1; i< importsValues.length(); ++i){
                JSONObject importsNode = (JSONObject) importsValues.get(i);
                JSONObject interestsNode = (JSONObject) interestsValues.get(i);
                try {
                    Log.i("Node-date", importsNode.get("date").toString());
                    Log.i("Node-value", importsNode.get("value").toString());
                    importsData.put(Integer.parseInt((String) importsNode.get("date")), Float.parseFloat((String) importsNode.get("value")));
                    interestsData.put(Integer.parseInt((String) interestsNode.get("date")), Float.parseFloat((String) interestsNode.get("value")));
                } catch (ClassCastException e) {
                    Log.e("ClassCastException", e.toString());
                }
            }
        } catch (JSONException e) {
            Log.e("JSON", e.toString());
        }

//        renderLines(exportsData, GDPData);
    }
}

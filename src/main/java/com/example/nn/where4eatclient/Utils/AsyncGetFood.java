package com.example.nn.where4eatclient.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.nn.where4eatclient.adapters.FoodItem;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.LinkedList;

/**
 * Created by nn on 9/25/2015.
 */
public class AsyncGetFood extends AsyncTask<String, Void, JSONArray> {

    private Context context;
    private LinkedList<FoodItem> flist = new LinkedList<FoodItem>();

    public AsyncGetFood(Context context){
        this.context = context;
    }

    public void catchEverything(){
        JSONObject result = new JSONObject();
        try {

            result.put("username", "%%");
            result.put("venue", "%%");
            result.put("name", "%%");
            result.put("minrating", 0);
            result.put("maxrating", 5);
        //    result.put("minlat", -1000.00);
        //    result.put("maxlat", 1000.00);
        //    result.put("minlong", -1000.00);
        //    result.put("maxlong", 1000.00);
            result.put("distance",10000000);

            SimpleLocationListener loc = new SimpleLocationListener(context);
            result.put("latitude",
                    loc.getLocation().getLatitude());
            result.put("longitude",
                    loc.getLocation().getLongitude());

            this.execute(result.toString());

        } catch (JSONException e) {
            Log.e("GETFOOD", e.getLocalizedMessage());
        }


    }

    @Override
    protected JSONArray doInBackground(String... params) {

        JSONArray jasonAnswer = null;
        //test stuff

        try {

            //
         //   JSONObject jason = new JSONObject(params[0]);



            JSONObject send = new JSONObject(params[0]);



            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost( Constants.SERVER_URL + Constants.GET_FOOD_URL);

            // put data in http client
            StringEntity se = new StringEntity(send.toString());
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");
            // Send stuff

            HttpResponse httpResponse = httpClient.execute(httpPost);

            // receive answer

            InputStream inputStream = httpResponse.getEntity().getContent();
            String answerString = Convertor.convertInputStreamToString(inputStream);
            jasonAnswer = new JSONArray(answerString);

        } catch (Exception e){
            Log.e("Foods:", e.getLocalizedMessage());
        }


        return jasonAnswer;
    }
}

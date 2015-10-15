package com.example.nn.where4eatclient;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.nn.where4eatclient.utils.SimpleLocationListener;

import org.json.JSONException;
import org.json.JSONObject;


public class SearchAcitivity extends Activity {

    private static final int EARTH_RADIUS = 6378137;
    private TextView usernameView, foodView, venueView, minRatingView, maxRatingView;
    private TextView distanceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_acitivity);
        usernameView = (TextView) findViewById(R.id.search_user_prompt);
        foodView = (TextView) findViewById(R.id.search_food_prompt);
        venueView = (TextView) findViewById(R.id.search_venue_prompt);
        distanceView = (TextView) findViewById(R.id.search_distance);
        minRatingView = (TextView) findViewById(R.id.search_rating_min);
        maxRatingView = (TextView) findViewById (R.id.search_rating_max);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search_acitivity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showList (View view){
        Intent i = new Intent (this, ListFoodActivity.class);
        i.putExtra("JSON",packIntoString().toString());
        startActivity(i);
    }

    public void showMap (View view){
        Intent i = new Intent (this, FoodMapActivity.class);
        i.putExtra("JSON",packIntoString().toString());
        startActivity(i);
    }

    private JSONObject packIntoString(){
     //   int R = EARTH_RADIUS; // earch radius
     //   SimpleLocationListener sll = new SimpleLocationListener(this);
     //   double myLat = sll.getLocation().getLatitude();
     //   double myLong =  sll.getLocation().getLongitude();
     //   double dLat = Math.abs(Double.valueOf(distanceView.getText().toString())/R);
     //   double dLong = Math.abs(Double.valueOf(distanceView.getText().toString())/(R*Math.cos(Math.PI*myLat*180)));
     //   double minLat = myLat - dLat * 180/Math.PI;
     //   double maxLat = myLat + dLat *180/Math.PI;
     //   double minLong = myLong - dLong * 180/Math.PI;
     //   double maxLong = myLong + dLong * 180/Math.PI;

        JSONObject result = new JSONObject();
        try {
            result.put("username", "%"+usernameView.getText().toString()+"%");
            result.put("venue", "%"+venueView.getText().toString()+"%");
            result.put("name", "%"+foodView.getText().toString()+"%");
            result.put("minrating", minRatingView.getText().toString());
            result.put("maxrating", maxRatingView.getText().toString());;
        //    result.put("minlat", String.valueOf(minLat));
        //    result.put("maxlat", String.valueOf(maxLat));
        //    result.put("minlong", String.valueOf(minLong));
        //    result.put("maxlong", String.valueOf(maxLong));
            SimpleLocationListener loc = new SimpleLocationListener(this);
            result.put("latitude", loc.getLocation().getLatitude());
            result.put("longitude", loc.getLocation().getLongitude());

        result.put("distance",distanceView.getText());

        } catch (JSONException e){
            Log.d("Search", e.getLocalizedMessage());
        }
        return result;
    }
}

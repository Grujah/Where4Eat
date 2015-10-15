package com.example.nn.where4eatclient;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nn.where4eatclient.utils.Constants;
import com.example.nn.where4eatclient.utils.Convertor;
import com.example.nn.where4eatclient.utils.DownloadImageTask;
import com.example.nn.where4eatclient.utils.PathJSONParser;
import com.example.nn.where4eatclient.utils.SimpleLocationListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.example.nn.where4eatclient.utils.Convertor.convertInputStreamToString;


public class ShowFoodActivity extends FragmentActivity {


    private ImageView image;
    private TextView foodtext;
    private TextView venuetext;
    private String link = null;
    private GoogleMap mMap;
    private SimpleLocationListener locator;
    JSONObject infobox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food);

        image = (ImageView) findViewById(R.id.preview_image_2);
        foodtext = (TextView) findViewById(R.id.foodview_name);
        venuetext = (TextView) findViewById(R.id.foodview_venue);
        locator = new SimpleLocationListener(this);
        int id = getIntent().getIntExtra(Constants.TAG_FOOD_ID, 0);
        fetchInfo task = new fetchInfo();
        task.execute(id);





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_food, menu);
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


    public class fetchInfo extends AsyncTask<Integer, Void, JSONObject> {


        private ProgressDialog pd = new ProgressDialog(ShowFoodActivity.this);

        protected void onPreExecute() {
            super.onPreExecute();
   //         pd.setMessage("Getting Stuff!");
    //        pd.show();
        }

        @Override
        protected JSONObject doInBackground(Integer... params) {

            ArrayList<NameValuePair> requestData = new ArrayList<NameValuePair>();

            requestData = new ArrayList<NameValuePair>();
            requestData.add(new BasicNameValuePair("id", String.valueOf(params[0])));


            JSONObject jasonAnswer = null;
            try {
                UrlEncodedFormEntity jEntity = new UrlEncodedFormEntity(requestData);
                HttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(Constants.SERVER_URL + Constants.FETCH_SINGLE_ITEM_URL);
                httpPost.setEntity(jEntity);


                // Send stuff
                HttpResponse httpResponse = httpClient.execute(httpPost);
                //    HttpResponse httpResponse2 = httpClient.execute(imagePost);

                // receive infobox

                InputStream inputStream = httpResponse.getEntity().getContent();
                String answerString = convertInputStreamToString(inputStream);
                jasonAnswer = new JSONObject(answerString);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
                return null;
            }

            return jasonAnswer;
        }

        protected void onPostExecute(JSONObject j) {
            super.onPostExecute(j);
      //      pd.hide();
          //                 pd.dismiss();
            infobox = j;
            try {
                setUpMapIfNeeded();
                foodtext.setText(j.getString("name"));
                venuetext.setText(j.getString("venue"));
                DownloadImageTask downloader = new DownloadImageTask(image);
                link = Constants.SERVER_URL + Constants.SERVER_IMAGE_FOLDER + j.getString("image") + ".jpg";
                downloader.execute(link);

            } catch (JSONException e) {
                e.printStackTrace();
            }




        }

    }

    private class fetchDirections extends AsyncTask<JSONObject, Integer, List<List<HashMap<String, String>>>> {

        @Override
        protected List<List<HashMap<String, String>>> doInBackground(JSONObject... params) {


            List<List<HashMap<String, String>>> routes = null;

            try {
                JSONObject loc = params[0];
                HttpClient httpClient = new DefaultHttpClient();
                String s =
                        Constants.DIRECTIONS_URL +
                        "json?" +
                        "origin=" +
                        loc.getString("latitude")+ "," + loc.getString("longitude") +
                        "&destination=" +
                        locator.getLocation().getLatitude() + "," + locator.getLocation().getLongitude() +
                        "&key=" + Constants.DIRECTIONS_KEY;
                HttpGet httpGet = new HttpGet(s);



                // put data in http client



                // Send stuff
                HttpResponse httpResponse = httpClient.execute(httpGet);
                InputStream inputStream = httpResponse.getEntity().getContent();
                String answerString = Convertor.convertInputStreamToString(inputStream);
                JSONObject j = new JSONObject(answerString);

                PathJSONParser parser = new PathJSONParser();
                routes = parser.parse(j);


            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> list) {
            super.onPostExecute(list);
            ArrayList<LatLng> points = null;
            PolylineOptions polyLineOptions = null;

            // traversing through routes
            for (int i = 0; i < list.size(); i++) {
                points = new ArrayList<LatLng>();
                polyLineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = list.get(i);

                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                polyLineOptions.addAll(points);
                polyLineOptions.width(2);
                polyLineOptions.color(Color.BLUE);
            }
            mMap.addPolyline(polyLineOptions);
        }
    }

    public void showFullImage(View view){
        Intent intent = new Intent(this, FullImageActivity.class);
        intent.putExtra(Constants.TAG_IMAGEURL, link);
        this.startActivity(intent);
    }


    private void setUpMapIfNeeded() throws JSONException {

        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.small_map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        LatLngBounds.Builder builder = new LatLngBounds.Builder();


        MarkerOptions m = null;
        try {
            m = new MarkerOptions()
                    .position(new LatLng(infobox.getDouble("latitude"), infobox.getDouble("longitude")))
                    .title(infobox.getString("name"));
            Marker x = mMap.addMarker(m);
            builder.include(x.getPosition());
            builder.include(new LatLng(locator.getLocation().getLatitude(),locator.getLocation().getLongitude()));
            LatLngBounds bounds = builder.build();
            mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
            mMap.setMyLocationEnabled(true);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void getDirections (View view) {
        //fetchDirections fd = new fetchDirections();
        // fd.execute(infobox);

        try {
            Uri gmmIntentUri = Uri.parse(
                    "google.navigation:q=" +
                            infobox.getString("latitude") + "," + infobox.getString("longitude"));

            Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
            mapIntent.setPackage("com.google.android.apps.maps");
            startActivity(mapIntent);
        }
        catch (Exception e){
            //TODO
        }

    }

}



package com.example.nn.where4eatclient;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.nn.where4eatclient.Utils.AsyncGetFood;
import com.example.nn.where4eatclient.Utils.Constants;
import com.example.nn.where4eatclient.Utils.DownloadImageTask;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;

import static com.example.nn.where4eatclient.Utils.Convertor.convertInputStreamToString;


public class ShowFoodActivity extends FragmentActivity {


    private ImageView image;
    private TextView foodtext;
    private TextView venuetext;
    private String link = null;
    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_food);

        image = (ImageView) findViewById(R.id.preview_image_2);
        foodtext = (TextView) findViewById(R.id.foodview_name);
        venuetext = (TextView) findViewById(R.id.foodview_venue);

        int id = getIntent().getIntExtra(Constants.TAG_FOOD_ID, 0);
        setUpMapIfNeeded();
        uploadToServer task = new uploadToServer();
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


    public class uploadToServer extends AsyncTask<Integer, Void, JSONObject> {

        JSONObject answer;
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

                // receive answer

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
      //      pd.dismiss();

            try {
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

    public void showFullImage(View view){
        Intent intent = new Intent(this, FullImageActivity.class);
        intent.putExtra(Constants.TAG_IMAGEURL, link);
        this.startActivity(intent);
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.small_map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
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

        mMap.setMyLocationEnabled(true);
    }


}



package com.example.nn.where4eatclient;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.example.nn.where4eatclient.Utils.AsyncGetFood;
import com.example.nn.where4eatclient.Utils.SimpleLocationListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.json.JSONArray;

import java.io.InputStream;

public class FoodMapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private JSONArray jsArray;
    LocationManager lm;
    private Criteria criteria;
    private String provider;
    Location location;
    SimpleLocationListener locListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_map);

        //    provider = LocationManager.NETWORK_PROVIDER;
        locListener = new SimpleLocationListener(this);
        setUpMapIfNeeded();


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private class AsyncFoodLocations extends AsyncGetFood {

        public AsyncFoodLocations(Context context) {
            super(context);
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            super.onPostExecute(jsonArray);

            try {
                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                for (int i = 0; i < jsonArray.length(); i++) {

                    MarkerOptions m = new MarkerOptions()
                            .position(new LatLng(jsonArray.getJSONObject(i).getDouble("latitude"), jsonArray.getJSONObject(i).getDouble("longitude")))
                            .title(jsonArray.getJSONObject(i).getString("name"));
                    Marker x = mMap.addMarker(m);
                    builder.include(x.getPosition());


                    //          DownloadBitmapTask dl = new DownloadBitmapTask(x);
                    //          dl.execute("http://192.168.1.3:8081/where2eat/images/" + jsonArray.getJSONObject(i).getString("image") + "_t.jpg");
                }

                LatLngBounds bounds = builder.build();
                mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 20));
            } catch (Exception e) {
                Log.e("Mapa:", e.getLocalizedMessage());
            }


        }
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
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

        LatLng ll = null;
        if (locListener.getLocation() != null) {
        }
        ll = new LatLng(locListener.getLocation().getLatitude(), locListener.getLocation().getLongitude());
        CameraUpdate cu = CameraUpdateFactory.newLatLng(ll);
        mMap.moveCamera(cu);

        mMap.setMyLocationEnabled(true);
        AsyncFoodLocations fl = new AsyncFoodLocations(this);

        if (getIntent().hasExtra("JSON")) {
            fl.execute(getIntent().getStringExtra("JSON"));

        } else {
            fl.catchEverything();
        }
    }


    class DownloadBitmapTask extends AsyncTask<String, Void, Bitmap> {
        private final Marker marker;

        public DownloadBitmapTask(Marker marker) {
            this.marker = marker;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap image = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                image = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return image;
        }

        protected void onPostExecute(Bitmap result) {
            if (result != null) {
                marker.setIcon(BitmapDescriptorFactory.fromBitmap(result));
            }
        }
    }
}


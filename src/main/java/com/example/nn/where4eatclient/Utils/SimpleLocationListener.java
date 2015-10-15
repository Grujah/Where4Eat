package com.example.nn.where4eatclient.utils;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

/**
 * Created by nn on 9/28/2015.
 */
public class SimpleLocationListener implements LocationListener{

    private String provider;
    LocationManager myLocationManager;
    Location location;
    Context context;

    public SimpleLocationListener(Context context){
        this.context = context;
        myLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        provider = myLocationManager.getBestProvider(criteria,true);
        myLocationManager.requestLocationUpdates(provider,100,1,this);
        location = myLocationManager.getLastKnownLocation(provider);
        int a = 1;
    }


    @Override
    public void onLocationChanged(Location location) {
        this.location = location;

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public Location getLocation()  {
        if (location == null)
        {
            location = myLocationManager.getLastKnownLocation(provider);
        }
      //  if (location == null) throw new Exception();
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}

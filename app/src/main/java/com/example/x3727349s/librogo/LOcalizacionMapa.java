package com.example.x3727349s.librogo;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.maps.model.LatLng;


import static android.content.Context.LOCATION_SERVICE;

/**
 * Created by x3727349s on 07/03/17.
 */

public class LOcalizacionMapa extends MainActivityFragment {

    public LatLng getLocation()
    {
        // Coje la localizacion
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat,lon;
        try {
            lat = location.getLatitude ();
            lon = location.getLongitude ();

            return new LatLng(lat, lon);
        }
        catch (NullPointerException e){
            e.printStackTrace();
            return null;
        }
    }

}

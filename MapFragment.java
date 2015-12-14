package edu.usc.weatherapplication;


// import the AerisMapView & components
import com.google.android.gms.maps.MapView;
import com.hamweather.aeris.maps.AerisMapView;
import com.hamweather.aeris.maps.AerisMapView.AerisMapType;
import com.hamweather.aeris.maps.MapViewFragment;
import com.hamweather.aeris.communication.AerisCallback;

import android.util.Log;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import com.hamweather.aeris.model.AerisResponse;
import com.hamweather.aeris.communication.EndpointType;
import com.google.android.gms.maps.model.LatLng;
import com.hamweather.aeris.communication.AerisEngine;
import android.location.Location;
import com.hamweather.aeris.tiles.AerisTile;

import com.hamweather.aeris.maps.interfaces.OnAerisMapLongClickListener;

public class MapFragment extends MapViewFragment implements OnAerisMapLongClickListener, AerisCallback
{
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        AerisEngine.initWithKeys(this.getString(R.string.aeris_client_id), this.getString(R.string.aeris_client_secret), "edu.usc.weatherapplication");

        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mapView = (AerisMapView) view.findViewById(R.id.aerisfragment_map);
        mapView.init(savedInstanceState, AerisMapType.GOOGLE);

        Bundle bundle = getArguments();
        String lat = bundle.getString("lat");
        String lng = bundle.getString("lng");

        Location location = new Location("");
        location.setLatitude(Double.valueOf(lat));
        location.setLongitude(Double.valueOf(lng));

        mapView.moveToLocation(location,10);
        mapView.addLayer(AerisTile.RADSAT);

        mapView.setOnAerisMapLongClickListener(this);

        return view;
    }

    @Override
    public void onMapLongClick(double lat, double longitude) {
        // code to handle map long press. i.e. Fetch current conditions?
        // see demo app MapFragment.java
    }

    @Override
    public void onResult(EndpointType type, AerisResponse response) {

    }
}

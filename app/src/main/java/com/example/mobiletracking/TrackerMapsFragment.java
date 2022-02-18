package com.example.mobiletracking;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class TrackerMapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Marker currentPosition;
    private ArrayList<LatLng> arCoor;

    public TrackerMapsFragment()  {
        arCoor = new ArrayList<LatLng>();
        getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap gmap) { this.googleMap = gmap; }

    public void updateCurrentPosition(Double latitude, Double longitude){
        //Pas testé voila voila
        googleMap.clear();
        LatLng pos = new LatLng(latitude, longitude);
        arCoor.add(pos);

        this.currentPosition.setPosition(pos);

        googleMap.addMarker(new MarkerOptions()
                .position(arCoor.get(0))
                .title("Start"));

        googleMap.addMarker(new MarkerOptions()
                .position(arCoor.get(arCoor.size()-1))
                .title("End"));

        googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .addAll(arCoor));
        this.moveToCurrentLocation(pos);
    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        this.googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}

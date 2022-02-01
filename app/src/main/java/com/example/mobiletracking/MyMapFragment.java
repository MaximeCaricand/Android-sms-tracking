package com.example.mobiletracking;

import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MyMapFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;

    public MyMapFragment()  {
        getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap gmap) {
        this.googleMap = gmap;

    }

    public void newPos(Double latitude, Double longitude){
        LatLng pos = new LatLng(latitude, longitude);
        this.googleMap.addMarker(new MarkerOptions().position(pos).title("Marker in Vietnam"));
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
    }
}

package com.example.mobiletracking;


import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class TrackerMapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private Marker currentPositon;

    public TrackerMapsFragment()  {
        getMapAsync(this);
        //this.currentPositon = this.googleMap.addMarker(new MarkerOptions().title("Dernière position"));
    }

    @Override
    public void onMapReady(final GoogleMap gmap) { this.googleMap = gmap; }

    public void updateCurrentPosition(Double latitude, Double longitude){
        LatLng pos = new LatLng(latitude, longitude);
        this.currentPositon.setPosition(pos);
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
    }
}

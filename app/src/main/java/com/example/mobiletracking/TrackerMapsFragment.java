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
    private Marker currentPosition;

    public TrackerMapsFragment()  {
        getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap gmap) { this.googleMap = gmap; }

    public void updateCurrentPosition(Double latitude, Double longitude){
        LatLng pos = new LatLng(latitude, longitude);
        if (this.currentPosition == null) {
            this.currentPosition = this.googleMap.addMarker(new MarkerOptions().position(pos).title("Dernière position"));
        } else {
            this.currentPosition.setPosition(pos);
        }
        this.moveToCurrentLocation(pos);
    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        this.googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}

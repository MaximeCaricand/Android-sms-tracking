package com.example.mobiletracking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;

public class TrackerMapsFragment extends SupportMapFragment implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private ArrayList<LatLng> positions;
    private Bitmap runnerBitmap;

    public TrackerMapsFragment()  {
        positions = new ArrayList();
        getMapAsync(this);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        int runnerImageID = this.getResources().getIdentifier("tracked", "drawable", this.getContext().getPackageName());
        runnerBitmap = Bitmap.createScaledBitmap(
                BitmapFactory.decodeResource(getResources(), runnerImageID),
                80,
                80,
                false
        );
    }

    @Override
    public void onMapReady(final GoogleMap gmap) { this.googleMap = gmap; }

    public void updateCurrentPosition(LatLng position) {
        LatLng newPos = new LatLng(position.latitude, position.longitude);
        positions.add(newPos);
        googleMap.clear();

        // Start point
        googleMap.addMarker(new MarkerOptions().position(positions.get(0)).title(getString(R.string.startCourse)));

        // Last point
        if (positions.size() > 1) {
            googleMap.addMarker(new MarkerOptions()
                    .position(positions.get(positions.size()-1))
                    .icon(BitmapDescriptorFactory.fromBitmap(this.runnerBitmap))
                    .title(getString(R.string.currentPosition)));
        }

        // Paths
        googleMap.addPolyline(new PolylineOptions()
                .clickable(true)
                .color(getContext().getResources().getColor(R.color.red))
                .addAll(positions));

        this.moveToCurrentLocation(newPos);
    }

    private void moveToCurrentLocation(LatLng currentLocation) {
        this.googleMap.moveCamera(CameraUpdateFactory.newLatLng(currentLocation));
        if (positions.size() == 1) { // avoid reset user zoom after first point
            this.googleMap.animateCamera(CameraUpdateFactory.zoomIn());
            this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
        }
    }
}

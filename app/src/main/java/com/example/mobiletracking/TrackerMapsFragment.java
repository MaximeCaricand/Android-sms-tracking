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

    public void updateCurrentPosition(Double latitude, Double longitude) {
        LatLng newPos = new LatLng(latitude, longitude);
        positions.add(newPos);
        googleMap.clear();

        // Start point
        googleMap.addMarker(new MarkerOptions().position(positions.get(0)).title("Début du parcours"));

        // Last point
        if (positions.size() > 1) {
            googleMap.addMarker(new MarkerOptions()
                    .position(positions.get(positions.size()-1))
                    .icon(BitmapDescriptorFactory.fromBitmap(this.runnerBitmap))
                    .title("Position courante"));
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
        this.googleMap.animateCamera(CameraUpdateFactory.zoomIn());
        this.googleMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }
}

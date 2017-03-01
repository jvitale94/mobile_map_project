package com.example.jakevitale.mobile_project2;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private float lat1;
    private float lon1;
    private float lat2;
    private float lon2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Bundle bundle = getIntent().getExtras();
        float [] vals = bundle.getFloatArray("message");
        lat1 = vals[0];
        lon1 = vals[1];
        lat2 = vals[2];
        lon2 = vals[3];
        System.out.println(lat1);
        System.out.println(lat2);
        System.out.println(lon1);
        System.out.println(lon2);


    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        System.out.println(lat1);
        System.out.println(lat2);
        System.out.println(lon1);
        System.out.println(lon2);

        // Add a marker in Sydney and move the camera
        LatLng marker1 = new LatLng(lat1, lon1);
        LatLng marker2 = new LatLng(lat2, lon2);
        mMap.addMarker(new MarkerOptions().position(marker1).title("Marker 1"));
        mMap.addMarker(new MarkerOptions().position(marker2).title("Marker 2"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(marker1));
    }
}

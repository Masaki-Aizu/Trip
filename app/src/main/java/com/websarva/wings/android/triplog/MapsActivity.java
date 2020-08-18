package com.websarva.wings.android.triplog;

import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapLongClickListener;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private LatLng location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*
        Button sendButton = findViewById(R.id.save_location);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), SaveInfo.class);
                startActivity(intent);
            }
        });*/
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
        // 皇居辺りの緯度経度
        location = new LatLng(35.68, 139.76);
        // marker 追加
        mMap.addMarker(new MarkerOptions().position(location).title("Tokyo"));
        // camera 移動
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 10));

        // タップした時のリスナーをセット
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng tapLocation) {
                // tapされた位置の緯度経度
                location = new LatLng(tapLocation.latitude, tapLocation.longitude);
                String str = String.format(Locale.US, "%f, %f", tapLocation.latitude, tapLocation.longitude);
                /* Marker perth = mMap.addMarker(new MarkerOptions().position(location).title(str).draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 14));
                 */
            }
        });

        // 長押しのリスナーをセット
        mMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng longpushLocation) {
                LatLng newlocation = new LatLng(longpushLocation.latitude, longpushLocation.longitude);
                Marker perth  = mMap.addMarker(new MarkerOptions().position(newlocation).title("" + longpushLocation.latitude + " :" + longpushLocation.longitude).draggable(true));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(newlocation, 14));
                Intent intent = new Intent(getApplication(), SaveInfo.class);
                intent.putExtra("EXTRA_LONGTITUDE", longpushLocation.longitude);
                intent.putExtra("EXTRA_LATITUDE", longpushLocation.latitude);
                startActivity(intent);
                perth.remove();
            }
        });
    }

    /*vprivate void setScreenSub(){
        setContentView(R.layout.activity_save_location);

        Button returnButton = findViewById(R.id.save_info);
        returnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    } */

}

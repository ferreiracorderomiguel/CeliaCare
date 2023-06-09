package com.mfc.celiacare.ui.places;

import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mfc.celiacare.R;
import com.mfc.celiacare.model.Places;

import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private Button btnBack;
    private GoogleMap mMap;
    List<Places> placesList;
    private Double latitude;
    private Double longitude;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        initializeMap();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeElements(view);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initializeMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapPlaces);
        mapFragment.getMapAsync(this);
    }

    private void initializeElements(View view) {
        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });

        placesList = getArguments().getParcelableArrayList("places");


    }


    public void splitCoordinates(String coordinates) {
        String[] parts = coordinates.split(",");
        latitude = Double.valueOf(parts[0].trim());
        longitude = Double.valueOf(parts[1].trim());
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        if (placesList != null) {
            if (mMap != null) {
                for (Places place : placesList) {
                    splitCoordinates(place.getCoordinates());
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(place.getName())
                            .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
                }
            }
        }
    }
}
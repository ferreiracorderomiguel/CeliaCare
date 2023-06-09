package com.mfc.celiacare.ui.places;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mfc.celiacare.R;
import com.mfc.celiacare.model.Places;

import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private Button btnBack;
    private GoogleMap mMap;
    List<Places> placesList;
    private Double latitude;
    private Double longitude;
    private boolean hasExactLocation = false;
    double myLatitude;
    double myLongitude;

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
        checkLocationPermission();
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
        if (hasExactLocation){
            pointToExactLocation();
        }
    }

    private void pointToExactLocation() {
        getExactCoordinates();
        LatLng myLocation = new LatLng(36.9171211, -6.0621644);
        mMap.addMarker(new MarkerOptions()
                .position(myLocation)
                .title(getString(R.string.your_location))
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
        mMap.moveCamera(com.google.android.gms.maps.CameraUpdateFactory.newLatLngZoom(myLocation, 15));
    }

    private void getExactCoordinates() {
        LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                Location lastKnownLocation = null;

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                }

                if (lastKnownLocation == null) {
                    lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                }

                if (lastKnownLocation != null) {
                    myLatitude = lastKnownLocation.getLatitude();
                    myLongitude = lastKnownLocation.getLongitude();
                } else {
                    Toast.makeText(requireContext(), "No se pudo obtener la ubicación exacta", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void checkLocationPermission() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                hasExactLocation = true;
            } else {
                hasExactLocation = false;
            }
        }
    }
}
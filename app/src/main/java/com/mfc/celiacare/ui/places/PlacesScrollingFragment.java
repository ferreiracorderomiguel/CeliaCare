package com.mfc.celiacare.ui.places;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mfc.celiacare.R;
import com.mfc.celiacare.model.Places;


public class PlacesScrollingFragment extends Fragment implements OnMapReadyCallback {

    Double latitude;
    Double longitude;
    private GoogleMap mMap;
    Places place;

    public PlacesScrollingFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_scrolling, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        place = (Places) getArguments().getSerializable("places");
        initializeElements(view, place);
    }

    private void initializeElements(View view, Places place) {
        initializeTextViews(view, place);
        initializeMap(view, place);
    }

    private void initializeTextViews(View view, Places place) {
        TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText(place.getName());
        TextView textViewStreetAddress = view.findViewById(R.id.textViewStreetAddress);
        textViewStreetAddress.setText(place.getStreetAddress());
        TextView textViewCity = view.findViewById(R.id.textViewCity);
        textViewCity.setText(place.getCity());
        TextView textViewDescription = view.findViewById(R.id.textViewDescription);
        textViewDescription.setText(place.getDescription());
        TextView textViewPhoneNumber = view.findViewById(R.id.textViewPhoneNumber);
        textViewPhoneNumber.setText(place.getPhoneNumber());
    }

    private void initializeMap(View view, Places place) {
        splitCoordinates(place.getCoordinates());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapPlacesScrolling);
        mapFragment.getMapAsync(this);
    }

    public void splitCoordinates(String coordinates) {
        String[] parts = coordinates.split(",");
        latitude = Double.valueOf(parts[0].trim());
        longitude = Double.valueOf(parts[1].trim());

        Log.d("Coordinates", "Latitude: " + latitude + " Longitude: " + longitude);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng placeMap = new LatLng(latitude, longitude);
        mMap.addMarker(new MarkerOptions()
                .position(placeMap)
                .title(place.getName())
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(placeMap, 15));
    }
}
package com.mfc.celiacare.ui.places;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

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


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    //ArrayList<Places> placesData;
    Button btnBack;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*placesData = new ArrayList<>();

        Places places = new Places("Pizzería Roberto", "Calle San Jacinto, 87", "Chipiona", "Pizzería con horno de leña", 1, "954 33 33 33");
        placesData.add(places);*/
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;

        LatLng pdv = new LatLng(37.3553, -5.9891);
        mMap.addMarker(new MarkerOptions()
                .position(pdv)
                .title("Punta del Verde"));

        LatLng pisoSev = new LatLng(37.3648, -5.9882);
        mMap.addMarker(new MarkerOptions()
                .position(pisoSev)
                .title("Piso Sevilla")
                // Le doy color verde al marcador
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pdv, 15));
    }
}
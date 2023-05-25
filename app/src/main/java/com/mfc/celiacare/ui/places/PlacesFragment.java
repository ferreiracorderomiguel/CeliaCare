package com.mfc.celiacare.ui.places;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.ListViewPlacesAdapter;
import com.mfc.celiacare.model.PlacesModel;

import java.util.ArrayList;

public class PlacesFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public PlacesFragment() {
    }

    ArrayList<PlacesModel> placesData;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        placesData = new ArrayList<>();

        PlacesModel placesModel = new PlacesModel("Pizzería Roberto", "Calle San Jacinto, 87", "Chipiona", "Pizzería con horno de leña", 1, "954 33 33 33");
        placesData.add(placesModel);

        ListViewPlacesAdapter adapter = new ListViewPlacesAdapter();
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
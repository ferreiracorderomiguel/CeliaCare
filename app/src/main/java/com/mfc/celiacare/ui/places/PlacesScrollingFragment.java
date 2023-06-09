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
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.mfc.celiacare.R;
import com.mfc.celiacare.model.Places;


public class PlacesScrollingFragment extends Fragment implements OnMapReadyCallback {

    private Double latitude;
    private Double longitude;
    private GoogleMap mMap;
    private Places place;
    private final String URL = "https://celiacare-mfercor326v-default-rtdb.europe-west1.firebasedatabase.app";

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth auth;
    FirebaseUser currentUser;


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
        initializeViewElements(view, place);
        initializeFirebase();
        initializeMap(view, place);
    }

    private void initializeViewElements(View view, Places place) {
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
        FloatingActionButton fab = view.findViewById(R.id.fabPlacesScrolling);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    // Usuario logueado, ejecuta el método savePlaceOnUserProfile()
                    savePlaceOnUserProfile(place);
                } else {
                    // Usuario no logueado, muestra un mensaje de error o redirige al usuario a la pantalla de inicio de sesión
                    Toast.makeText(getContext(), "You need to be logged in to save a place", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void initializeFirebase() {
        database = FirebaseDatabase.getInstance(URL);
        myRef = database.getReference("favplaces");
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
    }

    private void savePlaceOnUserProfile(Places place) {
        Toast.makeText(getContext(), getString(R.string.place_save_message_1) + " " + place.getName() + " " + getString(R.string.place_save_message_2), Toast.LENGTH_SHORT).show();

        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            String userPath = userEmail.replace(".", "_");

            DatabaseReference userRef = myRef.child(userPath);

            DatabaseReference placeRef = userRef.push();
            
            placeRef.setValue(place.getName());
        }
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
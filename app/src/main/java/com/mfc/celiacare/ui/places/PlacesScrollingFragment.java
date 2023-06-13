package com.mfc.celiacare.ui.places;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mfc.celiacare.R;
import com.mfc.celiacare.model.Places;

/**
 * Fragment that displays details of a place and its location on the map.
 */
public class PlacesScrollingFragment extends Fragment implements OnMapReadyCallback {

    private Double latitude;
    private Double longitude;
    private GoogleMap mMap;
    private Places place;
    FloatingActionButton fab;
    private final String URL = "https://celiacare-mfercor326v-default-rtdb.europe-west1.firebasedatabase.app";

    FirebaseDatabase database;
    DatabaseReference myRef;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    Bitmap image;

    /**
     * Constructor for the PlacesScrollingFragment class.
     */
    public PlacesScrollingFragment() {}

    /**
     * Called when the fragment is being created. This method sets up the fragment
     * and initializes any necessary variables or resources.
     *
     * @param savedInstanceState The saved instance state Bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Inflates the layout for the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved instance state Bundle.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_scrolling, container, false);
        return view;
    }

    /**
     * Called immediately after the view hierarchy of the fragment is created.
     *
     * @param view               The View object returned by onCreateView.
     * @param savedInstanceState The saved instance state Bundle.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        place = (Places) getArguments().getSerializable("places");
        image = getArguments().getParcelable("image");

        initializeElements(view, place);
    }

    /**
     * Initializes the elements in the fragment's view.
     *
     * @param view  The View object returned by onViewCreated.
     * @param place The Places object representing the place to display.
     */
    private void initializeElements(View view, Places place) {
        initializeViewElements(view, place);
        initializeFirebase();
        initializeMap(view, place);
    }

    /**
     * Initializes the view elements in the fragment's layout.
     *
     * @param view  The View object returned by onViewCreated.
     * @param place The Places object representing the place to display.
     */
    private void initializeViewElements(View view, Places place) {
        ImageView imageViewPlace = view.findViewById(R.id.imageViewPlace);
        imageViewPlace.setImageBitmap(image);
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
        fab = view.findViewById(R.id.fabPlacesScrolling);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentUser != null) {
                    addToFavorites(place.getName());
                } else {
                    Toast.makeText(getContext(), "Necesitas iniciar sesión primero", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Initializes Firebase database and authentication.
     */
    private void initializeFirebase() {
        database = FirebaseDatabase.getInstance(URL);
        myRef = database.getReference("favplaces");
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        checkIfPlaceExistsInFavorites(place.getName());
    }

    /**
     * Checks if the current place exists in the user's favorites.
     *
     * @param placeName The name of the place to check.
     */
    private void checkIfPlaceExistsInFavorites(String placeName) {
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            String userPath = userEmail.replace(".", "_");

            DatabaseReference userRef = myRef.child(userPath);

            Query query = userRef.orderByValue().equalTo(placeName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        updateFabIcon(true);
                    } else {
                        updateFabIcon(false);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Error checking if place exists", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Updates the FAB icon based on whether the place is a favorite or not.
     *
     * @param isFavorite Boolean value indicating whether the place is a favorite or not.
     */
    private void updateFabIcon(boolean isFavorite) {
        if (isFavorite) {
            fab.setImageResource(R.drawable.ic_fav);
        } else {
            fab.setImageResource(R.drawable.ic_not_fav);
        }
    }

    /**
     * Adds or removes the place from the user's favorites.
     *
     * @param placeName The name of the place to add or remove.
     */
    private void addToFavorites(String placeName) {
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            String userPath = userEmail.replace(".", "_");

            DatabaseReference userRef = myRef.child(userPath);

            Query query = userRef.orderByValue().equalTo(placeName);
            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            snapshot.getRef().removeValue();
                        }
                        Toast.makeText(getContext(), getString(R.string.place_removed_fav), Toast.LENGTH_SHORT).show();
                        updateFabIcon(false);
                    } else {
                        DatabaseReference placeRef = userRef.push();
                        placeRef.setValue(placeName);
                        Toast.makeText(getContext(), getString(R.string.place_save_message_1) + " " + placeName + " " + getString(R.string.place_save_message_2), Toast.LENGTH_SHORT).show();
                        updateFabIcon(true);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Error adding/removing place", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * Initializes the map fragment and sets the place's coordinates.
     *
     * @param view  The View object returned by onViewCreated.
     * @param place The Places object representing the place to display.
     */
    private void initializeMap(View view, Places place) {
        splitCoordinates(place.getCoordinates());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mapPlacesScrolling);
        mapFragment.getMapAsync(this);
    }

    /**
     * Splits the place's coordinates into latitude and longitude values.
     *
     * @param coordinates The coordinates of the place.
     */
    public void splitCoordinates(String coordinates) {
        String[] parts = coordinates.split(",");
        latitude = Double.valueOf(parts[0].trim());
        longitude = Double.valueOf(parts[1].trim());
    }

    /**
     * Called when the map is ready to be used.
     *
     * @param googleMap The GoogleMap object representing the map.
     */
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
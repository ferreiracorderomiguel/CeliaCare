package com.mfc.celiacare.ui.places;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.PlacesAdapter;
import com.mfc.celiacare.model.News;
import com.mfc.celiacare.model.Places;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class PlacesFragment extends Fragment {

    Button btnMap;
    Button btnMyPlaces;
    RecyclerView recyclerPlaces;
    PlacesAdapter placesAdapter;
    List<Places> placesList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SwipeRefreshLayout swipePlaces;
    private final String URL = "https://celiacare-mfercor326v-default-rtdb.europe-west1.firebasedatabase.app";

    public PlacesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        initializeFirebase();
        return view;
    }

    private void initializeFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance(URL);
        databaseReference = firebaseDatabase.getReference("places");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnMap = view.findViewById(R.id.btnMap);
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView("map");
            }
        });
        btnMyPlaces = view.findViewById(R.id.btnMyPlaces);
        btnMyPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView("myPlaces");
            }
        });

        getPlacesFromFirebase();
        initializeElements(view);
    }

    private void getPlacesFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                placesList.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String name = childSnapshot.child("name").getValue(String.class);
                    String streetAddress = childSnapshot.child("streetAddress").getValue(String.class);
                    String city = childSnapshot.child("city").getValue(String.class);
                    String description = childSnapshot.child("description").getValue(String.class);
                    String image = childSnapshot.child("image").getValue(String.class);
                    String phoneNumber = childSnapshot.child("phoneNumber").getValue(String.class);
                    String date = childSnapshot.child("date").getValue(String.class);
                    String coordinates = childSnapshot.child("coordinates").getValue(String.class);

                    Places place = new Places(name, streetAddress, city, description, image, phoneNumber, date, coordinates);
                    placesList.add(place);
                }

                Collections.reverse(placesList);
                placesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR: ", error.getMessage());
            }
        });
    }

    private void initializeElements(View view) {
        recyclerPlaces = view.findViewById(R.id.recyclerViewPlaces);
        recyclerPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPlaces.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        placesAdapter = new PlacesAdapter(placesList, getContext());
        placesAdapter.setPlacesFragment(this);

        recyclerPlaces.setAdapter(placesAdapter);
        swipePlaces = view.findViewById(R.id.swipePlaces);
        swipePlaces.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPlacesFromFirebase();
                swipePlaces.setRefreshing(false);
            }
        });
    }

    public void openPlaceDetails(Places places) {
        NavController navAccount = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putSerializable("places", places);
        navAccount.navigate(R.id.action_navigation_places_to_navigation_places_scrolling, args);
    }

    private void changeView(String view) {
        if (view.equals("map")) {
            NavController navAccount = Navigation.findNavController(getView());
            navAccount.navigate(R.id.action_navigation_places_to_navigation_map);
        } else if (view.equals("myPlaces")) {
            NavController navAccount = Navigation.findNavController(getView());
            navAccount.navigate(R.id.action_navigation_places_to_navigation_map);
        }
    }


}
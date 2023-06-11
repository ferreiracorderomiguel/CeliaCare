package com.mfc.celiacare.ui.places;

import android.app.AlertDialog;
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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.PlacesAdapter;
import com.mfc.celiacare.model.Places;
import com.mfc.celiacare.services.FirebaseService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PlacesFragment extends Fragment {

    Button btnMap;
    Button btnMyPlaces;
    RecyclerView recyclerPlaces;
    PlacesAdapter placesAdapter;
    List<Places> placesList = new ArrayList<>();
    /*FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;*/
    SwipeRefreshLayout swipePlaces;
    /*FirebaseAuth auth;
    FirebaseUser currentUser;*/
    FirebaseService firebaseService;
    private final String URL = "https://celiacare-mfercor326v-default-rtdb.europe-west1.firebasedatabase.app";

    public PlacesFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        initializeFirebase();
        return view;
    }

    private void initializeFirebase() {
        firebaseService = new FirebaseService();

        /*if (opc == 1) {
            firebaseDatabase = FirebaseDatabase.getInstance(URL);
            databaseReference = firebaseDatabase.getReference("places");
        } else if (opc == 2) {
            auth = FirebaseAuth.getInstance();
            currentUser = auth.getCurrentUser();
        }*/
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
        firebaseService.getPlaces(new FirebaseService.PlacesCallback() {
            @Override
            public void onPlacesReceived(List<Places> places) {
                placesList.clear();
                placesList.addAll(places);
                Collections.reverse(placesList);
                placesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onFailure(String errorMessage) {
                Log.e("ERROR: ", errorMessage);
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

    public void openPlaceDetails(Places place) {
        NavController navAccount = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putSerializable("places", place);
        navAccount.navigate(R.id.action_navigation_places_to_navigation_places_scrolling, args);
    }

    private void changeView(String view) {
        if (view.equals("map")) {
            NavController navAccount = Navigation.findNavController(getView());
            Bundle args = new Bundle();
            args.putParcelableArrayList("places", new ArrayList<>(placesList));
            navAccount.navigate(R.id.action_navigation_places_to_navigation_map, args);
        } else if (view.equals("myPlaces")) {
            openMyPlaces();
        }
    }

    private void openMyPlaces() {
        firebaseService.openMyPlaces(new FirebaseService.MyPlacesCallback() {
            @Override
            public void onMyPlacesOpened() {
                NavController navAccount = Navigation.findNavController(getView());
                navAccount.navigate(R.id.action_navigation_places_to_navigation_my_places);
            }

            @Override
            public void onUserNotLoggedIn() {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(getString(R.string.notLoguedTitle));
                builder.setMessage(getString(R.string.notLoguedMessage));
                builder.setPositiveButton(getString(R.string.button_confirm), null);

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

}
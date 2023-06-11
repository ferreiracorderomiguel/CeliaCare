package com.mfc.celiacare.ui.places;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.MyPlacesAdapter;
import com.mfc.celiacare.adapters.PlacesAdapter;
import com.mfc.celiacare.model.Places;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MyPlacesFragment extends Fragment {

    MyPlacesAdapter myPlacesAdapter;
    RecyclerView recyclerMyPlaces;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReferencePlaces, databaseReferenceFavPlaces;
    FirebaseAuth auth;
    FirebaseUser currentUser;
    List<Places> placesList = new ArrayList<>();
    List<String> favPlacesStringList = new ArrayList<>();
    List<Places> favPlacesList = new ArrayList<>();
    LinearLayout llNoFavPlaces;

    private final String URL = "https://celiacare-mfercor326v-default-rtdb.europe-west1.firebasedatabase.app";

    public MyPlacesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_places, container, false);

        initializeFirebase();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initializeElements(view);
    }

    private void initializeElements(View view) {
        llNoFavPlaces = view.findViewById(R.id.llNoFavPlaces);
        recyclerMyPlaces = view.findViewById(R.id.recyclerViewMyPlaces);
        recyclerMyPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMyPlaces.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        myPlacesAdapter = new MyPlacesAdapter(favPlacesList, getContext());
        myPlacesAdapter.setPlacesFragment(this);

        recyclerMyPlaces.setAdapter(myPlacesAdapter);
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
    }

    private void initializeFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance(URL);
        databaseReferencePlaces = firebaseDatabase.getReference("places");
        databaseReferenceFavPlaces = firebaseDatabase.getReference("favplaces");
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        getPlacesFromFirebase();
        getFavPlacesFromFirebase(currentUser.getEmail());
    }

    private void getPlacesFromFirebase() {
        databaseReferencePlaces.addListenerForSingleValueEvent(new ValueEventListener() {
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
                myPlacesAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR: ", error.getMessage());
            }
        });
    }

    private void getFavPlacesFromFirebase(String email) {
        String formattedEmail = email.replace(".", "_");

        databaseReferenceFavPlaces.child(formattedEmail).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                favPlacesStringList.clear();
                favPlacesList.clear();

                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    String placeId = childSnapshot.getKey();
                    String placeName = childSnapshot.getValue(String.class);
                    favPlacesStringList.add(placeName);
                }
                myPlacesAdapter.notifyDataSetChanged();

                addFavPlacesToList();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR: ", error.getMessage());
            }
        });
    }

    private void addFavPlacesToList() {
        favPlacesList.clear();

        for (String placeName : favPlacesStringList) {
            for (Places place : placesList) {
                if (place.getName().equals(placeName) && !favPlacesList.contains(place)) {
                    favPlacesList.add(place);
                    break;
                }
            }
        }

        myPlacesAdapter.notifyDataSetChanged();

        showMessageEmptyList();
    }

    public void openPlaceDetails(Places place){
        NavController navAccount = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putSerializable("places", place);
        navAccount.navigate(R.id.action_navigation_my_places_to_navigation_places_scrolling, args);
    }

    public void deletePlace(Places place) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.remove_favplace_title));
        builder.setMessage(getString(R.string.remove_favplace_message) + " " + place.getName() + " " + getString(R.string.remove_favplace_message2));
        builder.setPositiveButton(getString(R.string.button_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String formattedEmail = currentUser.getEmail().replace(".", "_");
                DatabaseReference userRef = databaseReferenceFavPlaces.child(formattedEmail);

                Query query = userRef.orderByValue().equalTo(place.getName());
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                snapshot.getRef().removeValue();
                            }

                            favPlacesList.remove(place);
                            myPlacesAdapter.notifyDataSetChanged();
                            showMessageEmptyList();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e("DeletePlace", "Failed to remove place: " + databaseError.getMessage());
                    }
                });
            }
        });
        builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void showMessageEmptyList() {
        if (favPlacesList.isEmpty()) {
            llNoFavPlaces.setVisibility(View.VISIBLE);
        } else {
            llNoFavPlaces.setVisibility(View.GONE);
        }
    }
}
package com.mfc.celiacare.ui.places;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.MyPlacesAdapter;
import com.mfc.celiacare.model.Places;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The `MyPlacesFragment` class is a fragment that displays the user's favorite places.
 * It retrieves data from Firebase Realtime Database and Firebase Storage, and populates a RecyclerView
 * with a list of favorite places. The user can swipe to refresh the list, open place details, and delete places.
 */
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
    SwipeRefreshLayout swipeMyPlaces;
    private Map<String, Bitmap> imagesMap = new HashMap<>();

    private final String URL = "https://celiacare-mfercor326v-default-rtdb.europe-west1.firebasedatabase.app";

    /**
     * Constructs a new instance of the `MyPlacesFragment` class.
     */
    public MyPlacesFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The previously saved state of the fragment.
     * @return                   The root view of the fragment's layout.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_places, container, false);

        initializeFirebase();

        return view;
    }

    /**
     * Called immediately after the view has been created.
     *
     * @param view               The View returned by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadPlacesImages();
        initializeElements(view);
    }

    /**
     * Initializes the UI elements.
     *
     * @param view The root view of the fragment's layout.
     */
    private void initializeElements(View view) {
        llNoFavPlaces = view.findViewById(R.id.llNoFavPlaces);
        recyclerMyPlaces = view.findViewById(R.id.recyclerViewMyPlaces);
        recyclerMyPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerMyPlaces.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        myPlacesAdapter = new MyPlacesAdapter(favPlacesList, getContext(), imagesMap);
        myPlacesAdapter.setMyPlacesFragment(this);

        recyclerMyPlaces.setAdapter(myPlacesAdapter);
        Button btnBack = view.findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> {
            getActivity().onBackPressed();
        });
        swipeMyPlaces = view.findViewById(R.id.swipeMyPlaces);
        swipeMyPlaces.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPlacesFromFirebase();
                swipeMyPlaces.setRefreshing(false);
                loadPlacesImages();
            }
        });
    }

    /**
     * Initializes Firebase and gets a reference to the database.
     */
    private void initializeFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance(URL);
        databaseReferencePlaces = firebaseDatabase.getReference("places");
        databaseReferenceFavPlaces = firebaseDatabase.getReference("favplaces");
        auth = FirebaseAuth.getInstance();
        currentUser = auth.getCurrentUser();
        getPlacesFromFirebase();
        loadPlacesImages();
        getFavPlacesFromFirebase(currentUser.getEmail());
    }

    /**
     * Retrieves places from Firebase and stores them in the "placesList" list.
     */
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

    /**
     * Retrieves the user's favorite places from Firebase and stores them in the "favPlacesList" list.
     *
     * @param email The email of the current user.
     */
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

    /**
     * Adds the favorite places to the "favPlacesList" by using the place names stored in the "favPlacesStringList".
     */
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

    /**
     * Opens the place details screen for the selected place.
     *
     * @param place The place object containing the details of the selected place.
     */
    public void openPlaceDetails(Places place){
        NavController navAccount = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putSerializable("places", place);
        navAccount.navigate(R.id.action_navigation_my_places_to_navigation_places_scrolling, args);
    }

    /**
     * Deletes the specified place from the user's favorite places.
     *
     * @param place The place object to be deleted.
     */
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

    /**
     * Shows a message if the favorite places list is empty.
     */
    private void showMessageEmptyList() {
        if (favPlacesList.isEmpty()) {
            llNoFavPlaces.setVisibility(View.VISIBLE);
        } else {
            llNoFavPlaces.setVisibility(View.GONE);
        }
    }

    /**
     * Loads the images of the places from Firebase Storage and stores them in the "imagesMap".
     */
    public void loadPlacesImages() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference().child("places");
        storageRef.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    final String imageName = item.getName();
                    File localFile = new File(getContext().getFilesDir(), imageName);

                    if (localFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getPath());
                        imagesMap.put(imageName, bitmap);
                    } else {
                        item.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localFile.getPath());
                                imagesMap.put(imageName, bitmap);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("TAG", "Failed to download image: " + imageName);
                            }
                        });
                    }
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                Log.e("TAG", "Failed to retrieve image list: " + e.getMessage());
            }
        });
    }
}
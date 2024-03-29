package com.mfc.celiacare.ui.places;


import android.app.AlertDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.PlacesAdapter;
import com.mfc.celiacare.model.Places;
import com.mfc.celiacare.services.FirebaseService;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Fragment that displays a list of places.
 */
public class PlacesFragment extends Fragment {

    Button btnMap;
    Button btnMyPlaces;
    RecyclerView recyclerPlaces;
    PlacesAdapter placesAdapter;
    List<Places> placesList = new ArrayList<>();
    SwipeRefreshLayout swipePlaces;
    FirebaseService firebaseService;
    private Map<String, Bitmap> imagesMap = new HashMap<>();

    /**
     * Default constructor for the PlacesFragment class.
     */
    public PlacesFragment() {}

    /**
     * Called to create the view hierarchy associated with the fragment.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The previously saved state of the fragment.
     * @return                   The root view of the fragment's layout.
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);

        initializeFirebase();
        return view;
    }

    /**
     * Initializes Firebase and sets up the FirebaseService instance.
     */
    private void initializeFirebase() {
        firebaseService = new FirebaseService();
    }

    /**
     * Called after the view creation to initialize the UI elements and set up listeners.
     *
     * @param view               The root view of the fragment.
     * @param savedInstanceState The previously saved state of the fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getPlacesFromFirebase();
        loadPlacesImages();

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

        initializeElements(view);
    }

    /**
     * Retrieves the list of places from Firebase and updates the placesList.
     */
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

    /**
     * Initializes the UI elements and sets up the RecyclerView and SwipeRefreshLayout.
     *
     * @param view The root view of the fragment.
     */
    private void initializeElements(View view) {
        recyclerPlaces = view.findViewById(R.id.recyclerViewPlaces);
        recyclerPlaces.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerPlaces.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        placesAdapter = new PlacesAdapter(placesList, getContext(), imagesMap);
        placesAdapter.setPlacesFragment(this);

        recyclerPlaces.setAdapter(placesAdapter);
        swipePlaces = view.findViewById(R.id.swipePlaces);
        swipePlaces.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPlacesFromFirebase();
                swipePlaces.setRefreshing(false);
                loadPlacesImages();
            }
        });
    }

    /**
     * Opens the details page for a specific place.
     *
     * @param place The place to display details for.
     */
    public void openPlaceDetails(Places place) {
        NavController navAccount = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putSerializable("places", place);

        String imageName = place.getImage();
        Bitmap image = imagesMap.get(imageName);
        args.putParcelable("image", image);

        navAccount.navigate(R.id.action_navigation_places_to_navigation_places_scrolling, args);
    }

    /**
     * Changes the current view based on the provided view identifier.
     *
     * @param view The view identifier. Possible values are "map" and "myPlaces".
     */
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

    /**
     * Opens the "My Places" page if the user is logged in, otherwise displays an alert dialog.
     */
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

    /**
     * Loads the images of the places from Firebase Storage and stores them in the imagesMap.
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
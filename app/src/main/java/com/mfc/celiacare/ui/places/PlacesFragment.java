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

public class PlacesFragment extends Fragment {

    Button btnMap;
    Button btnMyPlaces;
    RecyclerView recyclerPlaces;
    PlacesAdapter placesAdapter;
    List<Places> placesList = new ArrayList<>();
    SwipeRefreshLayout swipePlaces;
    FirebaseService firebaseService;
    private Map<String, Bitmap> imagesMap = new HashMap<>();

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
    }

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
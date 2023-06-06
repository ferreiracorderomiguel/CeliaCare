package com.mfc.celiacare.ui.places;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.PlacesAdapter;
import com.mfc.celiacare.model.Places;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment {

    Button btnMap;
    Button btnMyPlaces;
    RecyclerView recyclerPlaces;
    PlacesAdapter placesAdapter;

    public PlacesFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places, container, false);
        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        initializeElements(view);
    }

    private void initializeElements(View view) {
        recyclerPlaces = view.findViewById(R.id.recyclerViewPlaces);
        recyclerPlaces.setLayoutManager(new LinearLayoutManager(getContext()));

        List<Places> placesList = new ArrayList<>();
        placesList.add(new Places("Bulevar Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));
        placesList.add(new Places("Pijama Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));
        placesList.add(new Places("Doña gamba Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));
        placesList.add(new Places("La gamba Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));
        placesList.add(new Places("La gamba Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));
        placesList.add(new Places("La gamba Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));
        placesList.add(new Places("La gamba Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));

        placesAdapter = new PlacesAdapter(placesList, getContext());

        recyclerPlaces.setAdapter(placesAdapter);
    }

    private void changeView(String view) {
        if (view.equals("map")) {
            NavController navAccount = Navigation.findNavController(getView());
            navAccount.navigate(R.id.action_navigation_dashboard_to_navigation_map);
        } else if (view.equals("myPlaces")) {
            NavController navAccount = Navigation.findNavController(getView());
            navAccount.navigate(R.id.action_navigation_dashboard_to_navigation_map);
        }
    }
}
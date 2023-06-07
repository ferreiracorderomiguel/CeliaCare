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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.PlacesAdapter;
import com.mfc.celiacare.model.News;
import com.mfc.celiacare.model.Places;

import java.util.ArrayList;
import java.util.List;

public class PlacesFragment extends Fragment {

    Button btnMap;
    Button btnMyPlaces;
    RecyclerView recyclerPlaces;
    PlacesAdapter placesAdapter;
    List<Places> placesList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

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
        firebaseDatabase = FirebaseDatabase.getInstance("https://celiacare-mfercor326v-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference("places");
    }

    /*@Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }*/

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
                    String description = childSnapshot.child("description").getValue(String.class);
                    String image = childSnapshot.child("image").getValue(String.class);
                    String date = childSnapshot.child("date").getValue(String.class);

                    Places place = new Places(name, "Calle", "Ciudad", description, 1, date);
                    placesList.add(place);
                }

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

        /*placesList.add(new Places("Bulevar Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten.", 1, "955971224"));
        placesList.add(new Places("Restaurante Jábega, arrocería & marisquería", "Avenida Kansas City, 92. 41007 Sevilla.", "Nervión, Sevilla", "A finales de este mes termina su protocolo de adhesión el restaurante Jábega, una arrocería valenciana con una cocina que combina lo moderno con lo clásico.\n" +
                "\n" +
                "Además de sus arroces podéis disfruta de un gran surtido de marisco además de unas riquísimas tapas.", 1, "955971224"));
        placesList.add(new Places("Grosso Napoletano", "Calle Alameda de Hércules, 46. 41002, Sevilla.", "AdH, Sevilla", "Un horno de leña, un fuego encendido y todos nuestros productos sin gluten, para poder disfrutar de la auténtica pizza napolitana 100% gluten free en Alameda de Hércules 46. La misma esencia de siempre para que todo el mundo se siente a la mesa, menos el gluten. Si somos la 3ª mejor cadena de pizzerías del mundo y la primera de España, no deberías perdértela. ", 1, "955971224"));
        placesList.add(new Places("La gamba Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));
        placesList.add(new Places("La gamba Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));
        placesList.add(new Places("La gamba Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));
        placesList.add(new Places("La gamba Pizza", "Océano Atlántico S/N", "Lebrija", "Sólo tienen pizzas sin gluten", 1, "955971224"));*/

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
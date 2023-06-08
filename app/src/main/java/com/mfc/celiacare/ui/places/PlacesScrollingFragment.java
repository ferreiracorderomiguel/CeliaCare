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

import com.mfc.celiacare.R;
import com.mfc.celiacare.model.Places;


public class PlacesScrollingFragment extends Fragment {

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

        Places places = (Places) getArguments().getSerializable("places");
        initializeToolbar(view, places);
    }

    private void initializeToolbar(View view, Places places) {
        TextView textViewName = view.findViewById(R.id.textViewName);
        textViewName.setText(places.getName());
        TextView textViewStreetAddress = view.findViewById(R.id.textViewStreetAddress);
        textViewStreetAddress.setText(places.getStreetAddress());
        TextView textViewCity = view.findViewById(R.id.textViewCity);
        textViewCity.setText(places.getCity());
        TextView textViewDescription = view.findViewById(R.id.textViewDescription);
        textViewDescription.setText(places.getDescription());
        TextView textViewPhoneNumber = view.findViewById(R.id.textViewPhoneNumber);
        textViewPhoneNumber.setText(places.getPhoneNumber());
    }
}
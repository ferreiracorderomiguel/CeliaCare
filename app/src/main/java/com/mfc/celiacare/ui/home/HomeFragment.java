package com.mfc.celiacare.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mfc.celiacare.R;
import com.mfc.celiacare.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    FirebaseAuth fAuth;
    FirebaseUser user;
    TextView textViewWelcome;
    LinearLayout linearLayoutPlaces;
    LinearLayout linearLayoutNotifications;
    LinearLayout linearLayoutCommunity;
    LinearLayout linearLayoutAccount;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        linearLayoutPlaces = view.findViewById(R.id.linearLayoutPlaces);
        linearLayoutPlaces.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView("places");
            }
        });

        linearLayoutNotifications = view.findViewById(R.id.linearLayoutNotifications);
        linearLayoutNotifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView("notifications");
            }
        });

        linearLayoutAccount = view.findViewById(R.id.linearLayoutAccount);
        linearLayoutAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeView("account");
            }
        });
    }

    private void changeView(String places) {
        switch (places) {
            case "places":
                NavController navPlaces = Navigation.findNavController(getView());
                navPlaces.navigate(R.id.action_navigation_home_to_navigation_places);
                break;
            case "notifications":
                NavController navNotifications = Navigation.findNavController(getView());
                navNotifications.navigate(R.id.action_navigation_home_to_navigation_notifications);
                break;
            case "account":
                NavController navAccount = Navigation.findNavController(getView());
                navAccount.navigate(R.id.action_navigation_home_to_navigation_account);
                break;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void saluteUser() {
        if (user != null) {
            textViewWelcome.setText(textViewWelcome.getText().toString() + ", " + user.getDisplayName());
        }
    }
}
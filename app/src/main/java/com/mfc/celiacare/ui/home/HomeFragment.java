package com.mfc.celiacare.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mfc.celiacare.R;
import com.mfc.celiacare.databinding.FragmentHomeBinding;

/**
 * The HomeFragment class represents a fragment that displays the home screen of the application.
 * It provides navigation options to different sections of the app such as places, notifications, and user account.
 */
public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    FirebaseAuth fAuth;
    FirebaseUser user;
    TextView textViewWelcome;
    LinearLayout linearLayoutPlaces;
    LinearLayout linearLayoutNotifications;
    LinearLayout linearLayoutAccount;

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          If non-null, this is the parent view that the fragment's UI should be attached to.
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     * @return Return the View for the fragment's UI, or null.
     */
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        fAuth = FirebaseAuth.getInstance();
        user = fAuth.getCurrentUser();

        return root;
    }

    /**
     * Called immediately after onCreateView(LayoutInflater, ViewGroup, Bundle) has returned,
     * but before any saved state has been restored in to the view.
     *
     * @param view               The View created by onCreateView(LayoutInflater, ViewGroup, Bundle).
     * @param savedInstanceState If non-null, this fragment is being re-constructed from a previous saved state.
     */
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

    /**
     * Change the current view based on the specified destination.
     *
     * @param places The destination to navigate to.
     */
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

    /**
     * Called when the view previously created by onCreateView(LayoutInflater, ViewGroup, Bundle)
     * has been detached from the fragment.
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
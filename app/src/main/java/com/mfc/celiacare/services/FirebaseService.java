package com.mfc.celiacare.services;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mfc.celiacare.model.News;
import com.mfc.celiacare.model.Places;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

/**
 * This class provides Firebase-related services for retrieving data from the database.
 */
public class FirebaseService {
    private final String URL = "https://celiacare-mfercor326v-default-rtdb.europe-west1.firebasedatabase.app";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;
    private FirebaseAuth auth;

    /**
     * Callback interface for retrieving places from the database.
     */
    public interface PlacesCallback {

        /**
         * Called when places data is received successfully.
         *
         * @param places List of places retrieved from the database.
         */
        void onPlacesReceived(List<Places> places);

        /**
         * Called when there is an error retrieving places data.
         *
         * @param errorMessage Error message describing the cause of the failure.
         */
        void onFailure(String errorMessage);
    }

    /**
     * Callback interface for opening "My Places" section.
     */
    public interface MyPlacesCallback {

        /**
         * Called when "My Places" section is opened successfully.
         */
        void onMyPlacesOpened();

        /**
         * Called when the user is not logged in.
         */
        void onUserNotLoggedIn();
    }

    /**
     * Callback interface for retrieving news from the database.
     */
    public interface NewsCallback {

        /**
         * Called when news data is received successfully.
         *
         * @param newsList List of news retrieved from the database.
         */
        void onNewsReceived(List<News> newsList);

        /**
         * Called when there is an error retrieving news data.
         *
         * @param errorMessage Error message describing the cause of the failure.
         */
        void onFailure(String errorMessage);
    }

    /**
     * Initializes the FirebaseService with the Firebase Realtime Database URL.
     */
    public FirebaseService() {
        firebaseDatabase = FirebaseDatabase.getInstance(URL);
    }

    /**
     * Retrieves places data from the database.
     *
     * @param callback Callback to handle the places data.
     */
    public void getPlaces(final PlacesCallback callback) {
        databaseReference = firebaseDatabase.getReference("places");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Places> placesList = new ArrayList<>();

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

                callback.onPlacesReceived(placesList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    /**
     * Opens the "My Places" section if the user is logged in.
     *
     * @param callback Callback to handle the opening of "My Places" section.
     */
    public void openMyPlaces(final MyPlacesCallback callback) {
        auth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = auth.getCurrentUser();

        if (currentUser != null) {
            callback.onMyPlacesOpened();
        } else {
            callback.onUserNotLoggedIn();
        }
    }

    /**
     * Retrieves news data from the database.
     *
     * @param callback Callback to handle the news data.
     */
    public void getNews(final NewsCallback callback) {
        databaseReference = firebaseDatabase.getReference("news");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<News> newsList = new ArrayList<>();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String title = childSnapshot.child("title").getValue(String.class);
                    String description = childSnapshot.child("description").getValue(String.class);
                    String image = childSnapshot.child("image").getValue(String.class);
                    String date = childSnapshot.child("date").getValue(String.class);
                    String source = childSnapshot.child("source").getValue(String.class);

                    String timeSinceUpdated = getLastUpdatedTime(date);

                    News news = new News(title, description, image, date, source, timeSinceUpdated);
                    newsList.add(news);
                }

                Collections.reverse(newsList);
                callback.onNewsReceived(newsList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailure(databaseError.getMessage());
            }
        });
    }

    /**
     * Returns the time difference between the given date and the current date as a formatted string.
     *
     * @param date The date to compare with the current date.
     * @return The time difference as a formatted string.
     */
    private String getLastUpdatedTime(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        try {
            Date updatedDate = dateFormat.parse(date);
            Date currentDate = new Date();

            long differenceMillis = currentDate.getTime() - updatedDate.getTime();
            long differenceMinutes = TimeUnit.MILLISECONDS.toMinutes(differenceMillis);

            if (differenceMinutes < 60) {
                return differenceMinutes + "m";
            } else {
                long differenceHours = TimeUnit.MINUTES.toHours(differenceMinutes);
                if (differenceHours < 24) {
                    return differenceHours + " h";
                } else if (differenceHours < 24 * 30) {
                    long differenceDays = differenceHours / 24;
                    return differenceDays + " d";
                } else if (differenceHours < 24 * 365) {
                    long differenceMonths = differenceHours / (24 * 30);
                    return differenceMonths + " M";
                } else {
                    long differenceYears = differenceHours / (24 * 365);
                    return differenceYears + " a";
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return "";
    }
}

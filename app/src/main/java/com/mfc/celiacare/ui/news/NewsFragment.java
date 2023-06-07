package com.mfc.celiacare.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.NewsAdapter;
import com.mfc.celiacare.model.News;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class NewsFragment extends Fragment {

    RecyclerView recyclerNews;
    NewsAdapter newsAdapter;
    List<News> newsList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    SwipeRefreshLayout swipeNews;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        initializeFirebase();
        return view;
    }

    private void initializeFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance("https://celiacare-mfercor326v-default-rtdb.europe-west1.firebasedatabase.app");
        databaseReference = firebaseDatabase.getReference("news");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getNewsFromFirebase();
        initializeElements(view);
    }

    private void getNewsFromFirebase() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                newsList.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String title = childSnapshot.child("title").getValue(String.class);
                    String description = childSnapshot.child("description").getValue(String.class);
                    String image = childSnapshot.child("image").getValue(String.class);
                    String date = childSnapshot.child("date").getValue(String.class);

                    String timeSinceUpdated = getLastUpdatedTime(date);

                    News news = new News(title, description, image, date, timeSinceUpdated);
                    newsList.add(news);
                }

                Collections.reverse(newsList);
                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR: ", error.getMessage());
            }
        });
    }

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

    private void initializeElements(View view) {
        recyclerNews = view.findViewById(R.id.recyclerViewNews);
        recyclerNews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerNews.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        newsAdapter = new NewsAdapter(newsList, getContext());
        newsAdapter.setNewsFragment(this);

        recyclerNews.setAdapter(newsAdapter);
        swipeNews = view.findViewById(R.id.swipeNews);
        swipeNews.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getNewsFromFirebase();
                swipeNews.setRefreshing(false);

            }
        });
    }

    public void openNewsDetails(News news) {
        NavController navController = Navigation.findNavController(requireView());
        Bundle args = new Bundle();
        args.putSerializable("news", news);
        navController.navigate(R.id.action_navigation_news_to_navigation_news_scrolling, args);
    }
}
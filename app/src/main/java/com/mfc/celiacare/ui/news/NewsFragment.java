package com.mfc.celiacare.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.NewsAdapter;
import com.mfc.celiacare.model.News;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    RecyclerView recyclerNews;
    NewsAdapter newsAdapter;
    List<News> newsList = new ArrayList<>();
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);

        initializeFirebase();
        return view;
    }

    private void initializeFirebase() {
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("news");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getNewsFromFirebase();
        initializeElements(view);
    }

    private void getNewsFromFirebase() {
        Log.d("NOTICIAS: ", "getNewsFromFirebase");

        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("NOTICIAS: ", "onDataChange");
                newsList.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String title = childSnapshot.child("title").getValue(String.class);
                    String description = childSnapshot.child("description").getValue(String.class);
                    String image = childSnapshot.child("image").getValue(String.class);
                    String date = childSnapshot.child("date").getValue(String.class);

                    News news = new News(title, description, image, date);
                    newsList.add(news);
                }

                newsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ERROR: ", error.getMessage());
            }
        });
    }



    private void initializeElements(View view) {
        recyclerNews = view.findViewById(R.id.recyclerViewNews);
        recyclerNews.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerNews.addItemDecoration(new DividerItemDecoration(requireContext(), LinearLayoutManager.VERTICAL));

        newsList.add(new News("FACE lanza una encuesta para conocer la realidad social y sanitaria de las personas celiacas", "Desde FACE y las asociaciones miembros vamos a lanzar una encuesta como parte de un proyecto que pretende conocer la realidad social y sanitaria de las personas con enfermedad celiaca en España.", "Imagen", "06/06/2023"));
        newsList.add(new News("Noticia 2", "Descripción", "Imagen", "06/06/2023"));
        newsList.add(new News("Noticia 3", "Descripción", "Imagen", "06/06/2023"));
        newsList.add(new News("Noticia 4", "Descripción", "Imagen", "06/06/2023"));
        newsList.add(new News("Noticia 5", "Descripción", "Imagen", "06/06/2023"));
        newsList.add(new News("Noticia 6", "Descripción", "Imagen", "06/06/2023"));

        newsAdapter = new NewsAdapter(newsList, getContext());
        recyclerNews.setAdapter(newsAdapter);
    }
}
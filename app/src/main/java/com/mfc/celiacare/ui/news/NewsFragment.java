package com.mfc.celiacare.ui.news;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.NewsAdapter;
import com.mfc.celiacare.model.NewsModel;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    RecyclerView recyclerNews;
    NewsAdapter newsAdapter;
    List<NewsModel> newsModelList = new ArrayList<>();
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
        // https://www.youtube.com/watch?v=8FN0Itw28LE&list=PLs1bCj3TvmWmM-qN3FsCuPTTX-29I8Gh7&index=6
        Query query = databaseReference.orderByChild("date");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot newsSnapshot : snapshot.getChildren()) {
                    NewsModel newsModel = newsSnapshot.getValue(NewsModel.class);
                    newsModelList.add(newsModel);
                    Log.d("News", newsModel.getTitle());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initializeElements(View view) {
        recyclerNews = view.findViewById(R.id.recyclerViewNews);
        recyclerNews.setLayoutManager(new LinearLayoutManager(getContext()));

        newsModelList.add(new NewsModel("Noticia 1", "Descripción", "Imagen", "06/06/2023"));
        newsModelList.add(new NewsModel("Noticia 2", "Descripción", "Imagen", "06/06/2023"));
        newsModelList.add(new NewsModel("Noticia 3", "Descripción", "Imagen", "06/06/2023"));
        newsModelList.add(new NewsModel("Noticia 4", "Descripción", "Imagen", "06/06/2023"));
        newsModelList.add(new NewsModel("Noticia 5", "Descripción", "Imagen", "06/06/2023"));
        newsModelList.add(new NewsModel("Noticia 6", "Descripción", "Imagen", "06/06/2023"));

        newsAdapter = new NewsAdapter(newsModelList, getContext());
        recyclerNews.setAdapter(newsAdapter);
    }
}
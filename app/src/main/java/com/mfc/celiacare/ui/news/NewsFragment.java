package com.mfc.celiacare.ui.news;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mfc.celiacare.R;
import com.mfc.celiacare.adapters.NewsAdapter;
import com.mfc.celiacare.model.NewsModel;

import java.util.ArrayList;
import java.util.List;

public class NewsFragment extends Fragment {

    RecyclerView recyclerNews;
    NewsAdapter newsAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_news, container, false);


        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initializeElements(view);
    }

    private void initializeElements(View view) {
        recyclerNews = view.findViewById(R.id.recyclerViewNews);
        recyclerNews.setLayoutManager(new LinearLayoutManager(getContext()));

        List<NewsModel> newsModelList = new ArrayList<>();
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
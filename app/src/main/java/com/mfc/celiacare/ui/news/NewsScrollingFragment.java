package com.mfc.celiacare.ui.news;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.mfc.celiacare.R;
import com.mfc.celiacare.model.News;

public class NewsScrollingFragment extends Fragment {

    public NewsScrollingFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_scrolling, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        News news = (News) getArguments().getSerializable("news");

        initializeElements(view, news);
    }

    private void initializeElements(View view, News news) {
        initializeTextViews(view, news);
    }

    private void initializeTextViews(View view, News news) {
        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        textViewTitle.setText(news.getTitle());
        TextView textView = view.findViewById(R.id.textViewNews);
        textView.setText(news.getDescription());
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        textViewDate.setText(news.getDate());
        TextView textViewSource = view.findViewById(R.id.textViewSource);
        textViewSource.setText(news.getSource());
    }
}
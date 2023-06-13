package com.mfc.celiacare.ui.news;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mfc.celiacare.R;
import com.mfc.celiacare.model.News;

/**
 * A fragment that displays the details of a news article.
 */
public class NewsScrollingFragment extends Fragment {

    Bitmap image;

    /**
     * Default constructor for the fragment.
     */
    public NewsScrollingFragment() {}

    /**
     * Called when the fragment is being created.
     *
     * @param savedInstanceState The saved state of the fragment.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Called to have the fragment instantiate its user interface view.
     *
     * @param inflater           The LayoutInflater object that can be used to inflate any views in the fragment.
     * @param container          The parent view that the fragment's UI should be attached to.
     * @param savedInstanceState The saved state of the fragment.
     * @return The View for the fragment's UI.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news_scrolling, container, false);
        return view;
    }

    /**
     * Called immediately after the view has been created.
     *
     * @param view               The created view.
     * @param savedInstanceState The saved state of the fragment.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        News news = (News) getArguments().getSerializable("news");
        image = getArguments().getParcelable("image");

        initializeElements(view, news);
    }

    /**
     * Initializes the UI elements of the fragment.
     *
     * @param view The root view of the fragment.
     * @param news The news article to display details for.
     */
    private void initializeElements(View view, News news) {
        initializeTextViews(view, news);
    }

    /**
     * Initializes the TextViews in the fragment with the news article details.
     *
     * @param view The root view of the fragment.
     * @param news The news article to display details for.
     */
    private void initializeTextViews(View view, News news) {
        ImageView imageViewNews = view.findViewById(R.id.imageViewNews);
        imageViewNews.setImageBitmap(image);
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
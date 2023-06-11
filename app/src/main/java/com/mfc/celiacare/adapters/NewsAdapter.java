package com.mfc.celiacare.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.mfc.celiacare.R;
import com.mfc.celiacare.model.News;
import com.mfc.celiacare.ui.news.NewsFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsList;
    private Context context;
    private NewsFragment newsFragment;
    private Map<String, Bitmap> imagesMap = new HashMap<>();

    public NewsAdapter(List<News> newsList, Context context, Map<String, Bitmap> imagesMap) {
        this.newsList = newsList;
        this.context = context;
        this.imagesMap = imagesMap;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list, parent, false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder holder, int position) {
        String imageName = newsList.get(position).getImage();
        if (imagesMap.containsKey(imageName)) {
            Bitmap bitmap = imagesMap.get(imageName);
            holder.newsListImage.setImageBitmap(bitmap);
        }
        holder.newsTitleTextView.setText(newsList.get(position).getTitle());
        holder.descriptionTextView.setText(newsList.get(position).getDescription());
        holder.lastUpdatedTextView.setText(newsList.get(position).getTimeSinceUpdated());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newsFragment.openNewsDetails(newsList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView newsListImage;
        private TextView newsTitleTextView;
        private TextView descriptionTextView;
        private TextView lastUpdatedTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsListImage = itemView.findViewById(R.id.newsListImage);
            newsTitleTextView = itemView.findViewById(R.id.newsTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            lastUpdatedTextView = itemView.findViewById(R.id.lastUpdatedTextView);
        }
    }

    public void setNewsFragment(NewsFragment newsFragment) {
        this.newsFragment = newsFragment;
    }
}
package com.mfc.celiacare.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mfc.celiacare.R;
import com.mfc.celiacare.model.NewsModel;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<NewsModel> newsAdapterList;
    private Context context;

    public NewsAdapter(List<NewsModel> newsAdapterList, Context context) {
        this.newsAdapterList = newsAdapterList;
        this.context = context;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list, parent, false);
        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder holder, int position) {
        //holder.list_image.setImageResource(placesModelList.get(position).getImage());
        holder.newsTitleTextView.setText(newsAdapterList.get(position).getTitle());
        holder.descriptionTextView.setText(newsAdapterList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return newsAdapterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView newsListImage;
        private TextView newsTitleTextView;
        private TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsListImage = itemView.findViewById(R.id.list_image);
            newsTitleTextView = itemView.findViewById(R.id.newsTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }
}
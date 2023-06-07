package com.mfc.celiacare.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.mfc.celiacare.R;
import com.mfc.celiacare.model.News;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsAdapterList;
    private Context context;

    public NewsAdapter(List<News> newsAdapterList, Context context) {
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
        holder.lastUpdatedTextView.setText(newsAdapterList.get(position).getTimeSinceUpdated());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked on "+ position + " " + newsAdapterList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsAdapterList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView newsListImage;
        private TextView newsTitleTextView;
        private TextView descriptionTextView;
        private TextView lastUpdatedTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            newsListImage = itemView.findViewById(R.id.list_image);
            newsTitleTextView = itemView.findViewById(R.id.newsTitleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            lastUpdatedTextView = itemView.findViewById(R.id.lastUpdatedTextView);
        }
    }
}

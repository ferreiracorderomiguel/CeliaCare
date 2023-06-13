package com.mfc.celiacare.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;
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

/**
 * Adapter class for displaying news items in a RecyclerView.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private List<News> newsList;
    private Context context;
    private NewsFragment newsFragment;
    private Map<String, Bitmap> imagesMap = new HashMap<>();

    /**
     * Constructs a NewsAdapter object with the provided data and dependencies.
     *
     * @param newsList   The list of news items to display.
     * @param context    The context of the parent activity or fragment.
     * @param imagesMap  A map containing the image bitmaps associated with the news items.
     */
    public NewsAdapter(List<News> newsList, Context context, Map<String, Bitmap> imagesMap) {
        this.newsList = newsList;
        this.context = context;
        this.imagesMap = imagesMap;
    }

    /**
     * Called when RecyclerView needs a new ViewHolder of the given type to represent an item.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param viewType The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     */
    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_list, parent, false);
        return new NewsAdapter.ViewHolder(view);
    }

    /**
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the ViewHolder to reflect the item at the given position.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the item
     *                 at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
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

    /**
     * Returns the total number of items in the data set held by the adapter.
     *
     * @return The total number of items in this adapter.
     */
    @Override
    public int getItemCount() {
        return newsList.size();
    }

    /**
     * ViewHolder class for caching and binding views in the RecyclerView.
     */
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

    /**
     * Sets the NewsFragment for handling click events on news items.
     *
     * @param newsFragment The NewsFragment instance.
     */
    public void setNewsFragment(NewsFragment newsFragment) {
        this.newsFragment = newsFragment;
    }
}
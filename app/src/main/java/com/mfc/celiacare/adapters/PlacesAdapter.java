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
import com.mfc.celiacare.model.Places;
import com.mfc.celiacare.ui.places.PlacesFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The `PlacesAdapter` class is responsible for populating a RecyclerView with a list of places.
 * It uses a custom ViewHolder to efficiently display the place information and handles user interactions.
 */
public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder>{

    private List<Places> placesList;
    private Context context;
    private PlacesFragment placesFragment;
    private Map<String, Bitmap> imagesMap = new HashMap<>();

    /**
     * Constructs a new `PlacesAdapter` with the provided list of places, context, and images map.
     *
     * @param placesList  The list of places to display.
     * @param context     The context in which the adapter is being used.
     * @param imagesMap   The map of place image names to bitmaps.
     */
    public PlacesAdapter(List<Places> placesList, Context context, Map<String, Bitmap> imagesMap) {
        this.placesList = placesList;
        this.context = context;
        this.imagesMap = imagesMap;
    }

    /**
     * Called when the RecyclerView needs a new ViewHolder to represent an item.
     *
     * @param parent  The ViewGroup into which the new View will be added.
     * @param viewType  The view type of the new View.
     * @return  A new ViewHolder that holds a View for each place item.
     */
    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_list, parent, false);
        return new ViewHolder(view);
    }

    /**
     * Called by the RecyclerView to display the data at the specified position.
     *
     * @param holder  The ViewHolder that should be updated to represent the contents of the item at the given position.
     * @param position  The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder holder, int position) {
        String imageName = placesList.get(position).getImage();
        if (imagesMap.containsKey(imageName)) {
            Bitmap bitmap = imagesMap.get(imageName);
            holder.list_image.setImageBitmap(bitmap);
        }
        holder.titleTextView.setText(placesList.get(position).getName());
        holder.cityTextView.setText(placesList.get(position).getCity());
        holder.descriptionTextView.setText(placesList.get(position).getDescription());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placesFragment.openPlaceDetails(placesList.get(position));
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
        return placesList.size();
    }

    /**
     * The ViewHolder class represents each item within the RecyclerView.
     * It holds references to the views that will be populated with place information.
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView list_image;
        private TextView titleTextView;
        private TextView cityTextView;
        private TextView descriptionTextView;

        /**
         * Constructs a new ViewHolder for the place item view.
         *
         * @param itemView  The view representing a single place item.
         */
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            list_image = itemView.findViewById(R.id.list_image);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }

    /**
     * Sets the fragment that handles place details opening.
     *
     * @param placesFragment  The fragment responsible for opening place details.
     */
    public void setPlacesFragment(PlacesFragment placesFragment) {
        this.placesFragment = placesFragment;
    }
}

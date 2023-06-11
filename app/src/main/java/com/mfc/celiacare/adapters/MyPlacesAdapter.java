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
import com.mfc.celiacare.ui.places.MyPlacesFragment;
import com.mfc.celiacare.ui.places.PlacesFragment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyPlacesAdapter extends RecyclerView.Adapter<MyPlacesAdapter.ViewHolder> {

    private List<Places> placesList;
    private Context context;
    private MyPlacesFragment myPlacesFragment;
    private Map<String, Bitmap> imagesMap = new HashMap<>();

    public MyPlacesAdapter(List<Places> placesList, Context context, Map<String, Bitmap> imagesMap) {
        this.placesList = placesList;
        this.context = context;
        this.imagesMap = imagesMap;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_places_list, parent, false);
        return new ViewHolder(view);
    }

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
                myPlacesFragment.openPlaceDetails(placesList.get(position));
            }
        });

        holder.deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myPlacesFragment.deletePlace(placesList.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView list_image;
        private TextView titleTextView;
        private TextView cityTextView;
        private TextView descriptionTextView;
        private ImageView deleteImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            list_image = itemView.findViewById(R.id.list_image);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            deleteImageView = itemView.findViewById(R.id.deleteImageView);
        }
    }

    public void setPlacesFragment(MyPlacesFragment myPlacesFragment) {
        this.myPlacesFragment = myPlacesFragment;
    }
}

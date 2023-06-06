package com.mfc.celiacare.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.mfc.celiacare.R;
import com.mfc.celiacare.model.Places;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder>{
    // https://www.youtube.com/watch?v=-fiZaeNynFk

    private List<Places> placesList;
    private Context context;

    public PlacesAdapter(List<Places> placesList, Context context) {
        this.placesList = placesList;
        this.context = context;
    }

    @androidx.annotation.NonNull
    @Override
    public ViewHolder onCreateViewHolder(@androidx.annotation.NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.places_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@androidx.annotation.NonNull ViewHolder holder, int position) {
        //holder.list_image.setImageResource(placesModelList.get(position).getImage());
        holder.titleTextView.setText(placesList.get(position).getName());
        holder.cityTextView.setText(placesList.get(position).getCity());
        holder.descriptionTextView.setText(placesList.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return placesList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //private ImageView list_image;
        private TextView titleTextView;
        private TextView cityTextView;
        private TextView descriptionTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //list_image = itemView.findViewById(R.id.list_image);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
        }
    }

}

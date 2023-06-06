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
import com.mfc.celiacare.model.PlacesModel;

import java.util.List;

public class PlacesAdapter extends RecyclerView.Adapter<PlacesAdapter.ViewHolder>{
    // https://www.youtube.com/watch?v=-fiZaeNynFk

    private List<PlacesModel> placesModelList;
    private Context context;

    public PlacesAdapter(List<PlacesModel> placesModelList, Context context) {
        this.placesModelList = placesModelList;
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
        holder.titleTextView.setText(placesModelList.get(position).getName());
        holder.cityTextView.setText(placesModelList.get(position).getCity());
    }

    @Override
    public int getItemCount() {
        return placesModelList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        //private ImageView list_image;
        private TextView titleTextView;
        private TextView cityTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //list_image = itemView.findViewById(R.id.list_image);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            cityTextView = itemView.findViewById(R.id.cityTextView);
        }
    }

}

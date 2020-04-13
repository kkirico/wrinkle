package com.flagtag.wrinkle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flagtag.wrinkle.R;

import java.util.ArrayList;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.PhotoViewHolder> {

    private ArrayList<String> imageUrls;


    public PhotoAdapter(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    @NonNull
    @Override
    public PhotoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.image_layout, parent, false) ;

        return new PhotoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PhotoViewHolder holder, int position) {
        String url = imageUrls.get(position);

        Glide.with(holder.itemView.getContext())
                .load(url)
                .into(holder.image_part);
    }


    @Override
    public int getItemCount() {
        //return imageUrls.size();
        return 0;
    }

    public class PhotoViewHolder extends RecyclerView.ViewHolder {

        ImageView image_part;
        ConstraintLayout image_layout;
        public PhotoViewHolder(@NonNull View itemView) {
            super(itemView);
            image_part = itemView.findViewById(R.id.image_part);
            image_layout = itemView.findViewById(R.id.image_layout);
        }
    }
}

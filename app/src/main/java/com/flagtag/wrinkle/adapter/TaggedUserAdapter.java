package com.flagtag.wrinkle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.flagtag.wrinkle.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaggedUserAdapter extends RecyclerView.Adapter {

    ArrayList<String> taggedUsers = new ArrayList<>();
    public class TaggedUserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView iamgeview;
        TextView textView;

        public TaggedUserViewHolder(@NonNull View itemView) {
            super(itemView);

            iamgeview = itemView.findViewById(R.id.tagged_user_item_image);
            textView = itemView.findViewById(R.id.tagged_user_item_text);
        }
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.tagged_user_item, parent, false) ;

        return new TaggedUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        String primaryKey = taggedUsers.get(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").get()

    }

    @Override
    public int getItemCount() {
        return taggedUsers.size();
    }
}

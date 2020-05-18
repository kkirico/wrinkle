package com.flagtag.wrinkle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flagtag.wrinkle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class TaggedUserAdapter extends RecyclerView.Adapter<TaggedUserAdapter.TaggedUserViewHolder> {

    ArrayList<String> taggedUsers = new ArrayList<>();
    public class TaggedUserViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView textView;

        public TaggedUserViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.tagged_user_item_image);
            textView = itemView.findViewById(R.id.tagged_user_item_text);
        }
    }


    @NonNull
    @Override
    public TaggedUserAdapter.TaggedUserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.tagged_user_item, parent, false) ;

        return new TaggedUserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final TaggedUserAdapter.TaggedUserViewHolder holder, int position) {
        String primaryKey = taggedUsers.get(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userCollection = db.collection("users");
        userCollection.document(primaryKey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        String name = documentSnapshot.getData().get("name").toString();
                        String imageUrl = documentSnapshot.getData().get("photoUrl").toString();

                        Glide.with(holder.imageView.getContext()).load(imageUrl).into(holder.imageView);
                        holder.textView.setText(name);
                    }
                }
            }
        });

    }

    public void addItem(String primaryKey){
        taggedUsers.add(primaryKey);
    }

    @Override
    public int getItemCount() {
        return taggedUsers.size();
    }
}

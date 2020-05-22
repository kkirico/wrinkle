package com.flagtag.wrinkle.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flagtag.wrinkle.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchUserAdapter extends RecyclerView.Adapter<SearchUserAdapter.searchViewHolder> {

    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> selectedItems = new ArrayList<>();

    @NonNull
    @Override
    public SearchUserAdapter.searchViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;
        View view = inflater.inflate(R.layout.search_user_item, parent, false) ;

        return new SearchUserAdapter.searchViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull final SearchUserAdapter.searchViewHolder holder, int position) {
        final String primaryKey = items.get(position);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference userCollection = db.collection("users");

        holder.addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v.isSelected()){
                    v.setSelected(false);
                    holder.addButton.setText("추가");
                    int index = selectedItems.indexOf(primaryKey);
                    selectedItems.remove(index);
                }else{
                    v.setSelected(true);
                    holder.addButton.setText("취소");
                    selectedItems.add(primaryKey);
                }
            }
        });

        //이미 선택된 것이라면
        if(selectedItems.indexOf(primaryKey) != -1){
            holder.addButton.setText("취소");
            holder.addButton.setSelected(true);

        }

        userCollection.document(primaryKey).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        String name = documentSnapshot.getData().get("name").toString();
                        String email = documentSnapshot.getData().get("email").toString();
                        String imageUrl = documentSnapshot.getData().get("photoUrl").toString();

                        Glide.with(holder.imageView.getContext()).load(imageUrl).into(holder.imageView);
                        holder.name.setText(name);
                        holder.email.setText(email);
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItem(String primaryKey){
        items.add(primaryKey);
    }

    public ArrayList<String> getItems() {
        return items;
    }

    public ArrayList<String> getSelectedItems(){
        return selectedItems;
    }

    public void setItems(List<String> list){
        items.addAll(list);
    }

    public void addSelectedItems(ArrayList<String> list){
        selectedItems.addAll(list);
    }

    public class searchViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imageView;
        TextView name;
        TextView email;
        ConstraintLayout container;
        Button addButton;

        public searchViewHolder(@NonNull View itemView) {
            super(itemView);
            container = itemView.findViewById(R.id.search_user_item_container);
            imageView = itemView.findViewById(R.id.search_item_image);
            name = itemView.findViewById(R.id.search_item_name);
            email = itemView.findViewById(R.id.search_item_email);
            addButton = itemView.findViewById(R.id.search_item_add_button);
        }
    }

    public void clear(){
        items.clear();
    }
}

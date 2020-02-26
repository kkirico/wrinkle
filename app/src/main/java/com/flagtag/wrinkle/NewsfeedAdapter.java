package com.flagtag.wrinkle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedViewHolder> {

    //뉴스피드에 각각의 아이템의 데이터를 가지고 있는 arrayList
    private ArrayList<NewsfeedItem> items;

    //position번째 item에 해당하는 데이터를 뷰홀더의 item view에 표시
    @Override
    public void onBindViewHolder(@NonNull NewsfeedViewHolder holder, int position) {
        //position번째 아이템을 arrayList에서 가져옴
        NewsfeedItem item = items.get(position);

        //뷰홀더에 넣음
        holder.writing_date.setText("어쩌고 저쩌고");
        //동적으로 넣을 것들은 새로 생성해서 넣어줘야함.
    }

    @NonNull
    @Override
    public NewsfeedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        //layoutinflater로 newsfeed_cardview_layout inflate
        View view = inflater.inflate(R.layout.newsfeed_cardview_layout, parent, false) ;
        //뷰홀더 붙여줌.
        NewsfeedViewHolder vh = new NewsfeedViewHolder(view) ;

        return vh ;
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

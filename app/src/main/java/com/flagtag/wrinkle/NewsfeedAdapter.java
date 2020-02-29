package com.flagtag.wrinkle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator3;

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedViewHolder> {

    //뉴스피드에 각각의 아이템의 데이터를 가지고 있는 arrayList
    private ArrayList<Post> items;

    public NewsfeedAdapter() {
        this.items = items = new ArrayList<>();;
    }

    //position번째 item에 해당하는 데이터를 뷰홀더의 item view에 표시
    @Override
    public void onBindViewHolder(@NonNull NewsfeedViewHolder holder, int position) {
        //position번째 아이템을 arrayList에서 가져옴
        Post item = items.get(position);

        //뷰홀더에 넣음

        //태그된 사용자를 넣는것.
        for(int i=0; i<item.taggedUsers.size(); i++){
            Button button = new Button(holder.tagged_user_container.getContext());
            button.setText(item.taggedUsers.get(i).getName());
            holder.tagged_user_container.addView(button);
        }
        //title
        holder.title.setText(item.title);
        //날짜
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        holder.writing_date.setText(format.format(item.writingDate));

        //이미지 (뷰페이저 부분)
        PhotoAdapter imageAdapter = new PhotoAdapter(item.imageUrls);
        holder.image_viewpager.setAdapter(imageAdapter);

        CircleIndicator3 indicator = holder.indicator;
        indicator.setViewPager(holder.image_viewpager);
        imageAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());

        //내용(글)
        holder.content_writing.setText(item.contentWriting);
        //태그
        for(int i=0; i<item.tags.size(); i++){
            TextView textView = new TextView(holder.tags_container.getContext());
            textView.setText(item.tags.get(i));
            holder.tags_container.addView(textView);
        }
        //좋아요
        holder.likes.setText("좋아요 "+ item.likes);
        //댓글
        holder.comments.setText("댓글 "+item.comments);

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

    public void addItem(Post post){
        items.add(post);
    }
}

package com.flagtag.wrinkle;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class NewsfeedViewHolder extends RecyclerView.ViewHolder {

    LinearLayout tagged_user_container;
    TextView title;
    TextView writing_date;
    ViewPager image_container;
    TextView content_writing;
    LinearLayout tags_container;
    Button likes;
    Button comments;
    public NewsfeedViewHolder(@NonNull View itemView) {

        super(itemView);

        tagged_user_container = itemView.findViewById(R.id.tagged_users_container);
        title = itemView.findViewById(R.id.title);
        writing_date = itemView.findViewById(R.id.writing_date);
        image_container = itemView.findViewById(R.id.image_container);
        content_writing = itemView.findViewById(R.id.content_writing);
        tags_container = itemView.findViewById(R.id.tags_container);
        likes = itemView.findViewById(R.id.likes);
        comments = itemView.findViewById(R.id.comments);
    }
}

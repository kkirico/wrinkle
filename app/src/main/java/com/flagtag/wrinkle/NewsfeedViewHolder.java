package com.flagtag.wrinkle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import me.relex.circleindicator.CircleIndicator3;

public class NewsfeedViewHolder extends RecyclerView.ViewHolder {

    LinearLayout tagged_user_container;
    TextView title;
    TextView writing_date;
    ViewPager2 image_viewpager;
    TextView content_writing;
    LinearLayout tags_container;
    Button likes;
    Button comments;
    CircleIndicator3 indicator;
    SeekBar time_bar;
    ConstraintLayout timelinebar_container;
    public NewsfeedViewHolder(@NonNull View itemView) {

        super(itemView);

        tagged_user_container = itemView.findViewById(R.id.tagged_users_container);
        title = itemView.findViewById(R.id.title);
        writing_date = itemView.findViewById(R.id.writing_date);
        image_viewpager = itemView.findViewById(R.id.image_viewpager);
        content_writing = itemView.findViewById(R.id.content_writing);
        tags_container = itemView.findViewById(R.id.tags_container);
        likes = itemView.findViewById(R.id.likes);
        comments = itemView.findViewById(R.id.comments);
        indicator = itemView.findViewById(R.id.indicator);
        time_bar = itemView.findViewById(R.id.time_bar);
        timelinebar_container = itemView.findViewById(R.id.timelinebar_container);

        Log.d("text", content_writing.getText().toString());
    }
}

package com.flagtag.wrinkle;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.MotionEventCompat;
import androidx.customview.widget.ViewDragHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.flagtag.wrinkle.activity.MainActivity;
import com.flagtag.wrinkle.activity.SinglefeedActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedViewHolder> {

    //뉴스피드에 각각의 아이템의 데이터를 가지고 있는 arrayList
    private ArrayList<PostInfo> items;
    LinearLayout contents;
    Activity activity;

    public NewsfeedAdapter(Activity activity) {
        this.items = new ArrayList<>();
        this.activity = activity;
    }

    public NewsfeedAdapter(ArrayList<PostInfo> postList) {
        this.items = postList;
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



    //position번째 item에 해당하는 데이터를 뷰홀더의 item view에 표시
    @Override
    public void onBindViewHolder(@NonNull final NewsfeedViewHolder holder, final int position) {

        //position번째 아이템을 arrayList에서 가져옴
        final PostInfo item = items.get(position);

        //타임바 부분
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
        format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

        //날짜 세개(오늘, 기억의 날짜, 작성자생일)구하기 및 타임스탬프화
        Calendar calendar = Calendar.getInstance();
        Date today = new Date(calendar.getTimeInMillis());
        String todayDate = format.format(today);
        long todayTimeStamp=0;
        long birthdayTimeStamp=0;
        long dateOfMemoryTimeStamp=0;
        final String birthdayOfPublisher = item.getBirthdayOfPublisher();
        final String dateOfMemory = item.getDateOfMemory();
        try {
            todayTimeStamp = format.parse(todayDate).getTime()/(60*60*24*1000);
            birthdayTimeStamp = format.parse(birthdayOfPublisher).getTime()/(60*60*24*1000);
            dateOfMemoryTimeStamp = format.parse(dateOfMemory).getTime()/(60*60*24*1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //생일 날짜구하기

        double a =(((double)dateOfMemoryTimeStamp-birthdayTimeStamp)/(todayTimeStamp-birthdayTimeStamp))*100;
        holder.time_bar.setProgress((int)a);

        int height = holder.timelinebar_container.getHeight();
        int width = holder.timelinebar_container.getWidth();

        holder.time_bar.setRotation(90);
        holder.time_bar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        View view = holder.time_bar;
        ImageButton singlefeed_button = holder.singlefeed_button;

        final String title = item.title;
        final String writingDate = format.format(item.getCreatedAt());
        //title
        holder.title.setText(title);
        //날짜

        holder.writing_date.setText(writingDate);

        final ArrayList<String> contentList = item.getContents();

        final String publisher_id = item.getPublisher();

        singlefeed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(holder.comments.getContext(), SinglefeedActivity.class);
                intent.putExtra("title",title);
                intent.putExtra("writingDate",writingDate);
                intent.putStringArrayListExtra("contentList",contentList);
                intent.putExtra("pid",publisher_id);
                intent.putExtra("birthdayOfPublisher",birthdayOfPublisher);
                intent.putExtra("dateOfMemory",dateOfMemory);



                holder.comments.getContext().startActivity(intent);
            }
        });




 /*

        //태그된 사용자를 넣는것.
        holder.tagged_user_container.removeAllViews();
        for(int i=0; i<item.taggedUsers.size(); i++){
            Button button = new Button(holder.tagged_user_container.getContext());
            button.setText(item.taggedUsers.get(i).getName()+position);
            button.setTextSize(10);
            button.setPadding(1,1,1,1);
            //button.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.MATCH_PARENT,1));
            holder.tagged_user_container.addView(button,new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT,1));
        }*/


        FirebaseFirestore db = FirebaseFirestore.getInstance();

        DocumentReference docRef = db.collection("users").document(publisher_id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String pid = document.getData().get("name").toString();
                        String pUrl = document.getData().get("photoUrl").toString();

                        holder.publisher_id.setText(pid);
                        Glide.with(holder.time_bar.getContext()).load(pUrl).into(holder.publisher_pic);
                    } else {
                    }
                } else {
                }
            }
        });

/*

        holder.content_container.setOnScrollChangeListener(new ConstraintLayout.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Intent intent = new Intent(holder.comments.getContext(), SinglefeedActivity.class);
                activity.startActivity(intent);
            }

        });
        */


        holder.content_container.setOnTouchListener(new MainActivity.OnSwipeTouchListener(holder.content_container.getContext()){
            public void onSwipeTop() {
                Toast.makeText(holder.content_container.getContext(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(holder.content_container.getContext(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(holder.content_container.getContext(), "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(holder.content_container.getContext(), "bottom", Toast.LENGTH_SHORT).show();
            }
        });






        ArrayList<String> imageUrls = new ArrayList<>();
        ArrayList<String> texts = new ArrayList<>();
        String firstText = "";
        //이미지 (뷰페이저 부분)
        for(int i =0; i<contentList.size(); i++) {
            String contents = contentList.get(i);
            String[] type = contents.split("\\.");
            if (type[type.length - 1].substring(0, 3).equals("jpg")) {
                Glide.with(holder.time_bar.getContext()).load(contentList.get(i)).into(holder.content_image);
                imageUrls.add(contentList.get(i));
                break;
            }
        }
        for(int i =0; i<contentList.size(); i++) {
            String contents = contentList.get(i);
            String[] type = contents.split("\\.");
            if (type[type.length - 1].substring(0, 3).equals("jpg")) {
            }
            else {
                holder.content_writing.setText(contentList.get(i)+"...");
                break;
            }
        }

/*
        PhotoAdapter imageAdapter = new PhotoAdapter(imageUrls);
        holder.image_viewpager.setAdapter(imageAdapter);
        CircleIndicator3 indicator = holder.indicator;
        indicator.setViewPager(holder.image_viewpager);
        imageAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());


*/
        //태그
        /*
        holder.tags_container.removeAllViews();
        for(int i=0; i<item.tags.size(); i++){
            TextView textView = new TextView(holder.tags_container.getContext());
            textView.setText("#"+item.tags.get(i)+position);
            textView.setTextSize(20);
            holder.tags_container.addView(textView);
        }
        //좋아요
        holder.likes.setText("좋아요 "+ item.likes);
        //댓글
        holder.comments.setText("댓글 "+item.comments);

        //동적으로 넣을 것들은 새로 생성해서 넣어줘야함.
        */
    }



    @Override
    public int getItemCount() {
        return items.size();
    }
    public void addItem(PostInfo post){

        items.add(post);
    }

}

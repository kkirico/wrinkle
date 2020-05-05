package com.flagtag.wrinkle;

import android.content.Context;
import android.text.Spannable;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.flagtag.wrinkle.adapter.PhotoAdapter;
import com.flagtag.wrinkle.fragement.NewsfeedFragment;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import me.relex.circleindicator.CircleIndicator3;

public class NewsfeedAdapter extends RecyclerView.Adapter<NewsfeedViewHolder> {

    //뉴스피드에 각각의 아이템의 데이터를 가지고 있는 arrayList
    private ArrayList<PostInfo> items;

    public NewsfeedAdapter() {
        this.items = new ArrayList<>();;
    }

    public NewsfeedAdapter(NewsfeedFragment newsfeedFragment, ArrayList<PostInfo> postList) {
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
    public void onBindViewHolder(@NonNull NewsfeedViewHolder holder, int position) {

        //position번째 아이템을 arrayList에서 가져옴
        PostInfo item = items.get(position);

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
        String birthdayOfPublisher = item.getBirthdayOfPublisher();
        String dateOfMemory = item.getDateOfMemory();
        try {
            todayTimeStamp = format.parse(todayDate).getTime()/(60*60*24*1000);
            birthdayTimeStamp = format.parse(birthdayOfPublisher).getTime()/(60*60*24*1000);
            dateOfMemoryTimeStamp = format.parse(dateOfMemory).getTime()/(60*60*24*1000);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //생일 날짜구하기

        MemberInfo memberInfo = MemberInfo.getInstance();
        String name = memberInfo.getName();
        String birthDay = memberInfo.getBirthDay();
        String profilePicture = memberInfo.getPhotoUrl();


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
        //title
        holder.title.setText(item.title);
        //날짜

        holder.writing_date.setText(format.format(item.getCreatedAt()));



        ArrayList<String> imageUrls = new ArrayList<>();
        ArrayList<String> texts = new ArrayList<>();
        ArrayList<String> videoUrls = new ArrayList<>();
        int countImages=0;
        int countVideos= 0;
        int countTexts=0;
        //이미지 (뷰페이저 부분)
        ArrayList<String> contentList = item.getContents();
        for(int i =0; i<contentList.size(); i++) {
            String contents = contentList.get(i);
            String[] type = contents.split("\\.");
            if (type[type.length - 1].substring(0, 3).equals("jpg")) {
                imageUrls.add(contentList.get(i));
                //이미지 넣을때 마다 카운트 증가
                countImages++;
            } /* video adapter만들어야
            else if(type[type.length - 1].substring(0, 3).equals("avi")||type[type.length - 1].substring(0, 3).equals("mp4")){
                videoUrls.add(URLs.get(i));
                countVideos++;
            }*/
            else {
                texts.add(contentList.get(i));
                item.setContentWriting(texts.get(0));
                //내용(글) 위에서 걸러낸 texts를 사용해야하는디...
                holder.content_writing.setText(item.contentWriting);
                //택스트 중 더 보기
                Spannable span = (Spannable) holder.content_writing.getText();
                int start = 0;
                int end = start + holder.content_writing.getText().length();
                span.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(@NonNull View view) {
                        //이 게시물이 있는 페이지로 이동하게 하기
                    }
                }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                countTexts++;
            }
        }

        PhotoAdapter imageAdapter = new PhotoAdapter(imageUrls);
        holder.image_viewpager.setAdapter(imageAdapter);
        CircleIndicator3 indicator = holder.indicator;
        indicator.setViewPager(holder.image_viewpager);
        imageAdapter.registerAdapterDataObserver(indicator.getAdapterDataObserver());


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
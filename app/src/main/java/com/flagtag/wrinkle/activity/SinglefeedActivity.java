package com.flagtag.wrinkle.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flagtag.wrinkle.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class SinglefeedActivity extends BasicActivity {

    private static final String TAG = "Singlefeed Activity" ;
    public ImageView publisher_pic;
    private LinearLayout tagged_user_container;
    private TextView titleET;
    private TextView writing_date;
    private LinearLayout tags_container;
    private Button likes;
    private Button comments;
    public SeekBar time_bar;
    private ConstraintLayout timelinebar_container;
    private LinearLayout content_container;
    private TextView publisher_id;




    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singlefeed);

        Intent intent = this.getIntent();
        if (intent != null) {

            String title = intent.getExtras().getString("title");
            String pid = intent.getExtras().getString("pid");
            String writingDate = intent.getExtras().getString("writingDate");
            ArrayList<String> contentList = intent.getStringArrayListExtra("contentList");
            String birthdayOfPublisher = intent.getExtras().getString("birthdayOfPublisher");
            String dateOfMemory = intent.getExtras().getString("dateOfMemory");


            content_container = findViewById(R.id.contents);
            tagged_user_container = findViewById(R.id.tagged_user_container);
            titleET = findViewById(R.id.titleSF);
            writing_date = findViewById(R.id.writing_date);
            tags_container = findViewById(R.id.tags_container);
            likes = findViewById(R.id.likes);
            comments = findViewById(R.id.comments);
            time_bar = findViewById(R.id.time_bar);
            timelinebar_container = findViewById(R.id.timelinebar_container);
            publisher_id = findViewById(R.id.publisher_id);
            publisher_pic = findViewById(R.id.publisher_pic);


            titleET.setText(title);
            writing_date.setText(writingDate);


            // ArrayList<String> imageUrls = new ArrayList<>();
            // ArrayList<String> texts = new ArrayList<>();
            //이미지 (뷰페이저 부분)
            for (int i = 0; i < contentList.size(); i++) {
                String contents = contentList.get(i);
                String[] type = contents.split("\\.");
                if (type[type.length - 1].substring(0, 3).equals("jpg")) {
                    //contents 에 image_view 생성 후 contentList.get(i)를 넣기
                    ImageView imageView = new ImageView(time_bar.getContext());
                    content_container.addView(imageView);
                    Glide.with(time_bar.getContext()).load(contentList.get(i)).into(imageView);
                    //imageUrls.add(contentList.get(i));
                } else {
                    //contents안에 textview생성 하고, contentList.get(i)를 넣
                    TextView textView = new TextView(time_bar.getContext());
                    content_container.addView(textView);
                    textView.setText(contentList.get(i));
                }
            }


            //타임바 부분
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);
            format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));

            //날짜 세개(오늘, 기억의 날짜, 작성자생일)구하기 및 타임스탬프화
            Calendar calendar = Calendar.getInstance();
            Date today = new Date(calendar.getTimeInMillis());
            String todayDate = format.format(today);
            long todayTimeStamp = 0;
            long birthdayTimeStamp = 0;
            long dateOfMemoryTimeStamp = 0;
            try {
                todayTimeStamp = Objects.requireNonNull(format.parse(todayDate)).getTime() / (60 * 60 * 24 * 1000);
                assert birthdayOfPublisher != null;
                birthdayTimeStamp = Objects.requireNonNull(format.parse(birthdayOfPublisher)).getTime() / (60 * 60 * 24 * 1000);
                assert dateOfMemory != null;
                dateOfMemoryTimeStamp = Objects.requireNonNull(format.parse(dateOfMemory)).getTime() / (60 * 60 * 24 * 1000);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            double a = (((double) dateOfMemoryTimeStamp - birthdayTimeStamp) / (todayTimeStamp - birthdayTimeStamp)) * 100;
            time_bar.setProgress((int) a);

            int height = timelinebar_container.getHeight();
            int width = timelinebar_container.getWidth();

            time_bar.setRotation(90);
            time_bar.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return true;
                }
            });


        }
        else{
            Log log = null;
            log.d(TAG,"intent is null");
        }

    }

    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }
}

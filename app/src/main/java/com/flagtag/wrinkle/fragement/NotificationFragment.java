package com.flagtag.wrinkle.fragement;


import android.content.Context;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.StyleSpan;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.OverScroller;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.SmartEditText;

import java.util.ArrayList;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.graphics.Typeface.NORMAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    TextView textView1;
    TextView textView2;
    TextView textView3;
    ConstraintLayout constraintLayout;
    Button button;
    GestureDetector mDetector;

    ScrollView scrollView;
    OverScroller overScroller; //스크롤

    Boolean visible = false;

    ConstraintLayout.LayoutParams layoutParamsLong;
    ConstraintLayout.LayoutParams layoutParamsShort;

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_notification, container, false);

        constraintLayout = rootView.findViewById(R.id.aa);
        textView1 = rootView.findViewById(R.id.text1);
        textView2 = rootView.findViewById(R.id.text2);
        textView3 = rootView.findViewById(R.id.text3);

        scrollView = rootView.findViewById(R.id.scrollView);

        layoutParamsLong = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,500);
        layoutParamsShort = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_PARENT,300);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mDetector.onTouchEvent(event);
                return true;
            }
        });

        mDetector = new GestureDetector(getContext(), new GestureDetector.OnGestureListener() {
            @Override
            public boolean onDown(MotionEvent e) {
                return false;
            }

            @Override
            public void onShowPress(MotionEvent e) {

            }

            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return false;
            }

            @Override
            public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
                if(visible == true &&distanceY > 0){

                    textView2.setVisibility(View.GONE);
                    textView3.setVisibility(View.GONE);
                    visible = false;
                    constraintLayout.setLayoutParams(layoutParamsShort);
                }
                startToast("onscroll"+String.valueOf(distanceY));
                return false;
            }

            @Override
            public void onLongPress(MotionEvent e) {

            }

            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                //startToast("onFling"+String.valueOf(velocityY));
                if(visible == false && velocityY < 0) {

                    textView2.setVisibility(View.VISIBLE);
                    textView3.setVisibility(View.VISIBLE);
                    visible = true;
                    constraintLayout.setLayoutParams(layoutParamsLong);
                }
                    return false;

            }
        });


        return rootView;
    }

    public void startToast(String text){
        Toast.makeText(getContext(),text,Toast.LENGTH_SHORT).show();
    }
}

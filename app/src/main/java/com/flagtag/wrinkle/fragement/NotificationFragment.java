package com.flagtag.wrinkle.fragement;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.flagtag.wrinkle.R;

import java.util.ArrayList;
import java.util.Arrays;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.graphics.Typeface.NORMAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    EditText text;
    ArrayList<StyleSpan> spanArrayList;

    Button button;
    Button newSpanButton;
    Button flagButton;
    boolean flag = false;


    public NotificationFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_notification, container, false);

        //button 가져오기
        button = (Button) rootView.findViewById(R.id.aButton);
        newSpanButton = (Button) rootView.findViewById(R.id.newSpanButton);
        flagButton = (Button)rootView.findViewById(R.id.flagButton) ;
        flagButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder(text.getText());
                StyleSpan[] styleSpansArr = stringBuilder.getSpans(0,stringBuilder.length(), StyleSpan.class );
                int start = stringBuilder.getSpanStart(styleSpansArr[0]);
                int end = stringBuilder.getSpanEnd(styleSpansArr[0]);
                stringBuilder.removeSpan(styleSpansArr[0]);
                stringBuilder.setSpan(new StyleSpan(ITALIC),start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                text.setText(stringBuilder);



            }
        });

        //start와 end 위치를 띄워주는 버튼
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start;
                int end;
                SpannableStringBuilder stringBuilder;
                stringBuilder = new SpannableStringBuilder(text.getText());
                StyleSpan[] styleSpans = stringBuilder.getSpans(0, stringBuilder.length(),StyleSpan.class);


                //Toast.makeText(getContext(), styleSpans.length, Toast.LENGTH_SHORT).show();
                for(StyleSpan span: styleSpans){
                    start= stringBuilder.getSpanStart(span);
                    end = stringBuilder.getSpanEnd(span);
                    String string = "start : "+Integer.toString(start)+", end : " + Integer.toString(end);
                    text.append("\n"+string);
                }
            }
        });

        newSpanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpannableStringBuilder stringBuilder = new SpannableStringBuilder("\nabcdefg");


                stringBuilder.setSpan(new StyleSpan(BOLD),1, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.append(stringBuilder);
            }
        });

        //editText 가져오기
        text = (EditText) rootView.findViewById(R.id.editText);
        //text 글자 주기
        text.setText("가나다라마바사아자차카타파하");

        //spannableStringBuilder를 editText와 연결
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text.getText());
        //가나다라를 굵게 start: 0 , end : 4
        spannableStringBuilder.setSpan(new StyleSpan(BOLD),0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(NORMAL),5, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //자차카타를 빨간색으로 start : 8, end : 12
        spannableStringBuilder.setSpan(new StyleSpan(BOLD),8, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);


        text.addTextChangedListener(new TextWatcher() {

            int cursor;
            SpannableStringBuilder stringBuilder;
            StyleSpan[] styleSpans ;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if(flag){
                    return;
                }
                cursor = text.getSelectionStart();
                stringBuilder = new SpannableStringBuilder(text.getText());
                styleSpans = stringBuilder.getSpans(0, stringBuilder.length(), StyleSpan.class);
                int mStart, mEnd;
                StyleSpan style;
                for(StyleSpan span : styleSpans){
                    mStart = stringBuilder.getSpanStart(span);
                    mEnd = stringBuilder.getSpanEnd(span);
                    style = new StyleSpan(span.getStyle());
                    stringBuilder.removeSpan(span);

                    stringBuilder.setSpan(style, mStart,mEnd,Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                }


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(flag){
                    return;
                }
                String string = s.toString().substring(cursor,cursor+count-before);
                Log.d("string", string);
                Log.d("start", Integer.toString(start));
                Log.d("before", Integer.toString(before));
                Log.d("count", Integer.toString(count));
                stringBuilder.append(s, start, before);
                stringBuilder.setSpan(new StyleSpan(BOLD), cursor, cursor+s.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                flag = true;
                text.setText(stringBuilder);



            }

            @Override
            public void afterTextChanged(Editable s) {
                flag = false;
            }
        });
        return rootView;
    }

    //커서 위치가 바뀌면 inclusive_inclusive를 exclusive_exclusive로 바꿔야함.


    //프래그먼트가 액티비티에 올라올 때 호출되는 함수
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    //프래그먼트가 액티비티에서 사라질 때 호출되는 함수
    @Override
    public void onDetach() {
        super.onDetach();

    }



}

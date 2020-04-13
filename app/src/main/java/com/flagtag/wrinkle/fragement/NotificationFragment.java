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
import com.flagtag.wrinkle.SmartEditText;

import java.util.ArrayList;
import java.util.Arrays;

import static android.graphics.Typeface.BOLD;
import static android.graphics.Typeface.ITALIC;
import static android.graphics.Typeface.NORMAL;


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    SmartEditText text;
    ArrayList<StyleSpan> spanArrayList;
    TextView textView;
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
        textView = (TextView)rootView.findViewById(R.id.textView);
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
                    textView.append("\n"+string);
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
        text = (SmartEditText) rootView.findViewById(R.id.editText);
        //text 글자 주기
        text.setText("가나다라마바사아자차카타파하");

        //spannableStringBuilder를 editText와 연결
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text.getText());
        //가나다라를 굵게 start: 0 , end : 4
        spannableStringBuilder.setSpan(new StyleSpan(BOLD),0, 4, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.setSpan(new StyleSpan(NORMAL),5, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //자차카타를 빨간색으로 start : 8, end : 12
        spannableStringBuilder.setSpan(new StyleSpan(BOLD),8, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);


        /**
         * onSelectionChanged를 사용해서 cursor 위치가 바뀌었을 때
         * 이전 cursor가 있던 곳의 span을 inclusive inclusive에서 exclusive exclusive로 바꿔준다.
         *
         * beforeTextChanged에서 span들을 모두 받고 현재 커서 위치가 있는 span만 inclusive inclusive로 바꿔준다.
         * afterTextChanged에서 span들을 다시 그려준다.
         */


        text.addTextChangedListener(new TextWatcher() {

            int cursor;
            SpannableStringBuilder stringBuilder;
            StyleSpan[] styleSpans ;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                stringBuilder = new SpannableStringBuilder(text.getText());
                cursor = text.getSelectionStart();

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(flag){
                    return;
                }



                String string = s.toString().substring(cursor,cursor+count-before);

                Log.d("stringBuilder", stringBuilder.toString());
                stringBuilder.insert(cursor, string);
                StyleSpan[] styleSpans = stringBuilder.getSpans(0, stringBuilder.length(),StyleSpan.class);
                for(StyleSpan span : styleSpans){
                    int spanStart = stringBuilder.getSpanStart(span);
                    int spanEnd = stringBuilder.getSpanEnd(span);
                    int style = span.getStyle();

                    if(spanStart <= cursor && cursor <= spanEnd){
                        if(style != Typeface.BOLD){
                            stringBuilder.removeSpan(span);
                            stringBuilder.setSpan(new StyleSpan(style), spanStart, cursor, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            stringBuilder.setSpan(new StyleSpan(BOLD), cursor, cursor+count-before, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                            stringBuilder.setSpan(new StyleSpan(style), cursor+count-before, spanEnd, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }else{
                            stringBuilder.removeSpan(span);
                            stringBuilder.setSpan(new StyleSpan(BOLD), spanStart, spanEnd+cursor+count-before, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        }
                    }

                }


                flag = true;
                //cursor += count-before;
                cursor--;
                text.setText(stringBuilder);




            }

            @Override
            public void afterTextChanged(Editable s) {


                flag = false;
                text.setSelection(cursor);
            }
        });



        return rootView;
    }




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

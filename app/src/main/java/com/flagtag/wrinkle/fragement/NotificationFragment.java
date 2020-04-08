package com.flagtag.wrinkle.fragement;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.TypefaceSpan;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class NotificationFragment extends Fragment {

    EditText text;
    ArrayList<SpannableStringBuilder> spannableStringBuilderArrayList;

    Button button;
    Button newSpanButton;
    Button flagButton;
    Spannable spannable;

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
                StyleSpan[] styleSpansArr = spannableStringBuilderArrayList.get(0).getSpans(0,spannableStringBuilderArrayList.get(0).length(), StyleSpan.class );
                int start = spannableStringBuilderArrayList.get(0).getSpanStart(styleSpansArr[0]);
                int end = spannableStringBuilderArrayList.get(0).getSpanEnd(styleSpansArr[0]);
                spannableStringBuilderArrayList.get(0).removeSpan(styleSpansArr[0]);
                spannableStringBuilderArrayList.get(0).setSpan(new StyleSpan(ITALIC),start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                text.setText(spannableStringBuilderArrayList.get(0));
                for(int i=1; i<spannableStringBuilderArrayList.size(); i++){
                    text.append(spannableStringBuilderArrayList.get(i));
                }


            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int start;
                int end;
                spannable = text.getText();

                ArrayList<StyleSpan> styleSpans = new ArrayList<>();
                for(int i=0; i<spannableStringBuilderArrayList.size(); i++){
                    StyleSpan[] styleSpansArr = spannableStringBuilderArrayList.get(i).getSpans(0,spannableStringBuilderArrayList.get(i).length(), StyleSpan.class );
                    styleSpans.addAll(Arrays.asList(styleSpansArr));
                }



                //Toast.makeText(getContext(), styleSpans.length, Toast.LENGTH_SHORT).show();
                for(StyleSpan span: styleSpans){
                    start= spannable.getSpanStart(span);
                    end = spannable.getSpanEnd(span);
                    String string = "start : "+Integer.toString(start)+", end : " + Integer.toString(end);
                    text.append("\n"+string);
                }
            }
        });

        newSpanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("\nabcdefg");
                spannableStringBuilderArrayList.add(spannableStringBuilder);

                spannableStringBuilder.setSpan(new StyleSpan(BOLD),1, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                text.append(spannableStringBuilderArrayList.get(spannableStringBuilderArrayList.size()-1));
            }
        });

        //editText 가져오기
        text = (EditText) rootView.findViewById(R.id.editText);
        //text 글자 주기
        text.setText("가나다라마바사아자차카타파하");
        //arraylist 생성
        spannableStringBuilderArrayList = new ArrayList<>();
        //spannableStringBuilder를 editText와 연결
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(text.getText());
        spannableStringBuilderArrayList.add(spannableStringBuilder);
        //가나다라를 굵게 start: 0 , end : 4
        spannableStringBuilder.setSpan(new StyleSpan(BOLD),0, 0, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        //자차카타를 빨간색으로 start : 8, end : 12
        spannableStringBuilder.setSpan(new StyleSpan(BOLD),8, 12, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        text.setText(spannableStringBuilder, TextView.BufferType.SPANNABLE);

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

package com.flagtag.wrinkle.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.InputType;
import android.text.Spannable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.style.StyleSpan;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

public class WritingTextView extends WritingView {

    public EditText text;
    public Spannable spanText;
    public ArrayList<StyleSpan> styleSpanArr;


    public WritingTextView(Context context) {
        super(context);
        initView();
    }

    public WritingTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WritingTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }






    public void initView(){

        super.initView();
        Context context = getContext();

        mIsSelected = false;
        text = new EditText(context);
        styleSpanArr = new ArrayList<>();
        //styleSpanArr에 처음에 normal을 넣어준다.
        styleSpanArr.add(new StyleSpan(Typeface.NORMAL));
        //editText 안에 spannable을 만들어주는거
        spanText = (Spannable)text.getText();
        //처음에는 normal, inclusive-inclusive로
        spanText.setSpan(styleSpanArr.get(0), 0,0,Spannable.SPAN_INCLUSIVE_INCLUSIVE);


        ConstraintLayout.LayoutParams textViewLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        textViewLayoutParams.endToEnd =guideline_right.getId();
        textViewLayoutParams.startToStart = guideline_left.getId();
        textViewLayoutParams.topToTop = guideline_top.getId();
        textViewLayoutParams.bottomToTop = guideline_bottom.getId();
        //layout 설정
        text.setLayoutParams(textViewLayoutParams);
        //multiline 설정
        text.setInputType(InputType.TYPE_CLASS_TEXT|InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //gravity 기본 설정 위 왼쪽
        text.setGravity(Gravity.LEFT| Gravity.START);
        //text underline 없애기
        text.setBackgroundColor(Color.TRANSPARENT);




        background.addView(text);

    }



    public String getText(){
        String textString = text.getText().toString();
        return textString;
    }

    public void setMinLines(int num){
        text.setMinLines(num);
    }
    public void setHint(String string){
        text.setHint(string);
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener l) {
        text.setOnFocusChangeListener(l);
    }

    @Override
    public void unsetSelected() {
        super.unsetSelected();
        text.clearFocus();
    }

    public int isPositionInSpanArr(int cursorPosition){
        int start, end;
        for(StyleSpan span : styleSpanArr){
            //styleSpanArr 안에 span의 시작과 끝을 가져온다.
            start = spanText.getSpanStart(span);
            end = spanText.getSpanEnd(span);

            //만약 cursorPosition이 start와 end 사이에 있으면
            if(cursorPosition>=start && cursorPosition<= end) {
                //만약 그 span의 종류가 bold이면
                if (span.getStyle() == Typeface.BOLD) {
                    return Typeface.BOLD;
                } else if (span.getStyle() == Typeface.ITALIC) {
                    return Typeface.ITALIC;
                } else if (span.getStyle() == Typeface.BOLD_ITALIC) {
                    return Typeface.BOLD_ITALIC;
                } else if (span.getStyle() == Typeface.NORMAL) {
                    return Typeface.NORMAL;
                }
            }
        }
        return Typeface.NORMAL;
    }

    //현재 포지션을 포함하는 모든 stylespan의 styleSpanArr 안에서의 인덱스를 리턴한다.
    public ArrayList<Integer> spansIncludePosition(int cursorPosition){
        int start, end;
        ArrayList<Integer> indexes = new ArrayList<>();
        for(int i=0; i<styleSpanArr.size(); i++){
            StyleSpan span = styleSpanArr.get(i);
            start = spanText.getSpanStart(span);
            end = spanText.getSpanEnd(span);
            if(cursorPosition>=start && cursorPosition<= end){
                indexes.add(i);
            }

        }

        return indexes;
    }

    public void changeStyleSpan(ArrayList<Integer> indexes, int style, int cursorPosition){

    }
}

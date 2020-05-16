package com.flagtag.wrinkle.view;

import android.content.Context;
import android.graphics.Color;

import android.text.Editable;
import android.text.InputType;

import android.text.TextWatcher;
import android.util.AttributeSet;

import android.util.TypedValue;
import android.view.Gravity;

import android.widget.EditText;

import android.text.style.StyleSpan;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class WritingTextView extends WritingView {

    public EditText text;

    Editable editable;
    Context context;
    public Boolean isBold;



    public WritingTextView(Context context) {
        super(context);
        this.context = context;
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
        final Context context = getContext();

        mIsSelected = false;
        text = new EditText(context);

        editable = text.getEditableText();

        isBold = false;



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

        //크기 지정
        text.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);






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


    public void setTextChangeListner(TextWatcher textWatcher){
        text.addTextChangedListener(textWatcher);
    }

    public void clearFocus(){
        text.clearFocus();
    }


    public void requestTextFocus(){
        text.requestFocus();
    }

    public void toggleIsBold(){
        if(isBold){
            isBold = true;
        }else{
            isBold = false;
        }
    }


}

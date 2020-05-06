package com.flagtag.wrinkle.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Editable;
import android.text.InputType;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.inputmethod.BaseInputConnection;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.widget.EditText;
import android.widget.ImageView;
import android.text.style.StyleSpan;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.util.ArrayList;

import io.opencensus.trace.Span;

public class WritingTextView extends WritingView {

    public EditText text;
    ArrayList<SpanInfo> spanInfoArrayList;
    Editable editable;


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
        spanInfoArrayList = new ArrayList<>();
        editable = text.getEditableText();
        this.setStyleAt(0, Typeface.NORMAL);


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

        //text watcher 붙이기
        text.addTextChangedListener(new TextWatcher() {

            int lastCursor, curCursor;

            //모든 span을 나타낸다.
            StyleSpan[] styleSpans ;
            //현재의 inclusive span을 나타낸다.
            StyleSpan inclusiveSpan = null;
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d("onTextChanged editable",editable.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });




        background.addView(text);

    }



    public String getText(){
        String textString = text.getText().toString();
        return textString;
    }

    public void clearComposingText(int cursor){

        InputConnection inputConnection = text.onCreateInputConnection(new EditorInfo());
        inputConnection.commitText(editable.toString(), cursor);
        Log.e("text in clearComposing Text", text.getText().toString());
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

    public boolean isCursorInSpan(int cursor, int style){
        StyleSpan[] spans = editable.getSpans(0, editable.length(), StyleSpan.class);

        for(StyleSpan span : spans){
            int spanStart = editable.getSpanStart(span);
            int spanEnd = editable.getSpanEnd(span);
            int spanStyle = span.getStyle();
            if(spanStyle == style){
                if(spanStart<=cursor && cursor<=spanEnd){
                    return true;
                }
            }
        }

        return false;
    }

    public void setStyleAt(int cursor, int style){

        editable.setSpan(new StyleSpan(style), cursor, cursor, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

    }



    class SpanInfo{
        int style;
        int start;
        int end;
        int flag;

        public SpanInfo(int style, int start, int end,int flag) {
            this.style = style;
            this.start = start;
            this.end = end;
            this.flag = flag;
        }

        public int getStyle() {
            return style;
        }

        public int getStart() {
            return start;
        }

        public int getEnd() {
            return end;
        }

        public int getFlag() { return flag;}
    }
}

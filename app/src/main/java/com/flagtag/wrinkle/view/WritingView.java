package com.flagtag.wrinkle.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.Guideline;

import com.flagtag.wrinkle.R;

public class WritingView extends LinearLayout {

    public ConstraintLayout background;
    Guideline guideline_top;
    Guideline guideline_left;
    Guideline guideline_right;
    Guideline guideline_bottom;

    protected static final int[] mStates = { R.attr.state_on };
    protected boolean mIsSelected = true;

    public WritingView(Context context) {
        super(context);

    }

    public WritingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WritingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        int[] drawableStates =  super.onCreateDrawableState(extraSpace + mStates.length);
        //선택되었으면 mStates 안의 attribute들도 추가시킨다.
        if(mIsSelected){
            mergeDrawableStates(drawableStates, mStates);
        }
        return drawableStates;
    }

    //
    public void toggleSelected() {
        if ( mIsSelected ){
            mIsSelected = false;


        }else{
            mIsSelected = true;

        }
        //다시 화면에 그려주는 역할
        refreshDrawableState();
        invalidate();
    }

    public void unsetSelected(){
        mIsSelected = false;
        refreshDrawableState();
        invalidate();

    }

    public void initView(){
        String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inflaterService);
        View view = layoutInflater.inflate(R.layout.writing_view,this,false);

        view.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        //inflate한 view를 추가한다.
        addView(view);


        background = findViewById(R.id.background);
        guideline_top = findViewById(R.id.guideline_top);
        guideline_left = findViewById(R.id.guideline_left);
        guideline_right = findViewById(R.id.guideline_right);
        guideline_bottom = findViewById(R.id.guideline_bottom);

        //backgroundResource를 selector로 지정해준다.
        //반드시 this에 해줘야하지 view에 하려고 하면 안됨.
        this.setBackgroundResource(R.drawable.wrting_imageview_selector);
    }




}

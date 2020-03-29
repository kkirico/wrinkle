package com.flagtag.wrinkle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.flagtag.wrinkle.R;


public class BoldButton extends LinearLayout {

    protected static final int[] mStates = { R.attr.state_on };
    protected boolean mIsSelected = true;

    public BoldButton(Context context) {
        super(context);
    }

    public BoldButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BoldButton(Context context, AttributeSet attrs, int defStyleAttr) {
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

    public void initView(){
        String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inflaterService);
        View view = layoutInflater.inflate(R.layout.bold_button,this, false);



        //backgroundResource를 selector로 지정해준다.
        //반드시 this에 해줘야하지 view에 하려고 하면 안됨.
        this.setBackgroundResource(R.drawable.bold_button_selector);
    }
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
}

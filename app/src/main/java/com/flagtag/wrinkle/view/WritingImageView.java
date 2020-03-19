package com.flagtag.wrinkle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WritingImageView extends LinearLayout {

    public ConstraintLayout background;
    ImageView imageView;
    EditText text;

    private static final int[] mStates = { R.attr.state_on };
    private boolean mIsSelected = true;

    public WritingImageView(Context context) {
        super(context);
        initView();
    }

    public WritingImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public WritingImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
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


    public void setSelected() {
        if ( mIsSelected ){
            mIsSelected = false;
            if(text.getText().toString().isEmpty()){
                background.removeView(text);
            }

        }else{
            mIsSelected = true;
            background.addView(text);
        }
        //다시 화면에 그려주는 역할
        refreshDrawableState();
        invalidate();
    }

    public void initView(){
        String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inflaterService);
        View view = layoutInflater.inflate(R.layout.writing_imageview,this,false);

        //inflate한 view를 추가한다.
        addView(view);

        background = findViewById(R.id.background);
        imageView = findViewById(R.id.writing_image);
        text = findViewById(R.id.writing_text);


        //backgroundResource를 selector로 지정해준다.
        //반드시 this에 해줘야하지 view에 하려고 하면 안됨.
        this.setBackgroundResource(R.drawable.wrting_imageview_selector);
    }

    public void setImageView(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }


}

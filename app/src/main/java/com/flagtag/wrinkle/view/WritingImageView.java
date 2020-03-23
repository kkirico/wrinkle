package com.flagtag.wrinkle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.InputType;
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
import androidx.constraintlayout.widget.Guideline;

import com.flagtag.wrinkle.R;

public class WritingImageView extends WritingView {

    ImageView imageView;
    EditText text;



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




    public void setSelected() {
        super.setSelected();
        if ( !mIsSelected ){

            if(text.getText().toString().isEmpty()){
                background.removeView(text);
            }

        }else{
            mIsSelected = true;
            background.addView(text);
        }

    }

    public void initView(){

        super.initView();
        Context context = getContext();
        imageView = new ImageView(context);
        text = new EditText(context);

        ConstraintLayout.LayoutParams imageViewLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, ConstraintLayout.LayoutParams.WRAP_CONTENT);
        imageViewLayoutParams.endToEnd =guideline_right.getId();
        imageViewLayoutParams.startToStart = guideline_left.getId();
        imageViewLayoutParams.topToTop = guideline_top.getId();
        imageViewLayoutParams.bottomToTop = guideline_bottom.getId();



        imageView.setLayoutParams(imageViewLayoutParams);
        imageView.setAdjustViewBounds(true);


        background.addView(imageView);




    }

    public void setImageView(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }


}

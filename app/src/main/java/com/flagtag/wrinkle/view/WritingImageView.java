package com.flagtag.wrinkle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WritingImageView extends WritingView {

    ImageView imageView;
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

    public void initView(){
        super.initView();
        Context context = getContext();
        imageView = new ImageView(context);

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

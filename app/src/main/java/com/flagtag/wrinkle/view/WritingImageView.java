package com.flagtag.wrinkle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.flagtag.wrinkle.R;

public class WritingImageView extends ConstraintLayout {

    ConstraintLayout background;
    ImageView imageView;
    EditText text;

    private static final int[] mStates = { R.attr.state_notset, R.attr.state_on};
    private int mStateIndex = 0;
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
        String inflaterService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater layoutInflater = (LayoutInflater)getContext().getSystemService(inflaterService);
        View view = layoutInflater.inflate(R.layout.writing_imageview,this,false);
        addView(view);

        background = findViewById(R.id.background);
        imageView = findViewById(R.id.writing_image);
        text = findViewById(R.id.writing_text);
    }

    public void setImageView(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }

    @Override
    public void setOnClickListener(@Nullable OnClickListener l) {
        super.setOnClickListener(l);
    }
}

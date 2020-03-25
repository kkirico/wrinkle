package com.flagtag.wrinkle.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class WritingVideoView extends WritingView {

    private VideoView videoView;
    private MediaController mediaController;
    private Bitmap thumbnail;
    public WritingVideoView(Context context) {
        super(context);
        initView();
    }

    public WritingVideoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WritingVideoView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }


    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        return super.onCreateDrawableState(extraSpace);
    }

    @Override
    public void initView() {
        super.initView();


        Context context = getContext();
        videoView = new VideoView(context);
        mediaController = new MediaController(context);
        videoView.setMediaController(mediaController);
        

        ConstraintLayout.LayoutParams videoViewLayoutParams = new ConstraintLayout.LayoutParams(ConstraintLayout.LayoutParams.MATCH_CONSTRAINT, 700);
        videoViewLayoutParams.endToEnd =guideline_right.getId();
        videoViewLayoutParams.startToStart = guideline_left.getId();
        videoViewLayoutParams.topToTop = guideline_top.getId();
        videoViewLayoutParams.bottomToTop = guideline_bottom.getId();



        videoView.setLayoutParams(videoViewLayoutParams);



        background.addView(videoView);

    }

    public void setVideoView(Uri uri){
        //비디오 만들면 바로 시작함
        videoView.setVideoURI(uri);
        videoView.seekTo(1);

    }

    public void requestVideoFocus(){
        videoView.requestFocus();
    }
    public void start(){

        videoView.start();

    }




}

package com.flagtag.wrinkle;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

public class TagLayout extends LinearLayout {
    ConstraintLayout tagItemContainer;
    TextView tagItemTextView;
    Button tagRemoveButton;
    public EditText tagItemEditText;
    Boolean selected = false;

    public void initView(){
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.tag_layout, this, false);
        addView(v);

        tagItemContainer = findViewById(R.id.tag_item_container);
        tagItemContainer.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                //선택되어있지 않으면
                if(!selected){
                    tagRemoveButton.setVisibility(VISIBLE);
                    selected = true;
                }else{
                    clear();
                }
            }
        });
        tagItemEditText = findViewById(R.id.tag_item_edit_text);
        tagItemEditText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if(!hasFocus){
                    setText(tagItemEditText.getText().toString());
                    tagItemEditText.setVisibility(GONE);
                    tagItemTextView.setVisibility(VISIBLE);
                }
            }
        });
        tagItemTextView = findViewById(R.id.tag_item_text_view);
        tagRemoveButton = findViewById(R.id.tag_item_remove_button);


    }

    public void setText(String text){
        tagItemTextView.setText(text);
    }

    private void setTypeArray(TypedArray typedArray) {
        String text = typedArray.getString(R.styleable.tag_layout_text);
        setText(text);
    }

    private void getAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.tag_layout);
        setTypeArray(typedArray);
    }

    private void getAttrs(AttributeSet attrs, int defStyle) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.tag_layout, defStyle, 0);
        setTypeArray(typedArray);
    }



    public TagLayout(Context context) {
        super(context);
        initView();
    }

    public TagLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
        getAttrs(attrs);
    }

    public TagLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
        getAttrs(attrs, defStyleAttr);
    }

    public TagLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView();

    }

    public void clear(){
        tagRemoveButton.setVisibility(GONE);
        selected = false;
    }
}

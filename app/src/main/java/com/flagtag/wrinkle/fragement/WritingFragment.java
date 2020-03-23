package com.flagtag.wrinkle.fragement;


import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.view.WritingImageView;
import com.flagtag.wrinkle.activity.MainActivity;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.InputStream;
import java.util.ArrayList;


public class WritingFragment extends Fragment {

    FirebaseFirestore db;
    Toolbar toolbar;
    LinearLayout writing_content_container;
    ArrayList<View> contentArray;
    EditText writing;
    MainActivity activity;


    private static final int SELECT_IMAGE = 1;
    //현재 선택된 아이템을 뜻함.
    private static int CUR_INDEX = 0;

    private static int DEFAULT_MENU =0;
    private static int IMAGE_MENU =1;
    private static int VEDIO_MENU =2;
    private static int QUOTE_MENU =3;
    private static int LOCATION_MENU =4;
    private static int DIVISION_MENU =5;

    private int menu_page = 0;
    private int currentSelectedItem =0;

    public WritingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_writing, container, false);
        activity = (MainActivity)getActivity();

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        writing_content_container = rootView.findViewById(R.id.content_container);
        writing = rootView.findViewById(R.id.writing_content1);
        writing.setOnFocusChangeListener(focusChangeListener);
        contentArray = new ArrayList<View>();
        contentArray.add(writing);

        toolbar = rootView.findViewById(R.id.writing_fragment_toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.image_button){

                    //이미지 선택
                    startToast("이미지 선택");
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, SELECT_IMAGE);
                }else if(item.getItemId() == R.id.vidoe_button){
                    //동영상 선택
                }else if(item.getItemId() == R.id.location_button){
                    //위치 선택
                }else if(item.getItemId() == R.id.quote_button){
                    //인용 버튼
                }else if(item.getItemId() == R.id.division_line_button){
                    //구분선 버튼
                }else if(item.getItemId() == R.id.more_button){
                    //더보기 버튼
                    //아직 더보기 버튼을 어떻게 사용할지는 정하지 않았음
                     Menu menu = toolbar.getMenu();
                    if(menu_page==DEFAULT_MENU && menu_page != currentSelectedItem){

                        menu.setGroupVisible(R.id.default_group, false);
                        menu_page = currentSelectedItem;
                    }else{
                        menu.setGroupVisible(R.id.default_group, true);
                        menu_page = DEFAULT_MENU;
                    }



                }
                return false;
            }
        });





        return rootView;
    }


    //프래그먼트가 액티비티에 올라올 때 호출되는 함수
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    //프래그먼트가 액티비티에서 사라질 때 호출되는 함수
    @Override
    public void onDetach() {
        super.onDetach();

    }

    private void startToast(String msg){
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //이미지를 선택했을 때
        if(requestCode==SELECT_IMAGE){
            try {
                // 선택한 이미지에서 비트맵 생성
                InputStream in = activity.getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                // Custom WritingImageView 생성
                final WritingImageView imageView = new WritingImageView(activity);
                //Custom WritingImageView 안의 imageView 안에 이미지 설정을 해준다.
                imageView.setImageView(img);
                //WritingImageView의 onClickListener를 만든다.
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        CUR_INDEX = writing_content_container.indexOfChild(v);
                        startToast(Integer.toString(CUR_INDEX));
                        //WritingImageView가 선택되었을 때 setSelected() 함수 호출
                        imageView.setSelected();
                        currentSelectedItem = IMAGE_MENU;

                    }
                });
                //container에 WritingImageView를 넣어준다.
                writing_content_container.addView(imageView,CUR_INDEX+1);


                //현재 인덱스의 뷰를 불러온다.
                View cur_view = writing_content_container.getChildAt(CUR_INDEX);
                //현재 인덱스의 뷰가 텍스트일 때,
                if(cur_view instanceof EditText){
                    String text = ((EditText) cur_view).getText().toString();
                    //텍스트에 아무 것도 안 쓰여있었으면,
                    if(text.equals("")){
                        //그 텍스트 뷰를 삭제한다.
                        writing_content_container.removeView(cur_view);
                        CUR_INDEX--;
                    }else{
                        //텍스트뷰에 글이 쓰여있었으면 최소 라인 수를 없앤다.
                        ((EditText) cur_view).setMinLines(0);
                    }
                    //그 밑에 텍스트뷰를 넣어준다.
                    EditText editText = new EditText(activity);
                    editText.setOnFocusChangeListener(focusChangeListener);
                    editText.setMinLines(3);
                    writing_content_container.addView(editText, CUR_INDEX+2, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));

                }
                currentSelectedItem = IMAGE_MENU;
                startToast(Integer.toString(CUR_INDEX));

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean gainFocus) {
            if(gainFocus){
                CUR_INDEX = writing_content_container.indexOfChild(view);
                currentSelectedItem = DEFAULT_MENU;
                startToast(Integer.toString(CUR_INDEX));

            }
        }

    };


}

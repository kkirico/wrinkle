package com.flagtag.wrinkle.fragement;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.view.WritingImageView;
import com.flagtag.wrinkle.activity.MainActivity;
import com.flagtag.wrinkle.view.WritingTextView;
import com.flagtag.wrinkle.view.WritingVideoView;
import com.flagtag.wrinkle.view.WritingView;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;


public class WritingFragment extends Fragment {

    FirebaseFirestore db;
    Toolbar toolbar;
    LinearLayout writing_content_container;
    WritingTextView writing;
    MainActivity activity;


    private static final int SELECT_IMAGE = 1;
    private static final int SELECT_VIDEO = 2;
    //현재 선택된 아이템을 뜻함.
    private static int CUR_INDEX = 0;

    private static int DEFAULT_MENU =0;
    private static int IMAGE_MENU =1;
    private static int VEDIO_MENU =2;
    private static int QUOTE_MENU =3;
    private static int LOCATION_MENU =4;
    private static int DIVISION_MENU =5;
    private static int TEXT_MENU = 6;

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
        writing = new WritingTextView(activity);
        writing.setMinLines(20);
        writing.setOnFocusChangeListener(focusChangeListener);
        writing.setHint("무슨 일이 있었나요?");
        writing_content_container.addView(writing);

        toolbar = rootView.findViewById(R.id.writing_fragment_toolbar);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int numberofItem = writing_content_container.getChildCount();
                if(item.getItemId() == R.id.image_button){

                    //이미지 선택
                    startToast("이미지 선택");
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, SELECT_IMAGE);
                }else if(item.getItemId() == R.id.vidoe_button){
                    //동영상 선택
                    startToast("동영상 선택");
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, SELECT_VIDEO);
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
                    if(menu_page ==DEFAULT_MENU && menu_page != currentSelectedItem){
                        changeToolbarMenu(toolbar.getMenu(),true);
                    }else{
                        changeToolbarMenu(toolbar.getMenu(),false);
                    }

                }else if(item.getItemId() == R.id.up_button){

                    if(CUR_INDEX ==0 || CUR_INDEX == numberofItem-1){
                        return false;
                    }else{
                        //선택한 아이템을 위로 올린다.

                        WritingView aboveView = (WritingView) writing_content_container.getChildAt(CUR_INDEX-1);
                        writing_content_container.removeView(aboveView);
                        writing_content_container.addView(aboveView, CUR_INDEX);
                        CUR_INDEX--;
                    }

                }else if(item.getItemId() == R.id.down_button){

                    if(CUR_INDEX == numberofItem-1|| CUR_INDEX == numberofItem-2){
                        return false;
                    }else{
                        WritingView curView = (WritingView) writing_content_container.getChildAt(CUR_INDEX);
                        writing_content_container.removeView(curView);
                        writing_content_container.addView(curView, ++CUR_INDEX);

                    }
                }else if(item.getItemId() == R.id.delete_button){
                    if(numberofItem==1 ||CUR_INDEX == numberofItem-1){
                        return false;
                    }
                    WritingView curView = (WritingView) writing_content_container.getChildAt(CUR_INDEX);
                    writing_content_container.removeView(curView);

                    changeToolbarMenu(toolbar.getMenu(),false);


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
        if (requestCode == SELECT_IMAGE) {
            try {
                // 선택한 이미지에서 비트맵 생성
                InputStream in = activity.getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                // Custom WritingImageView 생성
                final WritingImageView imageView = new WritingImageView(activity);
                //Custom WritingImageView 안의 imageView 안에 이미지 설정을 해준다.
                imageView.setImageView(img);
                addViewToContainer(imageView, IMAGE_MENU);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else if (requestCode == SELECT_VIDEO) {
            Uri videoURI = data.getData();
            if (videoURI != null) {
                String videoPath = getPath(videoURI);

                //WritingVideoView 생성
                WritingVideoView videoView = new WritingVideoView(activity);
                videoView.setVideoView(videoURI);
                videoView.requestFocus();


                addViewToContainer(videoView, VEDIO_MENU);
            }
        }
    }




    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean gainFocus) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);

            if (gainFocus) {

                WritingView curWritingView = (WritingView) (view.getParent().getParent());
                CUR_INDEX = writing_content_container.indexOfChild(curWritingView);

                unsetOtherViews(curWritingView);
                curWritingView.toggleSelected();

                currentSelectedItem = TEXT_MENU;
                changeToolbarMenu(toolbar.getMenu(),true);
                startToast(Integer.toString(CUR_INDEX));

            }else{
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        }

    };

    //매개변수로 주어진 view를 제외하고 writing_content_container 안의 모든 view를 unset한다.
    private void unsetOtherViews (View view){
        int number = writing_content_container.getChildCount();
        for (int i = 0; i < number; i++) {
            if (i != CUR_INDEX) {
                WritingView writingView = (WritingView) writing_content_container.getChildAt(i);
                writingView.unsetSelected();
            }
        }
    }

    private void changeToolbarMenu (Menu menu,boolean set){

        //menu_page : 현재의 메뉴페이지
        //currentSelectedItem : 현재 선택된 아이템
        if(set){
            menu.setGroupVisible(R.id.move_group, true);
            menu.setGroupVisible(R.id.text_group, false);
            menu.setGroupVisible(R.id.default_group, false);
            if(currentSelectedItem== TEXT_MENU) {
                menu.setGroupVisible(R.id.text_group, true);
            }
            menu_page = currentSelectedItem;
        }else{
            menu.setGroupVisible(R.id.move_group, false);
            menu.setGroupVisible(R.id.text_group, false);
            menu.setGroupVisible(R.id.default_group, true);
            menu_page = DEFAULT_MENU;
        }


    }

    private String getPath (Uri uri){
        String[] projection = {MediaStore.Video.Media.DATA};
        Cursor cursor = activity.getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL
            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } else
            return null;
    }

    private void addViewToContainer ( final WritingView view, final int type){
        //WritingImageView의 onClickListener를 만든다.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int PREVIOUS = CUR_INDEX;
                CUR_INDEX = writing_content_container.indexOfChild(v);

                startToast(Integer.toString(CUR_INDEX));
                //WritingImageView가 선택되었을 때 setSelected() 함수 호출
                view.toggleSelected();
                unsetOtherViews(view);

                currentSelectedItem = type;
                if(CUR_INDEX == PREVIOUS){
                    changeToolbarMenu(toolbar.getMenu(), false);
                }else{
                    changeToolbarMenu(toolbar.getMenu(), true);
                }


            }
        });
        //container에 WritingImageView를 넣어준다.
        writing_content_container.addView(view, CUR_INDEX + 1);


        //현재 인덱스의 뷰를 불러온다.
        View cur_view = writing_content_container.getChildAt(CUR_INDEX);
        //현재 인덱스의 뷰가 텍스트일 때,
        if (cur_view instanceof WritingTextView) {
            String text = ((WritingTextView) cur_view).getText().toString();
            //텍스트에 아무 것도 안 쓰여있었으면,
            if (text.equals("")) {
                //그 텍스트 뷰를 삭제한다.
                writing_content_container.removeView(cur_view);


            } else {
                //텍스트뷰에 글이 쓰여있었으면 최소 라인 수를 없앤다.
                ((WritingTextView) cur_view).setMinLines(0);
                CUR_INDEX++;
            }
            //그 밑에 텍스트뷰를 넣어준다.
            WritingTextView editText = new WritingTextView(activity);
            editText.setOnFocusChangeListener(focusChangeListener);
            editText.setMinLines(3);
            writing_content_container.addView(editText, CUR_INDEX + 1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        }else{
            CUR_INDEX++;
        }

        currentSelectedItem = type;
        //추가한 뷰 빼고 다른 뷰의 focus는 삭제한다.
        unsetOtherViews(view);
        changeToolbarMenu(toolbar.getMenu(),true);
        startToast(Integer.toString(CUR_INDEX));

    }
}

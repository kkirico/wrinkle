package com.flagtag.wrinkle.fragement;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.flagtag.wrinkle.MemberInfo;
import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.PostInfo;
import com.flagtag.wrinkle.TagLayout;
import com.flagtag.wrinkle.activity.UserSelectActivity;
import com.flagtag.wrinkle.adapter.TaggedUserAdapter;
import com.flagtag.wrinkle.view.WritingImageView;
import com.flagtag.wrinkle.activity.MainActivity;
import com.flagtag.wrinkle.view.WritingTextView;
import com.flagtag.wrinkle.view.WritingVideoView;
import com.flagtag.wrinkle.view.WritingView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.nex3z.flowlayout.FlowLayout;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;



public class WritingFragment extends Fragment {

    private static final String TAG = "Write Post Activity";

    private FirebaseUser user;

    FirebaseFirestore db;
    Toolbar toolbar;
    //제목을 적는 edittext
    EditText titleEditText;
    //writingView가 들어가는 linearlayout
    LinearLayout writing_content_container;
    //처음에 들어가있는 writingTextView
    WritingTextView writing;
    //프레그먼트가 올라가있는 메인액티비티
    MainActivity activity;




    //현재 이미지가 선택되어있다는 것을 의미
    private static final int SELECT_IMAGE = 1;
    private static final int ADD_TAGGED_USER = 2;
    //현재 선택된 writingView의 index를 뜻함.
    //이것으로 n 번째의 아이템을 선택할 수 있음
    private static int CUR_INDEX = 0;

    //현재의 메뉴가 무엇인지를 뜻함
    //이것에 따라서 툴바의 메뉴가 변함
    private static int DEFAULT_MENU = 0;
    private static int IMAGE_MENU = 1;
    private static int TEXT_MENU = 2;



    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;
    private RelativeLayout loaderLayout;

    private int pathCount=0;
    private int successCount;

    private int menu_page = 0;
    private int currentSelectedItem = 0;
    private boolean BOLD_BUTTON_CHECKED = false;

    //태그된 유저 리사이클러뷰에서 사용되는 어뎁터
    private TaggedUserAdapter taggedUserAdapter;
    //태그된 유저 리사이클러뷰
    private RecyclerView taggedUserRecyclerView;
    //태그된 유저를 추가하는 버튼
    private Button addTaggedUserButton;

    MemberInfo memberInfo = MemberInfo.getInstance();

    private static final int MAX_YEAR = 2021;
    private static final int MIN_YEAR = 1960;
    public Calendar cal = Calendar.getInstance();
    NumberPicker yearPicker;
    NumberPicker monthPicker;
    NumberPicker dayPicker;
    NumberPicker seasonPicker;

    FlowLayout tagContainer;

    TextWatcher textWatcher;
    public WritingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_writing, container, false);
        activity = (MainActivity) getActivity();
        parent = rootView.findViewById(R.id.content_container);
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        rootView.findViewById(R.id.check).setOnClickListener(onClickListener);
        titleEditText = rootView.findViewById(R.id.title);
        //contentEditText = rootView.findViewById(R.id.contentEditText);

        //addtaggedUserButton
        addTaggedUserButton = rootView.findViewById(R.id.add_tagged_user_button);
        addTaggedUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), UserSelectActivity.class);
                //이미 선택한 적이 있다면 그 리스트를 보낸다.
                if(taggedUserAdapter.getItemCount() != 0 ){
                    intent.putStringArrayListExtra("taggedUsers",taggedUserAdapter.getItems());

                }



                startActivityForResult(intent,ADD_TAGGED_USER);
            }
        });

        taggedUserRecyclerView = rootView.findViewById(R.id.tagged_user_recyclerview);

        taggedUserAdapter = new TaggedUserAdapter();

        taggedUserRecyclerView.setAdapter(taggedUserAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(activity.getApplicationContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        taggedUserRecyclerView.setLayoutManager(linearLayoutManager);


        writing_content_container = rootView.findViewById(R.id.content_container);
        writing = new WritingTextView(activity);
        writing.setMinLines(4);
        writing.setOnFocusChangeListener(focusChangeListener);
        writing.setHint("무슨 일이 있었나요?");
        writing_content_container.addView(writing);
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()==0){
                    return;
                }
                char character = s.charAt(start+count-1);
                if(character == '\n'){
                    WritingTextView curWritingTextView = (WritingTextView) writing_content_container.getChildAt(CUR_INDEX);
                    String string = s.toString();
                    curWritingTextView.text.setText(string.substring(0, string.length()-1));
                    curWritingTextView.clearFocus();
                    curWritingTextView.text.setMinLines(0);
                    ConstraintLayout.LayoutParams layoutParams = new ConstraintLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                    layoutParams.setMargins(0,0,0,32);

                    curWritingTextView.text.setLayoutParams(layoutParams);
                    WritingTextView writingTextView = new WritingTextView(activity);

                    writingTextView.setOnFocusChangeListener(focusChangeListener);
                    writingTextView.setTextChangeListner(this);
                    writing_content_container.addView(writingTextView,CUR_INDEX+1);
                    //선택된 것 말고 나머지 unset
                    unsetOtherViews(writingTextView);

                    writingTextView.requestTextFocus();
                    writingTextView.text.setCursorVisible(true);

                    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(writingTextView.text, InputMethodManager.SHOW_IMPLICIT);

                    //지금 선택된 아이템이 텍스트 메뉴라고 해주기
                    currentSelectedItem = TEXT_MENU;
                    changeToolbarMenu(toolbar.getMenu(),true);

                    if (BOLD_BUTTON_CHECKED) {

                        toolbar.getMenu().findItem(R.id.bold_button).setChecked(true);
                        toolbar.getMenu().findItem(R.id.bold_button).setIconTintList(ColorStateList.valueOf(Color.RED));


                    } else {
                        toolbar.getMenu().findItem(R.id.bold_button).setChecked(false);
                        toolbar.getMenu().findItem(R.id.bold_button).setIconTintList(null);


                    }


                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0 && CUR_INDEX !=0){
                    View cur_view = writing_content_container.getChildAt(CUR_INDEX);
                    writing_content_container.removeView(cur_view);


                    View writingView = writing_content_container.getChildAt(CUR_INDEX-1);
                    if(writingView instanceof WritingTextView){
                        WritingTextView writingTextView = (WritingTextView) writingView;
                        writingTextView.requestTextFocus();
                        writingTextView.text.setCursorVisible(true);
                        writingTextView.text.setSelection(writingTextView.text.getText().length());

                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(writingTextView.text, InputMethodManager.SHOW_IMPLICIT);
                    }

                    //CUR_INDEX--;
                }
            }
        };
        writing.setTextChangeListner(textWatcher);

        loaderLayout = rootView.findViewById(R.id.loaderLayout);
        monthPicker = rootView.findViewById(R.id.picker_month);
        yearPicker = rootView.findViewById(R.id.picker_year);
        dayPicker = rootView.findViewById(R.id.picker_day);
        seasonPicker = rootView.findViewById(R.id.picker_season);

        int year = cal.get(Calendar.YEAR);
        yearPicker.setMinValue(MIN_YEAR);
        yearPicker.setMaxValue(MAX_YEAR);
        monthPicker.setMinValue(1);
        monthPicker.setMaxValue(12);
        dayPicker.setMinValue(1);
        dayPicker.setMaxValue(31);
        seasonPicker.setMinValue(0);
        seasonPicker.setMaxValue(3);
        seasonPicker.setDisplayedValues( new String[] {"Summer", "Autumn", "Winter","Spring"});

        String pickedYear = String.valueOf(yearPicker.getValue());
        String pickedMonth = String.valueOf(monthPicker.getValue());
        String pickedDay = String.valueOf(dayPicker.getValue());



        yearPicker.setValue(year);
        monthPicker.setValue(cal.get(Calendar.MONTH) + 1);
        dayPicker.setValue(cal.get(Calendar.DATE));



        toolbar = rootView.findViewById(R.id.writing_fragment_toolbar);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int numberofItem = writing_content_container.getChildCount();
                if (item.getItemId() == R.id.image_button) {
                    startToast("이미지 선택");
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, SELECT_IMAGE);
                    //구분선 버튼
                } else if (item.getItemId() == R.id.more_button) {
                    //더보기 버튼
                    //아직 더보기 버튼을 어떻게 사용할지는 정하지 않았음
                    Menu menu = toolbar.getMenu();
                    if (menu_page == DEFAULT_MENU && menu_page != currentSelectedItem) {
                        changeToolbarMenu(toolbar.getMenu(), true);
                    } else {
                        changeToolbarMenu(toolbar.getMenu(), false);
                    }

                } else if (item.getItemId() == R.id.up_button) {

                    if (numberofItem == 1) {
                        return false;
                    }
                    WritingView curView = (WritingView) writing_content_container.getChildAt(CUR_INDEX);
                    writing_content_container.removeView(curView);
                    writing_content_container.addView(curView, --CUR_INDEX);

                } else if (item.getItemId() == R.id.down_button) {

                    if ( CUR_INDEX == numberofItem - 1) {
                        return false;
                    } else {
                        WritingView curView = (WritingView) writing_content_container.getChildAt(CUR_INDEX);
                        writing_content_container.removeView(curView);
                        writing_content_container.addView(curView, ++CUR_INDEX);

                    }
                } else if (item.getItemId() == R.id.delete_button) {
                    if (numberofItem == 1 || CUR_INDEX == numberofItem - 1) {
                        return false;
                    }
                    WritingView curView = (WritingView) writing_content_container.getChildAt(CUR_INDEX);
                    writing_content_container.removeView(curView);
                    changeToolbarMenu(toolbar.getMenu(), false);
                } else if (item.getItemId() == R.id.bold_button) {


                    WritingTextView curView = (WritingTextView) writing_content_container.getChildAt(CUR_INDEX);
                    BOLD_BUTTON_CHECKED = curView.isBold;
                    //not bold ->bold
                    if (!BOLD_BUTTON_CHECKED) {

                        item.setChecked(true);
                        item.setIconTintList(ColorStateList.valueOf(Color.RED));
                        curView.toggleIsBold();
                        startToast("boldbutton set");



                    } else {
                        item.setChecked(false);
                        item.setIconTintList(null);
                        BOLD_BUTTON_CHECKED = false;
                        curView.toggleIsBold();
                        startToast("boldbutton unset");

                    }
                    activity.invalidateOptionsMenu();

                }
                return false;
            }
        });

        tagContainer = rootView.findViewById(R.id.tags_container);
        tagContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TagLayout tagLayout = new TagLayout(getContext());
                tagLayout.tagItemEditText.requestFocus();
                InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(tagLayout.tagItemEditText, InputMethodManager.SHOW_IMPLICIT);
                tagContainer.addView(tagLayout);
            }
        });


        return rootView;
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check:
                    try {
                        storageUploader();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
    };

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



    private void startToast(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_CANCELED) {
            return;
        }
        //이미지를 선택했을 때
        if (requestCode == SELECT_IMAGE) {
            try {
                // 선택한 이미지에서 비트맵 생성
                pathList.add("0" + data.getData().toString());
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
        //UserSelectActivity에서 돌아오는 경우
        else if(requestCode == ADD_TAGGED_USER){
            ArrayList<String> userList = (ArrayList<String>) data.getSerializableExtra("users");

            taggedUserAdapter.claer();
            taggedUserAdapter.addItems(userList);

            taggedUserAdapter.notifyDataSetChanged();
        }

//        } else if (requestCode == SELECT_VIDEO) {
//            Uri videoURI = data.getData();
//            if (videoURI != null) {
//
//                pathList.add("1"+data.getData().toString());
//
//                //WritingVideoView 생성
//                WritingVideoView videoView = new WritingVideoView(activity);
//                videoView.setVideoView(videoURI);
//                videoView.requestFocus();
//                addViewToContainer(videoView, VEDIO_MENU);
//            }
//        }
    }


    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean gainFocus) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            MenuItem boldButton = toolbar.getMenu().findItem(R.id.bold_button);
            if (gainFocus) {
                //지금 선택된 것 인덱스 찾기
                WritingView curWritingView = (WritingView) (view.getParent().getParent());
                CUR_INDEX = writing_content_container.indexOfChild(curWritingView);
                //선택된 것 말고 나머지 unset
                unsetOtherViews(curWritingView);
                //지금 선택된것 선택되었다고 토글 해주기
                curWritingView.toggleSelected();
                //지금 선택된 아이템이 텍스트 메뉴라고 해주기
                currentSelectedItem = TEXT_MENU;
                changeToolbarMenu(toolbar.getMenu(),true);

                BOLD_BUTTON_CHECKED = ((WritingTextView)curWritingView).isBold;

                if (BOLD_BUTTON_CHECKED) {

                    toolbar.getMenu().findItem(R.id.bold_button).setChecked(true);
                    toolbar.getMenu().findItem(R.id.bold_button).setIconTintList(ColorStateList.valueOf(Color.RED));


                } else {
                    toolbar.getMenu().findItem(R.id.bold_button).setChecked(false);
                    toolbar.getMenu().findItem(R.id.bold_button).setIconTintList(null);


                }

            } else {
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

            }
        }

    };

    //매개변수로 주어진 view를 제외하고 writing_content_container 안의 모든 view를 unset한다.
    private void unsetOtherViews(View view) {
        int number = writing_content_container.getChildCount();
        for (int i = 0; i < number; i++) {
            if (i != CUR_INDEX) {
                WritingView writingView = (WritingView) writing_content_container.getChildAt(i);
                writingView.unsetSelected();
            }
        }
    }

    private void changeToolbarMenu(Menu menu, boolean set) {
        //menu_page : 현재의 메뉴페이지
        //currentSelectedItem : 현재 선택된 아이템
        if (set) {
            menu.setGroupVisible(R.id.move_group, true);
            menu.setGroupVisible(R.id.text_group, false);
            menu.setGroupVisible(R.id.default_group, false);
            if (currentSelectedItem == TEXT_MENU) {
                menu.setGroupVisible(R.id.text_group, true);
            }
            menu_page = currentSelectedItem;
        } else {
            menu.setGroupVisible(R.id.move_group, false);
            menu.setGroupVisible(R.id.text_group, false);
            menu.setGroupVisible(R.id.default_group, true);
            menu_page = DEFAULT_MENU;
        }


    }


    private void addViewToContainer(final WritingView view, final int type) {
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
                if (CUR_INDEX == PREVIOUS) {
                    changeToolbarMenu(toolbar.getMenu(), false);
                } else {
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
                ((WritingTextView) cur_view).setMinLines(3);
                CUR_INDEX++;
            }
            //그 밑에 텍스트뷰를 넣어준다.
            WritingTextView editText = new WritingTextView(activity);
            editText.setTextChangeListner(textWatcher);
            editText.setOnFocusChangeListener(focusChangeListener);
            editText.setMinLines(3);
            writing_content_container.addView(editText, CUR_INDEX + 1, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

        } else {
            CUR_INDEX++;
        }
        currentSelectedItem = type;
        //추가한 뷰 빼고 다른 뷰의 focus는 삭제한다.
        unsetOtherViews(view);
        changeToolbarMenu(toolbar.getMenu(), true);
        startToast(Integer.toString(CUR_INDEX));
    }

    private void storageUploader() throws ParseException {

        final String title = titleEditText.getText().toString();
        String pickedYear = String.valueOf(yearPicker.getValue());
        String pickedMonth = String.valueOf(monthPicker.getValue());
        String pickedDay = String.valueOf(dayPicker.getValue());
        //final ArrayList<String> taggedusers = taggedUserAdapter.get(taggedUser);
        //날짜를 8자리로 표현하기 위한 if
        if(pickedMonth.length()==1){
            pickedMonth = "0"+pickedMonth;
        }
        if(pickedDay.length()==1){
            pickedDay = "0"+pickedDay;
        }
       //날짜에 -붙이는 형식 맞추기
        String t = pickedYear+"-"+pickedMonth+"-"+pickedDay;
        final String dateOfMemory = t;
        final String publisherBirthday = memberInfo.getBirthDay();
        final String name  = memberInfo.getName();



        if (title.length() > 0) {
            loaderLayout.setVisibility(View.VISIBLE);
            final ArrayList<String> contentsList = new ArrayList<>();
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final DocumentReference documentReference = firebaseFirestore.collection("posts").document();

            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                if (view instanceof WritingTextView) {
                    String text = ((WritingTextView) view).text.getText().toString();
                    if (text.length() > 0) {
                        contentsList.add(text);
                    }
                } else {
                    String URI = pathList.get(pathCount);
                    contentsList.add(URI.substring(1));
                    final StorageReference mountainImagesRef;
                    if(URI.substring(0, 1).equals("0")) {
                        mountainImagesRef = storageRef.child("post/" + documentReference.getId() + "/" + pathCount + ".jpg");
                    }
                    else {
                        mountainImagesRef = storageRef.child("post/" + documentReference.getId() + "/" + pathCount + ".mp4");
                    }
                    try {
                        InputStream stream = activity.getContentResolver().openInputStream(Uri.parse(URI.substring(1)));
                        StorageMetadata metadata = new StorageMetadata.Builder().setCustomMetadata("index", "" + (contentsList.size() - 1)).build();
                        UploadTask uploadTask = mountainImagesRef.putStream(stream, metadata);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                // Handle unsuccessful uploads
                            }
                        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                final int index = Integer.parseInt(taskSnapshot.getMetadata().getCustomMetadata("index"));
                                mountainImagesRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        contentsList.set(index, uri.toString());
                                        successCount++;
                                        if (pathList.size() == successCount) {
                                            //완료
                                            PostInfo postInfo = new PostInfo(title, contentsList, user.getUid(), new Date(),dateOfMemory,publisherBirthday);
                                            storeUploader(documentReference, postInfo);
                                        }
                                    }
                                });
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Log.e("에러", "에러" + e.toString());
                    }
                    pathCount++;
                }
                if(pathList.size() ==0){
                    PostInfo postInfo = new PostInfo(title, contentsList, user.getUid(), new Date(),dateOfMemory,publisherBirthday);
                    storeUploader(documentReference, postInfo);
                }
            }
        } else {
            startToast("제목을 입력해 주세요");
        }
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
    }
    private void storeUploader(DocumentReference documentReference, PostInfo postInfo) {
        documentReference.set(postInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        loaderLayout.setVisibility(View.GONE);
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        fragmentManager.beginTransaction().remove(WritingFragment.this).commit();
                        fragmentManager.popBackStack();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        loaderLayout.setVisibility(View.GONE);
                    }
                });
    }



}

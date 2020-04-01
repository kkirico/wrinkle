package com.flagtag.wrinkle.fragement;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.WriteInfo;
import com.flagtag.wrinkle.activity.GalleryActivity;
import com.flagtag.wrinkle.activity.MemberActivity;
import com.flagtag.wrinkle.view.WritingImageView;
import com.flagtag.wrinkle.activity.MainActivity;
import com.flagtag.wrinkle.view.WritingTextView;
import com.flagtag.wrinkle.view.WritingVideoView;
import com.flagtag.wrinkle.view.WritingView;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;


public class WritingFragment extends Fragment {

    private static final String TAG = "Write Post Activity";
    private FirebaseUser user;

    FirebaseFirestore db;
    Toolbar toolbar;
    LinearLayout writing_content_container;
    WritingTextView writing;
    MainActivity activity;


    private static final int SELECT_IMAGE = 1;
    private static final int SELECT_VIDEO = 2;
    //현재 선택된 아이템을 뜻함.
    private static int CUR_INDEX = 0;

    private static int DEFAULT_MENU = 0;
    private static int IMAGE_MENU = 1;
    private static int VEDIO_MENU = 2;
    private static int QUOTE_MENU = 3;
    private static int LOCATION_MENU = 4;
    private static int DIVISION_MENU = 5;
    private static int TEXT_MENU = 6;
    EditText titleEditText;
    EditText contentEditText;
    private ArrayList<String> pathList = new ArrayList<>();
    private LinearLayout parent;

    private int menu_page = 0;
    private int currentSelectedItem = 0;

    private boolean BOLD_BUTTON_CHECKED = false;

    private int pathCount, successCount;

    public WritingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_writing, container, false);
        activity = (MainActivity) getActivity();
        parent = rootView.findViewById(R.id.content_container);
        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();
        rootView.findViewById(R.id.check).setOnClickListener(onClickListener);
        titleEditText = rootView.findViewById(R.id.title);
        contentEditText = rootView.findViewById(R.id.contentEditText);
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
                if (item.getItemId() == R.id.image_button) {

                    //이미지 선택
                    startToast("이미지 선택");
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, SELECT_IMAGE);
                } else if (item.getItemId() == R.id.video_button) {
                    //동영상 선택
                    startToast("동영상 선택");
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("video/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, SELECT_VIDEO);
                } else if (item.getItemId() == R.id.location_button) {
                    //위치 선택
                } else if (item.getItemId() == R.id.quote_button) {
                    //인용 버튼
                } else if (item.getItemId() == R.id.division_line_button) {
                    //구분선 버튼
                } else if (item.getItemId() == R.id.more_button) {
                    //더보기 버튼
                    //아직 더보기 버튼을 어떻게 사용할지는 정하지 않았음
                    Menu menu = toolbar.getMenu();
                    changeToolbarMenu(menu);

                } else if (item.getItemId() == R.id.up_button) {

                    if (CUR_INDEX == 0 || CUR_INDEX == numberofItem - 1) {
                        return false;
                    } else {
                        //선택한 아이템을 위로 올린다.

                        WritingView aboveView = (WritingView) writing_content_container.getChildAt(CUR_INDEX - 1);
                        writing_content_container.removeView(aboveView);
                        writing_content_container.addView(aboveView, CUR_INDEX);
                        CUR_INDEX--;
                    }

                } else if (item.getItemId() == R.id.down_button) {

                    if (CUR_INDEX == numberofItem - 1 || CUR_INDEX == numberofItem - 2) {
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
                    changeToolbarMenu(toolbar.getMenu());
                } else if (item.getItemId() == R.id.bold_button) {
                    WritingView curView = (WritingView) writing_content_container.getChildAt(CUR_INDEX);
                    startToast("boldbutton");


                    //not bold ->bold
                    if (!BOLD_BUTTON_CHECKED) {

                        item.setChecked(true);
                        item.setIconTintList(ColorStateList.valueOf(Color.RED));
                        BOLD_BUTTON_CHECKED = true;
                    } else {
                        item.setChecked(false);
                        item.setIconTintList(null);
                        BOLD_BUTTON_CHECKED = false;
                    }
                    activity.invalidateOptionsMenu();

                }
                return false;
            }
        });


        return rootView;
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check:
                    storageUploader();

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
        //이미지를 선택했을 때
        if (requestCode == SELECT_IMAGE) {
            try {
                String path = data.getStringExtra("profilePath");
                pathList.add(path);
                // 선택한 이미지에서 비트맵 생성
                InputStream in = activity.getContentResolver().openInputStream(data.getData());
                Bitmap img = BitmapFactory.decodeStream(in);
                in.close();
                // Custom WritingImageView 생성
                final WritingImageView imageView = new WritingImageView(activity);
                //Custom WritingImageView 안의 imageView 안에 이미지 설정을 해준다.
                imageView.setImageView(img);
                addViewToContainer(imageView);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == SELECT_VIDEO) {
            Uri videoURI = data.getData();
            if (videoURI != null) {
                String videoPath = getPath(videoURI);

                //WritingVideoView 생성
                WritingVideoView videoView = new WritingVideoView(activity);
                videoView.setVideoView(videoURI);
                videoView.requestFocus();


                addViewToContainer(videoView);
            }
        }
    }


    View.OnFocusChangeListener focusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean gainFocus) {
            if (gainFocus) {
                WritingView curWritingView = (WritingView) (view.getParent().getParent());
                CUR_INDEX = writing_content_container.indexOfChild(curWritingView);
                unsetOtherViews(curWritingView);
                curWritingView.toggleSelected();
                currentSelectedItem = TEXT_MENU;
                changeToolbarMenu(toolbar.getMenu());
                startToast(Integer.toString(CUR_INDEX));

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

    private void changeToolbarMenu(Menu menu) {
        int curMenuGroup = R.id.default_group;
        if (currentSelectedItem == TEXT_MENU) {
            curMenuGroup = R.id.text_group;
        } else if (currentSelectedItem == IMAGE_MENU) {
            //curMenuGroup =R.id.move_group;
        }
        //현재 메뉴가 DEFAULT이고 골라진 아이템이랑 현재 메뉴랑 다르면
        if (menu_page == DEFAULT_MENU && menu_page != currentSelectedItem) {
            menu.setGroupVisible(R.id.move_group, true);
            menu.setGroupVisible(curMenuGroup, true);
            menu.setGroupVisible(R.id.default_group, false);
            menu_page = currentSelectedItem;
        } else {
            menu.setGroupVisible(R.id.move_group, false);
            menu.setGroupVisible(curMenuGroup, false);
            menu.setGroupVisible(R.id.default_group, true);
            menu_page = DEFAULT_MENU;
        }
    }

    private String getPath(Uri uri) {
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

    private void addViewToContainer(final WritingView view) {
        //WritingImageView의 onClickListener를 만든다.
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUR_INDEX = writing_content_container.indexOfChild(v);
                startToast(Integer.toString(CUR_INDEX));
                //WritingImageView가 선택되었을 때 setSelected() 함수 호출
                view.toggleSelected();
                unsetOtherViews(view);
                currentSelectedItem = IMAGE_MENU;

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

        }
        currentSelectedItem = IMAGE_MENU;
        //추가한 뷰 빼고 다른 뷰의 focus는 삭제한다.
        unsetOtherViews(view);
        changeToolbarMenu(toolbar.getMenu());
        startToast(Integer.toString(CUR_INDEX));

    }

    private void storageUploader() {
        final String title = titleEditText.getText().toString();
        if (title.length() > 0) {
            final ArrayList<String> contentsList = new ArrayList<>();
            user = FirebaseAuth.getInstance().getCurrentUser();
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
            final DocumentReference documentReference = firebaseFirestore.collection("cities").document();
            for (int i = 0; i < parent.getChildCount(); i++) {
                View view = parent.getChildAt(i);
                if (view instanceof WritingTextView) {
                    String text = ((WritingTextView)view).text.getText().toString();
                    if (text.length() > 0) {
                        contentsList.add(text);
                    }
                } else if(view instanceof WritingImageView){
                        contentsList.add(pathList.get(pathCount));
                        final StorageReference mountainImagesRef = storageRef.child("post/" + documentReference.getId() + "/" + pathCount + ".jpg");
                    try {
                        InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));
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
                                            WriteInfo writeInfo = new WriteInfo(title, contentsList, user.getUid(), new Date());
                                            storeUploader(documentReference,writeInfo);
                                        }
                                    }
                                });
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Log.e("에러", "에러" + e.toString());
                    }
                }
                else if(view instanceof WritingVideoView){
                    contentsList.add(pathList.get(pathCount));
                    final StorageReference mountainImagesRef = storageRef.child("post/" + documentReference.getId() + "/" + pathCount + ".jpg");
                    try {
                        InputStream stream = new FileInputStream(new File(pathList.get(pathCount)));
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
                                            WriteInfo writeInfo = new WriteInfo(title, contentsList, user.getUid(), new Date());
                                            storeUploader(documentReference,writeInfo);
                                        }
                                    }
                                });
                            }
                        });
                    } catch (FileNotFoundException e) {
                        Log.e("에러", "에러" + e.toString());
                    }

                }
            }


        } else {
            startToast("제목을 입력해 주세");
        }
    }

    private void storeUploader(DocumentReference documentReference, WriteInfo writeInfo) {
        documentReference.set(writeInfo)
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    Log.d(TAG, "DocumentSnapshot successfully written!");
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                }
            });
    }

    private void myStartActivity(Class c) {
        Intent intent = new Intent(getContext(), c);
        startActivityForResult(intent, 0);
    }

}
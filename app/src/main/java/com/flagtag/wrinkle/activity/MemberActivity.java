package com.flagtag.wrinkle.activity;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.bumptech.glide.Glide;
import com.flagtag.wrinkle.MemberInfo;
import com.flagtag.wrinkle.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Objects;


public class MemberActivity extends BasicActivity {
    private static final String TAG = "Member Init Activity";
    private ImageView profileImageView;
    private RelativeLayout loaderLayout;
    private String profilePath;
    private FirebaseUser user;
    MemberInfo memberInfo = MemberInfo.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(onClickListener);
        loaderLayout = findViewById(R.id.loaderLayout);

        memberInfo = MemberInfo.getInstance();
        String address = memberInfo.getAddress();
        String email = memberInfo.getemail();
        String name = memberInfo.getName();
        String text = memberInfo.getText();
        ((TextView)findViewById(R.id.nameEditText)).setText(name);
        ((TextView)findViewById(R.id.emailEditText)).setText(email);
        ((TextView)findViewById(R.id.profileEditText)).setText(text);
        ((TextView)findViewById(R.id.addressEditText)).setText(address);


        findViewById(R.id.check).setOnClickListener(onClickListener);
        findViewById(R.id.picture).setOnClickListener(onClickListener);
        findViewById(R.id.gallery).setOnClickListener(onClickListener);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void onBackPressed(){
        super.onBackPressed();
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        switch (requestCode) {
            case 0:{
                if (resultCode == Activity.RESULT_OK) {
                    profilePath = data.getStringExtra("profilePath");
                    Glide.with(this).load(profilePath).centerCrop().override(300).into(profileImageView);
                }
                break;
            }
        }
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check:
                    storageUploader();
                    break;
                case R.id.profileImageView:
                    CardView cardView = findViewById(R.id.buttonsCardview);
                    if(cardView.getVisibility() == View.VISIBLE){
                        cardView.setVisibility(View.GONE);
                    }
                    else{
                        cardView.setVisibility(View.VISIBLE);
                    }
                    break;
                case R.id.picture:
                    CardView cardView1 = findViewById(R.id.buttonsCardview);
                    cardView1.setVisibility(View.GONE);
                    myStartActivity(CameraActivity.class);
                    break;
                case R.id.gallery:
                        myStartActivity(GalleryActivity.class);
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    myStartActivity(GalleryActivity.class);
                } else {
                    startToast("갤러리 사용 권한을 허가해 주세요2");

                }
            }
        }
    }

    private void storageUploader() {
        final String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        final String email = ((EditText) findViewById(R.id.emailEditText)).getText().toString();
        final String profileText = ((EditText) findViewById(R.id.profileEditText)).getText().toString();
        final String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();
        final String profilePicture = findViewById(R.id.profileImageView).toString();
        memberInfo.setAddress(address);
        memberInfo.setName(name);
        memberInfo.setText(profileText);
        memberInfo.setemail(email);
        memberInfo.setPhotoUrl(profilePicture);

        if (name.length()>0 && email.length()>9 && profileText.length()>5 && address.length()>0) {
            loaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            assert user != null;
            final StorageReference mountainImagesRef = storageRef.child("users/"+user.getUid()+"profileImage.jpg");

            if(profilePath ==null){
                storeUploader(memberInfo);
            }
            else{
                try{
                    InputStream stream = new FileInputStream(new File(profilePath));
                    UploadTask uploadTask = mountainImagesRef.putStream(stream);
                    uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                        @Override
                        public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                            if (!task.isSuccessful()) {
                                throw Objects.requireNonNull(task.getException());
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                assert downloadUri != null;
                                memberInfo.setPhotoUrl(downloadUri.toString());
                                storeUploader(memberInfo);
                             } else {
                                startToast("회원정보 저장 실패.");
                            }
                        }
                    });
                }catch( FileNotFoundException e){
                    Log.e("에러","에러"+e.toString());
                }

            }


        } else {
            startToast("회원정보를 입력해주세요.");
        }
    }

    private void storeUploader(MemberInfo memberInfo){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users").document(user.getUid()).set(memberInfo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        startToast("회원정보를 등록 성공.");
                        loaderLayout.setVisibility(View.GONE);
                        myStartActivity(MainActivity.class);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("회원정보를 등록 실패.");
                        loaderLayout.setVisibility(View.GONE);
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }


    //토스트 메시지띄우는 함수
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
    /* 이미지 돌리기
    public Bitmap rotatePicture(String photoPath,Bitmap bitmap)
            throws IOException {
        ExifInterface ei = new ExifInterface(photoPath);
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap rotatedBitmap = null;
        switch (orientation) {

            case ExifInterface.ORIENTATION_ROTATE_90:
                rotatedBitmap = rotateImage(bitmap, 90);
                break;

            case ExifInterface.ORIENTATION_ROTATE_180:
                rotatedBitmap = rotateImage(bitmap, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                rotatedBitmap = rotateImage(bitmap, 270);
                break;

            case ExifInterface.ORIENTATION_NORMAL:
            default:
                rotatedBitmap = bitmap;
        }
        return rotatedBitmap;
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(),
                matrix, true);
    }기*/
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);
    }
}

package com.flagtag.wrinkle.activity;
import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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
import java.io.IOException;
import java.io.InputStream;


public class MemberActivity extends BasicActivity {
    private static final String TAG = "Member Init Activity";
    private ImageView profileImageView;
    private RelativeLayout loaderLayout;
    private String profilePath;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(onClickListener);
        loaderLayout = findViewById(R.id.loaderLayout);


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
                    CardView cardView2 = findViewById(R.id.buttonsCardview);//카드뷰 다시 안보이게 하
                    cardView2.setVisibility(View.GONE);
                    if (ContextCompat.checkSelfPermission(MemberActivity.this ,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(MemberActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                1);
                        if (ActivityCompat.shouldShowRequestPermissionRationale(MemberActivity.this,
                                Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        } else {
                            startToast("갤러리 사용 권한을 허가해 주세요1");
                        }
                    } else {
                        myStartActivity(GalleryActivity.class);
                    }
                    break;
            }
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
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
        final String phoneNumber = ((EditText) findViewById(R.id.phoneNumberEditText)).getText().toString();
        final String birthDay = ((EditText) findViewById(R.id.birthDayEditText)).getText().toString();
        final String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();


        if (name.length()>0 && phoneNumber.length()>9 && birthDay.length()>5 && address.length()>0) {
            loaderLayout.setVisibility(View.VISIBLE);
            FirebaseStorage storage = FirebaseStorage.getInstance();
            StorageReference storageRef = storage.getReference();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            user = mAuth.getCurrentUser();
            final StorageReference mountainImagesRef = storageRef.child("users/"+user.getUid()+"profileImage.jpg");

            if(profilePath ==null){
                MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthDay, address);
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
                                throw task.getException();
                            }
                            return mountainImagesRef.getDownloadUrl();
                        }
                    }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                        @Override
                        public void onComplete(@NonNull Task<Uri> task) {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthDay, address,downloadUri.toString());
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
                        //myStartActivity(MainActivity.class);이거 왜 없어지는걸까정말루
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        startToast("회원정보를 등록 실패.");
                        //loaderLayout.setVisibility(View.GONE);
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    //토스트 메시지띄우는 함수
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
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
    }
    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);
    }
}

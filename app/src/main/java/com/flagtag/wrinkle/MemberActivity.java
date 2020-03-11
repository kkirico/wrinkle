package com.flagtag.wrinkle;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;



public class MemberActivity extends AppCompatActivity {
    private static final String TAG = "Member Init Activity";
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);

        profileImageView = findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(onClickListener);


        findViewById(R.id.check).setOnClickListener(onClickListener);
    }

    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode,data);
        switch (requestCode){
            case 0:
                if (requestCode== Activity.RESULT_OK){
                    String profilePath = data.getStringExtra("profilePath");
                    Log.e("로그", "profilePath"+profilePath);
                    Bitmap bmp = BitmapFactory.decodeFile(profilePath);
                    profileImageView.setImageBitmap(bmp);

                }
                break;

        }
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check:
                    profileUpdate();

                    break;
                case R.id.profileImageView:
                    myStartActivity(CameraActivity.class);


                    break;


            }
        }
    };

    private void profileUpdate() {

        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();
        String phoneNumber = ((EditText) findViewById(R.id.phoneNumberEditText)).getText().toString();
        String birthDay = ((EditText) findViewById(R.id.birthDayEditText)).getText().toString();
        String address = ((EditText) findViewById(R.id.addressEditText)).getText().toString();


        if (name.length()>0 && phoneNumber.length()>9 && birthDay.length()>5 && address.length()>0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            // Access a Cloud Firestore instance from your Activity
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            MemberInfo memberInfo = new MemberInfo(name, phoneNumber, birthDay, address);

            if(user != null){
                db.collection("users").document(user.getUid()).set(memberInfo)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Log.d(TAG, "DocumentSnapshot successfully written!");
                                startToast("회원정보를 등록 성공.");
                                //myStartActivity(MainActivity.class); 이거 왜 안해도되는지 어디서 처리했는지 찾아봐 헌준?
                                finish();
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                startToast("회원정보를 등록 실패.");
                                Log.w(TAG, "Error writing document", e);
                            }
                        });

            }




        } else {
            startToast("회원정보를 입력해주세요.");
        }
    }

    //토스트 메시지띄우는 함수
    private void startToast(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }


    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivityForResult(intent,0);
    }
}

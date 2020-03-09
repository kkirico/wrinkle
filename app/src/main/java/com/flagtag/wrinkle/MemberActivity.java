package com.flagtag.wrinkle;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;


public class MemberActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member);


        findViewById(R.id.check).setOnClickListener(onClickListener);
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.check:
                    profileUpdate();
                    break;


            }
        }
    };

    private void profileUpdate() {

        String name = ((EditText) findViewById(R.id.nameEditText)).getText().toString();


        if (name.length() > 0) {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                    .setDisplayName("Jane 0,User")
                    .setPhotoUri(Uri.parse("https://example.com/jane-q-User/profile.jpg"))
                    .build();

            if (user != null) {
                user.updateProfile(profileUpdates)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    startToast("회원정보 등록에 성공하셨습니다.");
                                    finish();
                                }
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
}


package com.flagtag.wrinkle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "registerActivity";

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.registerButton).setOnClickListener(onClickListener);
        findViewById(R.id.gotoLoginBtn).setOnClickListener(onClickListener);
    }


    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        //UI
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.registerButton:
                    signup();
                break;
                case R.id.gotoLoginBtn:
                    startLoginActivity();
                    break;

            }
        }
    };

    private void signup() {

        String email = ((EditText) findViewById(R.id.registerEmail)).getText().toString();
        String password = ((EditText) findViewById(R.id.registerPassword)).getText().toString();
        String passwordCheck = ((EditText) findViewById(R.id.registerPasswordCheck)).getText().toString();
        if (email.length()>0 && password.length()>0 &&passwordCheck.length()>0) {
            //비밀번호 확인
            if (password.equals(passwordCheck)) {
                mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    //화원가입 실행 부분
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            startLoginActivity();
                            startToast("회원가입 성공!");
                            FirebaseUser user = mAuth.getCurrentUser();
                            //UI
                        } else {
                            if (task.getException() != null) {
                                startToast(task.getException().toString());
                            }
                            //UI
                        }
                    }
                });
            } else {
                Toast.makeText(this, " 비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();


            }
        }
        else{
            startToast("이메일 또는 비밀번호가 입력되지 않았습니다.");
        }
    }
    //토스트 메시지띄우는 함수
    private void startToast(String msg){
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();

    }
    private void startLoginActivity(){
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }
    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}

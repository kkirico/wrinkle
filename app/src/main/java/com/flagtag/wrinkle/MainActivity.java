package com.flagtag.wrinkle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private MypageFragment mypageFragment;
    private NewsfeedFragment newsfeedFragment;
    private NotificationFragment notificationFragment;
    private SurfingFragment surfingFragment;
    private WritingFragment writingFragment;
    private BottomNavigationView bottomNavigationView;

    FragmentTransaction fragmentTransaction;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if(user == null){
            myStartActivity(RegisterActivity.class);
        }
        else{
            //회원가입 or 로그인

                for (UserInfo profile : user.getProviderData()) {
                    String name = profile.getDisplayName();
                    /*String email = profile.getEmail();
                    String photoUrl = profile.getPhotoUrl();*/
                    if(name != null) {
                        if (name.length() == 0) {
                            myStartActivity(MemberActivity.class);
                        }
                    }
            }
        }
        findViewById(R.id.logoutBtn).setOnClickListener(onClickListener);



        fragmentManager = getSupportFragmentManager();

        mypageFragment = new MypageFragment();
        newsfeedFragment = new NewsfeedFragment();
        notificationFragment = new NotificationFragment();
        surfingFragment = new SurfingFragment();
        writingFragment = new WritingFragment();

        //fragmentTransaction으로 fragment 추가, 변경 등의 작업을 할 수 있다.
        fragmentTransaction = fragmentManager.beginTransaction();
        //activity_main의 main_container를 newsfeedFragment로 바꾼다.
        //commitAllowingStateLoss()까지 하면 처음 실행할 때 기본으로 실행된다.
        fragmentTransaction.replace(R.id.main_container, newsfeedFragment).commitAllowingStateLoss();

        bottomNavigationView = findViewById(R.id.bottom_Navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new ItemSelectedListener());


    }
    class ItemSelectedListener implements BottomNavigationView.OnNavigationItemSelectedListener{
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

            fragmentTransaction = fragmentManager.beginTransaction();

            switch (menuItem.getItemId()){
                case R.id.newsfeed_tab:

                    fragmentTransaction.replace(R.id.main_container, newsfeedFragment).commitAllowingStateLoss();
                    break;
                case R.id.writing_tab:

                    fragmentTransaction.replace(R.id.main_container, writingFragment).commitAllowingStateLoss();
                    break;

                case R.id.sufing_tab:

                    fragmentTransaction.replace(R.id.main_container, surfingFragment).commitAllowingStateLoss();
                    break;

                case R.id.notification_tab:

                    fragmentTransaction.replace(R.id.main_container, notificationFragment).commitAllowingStateLoss();
                    break;
                case R.id.mypage_tab:

                    fragmentTransaction.replace(R.id.main_container, mypageFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }

    }


    View.OnClickListener onClickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.logoutBtn:
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(RegisterActivity.class);
                    break;
            }
        }
    };


    private void myStartActivity(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
    }
    public void onBackPressed(){
        super.onBackPressed();
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

}

package com.flagtag.wrinkle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "Main Activity";

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
        }else{

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            DocumentReference docRef = db.collection("users").document(user.getUid());
            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot document = task.getResult();
                        if(document != null){
                            if (document.exists()) {
                                Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            } else {
                                Log.d(TAG, "No such document");
                                myStartActivity(MainActivity.class);
                            }
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });
        }





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

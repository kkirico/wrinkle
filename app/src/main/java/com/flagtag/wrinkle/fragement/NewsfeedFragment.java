package com.flagtag.wrinkle.fragement;

import android.annotation.SuppressLint;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.flagtag.wrinkle.NewsfeedAdapter;
import com.flagtag.wrinkle.PostInfo;
import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.activity.MainActivity;
import com.flagtag.wrinkle.activity.SinglefeedActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;
import java.util.Date;


public class NewsfeedFragment extends Fragment implements GestureDetector.OnGestureListener {

    private static final String TAG = "오";
    private static final String DEBUG_TAG = "scroll detect";
    View view;
    NewsfeedAdapter newsfeedAdapter;
    ViewPager2 viewPager2;

    public NewsfeedFragment() {
        // Required empty public constructor
    }




    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_newsfeed, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);

        newsfeedAdapter = new NewsfeedAdapter();

        viewPager2 = rootView.findViewById(R.id.viewPager);
        viewPager2.setAdapter(newsfeedAdapter);



        viewPager2.setOnTouchListener(new MainActivity.OnSwipeTouchListener(viewPager2.getContext()){
            public void onSwipeTop() {
                Toast.makeText(viewPager2.getContext(), "top", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeRight() {
                Toast.makeText(viewPager2.getContext(), "right", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeLeft() {
                Toast.makeText(viewPager2.getContext(), "left", Toast.LENGTH_SHORT).show();
            }
            public void onSwipeBottom() {
                Toast.makeText(viewPager2.getContext(), "bottom", Toast.LENGTH_SHORT).show();
            }
        });

        viewPager2.setOnScrollChangeListener(new ViewPager.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                Intent intent = new Intent(getActivity(), SinglefeedActivity.class);
                startActivity(intent);
            }

        });


//        Toolbar toolbar = rootView.findViewById(R.id.newsfeed_toolbar);
        MainActivity mainActivity = (MainActivity) getActivity();
  //      mainActivity.setSupportActionBar(toolbar);

        FirebaseFirestore db = FirebaseFirestore.getInstance();



        db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<PostInfo> postList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                    postList.add(new PostInfo(
                                            document.getData().get("title").toString(),
                                            (ArrayList<String>) document.getData().get("contents"),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createdAt").getTime()),
                                            document.getData().get("dateOfMemory").toString(),
                                            document.getData().get("birthdayOfPublisher").toString()
                                    ));

                            }
                            viewPager2 = rootView.findViewById(R.id.viewPager);
                            NewsfeedAdapter mAdapter = new NewsfeedAdapter(postList);
                            viewPager2.setAdapter(mAdapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//리싸이클러 뷰 초기화
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
    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {

        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}

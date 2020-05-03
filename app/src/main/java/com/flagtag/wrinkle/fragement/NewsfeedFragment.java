package com.flagtag.wrinkle.fragement;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.flagtag.wrinkle.NewsfeedAdapter;
import com.flagtag.wrinkle.PostInfo;
import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.activity.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class NewsfeedFragment extends Fragment {

    private static final String TAG = "오";
    RecyclerView recyclerView;
    NewsfeedAdapter newsfeedAdapter;

    public NewsfeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_newsfeed, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL, false);

        newsfeedAdapter = new NewsfeedAdapter();

        recyclerView = rootView.findViewById(R.id.newsfeed_list);

        recyclerView.setLayoutManager(layoutManager);
        /*
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());
        newsfeedAdapter.addItem(new PostInfo());*/

        recyclerView.setAdapter(newsfeedAdapter);

        Toolbar toolbar = rootView.findViewById(R.id.newsfeed_toolbar);
        MainActivity mainActivity = (MainActivity) getActivity();
        mainActivity.setSupportActionBar(toolbar);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts").get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<PostInfo> postList = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                try {
                                    postList.add(new PostInfo(
                                            document.getData().get("title").toString(),
                                            (ArrayList<String>)document.getData().get("contents"),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createdAt").getTime()),//널이라서 뉴스피드가 안뜸
                                            transformDate(document.getData().get("dateOfMemory").toString()),
                                            transformDate(document.getData().get("birthdayOfPublisher").toString())
                                            ));
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                catch(NullPointerException k){
                                    postList.add(new PostInfo(
                                            document.getData().get("title").toString(),
                                            (ArrayList<String>)document.getData().get("contents"),
                                            document.getData().get("publisher").toString(),
                                            new Date(document.getDate("createdAt").getTime())));
                                }
                            }

                            recyclerView = rootView.findViewById(R.id.newsfeed_list);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                            RecyclerView.Adapter mAdapter = new NewsfeedAdapter(NewsfeedFragment.this,postList);
                            recyclerView.setAdapter(mAdapter);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

//리싸이클러 뷰 초기화
        return rootView;


    }


    public Date transformDate(String date) throws ParseException {
        SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");

        // Date로 변경하기 위해서는 날짜 형식을 yyyy-mm-dd로 변경해야 한다.
        SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-mm-dd");

        java.util.Date tempDate = null;

        try {
            // 현재 yyyymmdd로된 날짜 형식으로 java.util.Date객체를 만든다.
            tempDate = beforeFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // java.util.Date를 yyyy-mm-dd 형식으로 변경하여 String로 반환한다.
        String transDate = afterFormat.format(tempDate);

        // 반환된 String 값을 Date로 변경한다.
        Date d = beforeFormat.parse(transDate);

        return d;
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


}

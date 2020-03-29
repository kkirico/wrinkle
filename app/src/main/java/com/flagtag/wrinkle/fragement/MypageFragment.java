package com.flagtag.wrinkle.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flagtag.wrinkle.MemberInfo;
import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.activity.MemberActivity;
import com.flagtag.wrinkle.activity.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class MypageFragment extends Fragment {
    MemberInfo memberInfo;

    public MypageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

            // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_mypage, container, false);
        rootView.findViewById(R.id.userId).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.userName).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.userIntro).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.userAdress).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.timeLineBtn).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.myFeedBtn).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.logoutBtn).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.myProfileBtn).setOnClickListener(onClickListener);

        memberInfo = MemberInfo.getInstance();
        String address = memberInfo.getAddress();
        String email = memberInfo.getemail();
        String name = memberInfo.getName();
        String photoUrl = memberInfo.getPhotoUrl();
        String text = memberInfo.getText();
        ((TextView)rootView.findViewById(R.id.userName)).setText(name);
        ((TextView)rootView.findViewById(R.id.userId)).setText(email);
        ((TextView)rootView.findViewById(R.id.userIntro)).setText(text);
        ((TextView)rootView.findViewById(R.id.userAdress)).setText(address);
        ImageView imageView = rootView.findViewById(R.id.profilePicture);
        if(photoUrl != null){
            Glide.with(this).load(photoUrl).into(imageView);
            return rootView;
        }else{
            return rootView;
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
                case R.id.myProfileBtn:
                    myStartActivity(MemberActivity.class);
                    break;
                case R.id.timeLineBtn:
                    break;
                case R.id.myFeedBtn:
                    break;
            }
        }
    };

    private void myStartActivity(Class c){
        Intent intent = new Intent(getContext(), c);
        startActivity(intent);
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

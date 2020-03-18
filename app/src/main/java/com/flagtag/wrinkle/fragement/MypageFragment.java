package com.flagtag.wrinkle.fragement;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.activity.MemberActivity;
import com.flagtag.wrinkle.activity.RegisterActivity;
import com.google.firebase.auth.FirebaseAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class MypageFragment extends Fragment {


    public MypageFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_mypage, container, false);


        rootView.findViewById(R.id.logoutBtn).setOnClickListener(onClickListener);
        rootView.findViewById(R.id.myProfileBtn).setOnClickListener(onClickListener);
        return rootView;
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
                    FirebaseAuth.getInstance().signOut();
                    myStartActivity(MemberActivity.class);
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

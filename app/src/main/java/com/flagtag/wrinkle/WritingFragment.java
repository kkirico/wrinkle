package com.flagtag.wrinkle;


import android.content.Context;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;


public class WritingFragment extends Fragment {

    FirebaseFirestore db;

    public WritingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_writing, container, false);

        // Access a Cloud Firestore instance from your Activity
        db = FirebaseFirestore.getInstance();

        //Toolbar toolbar = rootView.findViewById(R.id.writing_fragment_toolbar);
        //MainActivity mainActivity = (MainActivity) getActivity();
        //mainActivity.setSupportActionBar(toolbar);
        //ActionBar actionBar = mainActivity.getSupportActionBar();



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

}

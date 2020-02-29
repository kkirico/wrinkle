package com.flagtag.wrinkle;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class NewsfeedFragment extends Fragment {

    RecyclerView recyclerView;
    NewsfeedAdapter newsfeedAdapter;
    public NewsfeedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_newsfeed, container, false);

        LinearLayoutManager layoutManager = new LinearLayoutManager(rootView.getContext(), LinearLayoutManager.VERTICAL,false);

        newsfeedAdapter = new NewsfeedAdapter();

        recyclerView = rootView.findViewById(R.id.newsfeed_list);

        recyclerView.setLayoutManager(layoutManager);

        newsfeedAdapter.addItem(new Post());
        newsfeedAdapter.addItem(new Post());
        newsfeedAdapter.addItem(new Post());
        newsfeedAdapter.addItem(new Post());
        newsfeedAdapter.addItem(new Post());
        newsfeedAdapter.addItem(new Post());
        newsfeedAdapter.addItem(new Post());

        recyclerView.setAdapter(newsfeedAdapter);

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

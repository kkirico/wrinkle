package com.flagtag.wrinkle.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.adapter.SearchUserAdapter;

public class UserSelectActivity extends AppCompatActivity {

    SearchUserAdapter adapter;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select);

        search = findViewById(R.id.user_search);
        adapter = new SearchUserAdapter();
    }
}

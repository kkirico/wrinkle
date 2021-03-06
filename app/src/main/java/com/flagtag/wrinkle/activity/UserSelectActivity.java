package com.flagtag.wrinkle.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.flagtag.wrinkle.NewsfeedAdapter;
import com.flagtag.wrinkle.PostInfo;
import com.flagtag.wrinkle.R;
import com.flagtag.wrinkle.adapter.SearchUserAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class UserSelectActivity extends AppCompatActivity {


    String selectedUserKey;

    private ArrayList<String> list;          // 데이터를 넣은 리스트변수
    private RecyclerView listView;          // 검색을 보여줄 리스트변수
    private EditText search;        // 검색어를 입력할 Input 창
    private SearchUserAdapter adapter;      // 리스트뷰에 연결할 아답터 여기다가 선택된 유아이 넣기
    private ArrayList<String> arraylist;
    private Button submitButton;
    private Button searchButton;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_select);

        db = FirebaseFirestore.getInstance();

        selectedUserKey= null;
        search = (EditText) findViewById(R.id.user_search);
        listView = (RecyclerView) findViewById(R.id.search_user_recyclerview);
        submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("users",adapter.getSelectedItems() );
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // input창에 문자를 입력할때마다 호출된다.
                // search 메소드를 호출한다.
                String text = search.getText().toString();

                search(text);
            }
        });
        // 리스트를 생성한다.
        list = new ArrayList<String>();

        // 검색에 사용할 데이터을 미리 저장한다.
        //settingList();

        // 리스트의 모든 데이터를 arraylist에 복사한다.// list 복사본을 만든다.
        arraylist = new ArrayList<String>();


        // 리스트에 연동될 아답터를 생성한다.
        adapter = new SearchUserAdapter();



        // 리스트뷰에 아답터를 연결한다.
        listView.setAdapter(adapter);
        listView.setLayoutManager(new LinearLayoutManager(this));

        Intent intent = getIntent();
        if(intent.hasExtra("taggedUsers")){
            adapter.addSelectedItems(intent.getStringArrayListExtra("taggedUsers"));
        }


    }

    // 검색을 수행하는 메소드
    public void search(String charText) {
        if(charText.equals("")){
            adapter.clear();
            adapter.notifyDataSetChanged();
            return;
        }
//
//        // 문자 입력시마다 리스트를 지우고 새로 뿌려준다.
//        list.clear();
//        adapter.clear();
//
//
//        // 문자 입력이 없을때는 모든 데이터를 보여준다.
//        if (charText.length() == 0) {
//            list.addAll(arraylist);
//        }
//        else
//        {
//            // 리스트의 모든 데이터를 검색한다.
//            for(int i = 0;i < arraylist.size(); i++)
//            {
//                // arraylist의 모든 데이터에 입력받은 단어(charText)가 포함되어 있으면 true를 반환한다.
//                if (arraylist.get(i).toLowerCase().contains(charText))
//                {
//                    // 검색된 데이터를 리스트에 추가한다.
//                    list.add(arraylist.get(i));
//                }
//            }
//        }
        db.collection("users")
                .orderBy("name")
                .whereGreaterThanOrEqualTo("name", charText)
                .whereLessThanOrEqualTo("name", charText+"\uf8ff")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                list.clear();
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        list.add(document.getId());

                    }


                    adapter.clear();
                    adapter.setItems(list);
                    // 리스트 데이터가 변경되었으므로 아답터를 갱신하여 검색된 데이터를 화면에 보여준다.
                    adapter.notifyDataSetChanged();

                } else {
                    Log.d("Search", "Error getting documents: ", task.getException());
                }
            }
        });;


    }


    // 검색에 사용될 데이터를 리스트에 추가한다.
    private void settingList(){

        db.collection("users")
//                .whereEqualTo(String.valueOf(search.getText()), true)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                list.add(document.getId());
                                Log.d("Setting list", document.getId() + " => " + document.getData());
                            }

                            arraylist.addAll(list);
                        } else {
                            Log.d("Setting list", "Error getting documents: ", task.getException());
                        }
                    }
                });





    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intent = new Intent();

        if (selectedUserKey != null) {
            intent.putExtra("selected user", selectedUserKey);

        }

    }



}

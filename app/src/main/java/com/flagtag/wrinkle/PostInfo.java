package com.flagtag.wrinkle;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class PostInfo extends ArrayList<String> {
    private static final String TAG ="오류" ;
    Date writingDate;
    ArrayList<String> tags;

    /*
    일단은 이렇게 하나의 String으로 했는데 나중에 원노트처럼
    누가 썼는지 옆에 나오게 하기 위해서는 다른 방법이 필요할 것 같음.
     */
    String contentWriting;

    String title;
    private ArrayList<String> contents;
    private String publisher;
    private Date createdAt;
    private String dateOfMemory;
    private String birthdayOfPublisher;
    ArrayList<String> taggedUsers;
    public PostInfo(String title, ArrayList<String> contents, String publisher, Date createdAt, String dateOfMemory, String birthdayOfPublisher){
        this.title = title;
        this.contents = contents ;
        this.publisher = publisher;
        this.createdAt = createdAt;
        this.dateOfMemory = dateOfMemory;
        this.birthdayOfPublisher = birthdayOfPublisher;
    }
    //getter and setter
    public String getTitle(){
        return this.title;
    }
    public void setString(String title){this.title  = title;}
    public ArrayList<String> getContents(){
        return this.contents;
    }
    public void setContents(ArrayList<String> contents){this.contents  = contents;}
    public String getPublisher(){return  this.publisher;}
    public  void setPublisher(String publisher){this.publisher = publisher;}
    public Date getCreatedAt(){return  this.createdAt;}
    public  void setCreatedAt(Date createdAt){this.createdAt = createdAt;
    }
    public String getContentWriting() {
        return contentWriting;
    }
    public void setContentWriting(String contentWriting) {
        this.contentWriting = contentWriting;
    }
    public String getDateOfMemory() {
        return dateOfMemory;
    }
    public void setDateOfMemory(String dateOfMemory) {
        this.dateOfMemory = dateOfMemory;
    }
    public String getBirthdayOfPublisher() {
        return birthdayOfPublisher;
    }
    public void setBirthdayOfPublisher(String birthdayOfPublisher) {
        this.birthdayOfPublisher = birthdayOfPublisher;
    }
    public ArrayList<String> getTaggedUsers() {
        return taggedUsers;
    }
    public void setTaggedUsers(ArrayList<String> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }



    /*
        comments랑 likes도 단순히 몇 개 체크되었는지 뿐만 아니라
        누가 체크했는지 등도 해야되기 때문에 클래스로 만들어야 할지도 모름.
         */
    int likes;
    int comments;

}

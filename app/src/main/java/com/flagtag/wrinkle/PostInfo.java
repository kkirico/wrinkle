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

public class PostInfo {
    private static final String TAG ="오류" ;
    ArrayList<User> taggedUsers;
    Date writingDate;
    ArrayList<String> tags;

    //User writer;
    //int postNumber;
    //Date realDate;
    //ArrayList<String> imageUrls;
    /*
    일단은 이렇게 하나의 String으로 했는데 나중에 원노트처럼
    누가 썼는지 옆에 나오게 하기 위해서는 다른 방법이 필요할 것 같음.
     */
    String contentWriting;

    String title;
    private ArrayList<String> contents;
    private String publisher;
    private Date createdAt;

    public PostInfo(String title, ArrayList<String> contents, String publisher, Date createdAt){
        this.title = title;
        this.contents = contents ;
        this.publisher = publisher;
        this.createdAt = createdAt;
    }
/*
    public PostInfo() {
        taggedUsers = new ArrayList<User>();
        taggedUsers.add(new User());
        writer = new User();
        postNumber = 1;
        title = "title";
        writingDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            realDate = dateFormat.parse("2010-01-29");
        } catch (ParseException e) {
            e.printStackTrace();
        }
        imageUrls = new ArrayList<>();
        imageUrls.add("https://ssl.pstatic.net/mimgnews/image/312/2020/03/02/0000437912_001_20200302180211645.jpg?type=w540");
        imageUrls.add("https://upload.wikimedia.org/wikipedia/commons/thumb/a/ac/No_image_available.svg/450px-No_image_available.svg.png");
        imageUrls.add("https://mimgnews.pstatic.net/image/117/2020/03/02/202003021714361802_1_20200302174409099.jpg?type=w540");
        contentWriting = "contents.....";
        tags = new ArrayList<>();
        tags.add("tag1");
    }
*/
    /*
        ==========================================================
        Getter와 Setter

    public ArrayList<User> getTaggedUsers() {
        return taggedUsers;
    }

    public void setTaggedUsers(ArrayList<User> taggedUsers) {
        this.taggedUsers = taggedUsers;
    }

    public User getWriter() {
        return writer;
    }

    public void setWriter(User writer) {
        this.writer = writer;
    }

    public int getPostNumber() {
        return postNumber;
    }

    public void setPostNumber(int postNumber) {
        this.postNumber = postNumber;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getWritingDate() {
        return writingDate;
    }

    public void setWritingDate(Date writingDate) {
        this.writingDate = writingDate;
    }

    public Date getRealDate() {
        return realDate;
    }

    public void setRealDate(Date realDate) {
        this.realDate = realDate;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public int getComments() {
        return comments;
    }

    public void setComments(int comments) {
        this.comments = comments;
    }*/
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


    /*
        comments랑 likes도 단순히 몇 개 체크되었는지 뿐만 아니라
        누가 체크했는지 등도 해야되기 때문에 클래스로 만들어야 할지도 모름.
         */
    int likes;
    int comments;

}

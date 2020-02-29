package com.flagtag.wrinkle;

import java.util.Date;

public class User {
    int user_number;
    String name;
    Date birthday;

    //리사이클러뷰 실험을 위해서 그냥 생성자 이렇게 해놓음.
    public User() {
        this.user_number = 1;
        this.name = "name";
        this.birthday = new Date();
    }

    /*
        ==========================================================
        Getter와 Setter
         */
    public int getUser_number() {
        return user_number;
    }

    public void setUser_number(int user_number) {
        this.user_number = user_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}

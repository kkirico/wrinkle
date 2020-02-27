package com.flagtag.wrinkle;

import java.util.Date;

public class User {
    int user_number;
    String name;
    Date birthday;


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

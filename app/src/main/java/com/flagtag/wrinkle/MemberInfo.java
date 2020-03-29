package com.flagtag.wrinkle;

public class MemberInfo{
        private String name;
        private String email;
        private String text;
        private String address;
        private String photoUrl;
        private static final MemberInfo memberInstance = new MemberInfo();

        public MemberInfo(String name, String email, String birthDay, String address, String photoUrl){
            this.name = name;
            this.email = email;
            this.text = birthDay;
            this.address = address;
            this.photoUrl = photoUrl;
        }

        public MemberInfo(){

        }

        public MemberInfo(String name, String email, String birthDay, String address){
        this.name = name;
        this.email = email;
        this.text = birthDay;
        this.address = address;
    }

        public String getName(){
            return this.name;
        }
        public void setName(String name){
            this.name  = name;
        }
        public String getemail(){
            return this.email;
        }
        public void setemail(String email){
            this.email  = email;
        }
        public String getAddress(){
            return this.address;
        }
        public void setAddress(String address){
            this.address  = address;
        }
        public String getText(){ return this.text; }
        public void setText(String text){
            this.text  = text;
        }
        public String getPhotoUrl(){
        return this.photoUrl;
    }
        public void setPhotoUrl(String photoUrl){
        this.photoUrl  = photoUrl;
    }

        public static MemberInfo getInstance(){
            return memberInstance;
        }




}

package com.flagtag.wrinkle;

public class MemberInfo{
        private String name;
        private String email;
        private String text;
        private String address;
        private String photoUrl;
        private String birthDay;
        private static final MemberInfo memberInstance = new MemberInfo();

        public MemberInfo(String name, String email, String text, String address, String photoUrl, String birthDay){
            this.name = name;
            this.email = email;
            this.text = text;
            this.address = address;
            this.photoUrl = photoUrl;
            this.birthDay = birthDay;
        }

        public MemberInfo(){

        }

        public MemberInfo(String name, String email, String text, String address, String birthDay){
        this.name = name;
        this.email = email;
        this.text = text;
        this.address = address;
        this.birthDay = birthDay;
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
        public String getBirthDay(){
        return this.birthDay;
    }
        public void setBirthDay(String birthDay){
        this.birthDay  = birthDay;
    }

        public static MemberInfo getInstance(){
            return memberInstance;
        }




}

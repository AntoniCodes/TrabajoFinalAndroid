package com.example.appfinal;

public class User {
    private String nickname;
    private String age;
    private String gender;
    private String location;
    private String aboutMe;
    private String ImageUrl;
    private String lolAcc;

    private String uid;

    public User() {}

    public User(String nickname, String age, String gender, String location, String aboutMe, String imageUrl, String lolAcc, String uid) {
        this.nickname = nickname;
        this.age = age;
        this.gender = gender;
        this.location = location;
        this.aboutMe = aboutMe;
        this.ImageUrl = imageUrl;
        this.lolAcc = lolAcc;
        this.uid = uid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public String getLolAcc() {
        return lolAcc;
    }

    public void setLolAcc(String lolAcc) {
        this.lolAcc = lolAcc;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}

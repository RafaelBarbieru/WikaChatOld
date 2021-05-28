package com.wika.wikachat.templates;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Profile implements Serializable {

    // Attributes
    private String uid;
    private String email;
    private String name;
    private String age;
    private String gender;
    private String countryOfOrigin;
    private String nativeLanguage;
    private String learningLanguage;
    private String define;
    private String biography;
    private int[] points = {0, 0, 0};

    // Constructors
    public Profile() {

    }

    // Methods

    public String getUid() {
        return uid;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public String getLearningLanguage() {
        return learningLanguage;
    }

    public String getNativeLanguage() {
        return nativeLanguage;
    }

    public String getDefine() {
        return define;
    }

    public String getBiography() {
        return biography;
    }

    public int[] getPoints() {
        return points;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public void setLearningLanguage(String learningLanguage) {
        this.learningLanguage = learningLanguage;
    }

    public void setNativeLanguage(String nativeLanguage) {
        this.nativeLanguage = nativeLanguage;
    }

    public void setDefine(String define) {
        this.define = define;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setPoints(int[] points) {
        this.points = points;
    }

    @NonNull
    @Override
    public String toString() {
        return "{Name: " + name + ", age: " + age + ", gender: " + gender + ", country: "
                + countryOfOrigin + ", native in: " + nativeLanguage + ", learning: " + learningLanguage
                + ", defining as: " + define + ", biography: " + biography + "}";
    }
}

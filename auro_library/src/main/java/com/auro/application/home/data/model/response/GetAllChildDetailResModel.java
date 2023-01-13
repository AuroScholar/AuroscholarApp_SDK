package com.auro.application.home.data.model.response;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Collection;
import java.util.Iterator;

public class GetAllChildDetailResModel  {  //implements Collection<UserDetailResModel>


    @SerializedName("student_name")
    @Expose
    private String student_name;


    @SerializedName("profile_pic")
    @Expose
    private String profile_pic;



    @SerializedName("user_id")
    @Expose
    private int user_id;
    @SerializedName("grade")
    @Expose
    private int grade;
    @SerializedName("user_name")
    @Expose
    private String user_name;
    @SerializedName("wallet")
    @Expose
    private int wallet;

    public String getStudent_name() {
        return student_name;
    }

    public void setStudent_name(String student_name) {
        this.student_name = student_name;
    }

    public String getProfile_pic() {
        return profile_pic;
    }

    public void setProfile_pic(String profile_pic) {
        this.profile_pic = profile_pic;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getGrade() {
        return grade;
    }

    public void setGrade(int grade) {
        this.grade = grade;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public int getWallet() {
        return wallet;
    }

    public void setWallet(int wallet) {
        this.wallet = wallet;
    }

//    @Override
//    public int size() {
//        return 0;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public boolean contains(@Nullable Object o) {
//        return false;
//    }
//
//    @NonNull
//    @Override
//    public Iterator<UserDetailResModel> iterator() {
//        return null;
//    }
//
//    @NonNull
//    @Override
//    public Object[] toArray() {
//        return new Object[0];
//    }
//
//    @NonNull
//    @Override
//    public <T> T[] toArray(@NonNull T[] a) {
//        return null;
//    }
//
//    @Override
//    public boolean add(UserDetailResModel userDetailResModel) {
//        return false;
//    }
//
//    @Override
//    public boolean remove(@Nullable Object o) {
//        return false;
//    }
//
//    @Override
//    public boolean containsAll(@NonNull Collection<?> c) {
//        return false;
//    }
//
//    @Override
//    public boolean addAll(@NonNull Collection<? extends UserDetailResModel> c) {
//        return false;
//    }
//
//    @Override
//    public boolean removeAll(@NonNull Collection<?> c) {
//        return false;
//    }
//
//    @Override
//    public boolean retainAll(@NonNull Collection<?> c) {
//        return false;
//    }
//
//    @Override
//    public void clear() {
//
//    }
}
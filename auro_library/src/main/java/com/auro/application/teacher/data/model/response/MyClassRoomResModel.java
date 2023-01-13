package com.auro.application.teacher.data.model.response;


import com.auro.application.teacher.data.model.response.MyClassRoomTeacherResModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class MyClassRoomResModel {

    @SerializedName("APImyclassroomteacher")
    @Expose
    private MyClassRoomTeacherResModel teacherResModel;


    public MyClassRoomTeacherResModel getTeacherResModel() {
        return teacherResModel;
    }

    public void setTeacherResModel(MyClassRoomTeacherResModel teacherResModel) {
        this.teacherResModel = teacherResModel;
    }
}
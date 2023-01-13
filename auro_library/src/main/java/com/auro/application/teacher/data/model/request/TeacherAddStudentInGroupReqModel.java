package com.auro.application.teacher.data.model.request;

import java.util.List;
import javax.annotation.Generated;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@Generated("jsonschema2pojo")
public class TeacherAddStudentInGroupReqModel {

    @SerializedName("group_id")
    @Expose
    private Integer groupId;
    @SerializedName("user_ids")
    @Expose
    private List<String> userIds = null;

    @SerializedName("user_id")
    @Expose
    private String userId = null;

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
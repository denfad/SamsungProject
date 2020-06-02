package ru.denfad.dbuniversity.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Group {

    @SerializedName("groupId")
    @Expose
    private int groupId;
    @SerializedName("faculty")
    @Expose
    private String faculty;

    public Group(int groupId, String faculty) {
        this.groupId = groupId;
        this.faculty = faculty;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getFaculty() {
        return faculty;
    }

    public void setFaculty(String faculty) {
        this.faculty = faculty;
    }
}

package ru.denfad.dbuniversity.model;

public class Group {

    private int groupId;
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

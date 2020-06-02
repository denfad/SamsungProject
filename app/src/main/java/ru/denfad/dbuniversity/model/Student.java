package ru.denfad.dbuniversity.model;

import com.google.gson.annotations.SerializedName;

public class Student {

    private int student_id;
    private String name;
    private String secondName;
    private String middleName;
    private String birthDate;
    private int groupId;

    public Student(int student_id, String name, String secondName, String middleName, String birthDate, int groupId) {
        this.student_id=student_id;
        this.name = name;
        this.secondName = secondName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.groupId = groupId;
    }

    public Student(String name, String secondName, String middleName, String birthDate, int groupId) {
        this.student_id=student_id;
        this.name = name;
        this.secondName = secondName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.groupId = groupId;
    }

    public Student( String name, String secondName, String middleName, String birthDate) {
        this.name = name;
        this.secondName = secondName;
        this.middleName = middleName;
        this.birthDate = birthDate;
    }
    public int getStudent_id() {
        return student_id;
    }

    public void setStudent_id(int student_id) {
        this.student_id = student_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }
}

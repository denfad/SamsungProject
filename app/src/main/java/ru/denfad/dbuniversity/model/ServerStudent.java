package ru.denfad.dbuniversity.model;

public class ServerStudent {

    private int student_id;
    private String name;
    private String secondName;
    private String middleName;
    private String birthDate;
    private Group group;


    public ServerStudent(int student_id, String name, String secondName, String middleName, String birthDate, Group group) {
        this.student_id=student_id;
        this.name = name;
        this.secondName = secondName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.group = group;
    }

    public ServerStudent(String name, String secondName, String middleName, String birthDate, Group group) {
        this.name = name;
        this.secondName = secondName;
        this.middleName = middleName;
        this.birthDate = birthDate;
        this.group = group;
    }

    public ServerStudent() {
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

    public Group getGroup() {
        return this.group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }
}

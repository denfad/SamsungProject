package ru.denfad.dbuniversity.DAO;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.denfad.dbuniversity.model.Group;
import ru.denfad.dbuniversity.model.ServerStudent;
import ru.denfad.dbuniversity.model.Student;

public interface DbConnector {
    int updateGroup(int groupId, String faculty);

    //UPDATE STUDENTS
    int updateStudent(int id, String name, String secondName, String middleName, String birthDate, int group_id);

    //DELETE GROUP
    void deleteGroup(int group_id);

    //DELETE STUDENT
    void deleteStudent(int id);

    //GET STUDENT BY ID
    Student selectStudent(int id);

    Group selectGroup(int id);
    //ADD NEW STUDENT
    long insertStudent(String name, String secondName, String middleName, String birthDate, int group_id);

    //ADD NEW GROUP
    long insertGroup(int groupId, String faculty);

    //GET LIST ALL OF STUDENTS
    List<Student> selectAllStudents();
    //GET LIST OF ALL GROUPS
    List<Group> selectAllGroups();

}

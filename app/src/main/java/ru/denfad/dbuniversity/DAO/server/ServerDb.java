package ru.denfad.dbuniversity.DAO.server;

import android.content.ContentValues;
import android.database.Cursor;
import android.widget.ListView;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.denfad.dbuniversity.DAO.DbConnector;
import ru.denfad.dbuniversity.model.Group;
import ru.denfad.dbuniversity.model.ServerStudent;
import ru.denfad.dbuniversity.model.Student;



public class ServerDb implements DbConnector {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    //UPDATE GROUP
    @Override
    public int updateGroup(int groupId, String faculty){
       return 0;
    }

    //UPDATE STUDENTS
    @Override
    public int updateStudent(int id, String name, String secondName, String middleName, String birthDate, int group_id){
        return 0;
    }

    //DELETE GROUP
    @Override
    public void deleteGroup(int group_id){
        class MyThread extends Thread {

            int group_id;

            MyThread(int id){
                group_id=id;
            }
            @Override
            public void run() {
                OkHttpClient httpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/group/delete/" + group_id)
                        .delete()
                        .build();

                try (Response response = httpClient.newCall(request).execute()) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        MyThread m = new MyThread(group_id);
        m.start();

    }

    @Override
    //DELETE STUDENT
    public void deleteStudent(int id){
        class MyThread extends Thread {
            int id;
            MyThread(int id){
                this.id=id;
            }

            @Override
            public void run() {
                OkHttpClient httpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/student/delete/"+id)
                        .delete()
                        .build();

                try(Response response = httpClient.newCall(request).execute()) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        MyThread m = new MyThread(id);
        m.start();

    }

    //GET STUDENT BY ID
    @Override
    public Student selectStudent(int id){
        class MyThread extends Thread{
            int id;
            Student student;
            MyThread(int id){
                this.id=id;
            }
            @Override
            public void run() {
                OkHttpClient httpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/student/"+id)
                        .get()
                        .build();

                ServerStudent serverStudent = null;
                try(Response response = httpClient.newCall(request).execute()) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    serverStudent = gsonBuilder.create().fromJson(response.body().string(), ServerStudent.class);

                } catch (IOException e) {
                    e.printStackTrace();

                }
                student = cast(serverStudent);
            }
            Student get(){
                return student;
            }
        }
        MyThread m = new MyThread(id);
        m.start();
        try {
            m.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m.get();
    }

    @Override
    public Group selectGroup(int id){
        class MyThread extends Thread{
            int id;
            Group group;

            MyThread(int id){
                this.id=id;
            }
            @Override
            public void run() {
                OkHttpClient httpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/group/"+id)
                        .get()
                        .build();

                Group group = null;
                try(Response response = httpClient.newCall(request).execute()) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    group = gsonBuilder.create().fromJson(response.body().string(), Group.class);

                } catch (IOException e) {
                    e.printStackTrace();

                }
                this.group = group;
            }

            Group get(){
                return this.group;
            }
        }
        MyThread m = new MyThread(id);
        m.start();
        try {
            m.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m.get();
    }

    //ADD NEW STUDENT
    @Override
    public long insertStudent(String name, String secondName, String middleName, String birthDate, int group_id){
        class MyThread extends Thread {

            String name,secondName,middleName,birthDate;
            int group_id;

            MyThread(String name, String secondName, String middleName, String birthDate, int group_id){
                this.name=name;
                this.secondName=secondName;
                this.middleName=middleName;
                this.birthDate=birthDate;
                this.group_id=group_id;
            }
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();

                GsonBuilder gsonBuilder = new GsonBuilder();

                String json = gsonBuilder.create().toJson(new Student(name,secondName,middleName,birthDate), Student.class);
                RequestBody body = RequestBody.create(JSON, json);
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/student/"+group_id)
                        .put(body)
                        .build();
                try(Response response = client.newCall(request).execute()) {
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        }
        MyThread m = new MyThread(name,secondName,middleName,birthDate,group_id);
        m.start();
        return 1L;
    }

    //ADD NEW GROUP
    @Override
    public long insertGroup(final int groupId, String faculty) {
        class MyThread extends Thread{
            int group_id;
            String faculty;

            MyThread(int groupId, String faculty){
                this.group_id=groupId;
                this.faculty=faculty;
            }

            @Override
            public void run(){
                OkHttpClient client = new OkHttpClient();

                GsonBuilder gsonBuilder = new GsonBuilder();

                String json = gsonBuilder.create().toJson(new Group(groupId,faculty), Student.class);
                RequestBody body = RequestBody.create(json,JSON);
                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/group/")
                        .put(body)
                        .build();
                try(Response response = client.newCall(request).execute()) {
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        MyThread m = new MyThread(groupId,faculty);
        m.start();
        return 1L;
    }

    //GET LIST ALL OF STUDENTS
    @Override
    public List<Student> selectAllStudents(){
        class MyThread extends Thread{
            List<Student> students;
            @Override
            public void run() {
                OkHttpClient httpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/student/")
                        .get()
                        .build();

                List<ServerStudent> serverStudents = new ArrayList<>();
                try(Response response = httpClient.newCall(request).execute()) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    serverStudents = Arrays.asList(gsonBuilder.create().fromJson(response.body().string(), ServerStudent[].class));

                } catch (IOException e) {
                    e.printStackTrace();

                }

                this.students = cast(serverStudents);
            }
            List<Student> get(){
                return students;
            }
        }
        MyThread m = new MyThread();
        m.start();
        try {
            m.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m.get();
    }
    //GET LIST OF ALL GROUPS
    @Override
    public List<Group> selectAllGroups(){
        class MyThread extends Thread{
            List<Group> groups;
            @Override
            public void run() {
                OkHttpClient httpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/group/")
                        .get()
                        .build();

                List<Group> groups = new ArrayList<>();
                try(Response response = httpClient.newCall(request).execute()) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    groups = Arrays.asList(gsonBuilder.create().fromJson(response.body().string(), Group[].class));

                } catch (IOException e) {
                    e.printStackTrace();

                }
                this.groups = groups;
            }
            List<Group> get(){
                return groups;
            }
        }
        MyThread m = new MyThread();
        m.start();
        try {
            m.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m.get();

    }

    private Student cast(ServerStudent serverStudent){
        return new Student(
                serverStudent.getStudent_id(),
                serverStudent.getName(),
                serverStudent.getSecondName(),
                serverStudent.getMiddleName(),
                serverStudent.getBirthDate(),
                serverStudent.getGroup().getGroupId());
    }

    private List<Student> cast(List<ServerStudent> serverStudents){
        List<Student> students = new ArrayList<>();
        for(ServerStudent serverStudent:serverStudents) {
            students.add(new Student(
                    serverStudent.getStudent_id(),
                    serverStudent.getName(),
                    serverStudent.getSecondName(),
                    serverStudent.getMiddleName(),
                    serverStudent.getBirthDate(),
                    serverStudent.getGroup().getGroupId()));
        }
        return students;
    }

}

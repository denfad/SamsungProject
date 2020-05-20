package ru.denfad.dbuniversity.DAO.server;

import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.denfad.dbuniversity.model.ServerStudent;
import ru.denfad.dbuniversity.model.Student;


public class ServerDbFilters {

    public List<Student> selectStudentsByGroup(int group_id){
        class MyThread extends Thread{
            List<Student> students;
            int group_id;

            MyThread(int id){
                group_id=id;
            }
            @Override
            public void run() {
                OkHttpClient httpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/student/group/"+group_id)
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
        MyThread m = new MyThread(group_id);
        m.start();
        try {
            m.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m.get();
    }

    public boolean checkIsEmptyGroup(int group_id){
        class MyThread extends Thread{
            boolean b;
            int group_id;

            MyThread(int id){
                group_id=id;
            }
            @Override
            public void run() {
                OkHttpClient httpClient = new OkHttpClient();

                Request request = new Request.Builder()
                        .url("http://10.0.2.2:8080/group/isempty/"+group_id)
                        .get()
                        .build();

                Boolean b = false;
                try(Response response = httpClient.newCall(request).execute()) {
                    GsonBuilder gsonBuilder = new GsonBuilder();
                    b = gsonBuilder.create().fromJson(response.body().string(), Boolean.class);

                } catch (IOException e) {
                    e.printStackTrace();

                }

                this.b = b;
            }
            boolean get(){
                return b;
            }
        }
        MyThread m = new MyThread(group_id);
        m.start();
        try {
            m.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return m.get();

    }

    private List<Student> cast(List<ServerStudent> serverStudents){
        ArrayList<Student> students = new ArrayList<>();
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

package ru.denfad.dbuniversity.DAO;

import android.content.ContentValues;
import android.database.Cursor;

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
import ru.denfad.dbuniversity.model.Group;
import ru.denfad.dbuniversity.model.ServerStudent;
import ru.denfad.dbuniversity.model.Student;


//TODO: связать UI и HTTP запросы к базе данных через интерфейс взаимодействия. Выполнять запросы в отдельном потоке
class ServerDb implements DbConnector {

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
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/group/delete/"+group_id)
                .delete()
                .build();

        try(Response response = httpClient.newCall(request).execute()) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    //DELETE STUDENT
    public void deleteStudent(int id){
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

    //GET STUDENT BY ID
    @Override
    public Student selectStudent(int id){
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

        return cast(serverStudent);

    }

    @Override
    public Group selectGroup(int id){
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

        return group;
    }

    //ADD NEW STUDENT
    @Override
    public long insertStudent(String name, String secondName, String middleName, String birthDate, int group_id){
        OkHttpClient client = new OkHttpClient();

        GsonBuilder gsonBuilder = new GsonBuilder();

        String json = gsonBuilder.create().toJson(new Student(name,secondName,middleName,birthDate), Student.class);
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                    .url("http://10.0.2.2:8080/student/"+group_id)
                    .put(body)
                    .build();
        return 1L;
    }

    //ADD NEW GROUP
    @Override
    public long insertGroup(int groupId, String faculty) {
        OkHttpClient client = new OkHttpClient();

        GsonBuilder gsonBuilder = new GsonBuilder();

        String json = gsonBuilder.create().toJson(new Group(groupId,faculty), Student.class);
        RequestBody body = RequestBody.create(json,JSON);
        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/group/")
                .put(body)
                .build();
        return 1L;
    }

    //GET LIST ALL OF STUDENTS
    @Override
    public ArrayList<Student> selectAllStudents(){
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

        return cast(serverStudents);
    }
    //GET LIST OF ALL GROUPS
    @Override
    public ArrayList<Group> selectAllGroups(){
        OkHttpClient httpClient = new OkHttpClient();

        Request request = new Request.Builder()
                .url("http://10.0.2.2:8080/group/")
                .get()
                .build();

        ArrayList<Group> groups = new ArrayList<>();
        try(Response response = httpClient.newCall(request).execute()) {
            GsonBuilder gsonBuilder = new GsonBuilder();
            groups = (ArrayList<Group>) Arrays.asList(gsonBuilder.create().fromJson(response.body().string(), Group[].class));

        } catch (IOException e) {
            e.printStackTrace();

        }

        return groups;
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

    private ArrayList<Student> cast(List<ServerStudent> serverStudents){
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

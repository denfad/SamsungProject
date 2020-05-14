package ru.denfad.dbuniversity.DAO;

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


//TODO: связать UI и HTTP запросы к базе данных через интерфейс взаимодействия. Выполнять запросы в отдельном потоке
public class ServerDbFilters {

    public ArrayList<Student> selectStudentsByGroup(int group_id){
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

        return cast(serverStudents);

    }

    public boolean checkIsEmptyGroup(int group_id){
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

        return b;
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

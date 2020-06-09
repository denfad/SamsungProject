package ru.denfad.dbuniversity.DAO.network;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.denfad.dbuniversity.model.Group;
import ru.denfad.dbuniversity.model.ServerStudent;
import ru.denfad.dbuniversity.model.Student;

public interface JSONPlaceHolderApi {

    @DELETE("/group/delete/{id}")
    public Call<String> deleteGroup(@Path("id") int id);

    @DELETE("/student/delete/{id}")
    public Call<String> deleteStudent(@Path("id") int id);

    @GET("/student/{id}")
    public Call<ServerStudent> selectStudent(@Path("id") int id);

    @GET("/group/{id}")
    public Call<Group> selectGroup(@Path("id") int id);

    @PUT("/student/{id}")
    public Call<String> addStudent(@Path("id") int groupId, @Body Student s);

    @PUT("/student/update")
    public Call<String> updateStudent(@Body ServerStudent s);

    @PUT("/group/")
    public Call<String> addGroup(@Body Group group);

    @GET("/student/")
    public Call<List<ServerStudent>> getAllStudents(@Query("sortType") String type);

    @GET("/group/")
    public Call<List<Group>> getAllGroups();

    @GET("/student/group/{id}")
    public Call<List<ServerStudent>> selectStudentByGroup(@Path("id") int groupId);

    @GET("/group/isempty/{id}")
    public Call<Boolean> checkIsEmptyGroup(@Path("id") int groupId);

    @POST("/student/search")
    public Call<List<ServerStudent>> searchStudent(@Body String query);
}

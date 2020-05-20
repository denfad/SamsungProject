package ru.denfad.dbuniversity.DAO;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ru.denfad.dbuniversity.DAO.client.DbFilters;
import ru.denfad.dbuniversity.DAO.client.DbWorker;
import ru.denfad.dbuniversity.DAO.server.ServerDb;
import ru.denfad.dbuniversity.DAO.server.ServerDbFilters;
import ru.denfad.dbuniversity.model.Group;
import ru.denfad.dbuniversity.model.Student;

public class DbService {

    private ServerDb dbWorker;
    private ServerDbFilters dbFilters;

    public DbService(Context context){
        dbWorker = new ServerDb();
        dbFilters = new ServerDbFilters();
    }

    public Student getStudentByID(int id){
        return dbWorker.selectStudent(id);
    }

    public Group getGroupByID(int id){
        return dbWorker.selectGroup(id);
    }

    public List<Student> getAllStudents(){
        return  dbWorker.selectAllStudents();
    }

    public List<Group> getAllGroups(){
        return dbWorker.selectAllGroups();
    }

    public void deleteStudent(int id){
        dbWorker.deleteStudent(id);
    }

    public void deleteStudent(Student student){
        dbWorker.deleteStudent(student.getStudent_id());
    }

    public boolean deleteGroup(int id){
        if(dbFilters.checkIsEmptyGroup(id)){
            dbWorker.deleteGroup(id);
            return true;
        }
        else return  false;
    }

    public void deleteGroupAnyway(int id){
        dbWorker.deleteGroup(id);
    }

    public boolean deleteGroup(Group group){
        if(dbFilters.checkIsEmptyGroup(group.getGroupId())){
            dbWorker.deleteGroup(group.getGroupId());
            return true;
        }
        else return  false;
    }

    public void updateStudent(Student student){
        dbWorker.updateStudent(student.getStudent_id(),student.getName(),student.getSecondName(),student.getMiddleName(),student.getBirthDate(),student.getGroupId());
    }

    public void updateGroup(Group group){
        dbWorker.updateGroup(group.getGroupId(),group.getFaculty());
    }

    public void addStudent(Student student){
        dbWorker.insertStudent(student.getName(),student.getSecondName(),student.getMiddleName(),student.getBirthDate(),student.getGroupId());
    }

    public void addGroup(Group group){
        dbWorker.insertGroup(group.getGroupId(),group.getFaculty());
    }

    public List<Student> findStudentsByGroup(Group group){
        return dbFilters.selectStudentsByGroup(group.getGroupId());
    }

    public List<Student> findStudentsByGroup(int group_id){
        return dbFilters.selectStudentsByGroup(group_id);
    }


}

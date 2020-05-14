package ru.denfad.dbuniversity.DAO;

import android.content.ContentValues;
import android.content.Context;

import java.util.ArrayList;

import ru.denfad.dbuniversity.model.Group;
import ru.denfad.dbuniversity.model.Student;

public class DbService {

    private DbWorker dbWorker;
    private DbFilters dbFilters;

    public DbService(Context context){
        dbWorker = new DbWorker(context);
        dbFilters = new DbFilters(dbWorker);
    }

    public Student getStudentByID(int id){
        return dbWorker.selectStudent(id);
    }

    public Group getGroupByID(int id){
        return dbWorker.selectGroup(id);
    }

    public ArrayList<Student> getAllStudents(){
        return  dbWorker.selectAllStudents();
    }

    public ArrayList<Group> getAllGroups(){
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

    public ArrayList<Student> findStudentsByGroup(Group group){
        return dbFilters.selectStudentsByGroup(group.getGroupId());
    }

    public ArrayList<Student> findStudentsByGroup(int group_id){
        return dbFilters.selectStudentsByGroup(group_id);
    }


}

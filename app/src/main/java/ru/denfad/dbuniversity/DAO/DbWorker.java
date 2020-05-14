package ru.denfad.dbuniversity.DAO;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

import ru.denfad.dbuniversity.model.Group;
import ru.denfad.dbuniversity.model.Student;


class DbWorker extends DbStructure implements DbConnector{

    private SQLiteDatabase mDataBase;

    DbWorker(Context context){
        OpenHelper mOpenHelper = new OpenHelper(context);
        mDataBase = mOpenHelper.getWritableDatabase();
    }

    //UPDATE GROUP
    @Override
    public int updateGroup(int groupId, String faculty){
        ContentValues cv=new ContentValues();

        cv.put(GROUP_FACULTY, faculty);
        return mDataBase.update(TABLE_GROUP, cv, GROUP_ID+" =?", new String[]{String.valueOf(groupId)});
    }

    //UPDATE STUDENTS
    @Override
    public int updateStudent(int id, String name, String secondName, String middleName, String birthDate, int group_id){
        ContentValues cv = new ContentValues();

        cv.put(STUDENT_NAME,name);
        cv.put(STUDENT_SECOND_NAME, secondName);
        cv.put(STUDENT_MIDDLE_NAME,middleName);
        cv.put(STUDENT_BIRTH_DATE, birthDate);
        cv.put(STUDENT_GROUP_ID, group_id);
        return mDataBase.update(TABLE_STUDENTS,cv,STUDENT_ID+" =?", new String[]{String.valueOf(id)});
    }

    //DELETE GROUP
    public void deleteGroup(int group_id){
        mDataBase.delete(TABLE_GROUP, GROUP_ID+" =?", new String[]{String.valueOf(group_id)});
    }

    //DELETE STUDENT
    @Override
    public void deleteStudent(int id){
        mDataBase.delete(TABLE_STUDENTS, STUDENT_ID+" =?", new String[]{String.valueOf(id)});
    }

    //GET STUDENT BY ID
    @Override
    public Student selectStudent(int id){
        Cursor mCursor = mDataBase.query(TABLE_STUDENTS, null, STUDENT_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        int studentId = mCursor.getInt(0); //column id
        String name = mCursor.getString(1); //column name
        String secondName = mCursor.getString(2); //column second name
        String middleName = mCursor.getString(3); //column middle name
        String birthDate = mCursor.getString(4); //column birth date
        int group_id = mCursor.getInt(5); //column group_id
        return new Student(studentId,name,secondName,middleName,birthDate,group_id);
    }

    @Override
    public Group selectGroup(int id){
        Cursor mCursor = mDataBase.query(TABLE_GROUP, null, GROUP_ID + " = ?", new String[]{String.valueOf(id)}, null, null, null);

        mCursor.moveToFirst();
        int groupId = mCursor.getInt(0); //column id
        String faculty = mCursor.getString(1); //column name

        return new Group(groupId,faculty);
    }

    //ADD NEW STUDENT
    @Override
    public long insertStudent(String name, String secondName, String middleName, String birthDate, int group_id){
        ContentValues cv=new ContentValues();
        cv.put(STUDENT_NAME,name);
        cv.put(STUDENT_SECOND_NAME, secondName);
        cv.put(STUDENT_MIDDLE_NAME,middleName);
        cv.put(STUDENT_BIRTH_DATE, birthDate);
        cv.put(STUDENT_GROUP_ID, group_id);
        return mDataBase.insert(TABLE_STUDENTS, null, cv);
    }

    //ADD NEW GROUP
    @Override
    public long insertGroup(int groupId, String faculty) {
        ContentValues cv=new ContentValues();
        cv.put(GROUP_ID, groupId);
        cv.put(GROUP_FACULTY, faculty);
        return mDataBase.insert(TABLE_GROUP, null, cv);
    }

    //GET LIST ALL OF STUDENTS
    @Override
    public ArrayList<Student> selectAllStudents(){
        Cursor mCursor = mDataBase.query(TABLE_STUDENTS, null, null, null, null, null, null);

        ArrayList<Student> arr = new ArrayList<Student>();
        mCursor.moveToFirst();

        if (!mCursor.isAfterLast()) {
            do {
                int studentId = mCursor.getInt(0); //column id
                String name = mCursor.getString(1); //column name
                String secondName = mCursor.getString(2); //column second name
                String middleName = mCursor.getString(3); //column middle name
                String birthDate = mCursor.getString(4); //column birth date
                int group_id = mCursor.getInt(5); //column group_id
                arr.add(new Student(studentId,name,secondName,middleName,birthDate,group_id));
            } while (mCursor.moveToNext());
        }
        return arr;

    }
    //GET LIST OF ALL GROUPS
    @Override
    public ArrayList<Group> selectAllGroups(){
        Cursor mCursor = mDataBase.query(TABLE_GROUP, null, null, null, null, null, null);

        ArrayList<Group> arr = new ArrayList<Group>();
        mCursor.moveToFirst();

        if (!mCursor.isAfterLast()) {
            do {
                int groupId = mCursor.getInt(0); //column group_id
                String faculty = mCursor.getString(1); //column faculty
                arr.add(new Group(groupId,faculty));
            } while (mCursor.moveToNext());
        }
        return arr;
    }

    public SQLiteDatabase getDataBase() {
        return mDataBase;
    }

    private class OpenHelper extends SQLiteOpenHelper {

        OpenHelper(Context context){
            super(context,DATABASE_NAME,null,DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase sqLiteDatabase) {
            //CREATE TABLE students;
            String query_students = "CREATE TABLE "+TABLE_STUDENTS+" ("+
                    STUDENT_ID+" INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    STUDENT_NAME+" TEXT, "+
                    STUDENT_SECOND_NAME+" TEXT, "+
                    STUDENT_MIDDLE_NAME+" TEXT, "+
                    STUDENT_BIRTH_DATE+" TEXT, "+
                    STUDENT_GROUP_ID+" INT REFERENCES "+TABLE_GROUP+"("+GROUP_ID+"));";
            Log.d("CREATE TABLE", query_students);

            //CREATE TABLE groups
            String query_group = "CREATE TABLE "+TABLE_GROUP+" ("+
                    GROUP_ID+" INTEGER PRIMARY KEY, "+
                    GROUP_FACULTY+" TEXT);";
            Log.d("CREATE TABLE", query_group);

            sqLiteDatabase.execSQL(query_group);
            sqLiteDatabase.execSQL(query_students);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_STUDENTS);
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_GROUP);
            onCreate(sqLiteDatabase);
        }
    }
}

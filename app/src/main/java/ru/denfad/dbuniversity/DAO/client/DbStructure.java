package ru.denfad.dbuniversity.DAO.client;

class DbStructure {
    //DB DESIGN
    public static final String DATABASE_NAME = "university.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_GROUP = "groups";
    public static final String TABLE_STUDENTS = "students";

    //STUDENT TABLE
    public static final String STUDENT_ID="student_id";
    public static final String STUDENT_NAME = "name";
    public static final String STUDENT_SECOND_NAME = "second_name";
    public static final String STUDENT_MIDDLE_NAME = "middle_name";
    public static final String STUDENT_BIRTH_DATE = "birth_date";
    public static final String STUDENT_GROUP_ID = "group_id";

    //GROUP TABLE
    public static final String GROUP_ID = "group_id";
    public static final String GROUP_FACULTY = "faculty";
}

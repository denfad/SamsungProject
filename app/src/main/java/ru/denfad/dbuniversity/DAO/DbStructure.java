package ru.denfad.dbuniversity.DAO;

class DbStructure {
    //DB DESIGN
    static final String DATABASE_NAME = "university.db";
    static final int DATABASE_VERSION = 1;
    static final String TABLE_GROUP = "groups";
    static final String TABLE_STUDENTS = "students";

    //STUDENT TABLE
    static final String STUDENT_ID="student_id";
    static final String STUDENT_NAME = "name";
    static final String STUDENT_SECOND_NAME = "second_name";
    static final String STUDENT_MIDDLE_NAME = "middle_name";
    static final String STUDENT_BIRTH_DATE = "birth_date";
    static final String STUDENT_GROUP_ID = "group_id";

    //GROUP TABLE
    static final String GROUP_ID = "group_id";
    static final String GROUP_FACULTY = "faculty";
}

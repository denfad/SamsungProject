package ru.denfad.dbuniversity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import ru.denfad.dbuniversity.DAO.DbService;
import ru.denfad.dbuniversity.model.Group;
import ru.denfad.dbuniversity.model.Student;

public class StudentProfileActivity extends AppCompatActivity {

    private Intent intent;
    private DbService dbService;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_activity);

        dbService=new DbService(getApplicationContext());

        intent=getIntent();

        final Student student = dbService.getStudentByID(intent.getIntExtra("student_id",Integer.MAX_VALUE));



        TextView studentName = findViewById(R.id.student_profile_name);
        TextView studentMiddleName = findViewById(R.id.student_profile_middle_name);
        TextView studentGroupId = findViewById(R.id.student_profile_group_id);
        TextView studentBirthDate = findViewById(R.id.student_profile_birth_date);
        TextView studentFaculty = findViewById(R.id.student_profile_faculty);
        TextView studentId = findViewById(R.id.student_profile_id);

        studentName.setText(student.getName()+" "+student.getSecondName());
        studentMiddleName.setText(student.getMiddleName());
        studentGroupId.setText("Номер группы: "+String.valueOf(student.getGroupId()));
        studentBirthDate.setText("Дата рождения: "+student.getBirthDate());
        studentId.setText("@"+String.valueOf(student.getStudent_id()));

        Group group = dbService.getGroupByID(student.getGroupId());
        studentFaculty.setText("Факультет: "+group.getFaculty());

        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageButton delete = findViewById(R.id.delete_student);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbService.deleteStudent(student.getStudent_id());
                Intent intent1;
                switch(intent.getStringExtra("activity")){
                    case "main":
                       intent1=new Intent(getApplicationContext(),MainActivity.class);
                       intent1.putExtra("active_list","student");
                       startActivity(intent1);
                       break;
                    case "student_list":
                        intent1=new Intent(getApplicationContext(),StudentsListActivity.class);
                        intent1.putExtra("group_id",student.getGroupId());
                        startActivity(intent1);
                        break;
                }
            }
        });
    }


}

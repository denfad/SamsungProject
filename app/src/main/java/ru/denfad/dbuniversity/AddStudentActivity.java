package ru.denfad.dbuniversity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import ru.denfad.dbuniversity.DAO.DbService;
import ru.denfad.dbuniversity.model.Student;

public class AddStudentActivity extends AppCompatActivity {

    private DbService dbService;
    private EditText studentGroupId;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_student_layout);
        dbService = new DbService(getApplicationContext());

        intent = getIntent();
        final EditText studentName=findViewById(R.id.name);
        final EditText studentSecondName = findViewById(R.id.second_name);
        final EditText studentMiddleName = findViewById(R.id.middle_name);
        final EditText studentBirthDate = findViewById(R.id.birth_date);
        studentGroupId = findViewById(R.id.group_id);

        if(!String.valueOf(intent.getIntExtra("group_id",Integer.MAX_VALUE)).equals("0")){
            studentGroupId.setText(String.valueOf(intent.getIntExtra("group_id",Integer.MAX_VALUE)));
        }

        Button addButton = findViewById(R.id.add_button);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbService.addStudent(new Student(
                        studentName.getText().toString(),
                        studentSecondName.getText().toString(),
                        studentMiddleName.getText().toString(),
                        studentBirthDate.getText().toString(),
                        Integer.parseInt(studentGroupId.getText().toString())));
                Intent intent1 = new Intent(getApplicationContext(),StudentsListActivity.class);
                intent1.putExtra("group_id",Integer.valueOf(studentGroupId.getText().toString()));
                startActivity(intent1);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        int a = intent.getIntExtra("group_id",Integer.MAX_VALUE);
        Intent intent1;
        if(a!=0) {
            intent1 = new Intent(getApplicationContext(),StudentsListActivity.class);
            intent1.putExtra("group_id", a);
        }
        else{
            intent1= new Intent(getApplicationContext(), MainActivity.class);
            intent1.putExtra("active_list","students");
        }
        startActivity(intent1);
    }
}

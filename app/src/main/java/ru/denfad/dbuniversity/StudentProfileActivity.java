package ru.denfad.dbuniversity;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.denfad.dbuniversity.DAO.DbService;
import ru.denfad.dbuniversity.DAO.network.NetworkService;
import ru.denfad.dbuniversity.model.Group;
import ru.denfad.dbuniversity.model.ServerStudent;
import ru.denfad.dbuniversity.model.Student;

public class StudentProfileActivity extends AppCompatActivity {

    private Intent intent;
    private DbService dbService;
    private int studentId;
    private int groupId;
    private Student student;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.student_profile_activity);

        dbService=new DbService(getApplicationContext());

        intent=getIntent();
        studentId=intent.getIntExtra("student_id",Integer.MAX_VALUE);
        updateStudentProfile(studentId);


        ImageButton back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        ImageButton edit = findViewById(R.id.student_edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditStudentDialog editStudentDialog = new EditStudentDialog(StudentProfileActivity.this);
                editStudentDialog.show();
            }
        });

        ImageButton delete = findViewById(R.id.delete_student);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(StudentProfileActivity.this);
                builder.setTitle("Удаление студента");
                builder.setMessage("Вы правда хотите удалить студента?");
                builder.setCancelable(true);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbService.deleteStudent(studentId);
                        Intent intent1;
                        switch(intent.getStringExtra("activity")){
                            case "main":
                                intent1=new Intent(getApplicationContext(),MainActivity.class);
                                intent1.putExtra("active_list","student");
                                startActivity(intent1);
                                break;
                            case "student_list":
                                intent1=new Intent(getApplicationContext(),StudentsListActivity.class);
                                intent1.putExtra("group_id",groupId);
                                startActivity(intent1);
                                break;
                        }
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }
                });
                builder.setNegativeButton("Отмена",new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss(); // Отпускает диалоговое окно
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    private void updateStudentProfile(int id){

        NetworkService.getInstance()
                .getJSONApi()
                .selectStudent(id)
                .enqueue(new Callback<ServerStudent>() {
                    @Override
                    public void onResponse(@NonNull Call<ServerStudent> call, @NonNull Response<ServerStudent> response) {
                        ServerStudent studentServer = response.body();
                        student = cast(studentServer);
                        TextView studentName = findViewById(R.id.student_profile_name);
                        TextView studentMiddleName = findViewById(R.id.student_profile_middle_name);
                        TextView studentGroupId = findViewById(R.id.student_profile_group_id);
                        TextView studentBirthDate = findViewById(R.id.student_profile_birth_date);
                        TextView studentFaculty = findViewById(R.id.student_profile_faculty);
                        TextView studentId = findViewById(R.id.student_profile_id);

                        studentName.setText(studentServer.getName()+" "+student.getSecondName());
                        studentMiddleName.setText(studentServer.getMiddleName());
                        studentGroupId.setText("Номер группы: "+String.valueOf(studentServer.getGroup().getGroupId()));
                        studentBirthDate.setText("Дата рождения: "+studentServer.getBirthDate());
                        studentId.setText("@"+String.valueOf(studentServer.getStudent_id()));
                        studentFaculty.setText("Факультет: "+studentServer.getGroup().getFaculty());
                        groupId = studentServer.getGroup().getGroupId();
                    }

                    @Override
                    public void onFailure(@NonNull Call<ServerStudent> call, @NonNull Throwable t) {
                        Log.e("groups", "fail");
                        t.printStackTrace();
                    }
                });


    }




    class EditStudentDialog extends Dialog {

        DbService dbService;

        public EditStudentDialog(@NonNull final Context context) {
            super(context);

            dbService = new DbService(context);

            this.setTitle("Редактирование студента");
            this.setContentView(R.layout.edit_student_dialog_view);


            final EditText studentName=findViewById(R.id.edit_name);
            final EditText studentSecondName = findViewById(R.id.edit_second_name);
            final EditText studentMiddleName = findViewById(R.id.edit_middle_name);
            final EditText studentBirthDate = findViewById(R.id.edit_birth_date);
            final EditText studentGroupId = findViewById(R.id.edit_group_id);

            studentName.setText(student.getName());
            studentSecondName.setText(student.getSecondName());
            studentMiddleName.setText(student.getMiddleName());
            studentBirthDate.setText(student.getBirthDate());
            studentGroupId.setText(String.valueOf(student.getGroupId()));

            Button edit = findViewById(R.id.edit_button);
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Student student1 =new Student(
                            student.getStudent_id(),
                            studentName.getText().toString(),
                            studentSecondName.getText().toString(),
                            studentMiddleName.getText().toString(),
                            studentBirthDate.getText().toString(),
                            Integer.parseInt(studentGroupId.getText().toString()));
                    dbService.updateStudent(student1);
                    updateStudentProfile(studentId);
                    Toast.makeText(getApplicationContext(),"You update student",Toast.LENGTH_SHORT).show();
                    EditStudentDialog.this.cancel();

                }
            });

            Button cancel = findViewById(R.id.not_edit_button);
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditStudentDialog.this.cancel();
                }
            });
        }
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
}

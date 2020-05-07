package ru.denfad.dbuniversity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import ru.denfad.dbuniversity.DAO.DbService;
import ru.denfad.dbuniversity.model.Group;

public class AddGroupActivity extends AppCompatActivity {

    public DbService dbService;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_group_layout);

        final EditText groupId=findViewById(R.id.group_num);
        final EditText faculty = findViewById(R.id.faculty);
        Button button = findViewById(R.id.add_button);

        dbService=new DbService(getApplicationContext());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbService.addGroup(new Group(Integer.valueOf(groupId.getText().toString()),faculty.getText().toString()));
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }
}

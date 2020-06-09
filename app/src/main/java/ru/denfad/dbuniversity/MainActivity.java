package ru.denfad.dbuniversity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import ru.denfad.dbuniversity.DAO.DbService;
import ru.denfad.dbuniversity.model.Group;
import ru.denfad.dbuniversity.model.Student;

public class MainActivity extends AppCompatActivity {

    public ListView listView;
    public List<Group> groups = new ArrayList<>();
    public List<Student> students = new ArrayList<>();

    public ArrayAdapter adapter;
    public DbService dbService;
    public Spinner sortSpinner;
    public Toolbar toolbar;
    public BottomSheetBehavior mBottomSheetBehavior;
    public boolean activeView = true; //true - all groups , false-all students;
    public String activeSortType = null;
    public String[] sortTypes = new String[]{null,"name","groupid"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar_actionbar);
        toolbar.setTitle("All groups");
        setSupportActionBar(toolbar);

        dbService=new DbService(getApplicationContext());
        listView = findViewById(R.id.models_list);

        final Intent intent = getIntent();
        if(Objects.equals(intent.getStringExtra("active_list"), "students")) activeView = false;
        Log.e("active view", String.valueOf(activeView));


        sortSpinner = findViewById(R.id.spinner);
        changeView();
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this,
                R.layout.spiner_item, R.id.sort_text, Arrays.asList(getResources().getStringArray(R.array.sort_types)));
        sortSpinner.setAdapter(spinnerAdapter);
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!activeView) {
                    Log.e("Set filters", "Set filters");
                    activeSortType = sortTypes[position];
                    adapter = new StudentsAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, students);
                    dbService.getAllStudents(listView, adapter, activeSortType);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                changeView();
            }
        });


        Button addingButton = findViewById(R.id.add);
        addingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(activeView) {
                    Intent intent = new Intent(getApplicationContext(), AddGroupActivity.class);
                    startActivity(intent);
                }
                else{
                    Intent intent = new Intent(getApplicationContext(), AddStudentActivity.class);
                    intent.putExtra("group_id",0);
                    startActivity(intent);
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(activeView){
                    Intent intent1 = new Intent(getApplicationContext(), StudentsListActivity.class);
                    intent1.putExtra("group_id", groups.get(i).getGroupId());
                    startActivity(intent1);
                }
                else{
                    Intent intent1 = new Intent(getApplicationContext(), StudentProfileActivity.class);
                    intent1.putExtra("student_id", students.get(i).getStudent_id());
                    intent.putExtra("activity","main");
                    startActivity(intent1);
                }
            }
        });

        View bottomSheet = findViewById(R.id.bottom_sheet);
        mBottomSheetBehavior=BottomSheetBehavior.from(bottomSheet);

        RadioGroup groupChangeView = findViewById(R.id.group_change_view);
        groupChangeView.check(R.id.group_view);
        groupChangeView.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.student_view:
                        activeView=false;
                        changeView();
                        break;
                    case R.id.group_view:
                        activeView=true;
                        changeView();
                        break;
                }
            }
        });

        List<String> s = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.sort_types)));
    }


    public class GroupAdapter extends ArrayAdapter<Group>{

        public GroupAdapter(@NonNull Context context, int resource, @NonNull List<Group> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent){
            final Group group = getItem(position);

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.group_item, null);

            GroupHolder holder = new GroupHolder();
            holder.group_id= convertView.findViewById(R.id.group);
            holder.faculty = convertView.findViewById(R.id.faculty_name);
            holder.delete=convertView.findViewById(R.id.delete_group);

            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(dbService.deleteGroup(group.getGroupId())){
                        Toast.makeText(getApplicationContext(),"Delete group",Toast.LENGTH_SHORT).show();
                        adapter=new GroupAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,groups);
                        dbService.getAllGroups(listView, adapter);
                    }
                    else  {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Удаление группы");
                        builder.setMessage("Группа не пустая, вы правда хотите удалить ее со студентами?");
                        builder.setCancelable(true);
                        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dbService.deleteGroupAnyway(group.getGroupId());
                                adapter=new GroupAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,groups);
                                dbService.getAllGroups(listView, adapter);
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

                }
            });

            holder.group_id.setText("Номер группы: " + String.valueOf(group.getGroupId()));
            holder.faculty.setText("Факультет: "+group.getFaculty());
            convertView.setTag(holder);

            return convertView;
        }
    }



    private static  class GroupHolder {
        public TextView group_id;
        public TextView faculty;
        public ImageButton delete;
    }

    public class StudentsAdapter extends ArrayAdapter<Student> {

        public StudentsAdapter(@NonNull Context context, int resource, @NonNull List<Student> objects) {
            super(context, resource, objects);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent){
            final Student student = getItem(position);

            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.student_item, null);

            StudentHolder holder = new StudentHolder();
            holder.fullName= convertView.findViewById(R.id.student_full_name);
            holder.birthDate = convertView.findViewById(R.id.student_birth_date);



            holder.fullName.setText(student.getName()+" "+student.getSecondName()+" "+student.getMiddleName());
            holder.birthDate.setText(student.getBirthDate());
            convertView.setTag(holder);

            return convertView;
        }
    }

    private static  class StudentHolder {
        public TextView fullName;
        public TextView birthDate;

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem mSearch = menu.findItem(R.id.action_search);
        SearchView mSearchView = (SearchView) mSearch.getActionView();
        mSearchView.setQueryHint("Search");
        mSearchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            }
        });
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                toolbar.setTitle("Найденные студенты");
                dbService.searchStudents(listView,new StudentsAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,students),query);
                return true;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                toolbar.setTitle("Найденные студенты");
                dbService.searchStudents(listView,new StudentsAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,students),newText);
                return true;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.action_filter:
                activeView= !activeView;
                mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                break;
        }
        return true;
    }


    public void changeView(){
        if(activeView){
            sortSpinner.setVisibility(Spinner.INVISIBLE);
            adapter=new GroupAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,groups);
            dbService.getAllGroups(listView, adapter);
            toolbar.setTitle("All groups");
            setSupportActionBar(toolbar);
        }
        else{
            sortSpinner.setVisibility(Spinner.VISIBLE);
            adapter=new StudentsAdapter(getApplicationContext(),android.R.layout.simple_list_item_1,students);
            dbService.getAllStudents(listView,adapter,activeSortType);
            toolbar.setTitle("All students");
            setSupportActionBar(toolbar);
        }
    }

    @Override
    public void onBackPressed() {
       System.exit(0);
    }
}


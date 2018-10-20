package net.net23.fahimabrar.teacherassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddStudent extends AppCompatActivity {
    EditText studentName,studentID;
    String tableName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        tableName = intent.getStringExtra("name");

        studentID = (EditText) findViewById(R.id.editTextID);
        studentName = (EditText) findViewById(R.id.editTextStudentName);


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabAddStudent);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    String ID,name;
                    ID = studentID.getText().toString();
                    name = studentName.getText().toString();

                    DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                    db.addStudent(name,ID,tableName);
                    db.addRoll(tableName+"_attendance",Integer.parseInt(ID));
                    Toast.makeText(getApplicationContext(),"Data Inserted",Toast.LENGTH_LONG).show();
                    finish();
                }catch (Exception E){
                    E.printStackTrace();
                }
            }
        });
    }

}

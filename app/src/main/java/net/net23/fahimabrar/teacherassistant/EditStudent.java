package net.net23.fahimabrar.teacherassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class EditStudent extends AppCompatActivity implements View.OnClickListener {

    EditText editTextID,editTextName,editTextCt1,editTextCt2,editTextCt3,editTextCt4,editTextCt5;
    ImageView imageViewLeft,imageViewRight;
    String name,ID,tableName,credit;
    ArrayList<Integer> studentId = new ArrayList<Integer>();
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Edit Marks");
        setSupportActionBar(toolbar);

        Intent i = getIntent();

        studentId = i.getIntegerArrayListExtra("ID");

        /*name = i.getStringExtra("name");
        ID = i.getStringExtra("ID");*/
        pos = i.getIntExtra("pos",0);
        System.out.println("POS = "+pos);
        ID = studentId.get(pos).toString();
        tableName = i.getStringExtra("table");
        credit = i.getStringExtra("credit");

        final DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

        String[] s = databaseHandler.getCtMarks(ID,tableName);

        editTextCt1 = (EditText) findViewById(R.id.editTextCt1);
        editTextCt2 = (EditText) findViewById(R.id.editTextCt2);
        editTextCt3 = (EditText) findViewById(R.id.editTextCt3);
        editTextCt4 = (EditText) findViewById(R.id.editTextCt4);
        editTextCt5 = (EditText) findViewById(R.id.editTextCt5);
        editTextName = (EditText) findViewById(R.id.editTextStudentNameE);
        editTextID = (EditText) findViewById(R.id.editTextIDE);

        imageViewLeft = (ImageView) findViewById(R.id.imageViewArrowLeft);
        imageViewRight = (ImageView) findViewById(R.id.imageViewArrowRight);

        if(pos==0)
            imageViewLeft.setVisibility(View.INVISIBLE);
        if(pos==studentId.size()-1)
            imageViewRight.setVisibility(View.INVISIBLE);

        System.out.println("Credit"+credit);

        imageViewRight.setOnClickListener(this);
        imageViewLeft.setOnClickListener(this);

        if(credit.equals("3")||credit.equals("4"))
            editTextCt4.setVisibility(View.VISIBLE);
        if(credit.equals("4"))
            editTextCt5.setVisibility(View.VISIBLE);


        editTextName.setText(s[0]);
        editTextID.setText(ID);

        if(s[2]!=null){
            editTextCt1.setText(s[2]);
        }
        if(s[3]!=null){
            editTextCt2.setText(s[3]);
        }
        if(s[4]!=null){
            editTextCt3.setText(s[4]);
        }
        if(s[5]!=null){
            editTextCt4.setText(s[5]);
        }
        if(s[6]!=null){
            editTextCt5.setText(s[6]);
        }

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ct1,ct2,ct3,ct4,ct5;
                ct1 = editTextCt1.getText().toString();
                ct2 = editTextCt2.getText().toString();
                ct3 = editTextCt3.getText().toString();
                ct4 = editTextCt4.getText().toString();
                ct5 = editTextCt5.getText().toString();
                name = editTextName.getText().toString();
                ID = editTextID.getText().toString();

                databaseHandler.setCtMarks(tableName,ID,name,ct1,ct2,ct3,ct4,ct5);
                finish();
            }
        });
    }

    @Override
    public void onClick(View v) {
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());
        String ct1,ct2,ct3,ct4,ct5;
        ct1 = editTextCt1.getText().toString();
        ct2 = editTextCt2.getText().toString();
        ct3 = editTextCt3.getText().toString();
        ct4 = editTextCt4.getText().toString();
        ct5 = editTextCt5.getText().toString();
        name = editTextName.getText().toString();
        ID = editTextID.getText().toString();

        databaseHandler.setCtMarks(tableName,ID,name,ct1,ct2,ct3,ct4,ct5);

        if(v.getId()==R.id.imageViewArrowRight){
            pos++;
        }else{
            pos--;
        }

        if(pos==0)
            imageViewLeft.setVisibility(View.INVISIBLE);
        else
            imageViewLeft.setVisibility(View.VISIBLE);

        if(pos==studentId.size()-1)
            imageViewRight.setVisibility(View.INVISIBLE);
        else
            imageViewRight.setVisibility(View.VISIBLE);

        ID = studentId.get(pos).toString();

        String[] s = databaseHandler.getCtMarks(ID,tableName);

        editTextName.setText(s[0]);
        editTextID.setText(ID);
        editTextCt1.setText("");
        if(s[2]!=null){
            editTextCt1.setText(s[2]);
        }
        editTextCt2.setText("");
        if(s[3]!=null){
            editTextCt2.setText(s[3]);
        }
        editTextCt3.setText("");
        if(s[4]!=null){
            editTextCt3.setText(s[4]);
        }
        editTextCt4.setText("");
        if(s[5]!=null){
            editTextCt4.setText(s[5]);
        }
        editTextCt5.setText("");
        if(s[6]!=null){
            editTextCt5.setText(s[6]);
        }
    }
}

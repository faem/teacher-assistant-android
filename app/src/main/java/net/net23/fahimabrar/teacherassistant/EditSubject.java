package net.net23.fahimabrar.teacherassistant;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteConstraintException;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.List;

public class EditSubject extends AppCompatActivity {

    EditText editTextSubjectNameE,editTextDeptNameE,editTextSubjectIdE,editTextSectionE;
    //RadioGroup radioGroupSeclectTypeE,radioGroupSemesterE,radioGroupCreditE;

    Spinner spinnerSemesterE,spinnerCreditE,spinnerTypeE;
    String[] Credit = {".75","1","1.5","2","3","4"};
    String[] Type = {"Theory","Lab"};
    String[] Semester = {"1st","2nd","3rd","4th","5th","6th","7th","8th"};

    Subject subject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //initialization

        editTextSubjectNameE = (EditText) findViewById(R.id.editTextSubjectNameE);
        editTextDeptNameE = (EditText) findViewById(R.id.editTextDeptNameE);
        editTextSubjectIdE = (EditText) findViewById(R.id.editTextSubjectIdE);
        editTextSectionE = (EditText) findViewById(R.id.editTextSectionE);
        spinnerCreditE = (Spinner) findViewById(R.id.spinnerCreditE);
        spinnerSemesterE = (Spinner) findViewById(R.id.spinnerSemesterE);
        spinnerTypeE = (Spinner) findViewById(R.id.spinnerTypeE);
        /*
        radioGroupCreditE = (RadioGroup) findViewById(R.id.radioGroupCreditE);
        radioGroupSemesterE = (RadioGroup) findViewById(R.id.radioGroupSemesterE);
        radioGroupSeclectTypeE = (RadioGroup) findViewById(R.id.radioGroupSelectTypeE);
        */


        Intent i = getIntent();
        int x = i.getIntExtra("pos",0);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        List<Subject> subjectList = databaseHandler.getDataFromDB();

        subject = subjectList.get(x);

        ArrayAdapter<String> adapter;
        /**Select Type **/

        adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,Type);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerTypeE.setAdapter(adapter);

        /**Credit **/
        adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,Credit);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCreditE.setAdapter(adapter);

        /**Semester **/
        adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,Semester);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSemesterE.setAdapter(adapter);

        editTextDeptNameE.setText(subject.getDeptName());
        editTextSubjectNameE.setText(subject.getSubjectName());
        editTextSubjectIdE.setText(subject.getSubjectId());

//        System.out.println(radioGroupType.getChildAt(0).getId());



        if(subject.getType().equals("Theory")){
            //radioGroupSeclectTypeE.check(radioGroupSeclectTypeE.getChildAt(0).getId());
            spinnerTypeE.setSelection(0);
        }else{
            spinnerTypeE.setSelection(1);
            //radioGroupSeclectTypeE.check(radioGroupSeclectTypeE.getChildAt(1).getId());
        }


        editTextSectionE.setText(subject.getSection());

        String y = subject.getSemester();

        x = Integer.parseInt(y.substring(0,1));
        spinnerSemesterE.setSelection(x-1);
        //radioGroupSemesterE.check(radioGroupSemesterE.getChildAt(x-1).getId());

        if(subject.getCredit().equals(".75")){
            spinnerCreditE.setSelection(0);
            // /radioGroupCreditE.check(radioGroupCreditE.getChildAt(0).getId());
        }else if(subject.getCredit().equals("1")){
            spinnerCreditE.setSelection(1);
            //radioGroupCreditE.check(radioGroupCreditE.getChildAt(1).getId());
        }else if(subject.getCredit().equals("1.5")){
            spinnerCreditE.setSelection(2);
            //radioGroupCreditE.check(radioGroupCreditE.getChildAt(2).getId());
        }else if(subject.getCredit().equals("2")){
            spinnerCreditE.setSelection(3);
            //radioGroupCreditE.check(radioGroupCreditE.getChildAt(3).getId());
        }else if(subject.getCredit().equals("3")){
            spinnerCreditE.setSelection(4);
            //radioGroupCreditE.check(radioGroupCreditE.getChildAt(4).getId());
        }else if(subject.getCredit().equals("4")){
            spinnerCreditE.setSelection(5);
            //radioGroupCreditE.check(radioGroupCreditE.getChildAt(5).getId());
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler databaseHandler = new DatabaseHandler(EditSubject.this);


                Subject s = new Subject();
                s.setSubjectName(editTextSubjectNameE.getText().toString());
                s.setSubjectId(editTextSubjectIdE.getText().toString());

                String type = Type[(int)spinnerTypeE.getSelectedItemId()];
                String semester = (String)spinnerSemesterE.getSelectedItem();
                String credit = (String) spinnerCreditE.getSelectedItem();

                /*int radioButtonID = radioGroupSeclectTypeE.getCheckedRadioButtonId();
                View radioButton = radioGroupSeclectTypeE.findViewById(radioButtonID);
                int radioId = radioGroupSeclectTypeE.indexOfChild(radioButton);
                RadioButton btn = (RadioButton) radioGroupSeclectTypeE.getChildAt(radioId);
                String type = (String) btn.getText();*/

                s.setType(type);

                System.out.println(s.getType()+"ok");


                /*radioButtonID = radioGroupSemesterE.getCheckedRadioButtonId();
                radioButton = radioGroupSemesterE.findViewById(radioButtonID);
                radioId = radioGroupSemesterE.indexOfChild(radioButton);
                btn = (RadioButton) radioGroupSemesterE.getChildAt(radioId);
                String Semester = (String) btn.getText();*/

                s.setSemester(semester);

                String section =  editTextSectionE.getText().toString();
                s.setSection(section);

                /*radioButtonID = radioGroupCreditE.getCheckedRadioButtonId();
                radioButton = radioGroupCreditE.findViewById(radioButtonID);
                radioId = radioGroupCreditE.indexOfChild(radioButton);
                btn = (RadioButton) radioGroupCreditE.getChildAt(radioId);
                String credit = (String) btn.getText();*/
                s.setCredit(credit);

                s.setDeptName(editTextDeptNameE.getText().toString());

                try{
                    databaseHandler.updateDatabase(s,subject);
                    Toast.makeText(EditSubject.this,"Data Updated",Toast.LENGTH_SHORT).show();

                    finish();
                }catch (SQLiteConstraintException e){
                    Snackbar.make(view,"Change Section!\nYou already have course with same section.",Snackbar.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();

        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("Alert");
        localBuilder.setMessage("Do you want to discard changes?");
        localBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                finish();
            }
        });

        localBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                paramAnonymousDialogInterface.dismiss();

            }
        });

        AlertDialog alert = localBuilder.create();
        alert.show();

        Button nbutton = alert.getButton(DialogInterface.BUTTON_NEGATIVE);
        nbutton.setTextColor(Color.BLUE);
        Button pbutton = alert.getButton(DialogInterface.BUTTON_POSITIVE);
        pbutton.setTextColor(Color.BLUE);

    }
}

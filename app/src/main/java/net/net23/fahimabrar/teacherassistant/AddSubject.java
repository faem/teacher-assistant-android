package net.net23.fahimabrar.teacherassistant;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.text.Text;
import com.roughike.swipeselector.SwipeItem;
import com.roughike.swipeselector.SwipeSelector;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URI;
import java.util.Scanner;

public class AddSubject extends AppCompatActivity {

    int count;
    TextView textViewSelectedCSV;
    SwipeSelector swipeSelector;
    Spinner spinnerType,spinnerCredit,spinnerSemester;
    Button buttonSelectCSV;
    String filePath;
    Uri fileUri;

    String[] Credit = {".75","1","1.5","2","3","4"};
    String[] Type = {"Theory","Lab"};
    String[] Semester = {"1st","2nd","3rd","4th","5th","6th","7th","8th"};

    String deptName,subjectId,subjectName,section;

    boolean flag = false;
    @Override
    protected void onPause() {
        super.onPause();
        overridePendingTransition(R.anim.hold,R.anim.pull_out_from_left);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        overridePendingTransition(R.anim.hold,R.anim.pull_in_from_left);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.pull_out_from_left, R.anim.hold);
        setContentView(R.layout.activity_add_subject);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAddSubject);
        //toolbar.setTitle("Add Subject");
        setSupportActionBar(toolbar);
        count =0;
        final DatabaseHandler db = new DatabaseHandler(this);

        final EditText editTextSubjectName,editTextDeptName,editTextSubjectId,editTextSection;

        editTextDeptName = (EditText) findViewById(R.id.editTextDeptName);
        editTextSubjectId = (EditText) findViewById(R.id.editTextSubjectId);
        editTextSubjectName = (EditText) findViewById(R.id.editTextSubjectName);
        editTextSection = (EditText) findViewById(R.id.editTextSection);
        textViewSelectedCSV = (TextView) findViewById(R.id.textViewSelectedCSV);
        buttonSelectCSV = (Button) findViewById(R.id.buttonSelectCSV);
        spinnerType = (Spinner) findViewById(R.id.spinnerType);
        spinnerCredit = (Spinner) findViewById(R.id.spinnerCredit);
        spinnerSemester = (Spinner) findViewById(R.id.spinnerSemester);

        //final RadioGroup radioGroupSemester,radioGroupCredit;

        //radioGroupSelectType = (RadioGroup) findViewById(R.id.radioGroupSelectType);
        //radioGroupSemester = (RadioGroup) findViewById(R.id.radioGroupSemester);
        //radioGroupCredit = (RadioGroup) findViewById(R.id.radioGroupCredit);

        /*swipeSelector = (SwipeSelector) findViewById(R.id.swipeSelector);

        swipeSelector.setItems(
                // The first argument is the value for that item, and should in most cases be unique for the
                // current SwipeSelector, just as you would assign values to radio buttons.
                // You can use the value later on to check what the selected item was.
                // The value can be any Object, here we're using ints.
                new SwipeItem(0, "", "Theory"),
                new SwipeItem(1, "", "Lab")
        );*/

        ArrayAdapter<String> adapter;
        /**Select Type **/

        adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,Type);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerType.setAdapter(adapter);

        /**Credit **/
        adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,Credit);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerCredit.setAdapter(adapter);

        /**Semester **/
        adapter = new ArrayAdapter<String>(this,R.layout.spinner_item,Semester);
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);
        spinnerSemester.setAdapter(adapter);

        FloatingActionButton fabAddSubject;
        fabAddSubject = (FloatingActionButton) findViewById(R.id.fabAddSubject);
        fabAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final ProgressDialog progressDialog = new ProgressDialog(AddSubject.this);
                deptName = editTextDeptName.getText().toString();
                subjectId = editTextSubjectId.getText().toString();
                subjectName = editTextSubjectName.getText().toString();
                section = editTextSection.getText().toString();

                if(deptName.equals("")||subjectId.equals("")||subjectName.equals("")||section.equals("")){
                    Snackbar.make(view,"Missing Field!", BaseTransientBottomBar.LENGTH_LONG).show();
                }else if(db.cheeck(subjectId,section)==0){
                    Snackbar.make(view,"Change Section!\nYou already have course with same section.", BaseTransientBottomBar.LENGTH_LONG).show();
                }else{
                    progressDialog.setTitle("Please wait...");
                    progressDialog.setMessage("Storing Data");
                    progressDialog.show();
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            String type =  Type[(int)spinnerType.getSelectedItemId()];
                            String semester = (String)spinnerSemester.getSelectedItem();
                            String credit = (String) spinnerCredit.getSelectedItem();

                                /*if (value == 0) {
                                    type = "Theory";
                                }else{
                                    type = "Lab";
                                }*/

                                /*SwipeItem selectedItem = swipeSelector.getSelectedItem();

                                int value = (Integer) selectedItem.value;
                                String type;

                                if (value == 0) {
                                    type = "Theory";
                                }else{
                                    type = "Lab";
                                }*/




                        /*        int radioButtonID,radioId;
                                View radioButton;
                                RadioButton btn;


                        radioButtonID = radioGroupSelectType.getCheckedRadioButtonId();
                        View radioButton = radioGroupSelectType.findViewById(radioButtonID);
                        int radioId = radioGroupSelectType.indexOfChild(radioButton);
                        RadioButton btn = (RadioButton) radioGroupSelectType.getChildAt(radioId);
                        String type = (String) btn.getText();*/

                                /*radioButtonID = radioGroupSemester.getCheckedRadioButtonId();
                                radioButton = radioGroupSemester.findViewById(radioButtonID);
                                radioId = radioGroupSemester.indexOfChild(radioButton);
                                btn = (RadioButton) radioGroupSemester.getChildAt(radioId);
                                String semester = (String) btn.getText();



                                radioButtonID = radioGroupCredit.getCheckedRadioButtonId();
                                radioButton = radioGroupCredit.findViewById(radioButtonID);
                                radioId = radioGroupCredit.indexOfChild(radioButton);
                                btn = (RadioButton) radioGroupCredit.getChildAt(radioId);
                                String credit = (String) btn.getText();
        */

                            String section = editTextSection.getText().toString();

                            db.addSubject(subjectName,subjectId,deptName,type,section,semester,credit);

                            String s1,s2,table;

                            s1 = subjectId;
                            s2 = section;

                            s1 = s1.replaceAll("\\s","_");
                            s1 = s1.replaceAll("-","_");

                            s2 = s2.replaceAll("\\s","_");
                            s2 = s2.replaceAll("-","_");


                            table = s1+"_"+s2;
                            db.createTableAttendance(table+"_attendance");
                            if(type.equals("Theory")){
                                db.createTableTheory(table);
                            }else{
                                db.createTableLab(table);
                            }
                            if(flag){
                                //File file = new File(filePath.replace("/file",""));
                                //another solution
                                InputStream inputStream = null;
                                try {
                                    inputStream = getContentResolver().openInputStream(fileUri);
                                } catch (FileNotFoundException e) {
                                    e.printStackTrace();
                                }

                                String text = new Scanner(inputStream).useDelimiter("\\A").next();
                                text = text.replaceAll("\"","");
                                String[] line = text.split("\n");

                                for(int i=1;i<line.length;i++){
                                    String[] word = line[i].split(",");
                                        DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                                        db.addStudent(word[1],word[0],table);
                                        db.addRoll(table+"_attendance",Integer.parseInt(word[0]));
                                }
                            }
                        }
                    });

                    progressDialog.dismiss();

                    finish();
                }

                //Toast.makeText(AddSubject.this,"Error",Toast.LENGTH_LONG).show();

            }
        });
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
        localBuilder.setTitle("Alert");
        localBuilder.setMessage("Do you want discard the changes?");

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

    public void startFileChooser(View view){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("text/*");
        startActivityForResult(intent,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode==RESULT_OK) {
            fileUri = data.getData();
            //filePath = data.getData().getPath();

            /*String uriToString = fileUri.toString();
            File f = new File(uriToString);*/
            //filePath = getRealPathFromURI(this,fileUri);

            textViewSelectedCSV.setText(fileUri.getPath());
            flag = true;
        }
    }
}

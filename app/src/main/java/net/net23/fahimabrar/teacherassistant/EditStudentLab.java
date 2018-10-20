package net.net23.fahimabrar.teacherassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

public class EditStudentLab extends AppCompatActivity implements View.OnClickListener {
    TextInputLayout[] textInputLayout = new TextInputLayout[15];
    Button addNew;
    ImageView imageViewLeft,imageViewRight;
    EditText editTextNameLabE,editTextIDLabE,editTextQuiz,editTextViva;
    EditText[] editText = new EditText[15];
    String tableName,quiz,viva,name;
    ArrayList<Integer> ID = new ArrayList<Integer>();
    int pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_student_lab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        ID = intent.getIntegerArrayListExtra("ID");
        pos = intent.getIntExtra("pos",0);
        name = intent.getStringExtra("name");
        tableName = intent.getStringExtra("table");

        editTextNameLabE = (EditText) findViewById(R.id.editTextNameLabE);
        editTextIDLabE = (EditText) findViewById(R.id.editTextIDLabE);
        editTextQuiz = (EditText) findViewById(R.id.editTextQuiz);
        editTextViva = (EditText) findViewById(R.id.editTextViva);

        imageViewLeft = (ImageView) findViewById(R.id.imageViewArrowLeftLab);
        imageViewRight = (ImageView) findViewById(R.id.imageViewArrowRightLab);

        editText[0] = (EditText) findViewById(R.id.editTextLab1);
        editText[1] = (EditText) findViewById(R.id.editTextLab2);
        editText[2] = (EditText) findViewById(R.id.editTextLab3);
        editText[3] = (EditText) findViewById(R.id.editTextLab4);
        editText[4] = (EditText) findViewById(R.id.editTextLab5);
        editText[5] = (EditText) findViewById(R.id.editTextLab6);
        editText[6] = (EditText) findViewById(R.id.editTextLab7);
        editText[7] = (EditText) findViewById(R.id.editTextLab8);
        editText[8] = (EditText) findViewById(R.id.editTextLab9);
        editText[9] = (EditText) findViewById(R.id.editTextLab10);
        editText[10] = (EditText) findViewById(R.id.editTextLab11);
        editText[11] = (EditText) findViewById(R.id.editTextLab12);
        editText[12] = (EditText) findViewById(R.id.editTextLab13);


        textInputLayout[0] = (TextInputLayout) findViewById(R.id.input_layout_lab1);
        textInputLayout[1] = (TextInputLayout) findViewById(R.id.input_layout_lab2);
        textInputLayout[2] = (TextInputLayout) findViewById(R.id.input_layout_lab3);
        textInputLayout[3] = (TextInputLayout) findViewById(R.id.input_layout_lab4);
        textInputLayout[4] = (TextInputLayout) findViewById(R.id.input_layout_lab5);
        textInputLayout[5] = (TextInputLayout) findViewById(R.id.input_layout_lab6);
        textInputLayout[6] = (TextInputLayout) findViewById(R.id.input_layout_lab7);
        textInputLayout[7] = (TextInputLayout) findViewById(R.id.input_layout_lab8);
        textInputLayout[8] = (TextInputLayout) findViewById(R.id.input_layout_lab9);
        textInputLayout[9] = (TextInputLayout) findViewById(R.id.input_layout_lab10);
        textInputLayout[10] = (TextInputLayout) findViewById(R.id.input_layout_lab11);
        textInputLayout[11] = (TextInputLayout) findViewById(R.id.input_layout_lab12);
        textInputLayout[12] = (TextInputLayout) findViewById(R.id.input_layout_lab13);

        if(pos==0)
            imageViewLeft.setVisibility(View.INVISIBLE);
        if(pos==ID.size()-1)
            imageViewRight.setVisibility(View.INVISIBLE);

        imageViewRight.setOnClickListener(this);
        imageViewLeft.setOnClickListener(this);

        addNew = (Button) findViewById(R.id.buttonAddNew);
        addNew.setOnClickListener(this);

        System.out.println(tableName);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        Object[] obj = databaseHandler.getLabMarks(tableName,Integer.toString(ID.get(pos)));

        String[] s = (String[]) obj[0];
        Double[] d = (Double[]) obj[1];

        editTextNameLabE.setText(s[0]);
        editTextIDLabE.setText(s[1]);

        editTextNameLabE.clearFocus();
        editTextIDLabE.clearFocus();

        for(int i=0;i<=12;i++){
            if(d[i]!=-1){
                editText[i].setText(Double.toString(d[i]));
                textInputLayout[i].setVisibility(View.VISIBLE);
            }
        }
        if(d[14]!=-1)
            editTextQuiz.setText(Double.toString(d[14]));
        if(d[15]!=-1)
            editTextViva.setText(Double.toString(d[15]));


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                String id;

                try{
                    name = editTextNameLabE.getText().toString();
                    id = editTextIDLabE.getText().toString();
                    quiz = editTextQuiz.getText().toString();
                    viva = editTextViva.getText().toString();

                    if(quiz.equals("")){
                        quiz = "-1";
                    }
                    if(viva.equals("")){
                        viva = "-1";
                    }

                    String[] s = new String[15];
                    for(int i=0;i<13;i++){
                        s[i] = editText[i].getText().toString();
                        if(s[i].equals("")){
                            s[i] = "-1";
                        }
                    }

                    DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

                    databaseHandler.setLabMarks(tableName,name,id,s,quiz,viva);

                }catch (Exception E){
                    E.printStackTrace();
                    Snackbar.make(view, "Missing Fields!", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View view) {
        if(view==addNew){
            for(int i=1;i<=12;i++)
                if(textInputLayout[i].getVisibility()==View.GONE){
                    textInputLayout[i].setVisibility(View.VISIBLE);
                    break;
                }

        }else if(view==imageViewLeft){
            pos--;
        }else if(view==imageViewRight){
            pos++;
        }

        if(view==imageViewLeft||view==imageViewRight){
            String id;
            name = editTextNameLabE.getText().toString();
            id = editTextIDLabE.getText().toString();
            quiz = editTextQuiz.getText().toString();
            viva = editTextViva.getText().toString();

            if(quiz.equals("")){
                quiz = "-1";
            }
            if(viva.equals("")){
                viva = "-1";
            }

            String[] x = new String[15];
            for(int i=0;i<13;i++){
                x[i] = editText[i].getText().toString();
                if(x[i].equals("")){
                    x[i] = "-1";
                }
            }

            DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

            databaseHandler.setLabMarks(tableName,name,id,x,quiz,viva);


            if(pos==0)
                imageViewLeft.setVisibility(View.INVISIBLE);
            else
                imageViewLeft.setVisibility(View.VISIBLE);

            if(pos==ID.size()-1)
                imageViewRight.setVisibility(View.INVISIBLE);
            else
                imageViewRight.setVisibility(View.VISIBLE);

            id = ID.get(pos).toString();


            databaseHandler = new DatabaseHandler(this);
            Object[] obj = databaseHandler.getLabMarks(tableName,id);

            String[] s = (String[]) obj[0];
            Double[] d = (Double[]) obj[1];

            editTextNameLabE.setText(s[0]);
            editTextIDLabE.setText(s[1]);

            editTextNameLabE.clearFocus();
            editTextIDLabE.clearFocus();

            for(int i=0;i<=12;i++){
                if(d[i]!=-1){
                    editText[i].setText(Double.toString(d[i]));
                    textInputLayout[i].setVisibility(View.VISIBLE);
                }

                if(textInputLayout[i].getVisibility()==View.VISIBLE&&d[i]==-1.0){
                    editText[i].setText("");
                }
            }
            if(d[14]!=-1.0)
                editTextQuiz.setText(Double.toString(d[14]));
            else
                editTextQuiz.setText("");

            if(d[15]!=-1.0)
                editTextViva.setText(Double.toString(d[15]));
            else
                editTextViva.setText("");


        }
    }
}

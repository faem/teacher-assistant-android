package net.net23.fahimabrar.teacherassistant;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class AttendanceActivity extends AppCompatActivity implements View.OnClickListener {

    int[] b = new int[1000];
    List<Integer> rollList = new ArrayList<Integer>();
    String currentDateTimeString,column;
    String month,day,year,hour,min,second,temp,ap,table;
    TimePicker timePicker;
    DatePicker datePicker;
    String[] newColumnName;
    Button buttonSet,buttonCancel;
    Dialog dialog;
    int l;
    boolean flag,flag2=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy/hh/mm/ss");
        currentDateTimeString = dateFormat.format(new Date());
        System.out.println(currentDateTimeString);
        String[] out = currentDateTimeString.split("/");


        day = Integer.toString(Integer.parseInt(out[0]));
        month = Integer.toString(Integer.parseInt(out[1]));
        year = Integer.toString(Integer.parseInt(out[2]));
        hour = Integer.toString(Integer.parseInt(out[3]));
        min = Integer.toString(Integer.parseInt(out[4]));
        second = Integer.toString(Integer.parseInt(out[5]));


        System.out.println(month+"_"+day+"_"+year+"_"+hour+"_"+min+"_"+second);

        Intent i = getIntent();

        table = i.getStringExtra("table");

        GridLayoutManager gridLayoutManager = new GridLayoutManager(AttendanceActivity.this,5);

        RecyclerView rView = (RecyclerView)findViewById(R.id.recyclerviewAttendance);
        rView.setHasFixedSize(true);
        rView.setLayoutManager(gridLayoutManager);
        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        List<Student> studentList = databaseHandler.getStudentFromDB(table);

        l = studentList.size();

        RecyclerAdapterAttendance rcAdapter = new RecyclerAdapterAttendance(AttendanceActivity.this,table,l,b);
        rView.setAdapter(rcAdapter);

        float offsetPx = getResources().getDimension(R.dimen.bottom_offset_dp);
        RecyclerAdapterAttendance.BottomOffsetDecoration bottomOffsetDecoration = new RecyclerAdapterAttendance.BottomOffsetDecoration((int) offsetPx);
        rView.addItemDecoration(bottomOffsetDecoration);

        b = rcAdapter.attendanceArray;

        for(int j=0;j<studentList.size();j++){
            Student s = studentList.get(j);
            rollList.add(Integer.parseInt(s.getStudentID()));
            System.out.println(Integer.parseInt(s.getStudentID()));
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(!flag2){
                    column = "a_"+day+"_"+month+"_"+year+"_"+hour+"_"+min+"_"+second;
                    //System.out.println(s1+"_"+s2);

                    DatabaseHandler databaseHandler = new DatabaseHandler(AttendanceActivity.this);
                    databaseHandler.addColumn(table+"_attendance",column);
                }

                System.out.println(rollList.size());

                for(int i=0;i<rollList.size();i++){
                    String x;
                    if(b[i]==0)
                        x="P";
                    else if(b[i]==1)
                        x="A";
                    else
                        x = "L";

                    System.out.println("attendance="+x);
                    DatabaseHandler databaseHandler = new DatabaseHandler(AttendanceActivity.this);
                    databaseHandler.addAttendance(table+"_attendance",column,x,rollList.get(i));
                }
                finish();
            }

        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.attendance_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.datetime){
            //showDateTime();

            dialog = new Dialog(this);
            flag = false;

            dialog.setContentView(R.layout.date_time_dialog);
            dialog.setTitle("Select Date and time");
            buttonSet = (Button) dialog.findViewById(R.id.buttonSet);
            buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
            timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
            datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
            buttonSet.setOnClickListener(this);
            buttonCancel.setOnClickListener(this);
            dialog.show();

            return true;
        }else if(item.getItemId()==R.id.editattendance){

            dialog = new Dialog(this);
            flag = true;
            dialog.setContentView(R.layout.date_time_dialog);
            dialog.setTitle("Select Date");
            buttonSet = (Button) dialog.findViewById(R.id.buttonSet);
            buttonCancel = (Button) dialog.findViewById(R.id.buttonCancel);
            timePicker = (TimePicker) dialog.findViewById(R.id.timePicker);
            datePicker = (DatePicker) dialog.findViewById(R.id.datePicker);
            timePicker.setVisibility(View.GONE);
            buttonSet.setOnClickListener(this);
            buttonCancel.setOnClickListener(this);
            dialog.show();

            return  true;
        }else if(item.getItemId()==R.id.home){

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

            return true;

        }else{
            return super.onOptionsItemSelected(item);
        }
    }

    /*boolean flag;
    void showDateTime(){


        if(flag){
            int mHour,mMinute;
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(getApplicationContext(),
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay,
                                              int minute) {

                            hour = Integer.toString(hourOfDay);
                            min = Integer.toString(minute);
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }

    }*/

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.buttonSet){

            if(!flag){
                year = Integer.toString(datePicker.getYear());
                month = Integer.toString(datePicker.getMonth()+1);
                day = Integer.toString(datePicker.getDayOfMonth());
                hour = Integer.toString(timePicker.getCurrentHour().intValue());
                min = Integer.toString(timePicker.getCurrentMinute().intValue());
                dialog.cancel();

            }else{
                String newYear,newMonth,newDay;
                newYear = Integer.toString(datePicker.getYear());
                newMonth = Integer.toString(datePicker.getMonth()+1);
                newDay = Integer.toString(datePicker.getDayOfMonth());

                temp = "a_"+newDay+"_"+newMonth+"_"+newYear;

                dialog.cancel();

                final DatabaseHandler databaseHandler = new DatabaseHandler(this);

                final String[] columnName = databaseHandler.columnName(table+"_attendance");
                final String[] x = new String[20];
                int j = 0;
                System.out.println("c = "+columnName[1]);
                for(int i=1;i<columnName.length;i++){
                    System.out.println(temp+" == "+columnName[i].substring(0,11));
                    if(columnName[i].substring(0,11).equals(temp)){
                        x[j] = columnName[i];
                        System.out.println(x[j]);
                        j++;
                    }
                }
                newColumnName = new String[j];

                for(int i =0 ;i<j;i++)
                    newColumnName[i] = x[i];

                System.out.println("j="+j);

                AlertDialog.Builder builder = new AlertDialog.Builder(AttendanceActivity.this);
                if(j!=0)
                    builder.setTitle("Make your selection");
                else
                    builder.setTitle("No Entry");
                builder.setItems(newColumnName, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        flag2 = true;
                        column = newColumnName[item];

                        int[] a = databaseHandler.showAttendance(table+"_attendance",newColumnName[item]);

                        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.recyclerviewAttendance);

                        mRecyclerView.setHasFixedSize(true);

                        RecyclerAdapterAttendance mAdapter = new RecyclerAdapterAttendance(AttendanceActivity.this,table,l,a);
                        mAdapter.notifyDataSetChanged();
                        mRecyclerView.setAdapter(mAdapter);

                       /*float offsetPx = getResources().getDimension(R.dimen.bottom_offset_dp);
                       RecyclerAdapterAttendance.BottomOffsetDecoration bottomOffsetDecoration = new RecyclerAdapterAttendance.BottomOffsetDecoration((int) offsetPx);
                       mRecyclerView.addItemDecoration(bottomOffsetDecoration);*/


                        b = mAdapter.attendanceArray;

                        //Toast.makeText(getApplicationContext(),"Item clicked",Toast.LENGTH_LONG).show();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        }else{
            dialog.cancel();
        }
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
}

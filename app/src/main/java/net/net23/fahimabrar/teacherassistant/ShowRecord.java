package net.net23.fahimabrar.teacherassistant;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

/*import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;*/
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class ShowRecord extends AppCompatActivity {
    int columnSize;
    String[] columnName;
    int[] ID = new int[1000];
    FragmentTransaction mFragmentTransaction;
    FragmentManager mFragmentManager;
    double[][] d;
    //Double[][] marks = new Double[20][1000];
    int rowLength;
    String tableName,credit,path,type;
    ProgressBar progressBar;
    FrameLayout frameLayout;
    View v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_record);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //progressBar = (ProgressBar) findViewById(R.id.progressBar);
        //frameLayout = (FrameLayout) findViewById(R.id.containerview);
        //frameLayout.setVisibility(View.GONE);
        //progressBar.setVisibility(View.GONE);
        Intent i = getIntent();
        tableName = i.getStringExtra("table");
        credit = i.getStringExtra("credit");
        type = i.getStringExtra("type");

        attendanceLoader loader = new attendanceLoader();
        loader.execute(0);


        /*Bundle bundle = new Bundle();
        bundle.putString("table", tableName);
        bundle.putString("credit",credit);

        TabFragment fragobj = new TabFragment();
        fragobj.setArguments(bundle);

        mFragmentManager = getSupportFragmentManager();
        mFragmentTransaction = mFragmentManager.beginTransaction();
        mFragmentTransaction.replace(R.id.containerview,fragobj).commit();

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        columnName = databaseHandler.columnName(tableName);
        rowLength = databaseHandler.rowNumber(tableName);
        d = databaseHandler.getCtMarks(tableName);*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //v = view;
                attendanceLoader loader = new attendanceLoader();
                loader.execute(1);

                Snackbar.make(view, "CSV created at path TeacherAssistant/CSV", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                /*if(type.equals("Theory")){
                    createTheoryCSV(tableName);
                    createAttendanceCSV(tableName);
                    *//*if(credit.equals("2"))
                        columnSize = 7;
                    if(credit.equals("3"))
                        columnSize = 8;
                    if(credit.equals("4"))
                        columnSize = 9;
                    try {
                        createPdf("/sdcard/TeacherAssistant/PDF/"+tableName+".csv");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }*//*

                }

                else{
                    createLabCSV(tableName);
                    createAttendanceCSV(tableName);
                    *//*columnSize = 6;
                    try {
                        createPdf("/sdcard/TeacherAssistant/PDF/"+tableName+".csv");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }*//*
                }*/
            }
        });


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        /*if(type.equals("Theory"))
            fillTableTheory();
        else
            fillTableLab();*/
        //fillTableAttendance();
    }

    public boolean createTheoryCSV(String tableName){
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

        File file = new File("/sdcard/TeacherAssistant/CSV/");

        if (!file.exists()) {
            file.mkdirs();
        }

        File csvFile = new File(file, tableName+".csv");

        path = csvFile.getAbsolutePath();
        try {
            if (csvFile.createNewFile()){
                System.out.println("File is created!");
                System.out.println("myfile.csv "+csvFile.getAbsolutePath());
            }else{
                System.out.println("File already exists.");
                System.out.println(file.getAbsolutePath());
            }

            CSVWriter csvWrite = new CSVWriter(new FileWriter(csvFile));
            SQLiteDatabase db = databaseHandler.getReadableDatabase();

            Cursor curCSV=db.rawQuery("select * from " + tableName,null);

            //String[] columnNames = curCSV.getColumnNames();
            String[] newColumnNames = new String[10];
            newColumnNames[0] = "ID";
            newColumnNames[1] = "Name";
            newColumnNames[2] = "CT 1";
            newColumnNames[3] = "CT 2";
            newColumnNames[4] = "CT 3";
            newColumnNames[5] = "Total";
            newColumnNames[6] = "Average";

            if(credit.equals("3")||credit.equals("4")){
                newColumnNames[5] = "CT 4";
                newColumnNames[6] = "Total";
                newColumnNames[7] = "Average";
            }

            if(credit.equals("4")){
                newColumnNames[6] = "CT 5";
                newColumnNames[7] = "Total";
                newColumnNames[8] = "Average";
            }

            csvWrite.writeNext(newColumnNames);
            int i = 0;

            //databaseHandler = new DatabaseHandler(this);
           // double[][] d = databaseHandler.getCtMarks(tableName);

            while(curCSV.moveToNext()) {
                System.out.println(curCSV.getString(0));
                String[] arrStr = new String[10];

                arrStr[0] = curCSV.getString(1);
                arrStr[1] = curCSV.getString(0);
                arrStr[2] = curCSV.getString(2);
                arrStr[3] = curCSV.getString(3);
                arrStr[4] = curCSV.getString(4);

                double min,total,avg;

                total = d[1][i]+d[2][i]+d[3][i]+d[4][i]+d[5][i];
                min = Math.min(d[1][i],d[2][i]);
                min = Math.min(min,d[3][i]);
                if(credit.equals("3")||credit.equals("4"))
                    min = Math.min(min,d[4][i]);
                if(credit.equals("4"))
                    min = Math.min(min,d[5][i]);

                total = total - min;

                arrStr[5] = Double.toString(Math.ceil(total));
                avg = total/2.0;
                arrStr[6] = Double.toString(Math.ceil(avg));

                if(credit.equals(("3"))||credit.equals("4")){
                    arrStr[5] = curCSV.getString(5);
                    arrStr[6] = Double.toString(Math.ceil(total));
                    avg = total/3.0;
                    arrStr[7] = Double.toString(Math.ceil(avg));
                }

                if(credit.equals("4")){
                    arrStr[6] = curCSV.getString(6);
                    arrStr[7] = Double.toString(Math.ceil(total));
                    avg = total/4.0;
                    arrStr[8] = Double.toString(Math.ceil(avg));
                }

                i++;
                csvWrite.writeNext(arrStr);
            }

            csvWrite.close();
            curCSV.close();
        /*String data="";
        data=readSavedData();
        data= data.replace(",", ";");
        writeData(data);*/

            return true;

        }

        catch(SQLException sqlEx){

            return false;

        }

        catch (IOException e){

            return false;

        }
    }


    public boolean createLabCSV(String tableName){
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

        Object[] objects = databaseHandler.getLabMarks(tableName);

        String[][] s = (String[][]) objects[0];

        File file = new File("/sdcard/TeacherAssistant/CSV/");

        if (!file.exists()) {
            file.mkdirs();
        }

        File csvFile = new File(file, tableName+".csv");

        path = csvFile.getAbsolutePath();
        try {
            if (csvFile.createNewFile()){
                System.out.println("File is created!");
                System.out.println("myfile.csv "+csvFile.getAbsolutePath());
            }else{
                System.out.println("File already exists.");
                System.out.println(file.getAbsolutePath());
            }

            CSVWriter csvWrite = new CSVWriter(new FileWriter(csvFile));

            Object[] object = databaseHandler.getLabMarks(tableName);

            Double[][] marks = (Double[][]) object[1];

            String[] columnNames = {"ID","Name","Lab","Quiz","Viva"};

            csvWrite.writeNext(columnNames);

            for(int i=0;i<rowLength;i++){
                String[] x = new String[10];
                x[0] = s[1][i];
                x[1] = s[0][i];
                double sum=0.0;

                for(int j=0;j<=12;j++)
                    if(marks[j][i]!=-1.0)
                        sum = sum + marks[j][i];

                if(!Double.isNaN(sum))
                    x[2] = Double.toString(sum);
                else
                    x[2] = "0";
                if(marks[14][i]!=-1)
                    x[3] = Double.toString(marks[14][i]);
                else
                    x[3] = "0";
                if(marks[15][i]!=-1)
                    x[4] = Double.toString(marks[15][i]);
                else
                    x[4] = "0";

                /*if(!Double.isNaN(marks[3][i]))
                    x[5] = Double.toString(Math.ceil(marks[3][i]));
                else
                    x[5] = "0";*/
                csvWrite.writeNext(x);
            }

            csvWrite.close();

        /*String data="";
        data=readSavedData();
        data= data.replace(",", ";");
        writeData(data);*/

            return true;

        }

        catch(SQLException sqlEx){

            return false;

        }

        catch (IOException e){

            return false;

        }
    }

    public boolean createAttendanceCSV(String tableName){
        DatabaseHandler databaseHandler = new DatabaseHandler(getApplicationContext());

        //Object[] objects = databaseHandler.getLabMarks(tableName);

        //String[][] s = (String[][]) objects[0];

        File file = new File("/sdcard/TeacherAssistant/CSV/");

        if (!file.exists()) {
            file.mkdirs();
        }

        File csvFile = new File(file, tableName+".csv");

        path = csvFile.getAbsolutePath();
        try {
            if (csvFile.createNewFile()){
                System.out.println("File is created!");
                System.out.println("myfile.csv "+csvFile.getAbsolutePath());
            }else{
                System.out.println("File already exists.");
                System.out.println(file.getAbsolutePath());
            }

            CSVWriter csvWrite = new CSVWriter(new FileWriter(csvFile));


            String[] columnNamesP = databaseHandler.columnName(tableName);
            int L = columnNamesP.length;
            String[] columnNames = new String[L+4];

            for(int i=0;i<L;i++)
                columnNames[i] = columnNamesP[i];

            columnNames[L] = "Present";
            columnNames[L+1] = "Absent";
            columnNames[L+2] = "late";
            columnNames[L+3] = "Percentage";
            //columnNames[L+4] = "Marks";

            csvWrite.writeNext(columnNames);

            SQLiteDatabase db = databaseHandler.getReadableDatabase();

            Cursor curCSV=db.rawQuery("select * from " + tableName,null);

            while(curCSV.moveToNext()){
                int p=0,a=0,l=0;
                String[] x = new String[L+4];
                for(int i=0;i<L;i++){
                    x[i] = curCSV.getString(i);
                    if(x[i]==null){
                        a++;
                        x[i] = "A";
                    }
                    else if(x[i].equals("P"))
                        p++;
                    else if(x[i].equals("A"))
                        a++;
                    else if(x[i].equals("L"))
                        l++;
                }

                x[L] = Integer.toString(p);
                x[L+1] = Integer.toString(a);
                x[L+2] = Integer.toString(l);
                x[L+3] = Double.toString(Math.ceil(((double)(p+l)/(double)(p+l+a))*100.00))+"%";

                csvWrite.writeNext(x);
            }

            csvWrite.close();

        /*String data="";
        data=readSavedData();
        data= data.replace(",", ";");
        writeData(data);*/

            return true;

        }

        catch(SQLException sqlEx){

            return false;

        }

        catch (IOException e){

            return false;

        }
    }

    public class attendanceLoader extends AsyncTask<Integer,Void,Boolean> {

        @Override
        protected Boolean doInBackground(Integer ... x) {
            if(x[0]==0){
                Bundle bundle = new Bundle();
                bundle.putString("table", tableName);
                bundle.putString("credit",credit);
                bundle.putString("type",type);
                TabFragment fragobj = new TabFragment();
                fragobj.setArguments(bundle);

                mFragmentManager = getSupportFragmentManager();
                mFragmentTransaction = mFragmentManager.beginTransaction();
                mFragmentTransaction.replace(R.id.containerview,fragobj).commit();

                DatabaseHandler databaseHandler = new DatabaseHandler(ShowRecord.this);

                columnName = databaseHandler.columnName(tableName);
                rowLength = databaseHandler.rowNumber(tableName);
                d = databaseHandler.getCtMarks(tableName);

                for(int i=0;i<=10000;i++)
                    System.out.println(i);

                ///System.out.println("d = "+d[0][0]);

            }else if(x[0]==1){
                if(type.equals("Theory")){
                    createTheoryCSV(tableName);
                    createAttendanceCSV(tableName+"_attendance");
                    for(int i=0;i<=10000;i++)
                        System.out.println(i);
                    //createAttendanceCSV(tableName);
                    /*if(credit.equals("2"))
                        columnSize = 7;
                    if(credit.equals("3"))
                        columnSize = 8;
                    if(credit.equals("4"))
                        columnSize = 9;
                    try {
                        createPdf("/sdcard/TeacherAssistant/PDF/"+tableName+".csv");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }*/
                }

                else{
                    createLabCSV(tableName);
                    createAttendanceCSV(tableName+"_attendance");
                    for(int i=0;i<=10000;i++)
                        System.out.println(i);
                    /*columnSize = 6;
                    try {
                        createPdf("/sdcard/TeacherAssistant/PDF/"+tableName+".csv");
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (DocumentException e) {
                        e.printStackTrace();
                    }*/
                }

            }

            return true;
        }

        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            progressDialog = ProgressDialog.show(ShowRecord.this,
                    "Updating",
                    "Updating Data. . .");
            //progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Boolean b) {
            progressDialog.dismiss();
            //progressBar.setVisibility(View.GONE);
            //frameLayout.setVisibility(View.VISIBLE);
        }
    }


    /*public void createPdf(String inputCSVFile) throws IOException,DocumentException {
        CSVReader reader = new CSVReader(new FileReader(inputCSVFile));
        String [] nextLine;

        Document my_pdf_data = new Document();
        PdfWriter.getInstance(my_pdf_data, new FileOutputStream("/sdcard/TeacherAssistant/PDF/"+tableName+".pdf"));
        my_pdf_data.open();
        PdfPTable my_first_table = new PdfPTable(columnSize);
        PdfPCell table_cell;
        while ((nextLine = reader.readNext()) != null) {
            for(int i=0;i<columnSize;i++){
                table_cell=new PdfPCell(new Phrase(nextLine[i]));
                my_first_table.addCell(table_cell);
            }
        }
        Snackbar.make(v, "Created pdf at "+"/sdcard/TeacherAssistant/PDF/"+tableName+".pdf", Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();
        my_pdf_data.add(my_first_table);
        my_pdf_data.close();
    }*/

    /*  void fillTableLab(){

        root = new LinearLayout(ShowRecord.this);
        root.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout linearLayout = new LinearLayout(ShowRecord.this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(4, -1));
        this.root.addView(linearLayout);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        Object[] obj = databaseHandler.getLabMarks(tableName);
        String[][] s = (String[][]) obj[0];
        Double[][] d = (Double[][]) obj[1];


        LinearLayout localLinearLayout;
        View view;
        TextView textView;

        String[] clmnName = {"ID","Name","Lab Performance","Attendance","Quiz & Viva"};

        for(int i=0;i<5;i++){
            localLinearLayout = new LinearLayout(ShowRecord.this);
            localLinearLayout.setOrientation(LinearLayout.VERTICAL);
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);

            textView.setText(clmnName[i]);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);

            for(int j=0;j<rowLength;j++){
                view = new View(ShowRecord.this);
                view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
                //view.setBackgroundColor(Color.parseColor("#F01230"));
                localLinearLayout.addView(view);
                textView = new TextView(ShowRecord.this);
                textView.setPadding(30, 10, 30, 10);
                textView.setGravity(17);
                int l = 0;
                if(i==0){
                    textView.setText(s[i+1][j]);
                }else if(i==1){
                    textView.setText(s[i-1][j]);
                } else if(i==2){
                    double sum;
                    sum = 0.0;
                    for(int k=0;k<=12;k++){
                        if(d[k][j]!=-1){
                            System.out.println("sum = "+sum);
                            sum = sum + d[k][j];
                            l++;
                        }
                    }

                    if(Double.toString(sum).equals("NaN"))
                        sum = 0;

                    if(credit.equals("1"))
                        textView.setText(Double.toString(sum*1.5/l));
                    else
                        textView.setText(Double.toString(sum*3/l));
                }else if(i==3){
                    if(d[13][j]!=-1)
                        textView.setText(Double.toString(d[13][j]*5.0/100.0));
                }else{
                    if(d[14][j]!=-1)
                        textView.setText(Double.toString(d[14][j]));
                }

                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                textView.setBackground(getResources().getDrawable(R.drawable.blue_bg));
                localLinearLayout.addView(textView);
            }
            root.addView(localLinearLayout);
        }

        hsv.addView(root);


        localLinearLayout = new LinearLayout(ShowRecord.this);
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(ShowRecord.this);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setText("Total");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        textView.setPadding(30, 10, 30, 10);
        textView.setGravity(17);
        localLinearLayout.addView(textView);
        for(int i=0;i<rowLength;i++)
        {
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);
            int l =0;

            double sum;
            sum = 0.0;
            for(int k=0;k<=12;k++) {
                if (d[k][i] != -1) {
                    System.out.println("sum = " + sum);
                    sum = sum + d[k][i];
                    l++;
                }
            }


            if(credit.equals("1")){
                marks[0][i] = sum*1.5/l;
                sum= sum*1.5/l+d[13][i]*2.5/100.0;
                if(d[13][i]!=-1)
                    marks[1][i] = d[13][i]*2.5/100.0;
                else
                    marks[1][i] = 0.0;
            }else{
                marks[0][i] = sum*3/l;
                sum = sum*3/l+d[13][i]*5.0/100.0;
                if(d[13][i]!=-1)
                    marks[1][i] = d[13][i]*5.0/100.0;
                else
                    marks[1][i] = 0.0;
            }


            sum = sum + d[14][i];

            marks[2][i] = d[14][i];
            marks[3][i] = sum;


            textView.setText(Double.toString(Math.ceil(sum)));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.blue_bg));
            textView.setGravity(17);
            localLinearLayout.addView(textView);
        }
        root.addView(localLinearLayout);

    }

    void fillTableTheory(){
        root = new LinearLayout(ShowRecord.this);
        root.setOrientation(LinearLayout.HORIZONTAL);


        LinearLayout linearLayout = new LinearLayout(ShowRecord.this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(4, -1));
        //linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        this.root.addView(linearLayout);

        //System.out.println("column = "+columnName.length+" "+attendance[0]);
        *//*LinearLayout localLinearLayout = new LinearLayout(ShowRecord.this);
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        View view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        TextView textView = new TextView(ShowRecord.this);
        textView.setText("ID");
        textView.setPadding(30, 10, 30, 10);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        int i=Integer.parseInt(IDFrom),j=Integer.parseInt(IDTo);
        while (i <= j)
        {
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);
            textView.setText(Integer.toString(i));
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
            i++;
        }


        view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        this.root.addView(localLinearLayout);*//*

        LinearLayout localLinearLayout;
        View view;
        TextView textView;

        for(int i=0;i<4;i++){
            localLinearLayout = new LinearLayout(ShowRecord.this);
            localLinearLayout.setOrientation(LinearLayout.VERTICAL);
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);
            if(columnName[i+1].equals("ct1"))
                textView.setText("CT 1");
            else if(columnName[i+1].equals("ct2"))
                textView.setText("CT 2");
            else if(columnName[i+1].equals("ct3"))
                textView.setText("CT 3");
            else
                textView.setText("ID");

            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);

            for(int j=0;j<rowLength;j++){
                view = new View(ShowRecord.this);
                view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
                //view.setBackgroundColor(Color.parseColor("#F01230"));
                localLinearLayout.addView(view);
                textView = new TextView(ShowRecord.this);
                textView.setPadding(30, 10, 30, 10);
                textView.setGravity(17);
                System.out.println(i+" "+j+" "+d[i][j]);
                double x = d[i][j]-(int)d[i][j];
                if(x==0.0)
                    textView.setText(Integer.toString((int)d[i][j]));
                else
                    textView.setText(Double.toString(d[i][j]));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                textView.setBackground(getResources().getDrawable(R.drawable.blue_bg));
                localLinearLayout.addView(textView);
            }
            root.addView(localLinearLayout);
        }

        hsv.addView(root);
//        view = (View) hsv.getParent();
//        view.setVisibility(View.VISIBLE);


        if(credit.equals("3")||credit.equals("4")){

            localLinearLayout = new LinearLayout(ShowRecord.this);
            localLinearLayout.setOrientation(LinearLayout.VERTICAL);
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);

            textView.setText("CT 4");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);

            for(int j=0;j<rowLength;j++){
                view = new View(ShowRecord.this);
                view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
                //view.setBackgroundColor(Color.parseColor("#F01230"));
                localLinearLayout.addView(view);
                textView = new TextView(ShowRecord.this);
                textView.setPadding(30, 10, 30, 10);
                textView.setGravity(17);
                double x = d[5][j]-(int)d[5][j];
                if(x==0.0)
                    textView.setText(Integer.toString((int)d[4][j]));
                else
                    textView.setText(Double.toString(d[4][j]));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                textView.setBackground(getResources().getDrawable(R.drawable.blue_bg));
                localLinearLayout.addView(textView);
            }
            root.addView(localLinearLayout);

        }

        if(credit.equals("4")){

            localLinearLayout = new LinearLayout(ShowRecord.this);
            localLinearLayout.setOrientation(LinearLayout.VERTICAL);
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);

            textView.setText("CT 5");
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);

            for(int j=0;j<rowLength;j++){
                view = new View(ShowRecord.this);
                view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
                //view.setBackgroundColor(Color.parseColor("#F01230"));
                localLinearLayout.addView(view);
                textView = new TextView(ShowRecord.this);
                textView.setPadding(30, 10, 30, 10);
                textView.setGravity(17);
                double x = d[5][j]-(int)d[5][j];
                if(x==0.0)
                    textView.setText(Integer.toString((int)d[5][j]));
                else
                    textView.setText(Double.toString(d[5][j]));
                textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
                textView.setBackground(getResources().getDrawable(R.drawable.blue_bg));
                localLinearLayout.addView(textView);
            }
            root.addView(localLinearLayout);


        }

        int presentCount,absentCount;

        localLinearLayout = new LinearLayout(ShowRecord.this);
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(ShowRecord.this);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setText("Total");
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
        textView.setPadding(30, 10, 30, 10);
        textView.setGravity(17);
        localLinearLayout.addView(textView);
        for(int i=0;i<rowLength;i++)
        {
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);
            double min,total;

            total = d[1][i]+d[2][i]+d[3][i]+d[4][i]+d[5][i];
            min = Math.min(d[1][i],d[2][i]);
            min = Math.min(min,d[3][i]);
            if(credit.equals("3")||credit.equals("4"))
                min = Math.min(min,d[4][i]);
            if(credit.equals("4"))
                min = Math.min(min,d[5][i]);

            total = total - min;
            textView.setText(Double.toString(Math.ceil(total)));
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,25);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.blue_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root.addView(localLinearLayout);





        *//*localLinearLayout = new LinearLayout(ShowRecord.this);
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(ShowRecord.this);
        textView.setText("Absent");
        textView.setPadding(30, 10, 30, 10);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        for(i=0;i<attendance.length-1;i++)
        {
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);
            absentCount = attendance[i].length()-attendance[i].replaceAll( "A", "" ).length();
            textView.setText(Integer.toString(absentCount));
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root.addView(localLinearLayout);
*//*
        *//*localLinearLayout = new LinearLayout(ShowRecord.this);
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(ShowRecord.this);
        textView.setText("Percentage");
        textView.setPadding(30, 10, 30, 10);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        for(i=0;i<attendance.length-1;i++)
        {
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);
            double total = attendance[i].length();
            double pC = total - attendance[i].replaceAll( "P", "" ).length();
            double percentage = (pC/total)*100;
            DecimalFormat four = new DecimalFormat("#0.00");
            String p = four.format(percentage);
            textView.setText(p+"%");
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root.addView(localLinearLayout);*//*
    }


    void fillTableAttendance(){

        String[] attendance;

        DatabaseHandler databaseHandler = new DatabaseHandler(this);

        List<Student> studentList = databaseHandler.getStudentFromDB(tableName);
        databaseHandler = new DatabaseHandler(this);
        attendance = databaseHandler.showAttendance(tableName+"_attendance",studentList.size());
        columnName = databaseHandler.columnName(tableName+"_attendance");

        root = new LinearLayout(ShowRecord.this);
        root.setOrientation(LinearLayout.HORIZONTAL);

        LinearLayout linearLayout = new LinearLayout(ShowRecord.this);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(4, -1));
        //linearLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        this.root.addView(linearLayout);

        LinearLayout localLinearLayout = new LinearLayout(ShowRecord.this);
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        View view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        TextView textView = new TextView(ShowRecord.this);
        textView.setText("ID");
        textView.setPadding(30, 10, 30, 10);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setGravity(17);

        localLinearLayout.addView(textView);

        for (int i=0; i<studentList.size();i++)
        {
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);
            textView.setText(studentList.get(i).getStudentID());
            System.out.println("s "+studentList.get(i).getStudentID());
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }


        view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        this.root.addView(localLinearLayout);
        //System.out.println("column = "+columnName.length+" "+attendance[0]);

        for(int i=0;i<columnName.length-1;i++){
            localLinearLayout = new LinearLayout(ShowRecord.this);
            localLinearLayout.setOrientation(LinearLayout.VERTICAL);
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);

            String[] temp = columnName[i+1].split("_");

            String c = temp[1]+"/"+temp[2]*//*+"/"+temp[3]*//*;

            textView.setText(c);
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);

            for(int j=0;j<studentList.size();j++){
                view = new View(ShowRecord.this);
                view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
                //view.setBackgroundColor(Color.parseColor("#F01230"));
                localLinearLayout.addView(view);
                textView = new TextView(ShowRecord.this);
                textView.setText("");
                textView.setPadding(30, 10, 30, 10);
                textView.setGravity(17);
                if(Character.toString(attendance[j].charAt(i)).equals("A")){
                    textView.setBackground(getResources().getDrawable(R.drawable.red_bg));

                }else if(Character.toString(attendance[j].charAt(i)).equals("P")){
                    textView.setBackground(getResources().getDrawable(R.drawable.green_bg));
                }else{
                    textView.setBackground(getResources().getDrawable(R.drawable.yellow_bg));
                }
                localLinearLayout.addView(textView);
            }
            root.addView(localLinearLayout);
        }

        hsv.addView(root);
//        view = (View) hsv.getParent();
//        view.setVisibility(View.VISIBLE);

        int presentCount,absentCount;

        localLinearLayout = new LinearLayout(ShowRecord.this);
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(ShowRecord.this);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setText("Present");
        textView.setPadding(30, 10, 30, 10);
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        for(int i=0;i<attendance.length-1;i++)
        {
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);
            presentCount = attendance[i].length()-attendance[i].replaceAll( "P", "" ).length();
            textView.setText(Integer.toString(presentCount));
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root.addView(localLinearLayout);

        localLinearLayout = new LinearLayout(ShowRecord.this);
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(ShowRecord.this);
        textView.setText("Absent");
        textView.setPadding(30, 10, 30, 10);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        for(int i=0;i<attendance.length-1;i++)
        {
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);
            absentCount = attendance[i].length()-attendance[i].replaceAll( "A", "" ).length();
            textView.setText(Integer.toString(absentCount));
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root.addView(localLinearLayout);

        localLinearLayout = new LinearLayout(ShowRecord.this);
        localLinearLayout.setOrientation(LinearLayout.VERTICAL);
        view = new View(ShowRecord.this);
        view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
        //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        localLinearLayout.addView(view);
        textView = new TextView(ShowRecord.this);
        textView.setText("Percentage");
        textView.setPadding(30, 10, 30, 10);
        textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
        textView.setGravity(17);

        localLinearLayout.addView(textView);
        for(int i=0;i<attendance.length-1;i++)
        {
            view = new View(ShowRecord.this);
            view.setLayoutParams(new LinearLayout.LayoutParams(-1, 4));
            //view.setBackgroundColor(Color.parseColor("#FFFFFF"));
            localLinearLayout.addView(view);
            textView = new TextView(ShowRecord.this);
            double total = attendance[i].length();
            double pC = total - attendance[i].replaceAll( "P", "" ).length();
            double percentage = (pC/total)*100;
            DecimalFormat four = new DecimalFormat("#0.00");
            String p = four.format(percentage);
            textView.setText(p+"%");
            textView.setPadding(30, 10, 30, 10);
            textView.setBackground(getResources().getDrawable(R.drawable.cell_bg));
            textView.setGravity(17);

            localLinearLayout.addView(textView);
        }
        root.addView(localLinearLayout);
    }
    */
}

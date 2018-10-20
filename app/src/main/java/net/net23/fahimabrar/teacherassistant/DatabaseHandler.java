package net.net23.fahimabrar.teacherassistant;

/**
 * Created by fahim on 2/26/2017.
 */


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {


    Context context;
    public DatabaseHandler(Context context) {
        super(context, "/sdcard/TeacherAssistant/data/data", null, 1);
        new File("/sdcard/TeacherAssistant/data/").mkdirs();
        SQLiteDatabase.openOrCreateDatabase("/sdcard/TeacherAssistant/data/data", null);
    }

    public File getDatabasePath(){
        File file = new File("/sdcard/TeacherAssistant/data/data");
        return  file;
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE subjects(subject_name TEXT,subject_id TEXT,dept TEXT, type TEXT,section TEXT,semester TEXT,credit double, CONSTRAINT PK PRIMARY KEY (subject_id,section))");
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS subjects");

        // Create tables again
        onCreate(db);
    }


    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    // Adding new contact

    /*void getStudent(String table){
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("select ID from "+table,null);

        while(cursor.moveToFirst()){

        }

        db.close();
    }*/

    void addSubject(String subjectName,String subjectId,String deptName, String type,String section, String semester,String credit) {
        SQLiteDatabase db = this.getWritableDatabase();
        //db.execSQL("ALTER TABLE subjects DROP PRIMARY KEY; ALTER TABLE subjects ADD PRIMARY KEY (subject_name,subject_id);");

        db.execSQL("INSERT INTO subjects(subject_name,subject_id,dept,type,section,semester,credit) " +
                "values('"+subjectName+"','"+subjectId+"','"+deptName+"','"+type+"','"+section+"','"+semester+"','"+credit+"')");
        db.close(); // Closing database connection
    }

    void addStudent(String name,String ID,String tableName){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("INSERT INTO "+tableName+"(ID,name) " +
                "values('"+ID+"','"+name+"')");
        db.close();
    }


    void createTableTheory(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table "+name+"(name TEXT,ID integer primary key,ct1 double,ct2 double,ct3 double,ct4 double,ct5 double)");
    }

    void createTableLab(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table "+name+"(name Text,ID integer primary key,Lab1 Double Default '-1',Lab2 Double Default '-1',Lab3 Double Default '-1'," +
                "Lab4 Double Default '-1',Lab5 Double Default '-1',Lab6 Double Default '-1',Lab7 Double Default '-1'," +
                "Lab8 Double Default '-1',Lab9 Double Default '-1',Lab10 Double Default '-1',Lab11 Double Default '-1',Lab12 Double Default '-1',Lab13 Double Default '-1'," +
                "attendance Double Default '-1',quiz Double Default '-1',viva Double Default '-1')");
    }

    int cheeck(String subjectId,String section){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from subjects where subject_id="+subjectId+" and section="+section+";",null);
        return cursor.getCount();
    }

    List<Subject> getDataFromDB(){
        List<Subject> modelList = new ArrayList<Subject>();
        String query = "select * from subjects";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                Subject model = new Subject();
                model.setSubjectId(cursor.getString(1));
                model.setSubjectName(cursor.getString(0));
                model.setDeptName(cursor.getString(2));
                model.setType(cursor.getString(3));
                model.setSection(cursor.getString(4));
                model.setSemester(cursor.getString(5));
                model.setCredit(cursor.getString(6));

                modelList.add(model);
            }while (cursor.moveToNext());
        }
        cursor.close();

        return modelList;
    }

    List<Student> getStudentFromDB(String tableName){
        List<Student> studentList = new ArrayList<Student>();
        String query = "select * from "+tableName+";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        if (cursor.moveToFirst()){
            do {
                Student student = new Student();
                student.setStudentName(cursor.getString(0));
                student.setStudentID(cursor.getString(1));
                student.setTableName(tableName);
                studentList.add(student);
            }while (cursor.moveToNext());
        }
        cursor.close();
        return studentList;
    }

    String[] getCtMarks(String ID,String tableName){
        String[] s = new String[100];
        String query = "select * from "+tableName+" where ID='"+ID+"';";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);
        cursor.moveToFirst();
        s[0]=cursor.getString(0);
        s[1]=cursor.getString(1);
        s[2]=cursor.getString(2);
        s[3]=cursor.getString(3);
        s[4]=cursor.getString(4);
        s[5]=cursor.getString(5);
        s[6]=cursor.getString(6);

        /*if(s[2]==null)
            s[2]="-1";
        s[3]=cursor.getString(3);
        if(s[3]==null)
            s[3]="-1";
        s[4]=cursor.getString(4);
        if(s[4]==null)
            s[4]="-1";*/
        /*if (cursor.moveToFirst()){
            do {
                cursor.
            }while (cursor.moveToNext());
        }*/
        cursor.close();

        return s;

    }

    public void setCtMarks(String tableName,String ID,String Name,String ct1,String ct2,String ct3,String ct4,String ct5){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        sqLiteDatabase.execSQL("Update "+tableName+" set name='"+Name+"',ct1='"+ct1+"',ct2='"+ct2+"',ct3='"+ct3+"',ct4='"+ct4+"',ct5='"+ct5+"' where ID='"+ID+"';");
    }

    int rowNumber(String tableName){
        int x;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery("Select count(*) from "+tableName,null);
        cursor.moveToFirst();
        x = Integer.parseInt(cursor.getString(0));

        cursor.close();
        return x;
    }

    double[][] getCtMarks(String tableName){
        double[][] d = new double[10][1000];
        String query = "select * from "+tableName+";";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query,null);

        int i = 0;

        if (cursor.moveToFirst()){
            do {
                d[0][i] = cursor.getInt(1);
                d[1][i] = cursor.getDouble(2);
                d[2][i] = cursor.getDouble(3);
                d[3][i] = cursor.getDouble(4);
                d[4][i] = cursor.getDouble(5);
                d[5][i] = cursor.getDouble(6);
                i++;
            }while (cursor.moveToNext());
        }
        cursor.close();
        return d;
    }

    public void updateDatabase(Subject subject,Subject prevSubject){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        String table_name,new_table_name,s1,s2,s3,s4;
        s1 = prevSubject.getSubjectId().replaceAll("-","_");
        s2 = subject.getSubjectId().replaceAll("-","_");
        s3 = prevSubject.getSection().replaceAll("-","_");
        s4 = subject.getSection().replaceAll("-","_");

        table_name = s1.replaceAll("\\s","_")+"_"+s3.replaceAll("\\s","_");
        new_table_name = s2.replaceAll("\\s","_")+"_"+s4.replaceAll("\\s","_");

        sqLiteDatabase.execSQL("UPDATE subjects " +
                "SET subject_name='"+subject.getSubjectName()+"',subject_id='"+subject.getSubjectId()+
                "',dept='"+subject.getDeptName()+"',type='" +
                ""+subject.getType()+"',semester='"+subject.getSemester()+
                "',section='"+subject.getSection()+"' where subject_id='"+prevSubject.getSubjectId()+"'and section='"+prevSubject.getSection()+"';");
        if(!table_name.equals(new_table_name)){
            sqLiteDatabase.execSQL("ALTER TABLE " +table_name+ "  RENAME TO "+new_table_name);
            sqLiteDatabase.execSQL("ALTER TABLE " +table_name+"_attendance"+ "  RENAME TO "+new_table_name+"_attendance");
        }


    }

    void deleteDataSubjects(String id,String section) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from subjects where subject_id='"+id+"' and section='"+section+"'");
        db.close();
    }

    void deleteData(String tableName,String id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE from "+tableName+" where ID='"+id+"'");
        db.close();
    }

    void dropTable(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("Drop table if exists "+name);
        db.close();
    }

    /*public void  addColumn(String tableName,String colName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("ALTER TABLE "+tableName+
                "  ADD "+colName+" TEXT");
        db.close();
    }
*/
    String[] columnName(String tableName){

        SQLiteDatabase db = getReadableDatabase();
        Cursor dbCursor = db.query(tableName, null, null, null, null, null, null);
        String[] columnNames = dbCursor.getColumnNames();

        System.out.println(columnNames.length);
        return  columnNames;
    }

    void setLabMarks(String tableName,String name,String ID,String[] s,String quiz, String viva){
        String query = "update "+tableName+" set name='"+name+"',Lab1="+s[0]+",Lab2="+s[1]+",Lab3="+s[2]+",Lab4="+s[3]+",Lab5="+s[4]+",Lab6="+s[5]+",Lab7="+s[6]+"," +
                "Lab8="+s[7]+",Lab9="+s[8]+",Lab10="+s[9]+",Lab11="+s[10]+",Lab12="+s[11]+",Lab13="+s[12]+",quiz="+quiz+",viva="+viva+" where ID='"+ID+"'";

        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(query);
    }



    Object[] getLabMarks(String tableName,String ID){
        String query;
        query = "Select * from "+tableName+" where ID="+ID;

        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query,null);
        String s[] = new String[5];
        Double d[] = new Double[16];
        if(cursor.moveToFirst()){
            do{
                s[0] = cursor.getString(0);
                s[1] = cursor.getString(1);

                for(int i=0;i<=15;i++){
                    d[i] = cursor.getDouble(i+2);
                    System.out.println(d[i]);
                }

            }while (cursor.moveToNext());
        }

        return new Object[]{s,d};
    }

    Object[] getLabMarks(String tableName){
        String query;
        query = "Select * from "+tableName;
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.rawQuery(query,null);
        String s[][] = new String[5][1000];
        Double d[][] = new Double[16][1000];
        int j = 0 ;
        if(cursor.moveToFirst()){
            do{
                s[0][j] = cursor.getString(0);
                s[1][j] = cursor.getString(1);

                for(int i=0;i<=15;i++){
                    d[i][j] = cursor.getDouble(i+2);
                }
                j++;
            }while (cursor.moveToNext());
        }

        return new Object[]{s,d};
    }

    void addAttendance(String name,String column,String at,int ID){
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("Update "+name+" set "+column+" = " +
                "'"+at+"' where ID="+ID);
        db.close();
    }

    void createTableAttendance(String name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("create table "+name+"(ID integer)");
    }
    void addRoll(String table,int ID){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("insert into "+table+"(ID) values("+ID+")");
    }
    void  addColumn(String tableName,String colName){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("ALTER TABLE "+tableName+
                "  ADD "+colName+" TEXT");
        db.close();
    }

    String[] showAttendance(String tableName,int length){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select * from "+tableName,null);
        int l = columnName(tableName).length;
        String[] s = new String[length+1];
        if (cursor.moveToFirst()){
            int j=0;
            do {
                s[j]="";
                for(int i=1;i<l;i++){
                    if(cursor.getString(i)!=null)
                        s[j]=s[j]+cursor.getString(i);
                    else
                        s[j]=s[j]+"A";
                }
                j++;
            }while (cursor.moveToNext());
        }
        cursor.close();

        return s;
    }

    int[] showAttendance(String tableName,String columnName){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("Select "+columnName+" from "+tableName,null);
        int[] s = new int[1000];

        if (cursor.moveToFirst()){
            int j=0;
            do {
                System.out.println("yes = "+cursor.getString(0));
                if(cursor.getString(0)==null)
                    s[j] = 1;
                else if(cursor.getString(0).equals("P"))
                    s[j] = 0;
                else if(cursor.getString(0).equals("A"))
                    s[j] = 1;
                else
                    s[j] = 2;

                j++;
            }while (cursor.moveToNext());
        }
        cursor.close();

        return s;
    }
}
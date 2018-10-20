package net.net23.fahimabrar.teacherassistant;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.RelativeLayout;

import java.util.ArrayList;
import java.util.List;


public class StudentActivity extends AppCompatActivity {
    List<Student> dbList;

    String tableName,courseName,credit,type;
    RelativeLayout relativeLayoutNoStudent;

    @Override
    protected void onResume() {
        super.onResume();
        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewStudent);

        mRecyclerView.setHasFixedSize(true);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        dbList= new ArrayList<Student>();
        dbList = databaseHandler.getStudentFromDB(tableName);

        if(dbList.isEmpty()){
            relativeLayoutNoStudent.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else{
            relativeLayoutNoStudent.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }

        // specify an adapter (see also next example)
        RecyclerAdapterStudent mAdapter = new RecyclerAdapterStudent(this,dbList,credit,type);
        mAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        relativeLayoutNoStudent = (RelativeLayout) findViewById(R.id.realtiveLayoutNoStudent);


        Intent intent = getIntent();
        tableName = intent.getStringExtra("name");
        courseName = intent.getStringExtra("course");
        credit = intent.getStringExtra("credit");
        type = intent.getStringExtra("type");

        toolbar.setTitle(courseName+" ("+type+")");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StudentActivity.this,AddStudent.class);
                intent.putExtra("name",tableName);
                System.out.println("credit "+credit);

                startActivity(intent);
            }
        });



        RecyclerView mRecyclerView = (RecyclerView)findViewById(R.id.recyclerViewStudent);
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        float offsetPx = getResources().getDimension(R.dimen.bottom_offset_dp);
        RecyclerAdapterStudent.BottomOffsetDecoration bottomOffsetDecoration = new RecyclerAdapterStudent.BottomOffsetDecoration((int) offsetPx);
        mRecyclerView.addItemDecoration(bottomOffsetDecoration);

        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        dbList= new ArrayList<Student>();
        dbList = databaseHandler.getStudentFromDB(tableName);

        if(dbList.isEmpty()){
            relativeLayoutNoStudent.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else {
            relativeLayoutNoStudent.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);

            // specify an adapter (see also next example)
            RecyclerAdapterStudent mAdapter = new RecyclerAdapterStudent(this, dbList, credit, type);
            mAdapter.notifyDataSetChanged();
            mRecyclerView.setAdapter(mAdapter);
        }
    }
}

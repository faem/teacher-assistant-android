package net.net23.fahimabrar.teacherassistant;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableString;
import android.text.style.TextAppearanceSpan;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.drive.Drive;
import com.google.android.gms.drive.DriveApi;
import com.google.android.gms.drive.DriveContents;
import com.google.android.gms.drive.DriveFile;
import com.google.android.gms.drive.DriveFolder;
import com.google.android.gms.drive.DriveId;
import com.google.android.gms.drive.DriveResource;
import com.google.android.gms.drive.Metadata;
import com.google.android.gms.drive.MetadataBuffer;
import com.google.android.gms.drive.MetadataChangeSet;
import com.google.android.gms.drive.query.Filters;
import com.google.android.gms.drive.query.Query;
import com.google.android.gms.drive.query.SearchableField;
import com.google.api.client.extensions.android.http.AndroidHttp;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.ExponentialBackOff;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.*;
import org.apache.commons.io.FileUtils;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener , GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private static final int EXTERNAL_STORAGE_PERMISSION_CONSTANT = 100;
    private static final int REQUEST_PERMISSION_SETTING = 101;
    private boolean sentToSettings = false;
    private SharedPreferences permissionStatus;

    List<Subject> dbList;
    RelativeLayout relativeLayoutNoCourse;

    private static final String TAG = "Google Drive Activity";
    private static final int REQUEST_CODE_RESOLUTION = 1;
    public GoogleApiClient mGoogleApiClient;
    private boolean fileOperation = false;
    public DriveFile file;
    File localDb;
    String fileId;
    private com.google.api.services.drive.Drive mService = null;
    GoogleAccountCredential credential;
    private static final String[] SCOPES = { DriveScopes.DRIVE_METADATA_READONLY };
    String dbPath;
    File mainFile,tempFile;
    InputStream inputStream;
    OutputStream outputStream;
    DriveContents driveContents;
    RecyclerView mRecyclerView;
    Bundle bundle = new Bundle();
    private Menu menu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        bundle = savedInstanceState;
        overridePendingTransition(R.anim.pull_in_from_left,R.anim.hold);
        setContentView(R.layout.activity_main);
        /*
        if(ContextCompat.checkSelfPermission(MainActivity.this,Manifest.permission.READ_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE});
        }*/

        relativeLayoutNoCourse = (RelativeLayout) findViewById(R.id.realtiveLayoutNoCourse);
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);
        //TrialPeriod();

        //color of more changes



        permissionStatus = getSharedPreferences("permissionStatus",MODE_PRIVATE);

        System.out.println(permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false));

        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                //Show Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setCancelable(true);
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.show();
            } else if (permissionStatus.getBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,false)) {
                //Previously Permission Request was cancelled with 'Dont Ask Again',
                // Redirect to Settings after showing Information about why you need the permission
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Need Storage Permission");
                builder.setMessage("This app needs storage permission.");
                builder.setCancelable(true);
                builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialogInterface) {
                        Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
                builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        sentToSettings = true;
                        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                        startActivityForResult(intent, REQUEST_PERMISSION_SETTING);
                        Toast.makeText(getBaseContext(), "Go to Permissions to Grant Storage", Toast.LENGTH_LONG).show();
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        finish();
                    }
                });
                builder.show();
            } else {
                //just request the permission
                ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);
            }

            SharedPreferences.Editor editor = permissionStatus.edit();
            editor.putBoolean(Manifest.permission.WRITE_EXTERNAL_STORAGE,true);
            editor.commit();


        } else {
            System.out.println("Permission");
            //You already have the permission, just go ahead.
            mainFile = new File("/sdcard/TeacherAssistant/Data/Data");
            tempFile = new File("/sdcard/TeacherAssistant/Data/Temp");
            try {
                boolean isEqual = FileUtils.contentEquals(mainFile,tempFile);

                if(!isEqual){
                    if (mGoogleApiClient == null) {
                        mGoogleApiClient = new GoogleApiClient.Builder(this)
                                .addApi(Drive.API)
                                .addScope(Drive.SCOPE_FILE)
                                .addConnectionCallbacks(this)
                                .addOnConnectionFailedListener(this)
                                .build();
                    }

                    mGoogleApiClient.connect();
                    Query query = new Query.Builder()
                            .addFilter(Filters.contains(SearchableField.TITLE, "TeacherAssistant.backup"))
                            .build();
                    Drive.DriveApi.query(mGoogleApiClient, query)
                            .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                @Override
                                public void onResult(DriveApi.MetadataBufferResult result) {
                                    if (!result.getStatus().isSuccess()) {
                                        return;
                                    }

                                    MetadataBuffer metadataBuffer = result.getMetadataBuffer();
                                    int x = metadataBuffer.getCount();
                                    if(x==0){
                                        Drive.DriveApi.newDriveContents(mGoogleApiClient)
                                                .setResultCallback(driveContentsCallback);
                                    }else{
                                        Log.e("X=",""+x);

                                        Metadata metadata = metadataBuffer.get(0);
                                        DriveFile cloudFile = metadata.getDriveId().asDriveFile();
                                        Log.e("filename",metadata.getDriveId().getResourceId());
                                        cloudFile.open(mGoogleApiClient, DriveFile.MODE_WRITE_ONLY, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                                            @Override
                                            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                                                if (!driveContentsResult.getStatus().isSuccess()) {
                                                    Log.e("connection","error");
                                                }
                                                driveContents = driveContentsResult.getDriveContents();
                                                Log.e("DriveContent",driveContents.getDriveId().getResourceId());
                                                OutputStream newOutputStream = driveContents.getOutputStream();
                                                OutputStream localOutputstream = null;
                                                try {
                                                    localOutputstream =new FileOutputStream(new File("/sdcard/TeacherAssistant/Data/Temp"));
                                                    inputStream= new FileInputStream(localDb);
                                                } catch (FileNotFoundException e) {
                                                    e.printStackTrace();
                                                }
                                                byte[] buffer = new byte[4096];
                                                int n;

                                                // IT SURE WOULD BE NICE IF TRY-WITH-RESOURCES WAS SUPPORTED IN OLDER SDK VERSIONS :(
                                                try {
                                                    while ((n = inputStream.read(buffer)) > 0) {
                                                        newOutputStream.write(buffer,0,n);
                                                        localOutputstream.write(buffer,0,n);
                                                        //Log.e("buffer",buffer.toString());
                                                    }
                                                    driveContents.commit(mGoogleApiClient, null).setResultCallback(new ResultCallback<Status>() {
                                                        @Override
                                                        public void onResult(@NonNull Status status) {
                                                            Log.e("result",""+status.getStatus().isSuccess());
                                                        }
                                                    });
                                                } catch (IOException e) {
                                                    Log.e("IOException", "a stream is null");
                                                    ;
                                                } finally {
                                                    try {
                                                        inputStream.close();
                                                    } catch (IOException e) {
                                                        Log.e("ioexception","inputstream");

                                                    }
                                                }
                                            }
                                        });
                                        Toast.makeText(getApplicationContext(), "Backup Created", Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            DatabaseHandler databaseHandler = new DatabaseHandler(this);
            localDb = databaseHandler.getDatabasePath();
            dbPath = localDb.getPath();
            dbList= new ArrayList<Subject>();
            dbList = databaseHandler.getDataFromDB();

            if(dbList.isEmpty()){
                relativeLayoutNoCourse.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            }else{
                relativeLayoutNoCourse.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                // specify an adapter (see also next example)
                RecyclerAdapter mAdapter = new RecyclerAdapter(this,dbList);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);

                float offsetPx = getResources().getDimension(R.dimen.bottom_offset_dp);
                RecyclerAdapter.BottomOffsetDecoration bottomOffsetDecoration = new RecyclerAdapter.BottomOffsetDecoration((int) offsetPx);
                mRecyclerView.addItemDecoration(bottomOffsetDecoration);
            }

        }

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);

        Menu menu = navigationView.getMenu();

        MenuItem tools= menu.findItem(R.id.more);
        SpannableString s = new SpannableString(tools.getTitle());
        s.setSpan(new TextAppearanceSpan(this, R.style.TextAppearance44), 0, s.length(), 0);
        tools.setTitle(s);
        navigationView.setNavigationItemSelectedListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //fab

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),AddSubject.class);
                startActivity(intent);

            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            DatabaseHandler databaseHandler = new DatabaseHandler(this);
            dbList = new ArrayList<Subject>();
            dbList = databaseHandler.getDataFromDB();

            if (dbList.isEmpty()) {
                relativeLayoutNoCourse.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                relativeLayoutNoCourse.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.VISIBLE);
            }

            if (mGoogleApiClient == null) {
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(Drive.API)
                        .addScope(Drive.SCOPE_FILE)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
            }

            mGoogleApiClient.connect();

            RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
            if (dbList.isEmpty()) {
                relativeLayoutNoCourse.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
            } else {
                mRecyclerView.setHasFixedSize(true);

                // use a linear layout manager
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                mRecyclerView.setLayoutManager(mLayoutManager);

                // specify an adapter (see also next example)
                RecyclerAdapter mAdapter = new RecyclerAdapter(this, dbList);
                mAdapter.notifyDataSetChanged();
                mRecyclerView.setAdapter(mAdapter);
            }
        }else{
            Toast.makeText(MainActivity.this,"Permission is not granted",Toast.LENGTH_LONG).show();

            //finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /** Navigation Item**/

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.home) {
            onResume();
        }else if (id == R.id.nav_about_us) {
            //Toast.makeText(this, "Pressed", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(MainActivity.this,AboutUsActivity.class);
            startActivity(intent);
        }else if(id == R.id.backup){
            final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this,
                    "Backup",
                    "Backing up Data on Google drive.");
            progressDialog.show();
            Thread timer = new Thread() {
                public void run() {
                    try {
                        sleep(1000);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            };
            timer.start();
            fileOperation = true;
            if(mGoogleApiClient==null){
                mGoogleApiClient = new GoogleApiClient.Builder(this)
                        .addApi(Drive.API)
                        .addScope(Drive.SCOPE_FILE)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .build();
                mGoogleApiClient.connect();
            }

            Query query = new Query.Builder()
                    .addFilter(Filters.contains(SearchableField.TITLE, "TeacherAssistant.backup"))
                    .build();
            Drive.DriveApi.query(mGoogleApiClient, query)
                    .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                        @Override
                        public void onResult(DriveApi.MetadataBufferResult result) {
                            if (!result.getStatus().isSuccess()) {
                                return;
                            }

                            MetadataBuffer metadataBuffer = result.getMetadataBuffer();
                            int x = metadataBuffer.getCount();
                            if(x==0){
                                Drive.DriveApi.newDriveContents(mGoogleApiClient)
                                        .setResultCallback(driveContentsCallback);
                                progressDialog.dismiss();
                            }else{
                                Log.e("X=",""+x);

                                Metadata metadata = metadataBuffer.get(0);
                                DriveFile cloudFile = metadata.getDriveId().asDriveFile();
                                Log.e("filename",metadata.getDriveId().getResourceId());
                                cloudFile.open(mGoogleApiClient, DriveFile.MODE_WRITE_ONLY, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                                    @Override
                                    public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                                        if (!driveContentsResult.getStatus().isSuccess()) {
                                            Log.e("connection","error");
                                        }
                                        driveContents = driveContentsResult.getDriveContents();
                                        Log.e("DriveContent",driveContents.getDriveId().getResourceId());
                                        OutputStream newOutputStream = driveContents.getOutputStream();
                                        OutputStream localOutputstream = null;
                                        try {
                                            localOutputstream =new FileOutputStream(new File("/sdcard/TeacherAssistant/Data/Temp"));
                                            inputStream= new FileInputStream(localDb);
                                        } catch (FileNotFoundException e) {
                                            e.printStackTrace();
                                            progressDialog.dismiss();
                                        }
                                        byte[] buffer = new byte[4096];
                                        int n;

                                        // IT SURE WOULD BE NICE IF TRY-WITH-RESOURCES WAS SUPPORTED IN OLDER SDK VERSIONS :(
                                        try {
                                            while ((n = inputStream.read(buffer)) > 0) {
                                                newOutputStream.write(buffer,0,n);
                                                localOutputstream.write(buffer,0,n);
                                                //Log.e("buffer",buffer.toString());
                                            }
                                            driveContents.commit(mGoogleApiClient, null).setResultCallback(new ResultCallback<Status>() {
                                                @Override
                                                public void onResult(@NonNull Status status) {
                                                    Log.e("result",""+status.getStatus().isSuccess());
                                                }
                                            });
                                        } catch (IOException e) {
                                            Log.e("IOException", "a stream is null");
                                            progressDialog.dismiss();
                                        } finally {
                                            try {
                                                inputStream.close();
                                            } catch (IOException e) {
                                                Log.e("ioexception","inputstream");
                                                progressDialog.dismiss();
                                            }
                                        }
                                    }
                                });

                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "Backup Created", Toast.LENGTH_LONG).show();
                            }
                        }
                    });

        }else if(id == R.id.restore){
            AlertDialog.Builder localBuilder = new AlertDialog.Builder(this);
            localBuilder.setTitle("Alert");
            localBuilder.setMessage("Do you want to restore database from Google Drive?");
            localBuilder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    final ProgressDialog progressDialog = ProgressDialog.show(MainActivity.this,
                            "Restore",
                            "Restoring Data from Google drive.");
                    progressDialog.show();
                    Thread timer = new Thread() {
                        public void run() {
                            try {
                                sleep(1000);
                            } catch (InterruptedException ie) {
                                ie.printStackTrace();
                            }
                        }
                    };
                    timer.start();

                    Query query = new Query.Builder()
                            .addFilter(Filters.contains(SearchableField.TITLE, "TeacherAssistant.backup"))
                            .build();
                    Drive.DriveApi.query(mGoogleApiClient, query)
                            .setResultCallback(new ResultCallback<DriveApi.MetadataBufferResult>() {
                                @Override
                                public void onResult(DriveApi.MetadataBufferResult result) {
                                    if (!result.getStatus().isSuccess()) {
                                        return;
                                    }

                                    MetadataBuffer metadataBuffer = result.getMetadataBuffer();
                                    int x = metadataBuffer.getCount();
                                    Log.e("count",""+x);
                                    if(x==0){
                                        //Snackbar.make(getconten,"No", BaseTransientBottomBar.LENGTH_LONG).show();
                                        Toast.makeText(MainActivity.this,"No Backup Data Found!",Toast.LENGTH_LONG).show();
                                        progressDialog.dismiss();
                                    }else{
                                        Metadata metadata = metadataBuffer.get(0);
                                        fileId = metadata.getDriveId().getResourceId();
                                        Log.e("fileid",fileId);
                                        Drive.DriveApi.fetchDriveId(mGoogleApiClient, fileId)
                                                .setResultCallback(new ResultCallback<DriveApi.DriveIdResult>() {
                                                    @Override
                                                    public void onResult(DriveApi.DriveIdResult result) {
                                                        if(mGoogleApiClient==null){
                                                            mGoogleApiClient = new GoogleApiClient.Builder(MainActivity.this)
                                                                    .addApi(Drive.API)
                                                                    .addScope(Drive.SCOPE_FILE)
                                                                    .addConnectionCallbacks(MainActivity.this)
                                                                    .addOnConnectionFailedListener(MainActivity.this)
                                                                    .build();
                                                            mGoogleApiClient.connect();
                                                        }
                                                        file = result.getDriveId().asDriveFile();
                                                        file.open(mGoogleApiClient, DriveFile.MODE_READ_ONLY, null).setResultCallback(new ResultCallback<DriveApi.DriveContentsResult>() {
                                                            @Override
                                                            public void onResult(@NonNull DriveApi.DriveContentsResult driveContentsResult) {
                                                                if (!driveContentsResult.getStatus().isSuccess()) {
                                                                    Log.e("connection","error");
                                                                }
                                                                try{
                                                                    driveContents = driveContentsResult.getDriveContents();
                                                                    Log.e("DriveContent",driveContents.toString());
                                                                    inputStream = driveContents.getInputStream();
                                                                    File localFile = new File(dbPath);
                                                                    try {
                                                                        outputStream = new FileOutputStream(localFile);
                                                                    } catch (FileNotFoundException e) {
                                                                        e.printStackTrace();
                                                                    }
                                                                    byte[] buffer = new byte[4096];
                                                                    int n;

                                                                    // IT SURE WOULD BE NICE IF TRY-WITH-RESOURCES WAS SUPPORTED IN OLDER SDK VERSIONS :(
                                                                    try {
                                                                        while ((n = inputStream.read(buffer)) > 0) {
                                                                            outputStream.write(buffer, 0, n);
                                                                        }
                                                                    } catch (IOException e) {
                                                                        Log.e("IOException", "fileCopyHelper | a stream is null");
                                                                    } finally {
                                                                        try {
                                                                            inputStream.close();
                                                                        } catch (IOException e) {
                                                                            // Squash
                                                                        }
                                                                        try {
                                                                            outputStream.close();
                                                                        } catch (IOException e) {
                                                                            // Squash
                                                                        }
                                                                    }
                                                                    onResume();
                                                                    Toast.makeText(MainActivity.this,"Database Restored!",Toast.LENGTH_LONG).show();

                                                                }catch (NullPointerException e){
                                                                    Toast.makeText(getApplicationContext(), "No Backup Data Found!", Toast.LENGTH_LONG).show();
                                                                    progressDialog.dismiss();
                                                                }
                                                                progressDialog.dismiss();
                                                            }
                                                        });
                                                    }
                                                });
                                    }
                                }
                            });

                }
            });

            localBuilder.setPositiveButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface paramAnonymousDialogInterface, int paramAnonymousInt) {
                    paramAnonymousDialogInterface.dismiss();

                }
            });
            AlertDialog alert = localBuilder.create();
            alert.show();

        }else if(id==R.id.csv){
            File dir = new File("/sdcard/TeacherAssistant/csv/");
            final String[] listCsv = dir.list();

            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            if(listCsv.length!=0)
                builder.setTitle("Select to open a file");
            else
                builder.setTitle("No CSV Exported!");

            builder.setItems(listCsv, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    // Do something with the selection
                    String name = listCsv[item];

                    File csvFile = new File("/sdcard/TeacherAssistant/csv/"+name);
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(Uri.fromFile(csvFile), "text/csv");
                    try{
                        startActivity(intent);
                    }catch (ActivityNotFoundException e){
                        Toast.makeText(getApplicationContext(),"No Available App to Open CSV!",Toast.LENGTH_LONG)
                                .show();
                    }

                       /*float offsetPx = getResources().getDimension(R.dimen.bottom_offset_dp);
                       RecyclerAdapterAttendance.BottomOffsetDecoration bottomOffsetDecoration = new RecyclerAdapterAttendance.BottomOffsetDecoration((int) offsetPx);
                       mRecyclerView.addItemDecoration(bottomOffsetDecoration);*/
                    //Toast.makeText(getApplicationContext(),"Item clicked",Toast.LENGTH_LONG).show();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    final ResultCallback<DriveApi.DriveContentsResult> driveContentsCallback =
            new ResultCallback<DriveApi.DriveContentsResult>() {
                @Override
                public void onResult(DriveApi.DriveContentsResult result) {

                    Log.d("driveContentsCallback","Called");

                    if (result.getStatus().isSuccess()) {

                        if (fileOperation == true) {

                            CreateFileOnGoogleDrive(result);

                        } /*else {

                            OpenFileFromGoogleDrive();

                        }*/
                    }


                }
            };






    /** Permission Override method**/
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_STORAGE_PERMISSION_CONSTANT) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //The External Storage Write Permission is granted to you... Continue your left job...
                //proceedAfterPermission();
                Toast.makeText(MainActivity.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                onResume();

            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    //Show Information about why you need the permission
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Need Storage Permission");
                    builder.setMessage("This app needs storage permission");
                    builder.setCancelable(true);
                    builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialogInterface) {
                            Toast.makeText(MainActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });
                    builder.setPositiveButton("Grant", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            dialog.cancel();

                            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_STORAGE_PERMISSION_CONSTANT);


                        }
                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                            finish();
                        }
                    });
                    builder.show();
                } else {
                    Toast.makeText(getBaseContext(),"Unable to get Permission",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }



    /**Google API override methods**/

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //Toast.makeText(getApplicationContext(), "Connected", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult result) {
        Log.i(TAG, "GoogleApiClient connection failed: " + result.toString());

        if (!result.hasResolution()) {

            // show the localized error dialog.
            GoogleApiAvailability.getInstance().getErrorDialog(this, result.getErrorCode(), 0).show();
            return;
        }

        /**
         *  The failure has a resolution. Resolve it.
         *  Called typically when the app is not yet authorized, and an  authorization
         *  dialog is displayed to the user.
         */

        try {

            result.startResolutionForResult(this, REQUEST_CODE_RESOLUTION);

        } catch (IntentSender.SendIntentException e) {

            Log.e(TAG, "Exception while starting resolution activity", e);
        }
    }

    public void CreateFileOnGoogleDrive(DriveApi.DriveContentsResult result){

        Log.d("Createfile","Called");

        final DriveContents driveContents = result.getDriveContents();

        // Perform I/O off the UI thread.
        new Thread() {
            @Override
            public void run() {
                // write content to DriveContents
                InputStream localDbInputStream;
                OutputStream outputStream = driveContents.getOutputStream();
                Log.e("path", localDb.toString());
                try {
                    localDbInputStream = new FileInputStream(localDb);
                    byte[] buffer = new byte[4096];
                    int n;

                    // IT SURE WOULD BE NICE IF TRY-WITH-RESOURCES WAS SUPPORTED IN OLDER SDK VERSIONS :(
                    try {
                        while ((n = localDbInputStream.read(buffer)) > 0) {
                            outputStream.write(buffer, 0, n);
                        }
                    } catch (IOException e) {
                        Log.e("IOException", "fileCopyHelper | a stream is null");
                    } finally {
                        try {
                            localDbInputStream.close();
                        } catch (IOException e) {
                            // Squash
                        }
                        try {
                            outputStream.close();
                        } catch (IOException e) {
                            // Squash
                        }
                    }
                } catch (FileNotFoundException e) {

                    Log.e("Controller", "Local Db file not found");

                }



                MetadataChangeSet changeSet = new MetadataChangeSet.Builder()
                        .setTitle("TeacherAssistant.backup")
                        .setMimeType("application/x-sqlite3")
                        .setStarred(true).build();

                // create a file in root folder
                Drive.DriveApi.getRootFolder(mGoogleApiClient)
                        .createFile(mGoogleApiClient, changeSet, driveContents)
                        .setResultCallback(fileCallback);
            }
        }.start();
    }
    final private ResultCallback<DriveFolder.DriveFileResult> fileCallback = new
            ResultCallback<DriveFolder.DriveFileResult>() {
                @Override
                public void onResult(DriveFolder.DriveFileResult result) {
                    if (result.getStatus().isSuccess()) {

                        fileId = result.getDriveFile().getDriveId().toString();

                        Toast.makeText(getApplicationContext(), "Backup Created", Toast.LENGTH_LONG).show();

                    }

                    return;
                }
            };
}

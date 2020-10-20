package com.example.dissertation814.controllers.receiveFiles;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dissertation814.R;
import com.example.dissertation814.controllers.auth.LoginActivity;
import com.example.dissertation814.controllers.student.StudentDashActivity;
import com.example.dissertation814.controllers.teacher.TeacherDashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Objects;

public class ReceiveFilesActivity extends AppCompatActivity {

    public String sharedFileName;
    public Uri sharedFileUri;
    public String currentUserAccount;
    public String teacherAccount = "Teacher";
    public String currentUserId;

    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_files);

        //get intent and MIME type
        if(mAuth.getCurrentUser() == null) {

            showLoginDialog();

        } else {
            Intent intent = getIntent();
            String type = intent.getType();

            assert type != null;
            if (type.startsWith("application/")) {
                //get shared file uri
                sharedFileUri = intent.getParcelableExtra(Intent.EXTRA_STREAM);
                //get shared file name for display
                assert sharedFileUri != null;
                sharedFileName = getFileName(sharedFileUri);
            }

            currentUserId = mAuth.getCurrentUser().getUid();

            //get account type from firebase of current user, show appropriate dialog
            //uid is current users userId from firebase
            String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            userDetails.keepSynced(true);

            userDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUserAccount = dataSnapshot.child("account").getValue().toString();
                    //dash nav if/else statements
                    if (currentUserAccount.equals(teacherAccount)) {
                        showTeacherDialog(sharedFileUri, sharedFileName);
                    } else {
                        showStudentDialog(sharedFileUri, sharedFileName);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

    }

    /**
     * ---------------------------------METHODS--------------------------------------
     */
    private void showLoginDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        //dialog xml
        final View dialogView = inflater.inflate(R.layout.file_login_dialog, null);

        builder.setView(dialogView);

        final Button loginButton = dialogView.findViewById(R.id.loginButton);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiveFilesActivity.this, LoginActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
                finish();
            }
        });
    }

    private void showTeacherDialog(final Uri sharedFileUri, final String sharedFileName){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        //dialog xml
        final View dialogView = inflater.inflate(R.layout.teacher_save_file_dialog, null);

        builder.setView(dialogView);

        final TextView fileNameTV = dialogView.findViewById(R.id.sharedFileNameTextView);
        final ImageButton buttonNo = dialogView.findViewById(R.id.imageButtonNo4);
        final ImageButton buttonYes = dialogView.findViewById(R.id.imageButtonYes4);

        //set text to the shared file name
        fileNameTV.setText(sharedFileName);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiveFilesActivity.this, TeacherDashActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
                finish();
            }
        });

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call save file method
                saveTeacherFile(sharedFileUri, sharedFileName);

                Intent intent = new Intent(ReceiveFilesActivity.this, TeacherDashActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
                finish();

            }
        });
    }

    private void saveTeacherFile(final Uri sharedFileUri, final String sharedFileName){

        String folder_main = "/GuitarAppStorage";

        //get or create parent folder for app
        String rootPath = String.valueOf(Environment.getExternalStorageDirectory());
        File parentFolder = new File(rootPath + folder_main);
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }

        //get or create child folder with currentUserId as folder name
        File childFolder = new File(rootPath + folder_main,currentUserId);
        if(!childFolder.exists()){
            childFolder.mkdirs();
        }

        //new file with path and filename
        File file = new File(childFolder, sharedFileName);

        //write to folder with uri
        try {
            InputStream in = getContentResolver().openInputStream(sharedFileUri); //passed in uri
            OutputStream out = new FileOutputStream(file); //file path created earlier
            byte[] buf = new byte[1024];
            int len;
            assert in != null;
            while ((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();

            Toast.makeText(this,"File Saved", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private void showStudentDialog(final Uri sharedFileUri, final String sharedFileName){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        //dialog xml
        final View dialogView = inflater.inflate(R.layout.student_save_file_dialog, null);

        builder.setView(dialogView);

        final TextView fileNameTV = dialogView.findViewById(R.id.sharedFileNameTextView);
        final Button buttonCancel = dialogView.findViewById(R.id.cancelButton);
        final Button buttonLesson = dialogView.findViewById(R.id.lessonsButton);
        final Button buttonHomework = dialogView.findViewById(R.id.homeworkButton);


        //set text to the shared file name
        fileNameTV.setText(sharedFileName);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReceiveFilesActivity.this, StudentDashActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
                finish();
            }
        });

        buttonLesson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call save lesson method
                saveLessonFile(sharedFileUri, sharedFileName);

                Intent intent = new Intent(ReceiveFilesActivity.this, StudentDashActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
                finish();

            }
        });

        buttonHomework.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //call save homework method
                saveHomeworkFile(sharedFileUri, sharedFileName);

                Intent intent = new Intent(ReceiveFilesActivity.this, StudentDashActivity.class);
                startActivity(intent);
                alertDialog.dismiss();
                finish();

            }
        });

    }

    private void saveLessonFile(final Uri sharedFileUri, final String sharedFileName){

        String folder_main = "/GuitarAppStorage";
        String lessonFolderName = "Lessons";

        //get or create parent folder for app
        String rootPath = String.valueOf(Environment.getExternalStorageDirectory());
        File parentFolder = new File(rootPath + folder_main);
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }

        //get or create child folder with currentUserId as folder name
        File childFolder = new File(rootPath + folder_main,currentUserId);
        if(!childFolder.exists()){
            childFolder.mkdirs();
        }

        //create 'lesson' folder inside child folder
        File lessonFolder = new File(childFolder, lessonFolderName);
        if (!lessonFolder.exists()){
            lessonFolder.mkdirs();
        }

        //new file with path and filename
        File file = new File(lessonFolder, sharedFileName);

        //write to folder with uri
        try {
            InputStream in = getContentResolver().openInputStream(sharedFileUri); //passed in uri
            OutputStream out = new FileOutputStream(file); //file path created earlier
            byte[] buf = new byte[1024];
            int len;
            assert in != null;
            while ((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();

            Toast.makeText(this,"File Saved", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void saveHomeworkFile(final Uri sharedFileUri, final String sharedFileName){

        String folder_main = "/GuitarAppStorage";
        String homeworkFolderName = "Homework";

        //get or create parent folder for app
        String rootPath = String.valueOf(Environment.getExternalStorageDirectory());
        File parentFolder = new File(rootPath + folder_main);
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }

        //get or create child folder with currentUserId as folder name
        File childFolder = new File(rootPath + folder_main,currentUserId);
        if(!childFolder.exists()){
            childFolder.mkdirs();
        }

        //create 'homework' folder inside child folder
        File homeworkFolder = new File(childFolder,homeworkFolderName);
        if (!homeworkFolder.exists()){
            homeworkFolder.mkdirs();
        }

        //new file with path and filename
        File file = new File(homeworkFolder, sharedFileName);

        //write to folder with uri
        try {
            InputStream in = getContentResolver().openInputStream(sharedFileUri); //passed in uri
            OutputStream out = new FileOutputStream(file); //file path created earlier
            byte[] buf = new byte[1024];
            int len;
            assert in != null;
            while ((len=in.read(buf))>0){
                out.write(buf,0,len);
            }
            out.close();
            in.close();

            Toast.makeText(this,"File Saved", Toast.LENGTH_LONG).show();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

}

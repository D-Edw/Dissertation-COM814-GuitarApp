package com.example.dissertation814.controllers.teacher.yourFiles;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.widget.SearchView;

import com.example.dissertation814.BuildConfig;
import com.example.dissertation814.R;
import com.example.dissertation814.adapters.EmailSelectionAdapter;
import com.example.dissertation814.adapters.PdfFileAdapter;
import com.example.dissertation814.controllers.emailExport.EmailExportActivity;
import com.example.dissertation814.models.PdfFile;
import com.example.dissertation814.models.Student;
import com.example.dissertation814.controllers.pdfViewer.PdfViewerActivity;
import com.example.dissertation814.controllers.student.StudentDashActivity;
import com.example.dissertation814.controllers.teacher.TeacherDashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class YourFilesActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth mAuth;

    //public variables
    public String currentUserAccount;
    public String teacherAccountNav = "Teacher";
    public String currentUserId;

    //file paths
    public String rootPath;
    public File parentFolder;
    public File childFolder;
    //parent folder string
    public String folder_main = "/GuitarAppStorage";

    //pdf recyclerView variables
    ArrayList<PdfFile> pdfList = new ArrayList<>();
    RecyclerView recyclerView;
    SearchView searchView;

    //student email recyclerView variables
    DatabaseReference ref;
    ArrayList<Student> list = new ArrayList<>();

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_files_teacher);

        //get current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //assert current user is not null and get current users id
        assert user != null;
        currentUserId = user.getUid();

        //getting firebase reference of current users students
        ref = FirebaseDatabase.getInstance().getReference().child("students").child(currentUserId);

        //create students list for email retrieval later
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //add students from firebase to an array list
                if(dataSnapshot.exists()){
                    list = new ArrayList<>();
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        list.add(ds.getValue(Student.class));
                    }

                    //sort by name
                    Collections.sort(list, Student.myName);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //create parent folder for app
        rootPath = String.valueOf(Environment.getExternalStorageDirectory());
        parentFolder = new File(rootPath + folder_main);
        if(!parentFolder.exists()){
            parentFolder.mkdirs();
        }

        //child folders creation with currentUserId as folder name
        childFolder = new File(rootPath + folder_main,currentUserId);
        if(!childFolder.exists()){
            childFolder.mkdirs();
        }

        //initialise rv view
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);

        //initialise search view
        searchView = findViewById(R.id.searchView);

    }

    /**
     * ---------------------------------onStart--------------------------------------
     */
    @Override
    protected void onStart(){
        super.onStart();

        pdfList.clear();

        PdfFile p;

        if(childFolder.exists()){
            //get all pdf files in child folder
            File [] files = childFolder.listFiles();

            //Loop through the pdf files and get name and uri
            for (int i=0; i<files.length; i++){
                File file = files[i];

                p = new PdfFile();
                p.setPdfFileName(file.getName());
                p.setPdfFileUri(Uri.fromFile(file));

                pdfList.add(p);
            }
        }
        //sort by name
        Collections.sort(pdfList, PdfFile.myFile);

        //set rv layout manager and adapter
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        PdfFileAdapter pdfFileAdapter = new PdfFileAdapter(pdfList);
        recyclerView.setAdapter(pdfFileAdapter);

        //click listeners for buttons
        pdfFileAdapter.setOnItemClickListener(new PdfFileAdapter.OnItemClickListener() {
            @Override
            public void onShareClick(int position) {

                //get pdf file selection position from array
                PdfFile pdfFile = pdfList.get(position);

                //getting uri to convert to file for File Provider
                Uri uri = pdfFile.getPdfFileUri();
                File file = new File (uri.getPath());
                Uri pdfUri = FileProvider.getUriForFile(YourFilesActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

                //get selected pdf filename
                String pdfName = pdfFile.getPdfFileName();

                //call student list dialog
                showShareDialog(pdfUri, pdfName);

            }

            @Override
            public void onOpenClick(int position) {

                //get pdf file selection position from array
                PdfFile pdfFile = pdfList.get(position);

                //getting uri
                Uri pdfSelectionUri = pdfFile.getPdfFileUri();
                String name = pdfFile.getPdfFileName();

                //call open pdf view method
                openPdfView(pdfSelectionUri, name);

            }

            @Override
            public void onDeleteClick(int position) {
                //handle delete click
                PdfFile pdfFile = pdfList.get(position);

                //show delete dialog
                showDeleteDialog(pdfFile.getPdfFileUri());

            }
        });

        //search view check
        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    /**
     * ---------------------------------METHODS--------------------------------------
     */
    //open pdf view method
    private void openPdfView(Uri uri, String fileName){

        //pass selected file Uri via intent to Pdf Viewer Activity for opening
        Intent intent = new Intent(YourFilesActivity.this, PdfViewerActivity.class);
        intent.putExtra("FILE_PATH_URI", uri);
        intent.putExtra("F_NAME", fileName);
        startActivity(intent);
    }

    private void showShareDialog(final Uri pdfUri, final String pdfName){

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.student_email_dialog, null);

        builder.setView(dialogView);

        //initialise recycler
        RecyclerView rv = dialogView.findViewById(R.id.studentEmailRv);

        //set layout manager and adapter
        rv.setLayoutManager(new LinearLayoutManager(this));
        EmailSelectionAdapter adapter = new EmailSelectionAdapter(list);
        rv.setAdapter(adapter);

        final AlertDialog dialog = builder.create();
        dialog.show();

        adapter.setOnItemClickListener(new EmailSelectionAdapter.OnItemClickListener() {
            @Override
            public void onSelectClick(int position) {
                Student student = list.get(position);

                String email = student.getStudentEmail();

                sharePdfFile(pdfUri, pdfName, email);


                dialog.dismiss();
            }
        });
    }

    private boolean sharePdfFile(Uri pdfUri, String pdfName, String email){

        Intent intent = new Intent(YourFilesActivity.this, EmailExportActivity.class);
        intent.putExtra("PDF_URI", pdfUri);
        intent.putExtra("PDF_NAME", pdfName);
        intent.putExtra("EMAIL_SELECTION", email);
        startActivity(intent);

        return true;
    }

    //dialog box for deleting pdf file
    private void showDeleteDialog(final Uri pdfFileUri){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        //dialog xml
        final View dialogView = inflater.inflate(R.layout.delete_file_dialog, null);

        dialogBuilder.setView(dialogView);

        final ImageButton buttonNo = dialogView.findViewById(R.id.imageButtonNo);
        final ImageButton buttonYes = dialogView.findViewById(R.id.imageButtonYes);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    deletePdfFile(pdfFileUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                alertDialog.dismiss();
            }
        });
    }

    //delete pdf file method
    private boolean deletePdfFile(Uri pdfFileUri) throws IOException {

        File file = new File (pdfFileUri.getPath());
        file.delete();
        if(file.exists()){
            file.getCanonicalFile().delete();
            if(file.exists()){
                getApplicationContext().deleteFile(file.getName());
            }
        }

        Toast.makeText(this, "File Deleted", Toast.LENGTH_LONG).show();

        //clear and get new list
        onStart();

        return true;
    }

    //search bar
    private void search(String str){

        final ArrayList<PdfFile> myList = new ArrayList<>();
        //adding main 'pdfList' to be filtered into 'myList'
        for(PdfFile object : pdfList){
            if(object.getPdfFileName().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }

        //sort by name
        Collections.sort(myList, PdfFile.myFile);

        //make 'searched' recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(YourFilesActivity.this));
        PdfFileAdapter pdfFileAdapter = new PdfFileAdapter(myList);
        recyclerView.setAdapter(pdfFileAdapter);

        //click listeners for buttons
        PdfFileAdapter.setOnItemClickListener(new PdfFileAdapter.OnItemClickListener() {
            @Override
            public void onShareClick(int position) {

                //get pdf file selection position from array
                PdfFile pdfFile = myList.get(position);

                //getting uri to convert to file for File Provider
                Uri uri = pdfFile.getPdfFileUri();
                File file = new File (uri.getPath());
                Uri pdfUri = FileProvider.getUriForFile(YourFilesActivity.this, BuildConfig.APPLICATION_ID + ".provider", file);

                //get selected pdf filename
                String pdfName = pdfFile.getPdfFileName();

                //call student list dialog
                showShareDialog(pdfUri, pdfName);

            }

            @Override
            public void onOpenClick(int position) {

                //get pdf file selection position from array
                PdfFile pdfFile = myList.get(position);

                //getting uri
                Uri pdfSelectionUri = pdfFile.getPdfFileUri();
                String name = pdfFile.getPdfFileName();

                //call open pdf view method
                openPdfView(pdfSelectionUri, name);

            }

            @Override
            public void onDeleteClick(int position) {
                //handle delete click
                PdfFile pdfFile = myList.get(position);

                //show delete dialog
                showDeleteDialog(pdfFile.getPdfFileUri());
            }
        });
    }

    /**
     * ---------------------------------NAVIGATION--------------------------------------
     */
    //home navigation
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onHomeClicked(View view) {
        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

        //get account type from firebase of current user, nav to appropriate dash
        //uid is current users userId from firebase
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userDetails.keepSynced(true);

        userDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserAccount = dataSnapshot.child("account").getValue().toString();
                //dash nav if/else statements
                if (currentUserAccount.equals(teacherAccountNav)) {
                    startActivity(new Intent(YourFilesActivity.this, TeacherDashActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(YourFilesActivity.this, StudentDashActivity.class));
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }//home nav

    /**
     * ---------------------------------HIDE SOFT KEYBOARD--------------------------------------
     */
    //hide soft keyboard
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    assert imm != null;
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }
}

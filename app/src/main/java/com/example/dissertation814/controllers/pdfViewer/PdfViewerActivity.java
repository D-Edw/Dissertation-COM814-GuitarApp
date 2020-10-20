package com.example.dissertation814.controllers.pdfViewer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dissertation814.R;
import com.example.dissertation814.controllers.student.StudentDashActivity;
import com.example.dissertation814.controllers.teacher.TeacherDashActivity;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.File;
import java.util.Objects;

public class PdfViewerActivity extends AppCompatActivity {

    //variables
    public String currentUserAccount;
    public String teacherAccountNav = "Teacher";

    PDFView pdfView;

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_viewer);

        //file name text view
        TextView fileName = findViewById(R.id.fileNameTextView);

        //PDFView to display PDFs
        pdfView = findViewById(R.id.pdfView);

        //use best quality
        pdfView.useBestQuality(true);

        //get data from intent
        Intent i = this.getIntent();
        Uri uri = i.getParcelableExtra("FILE_PATH_URI");
        String name = i.getStringExtra("F_NAME");

        //set title to file name
        fileName.setText(name);

        //Get the pdf file
        assert uri != null;
        File file = new File(Objects.requireNonNull(uri.getPath()));

        if(file.canRead()){
            //load pdf file
            pdfView.fromFile(file)
                    .defaultPage(0)
                    .enableSwipe(true)
                    .onLoad(new OnLoadCompleteListener() {
                @Override
                public void loadComplete(int nbPages) {
                    Toast.makeText(PdfViewerActivity.this, "No. of pages: " + nbPages, Toast.LENGTH_SHORT).show();
                }
            }).load();
        }

    }

    /**
     * ---------------------------------NAVIGATION--------------------------------------
     */

    public void onBackClicked(View view) {
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onHomeClicked (View view){
        //Firebase instance
        //firebase auth
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        //get account type from firebase of current user, nav to appropriate dash
        String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
        DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
        userDetails.keepSynced(true);

        userDetails.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                currentUserAccount = Objects.requireNonNull(dataSnapshot.child("account").getValue()).toString();
                //dash nav if/else statements
                if (currentUserAccount.equals(teacherAccountNav)) {
                    startActivity(new Intent(PdfViewerActivity.this, TeacherDashActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(PdfViewerActivity.this, StudentDashActivity.class));
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

package com.example.dissertation814.controllers.teacher;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dissertation814.R;
import com.example.dissertation814.controllers.teacher.create.CreateActivity;
import com.example.dissertation814.controllers.teacher.studentList.StudentListActivity;
import com.example.dissertation814.controllers.teacher.yourFiles.YourFilesActivity;
import com.example.dissertation814.controllers.userDetails.DetailsActivity;

public class TeacherDashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_dash);
    }

    public void onStudentListClicked(View view){
        startActivity(new Intent(TeacherDashActivity.this, StudentListActivity.class));
        finish();
    }

    public void onDetails (View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onYourFilesClicked(View view){
        startActivity(new Intent(TeacherDashActivity.this, YourFilesActivity.class));
        finish();
    }

    public void onCreateClicked(View view){
        startActivity(new Intent(TeacherDashActivity.this, CreateActivity.class));
        finish();
    }
}

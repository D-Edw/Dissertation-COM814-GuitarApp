package com.example.dissertation814.controllers.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.dissertation814.R;
import com.example.dissertation814.controllers.student.studentFiles.HomeworkFilesActivity;
import com.example.dissertation814.controllers.student.studentFiles.LessonFilesActivity;
import com.example.dissertation814.controllers.userDetails.DetailsActivity;

public class StudentDashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_dash);
    }

    public void onDetails (View view) {
        Intent intent = new Intent(this, DetailsActivity.class);
        startActivity(intent);
        finish();
    }

    public void onLessons (View view) {
        Intent intent = new Intent(this, LessonFilesActivity.class);
        startActivity(intent);
        finish();
    }

    public void onHomework (View view) {
        Intent intent = new Intent(this, HomeworkFilesActivity.class);
        startActivity(intent);
        finish();
    }
}

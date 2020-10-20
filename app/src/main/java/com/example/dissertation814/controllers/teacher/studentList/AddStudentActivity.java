package com.example.dissertation814.controllers.teacher.studentList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dissertation814.R;
import com.example.dissertation814.models.Student;
import com.example.dissertation814.controllers.student.StudentDashActivity;
import com.example.dissertation814.controllers.teacher.TeacherDashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class AddStudentActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth mAuth;

    //navigation variables
    public String currentUserAccount;
    public String teacherAccountNav = "Teacher";

    //user id from firebase
    public String currentUserId;

    //add student variables
    EditText editTextName;
    EditText editTextEmail;
    Button buttonAddStudent;

    private DatabaseReference databaseStudents;

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);

        //initialise views
        editTextName = findViewById(R.id.nameEditText);
        editTextEmail = findViewById(R.id.emailEditText);
        buttonAddStudent = findViewById(R.id.addStudentButton);

        //firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();
        //get reference to 'users' node
        databaseStudents = mFirebaseInstance.getReference("users");

        //using firebase user to get current user
        FirebaseUser user = mAuth.getCurrentUser();
        //asserting current user is not null and getting current users uid
        assert user != null;
        currentUserId = user.getUid();

        //creating new 'students' parent with a child of the current users id
        databaseStudents = FirebaseDatabase.getInstance().getReference("students").child(currentUserId);


        //calls addStudent method when clicked
        buttonAddStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addStudent();
            }
        });
    }

    /**
     * ---------------------------------METHODS--------------------------------------
     */
    //add new student method
    private void addStudent(){
        String studentName = editTextName.getText().toString().trim();
        String studentEmail = editTextEmail.getText().toString().trim();

        //email validation check pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if(TextUtils.isEmpty(studentName)) {
            editTextName.setError("Enter Name");
            return;
        }

        if(TextUtils.isEmpty(studentEmail)) {
            editTextEmail.setError("Enter Email");
            return;
        }

        //email validation
        if (!studentEmail.matches(emailPattern)){
            editTextEmail.setError("Enter Valid Email");
            return;
        }
            //this string is getting the id from firebase
            String id = databaseStudents.push().getKey();

            Student student = new Student(id, studentName, studentEmail);

            //extends databaseStudents with a child of the student id inside the child of current user id
            databaseStudents.child(id).setValue(student);

            Toast.makeText(this, "Student Added", Toast.LENGTH_LONG).show();

            startActivity(new Intent(AddStudentActivity.this, StudentListActivity.class));
            finish();

    }


    /**
     * ---------------------------------NAVIGATION--------------------------------------
     */
    //back button navigation
    public void onBackClicked(View view){
        startActivity(new Intent(AddStudentActivity.this, StudentListActivity.class));
        finish();
    }

    //home navigation
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onHomeClicked(View view) {
        //Firebase auth instance
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
                    startActivity(new Intent(AddStudentActivity.this, TeacherDashActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(AddStudentActivity.this, StudentDashActivity.class));
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

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

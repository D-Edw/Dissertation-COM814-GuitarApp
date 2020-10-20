package com.example.dissertation814.controllers.teacher.studentList;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;



import com.example.dissertation814.R;
import com.example.dissertation814.adapters.StudentAdapterClass;
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


import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

public class StudentListActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth mAuth;

    //public variables
    public String currentUserAccount;
    public String teacherAccountNav = "Teacher";
    public String currentUserId;

    //recyclerView variables
    DatabaseReference ref;
    ArrayList<Student> list;
    ArrayList<Student> myList;
    RecyclerView recyclerView;
    SearchView searchView;

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        //get current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //assert current user is not null and get current users id
        assert user != null;
        currentUserId = user.getUid();

        //getting firebase reference of current users students
        ref = FirebaseDatabase.getInstance().getReference().child("students").child(currentUserId);

        //initialise views
        recyclerView = findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        searchView = findViewById(R.id.searchView);

    }

    /**
     * ---------------------------------onStart-------------------------------------
     */
    @Override
    protected void onStart() {
        super.onStart();

        if(ref != null){
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

                        //make recycler view
                        recyclerView.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
                        StudentAdapterClass studentAdapterClass = new StudentAdapterClass(list);
                        recyclerView.setAdapter(studentAdapterClass);

                        //click listeners for buttons
                        studentAdapterClass.setOnItemClickListener(new StudentAdapterClass.OnItemClickListener() {

                            @Override
                            public void onUpdateClick(int position) {
                                //handle update click in here
                                Student student = list.get(position);

                                //show update dialog here
                                showUpdateDialog(student.getStudentId(), student.getStudentName(), student.getStudentEmail());
                            }

                            @Override
                            public void onDeleteClick(int position){
                                //handle delete click in here
                                Student student = list.get(position);

                                //show delete dialog here
                                showDeleteDialog(student.getStudentId(), position);

                            }

                        });

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(StudentListActivity.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }//if

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    search(s);
                    return true;
                }
            });
        }
    }

    /**
     * ---------------------------------METHODS--------------------------------------
     */
    //dialog box for updating student
    private void showUpdateDialog(final String studentId, String studentName, String studentEmail){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        //dialog xml
        final View dialogView = inflater.inflate(R.layout.update_student_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText editTextName = dialogView.findViewById(R.id.nameEditText);
        final EditText editTextEmail = dialogView.findViewById(R.id.emailEditText);
        final Button buttonUpdate = dialogView.findViewById(R.id.updateButton);

        final AlertDialog alertDialog = dialogBuilder.create();

        //show selected students current details
        editTextName.setText(studentName);
        editTextEmail.setText(studentEmail);

        alertDialog.show();

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editTextName.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();

                //email validation check pattern
                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

                if(TextUtils.isEmpty(name)){
                    editTextName.setError("Enter Name");
                    return;
                }

                if (TextUtils.isEmpty(email)){
                    editTextEmail.setError("Enter Email");
                    return;
                }

                //email validation
                if (!email.matches(emailPattern)){
                    editTextEmail.setError("Enter Valid Email");
                    return;
                }

                updateStudent(studentId, name, email);

                alertDialog.dismiss();
            }
        });
    }

    //update student method
    private void updateStudent(String id, String name, String email){
        //get current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //assert current user is not null and get current users id
        assert user != null;
        currentUserId = user.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students").child(currentUserId).child(id);

        Student student = new Student(id, name, email);

        databaseReference.setValue(student);

        Toast.makeText(this, "Student Updated", Toast.LENGTH_LONG).show();

    }

    //dialog box for deleting student
    private void showDeleteDialog(final String studentId, final int position){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        //dialog XML
        final View dialogView = inflater.inflate(R.layout.delete_student_dialog, null);

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

                deleteStudent(studentId, position);

                alertDialog.dismiss();
            }
        });
    }

    //delete student method
    private void deleteStudent(String id, int position){
        //get current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //assert current user is not null and get current users id
        assert user != null;
        currentUserId = user.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students").child(currentUserId).child(id);

        databaseReference.removeValue();

        list.remove(position);
        recyclerView.getAdapter().notifyDataSetChanged();

        Toast.makeText(this, "Student Deleted", Toast.LENGTH_LONG).show();
    }

    //dialog box for deleting student
    private void showSearchDeleteDialog(final String studentId, final int position){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        //dialog XML
        final View dialogView = inflater.inflate(R.layout.delete_student_dialog, null);

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

                deleteSearchStudent(studentId, position);

                alertDialog.dismiss();
            }
        });
    }

    //delete searched student method
    private void deleteSearchStudent(String id, int position){
        //get current user
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //assert current user is not null and get current users id
        assert user != null;
        currentUserId = user.getUid();

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("students").child(currentUserId).child(id);

        databaseReference.removeValue();

        myList.remove(position);
        recyclerView.getAdapter().notifyDataSetChanged();

        Toast.makeText(this, "Student Deleted", Toast.LENGTH_LONG).show();
    }


    //search bar
    private void search(String str){

        myList = new ArrayList<>();
        //adding main 'list' to be filtered into 'myList'
        for(Student object : list){
            if(object.getStudentName().toLowerCase().contains(str.toLowerCase())){
                myList.add(object);
            }
        }

        //sort by name
        Collections.sort(myList, Student.myName);

        //make 'searched' recycler view
        recyclerView.setLayoutManager(new LinearLayoutManager(StudentListActivity.this));
        StudentAdapterClass studentAdapterClass = new StudentAdapterClass(myList);
        recyclerView.setAdapter(studentAdapterClass);

        //click listeners for buttons
        studentAdapterClass.setOnItemClickListener(new StudentAdapterClass.OnItemClickListener() {

            @Override
            public void onUpdateClick(int position) {
                //handle update click in here
                Student student = myList.get(position);

                //show update dialog here
                showUpdateDialog(student.getStudentId(), student.getStudentName(), student.getStudentEmail());

            }

            @Override
            public void onDeleteClick(int position){
                //handle delete click in here
                Student student = myList.get(position);

                //show delete dialog here
                showSearchDeleteDialog(student.getStudentId(), position);

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
                    startActivity(new Intent(StudentListActivity.this, TeacherDashActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(StudentListActivity.this, StudentDashActivity.class));
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    //add student navigation
    public void onAddStudentClicked(View view){
        startActivity(new Intent(StudentListActivity.this, AddStudentActivity.class));
        finish();
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
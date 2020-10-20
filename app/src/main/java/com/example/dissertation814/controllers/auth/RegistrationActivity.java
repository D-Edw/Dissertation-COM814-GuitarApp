package com.example.dissertation814.controllers.auth;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dissertation814.R;
import com.example.dissertation814.controllers.student.StudentDashActivity;
import com.example.dissertation814.controllers.teacher.TeacherDashActivity;
import com.example.dissertation814.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class RegistrationActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    //variables
    private EditText inputEmail, inputPassword, inputName;
    private Spinner inputAccountType;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    public String accountTypeSelection;
    public String currentUserAccount;

    public String teacherAccountNav = "Teacher";

    DatabaseReference databaseUsers;

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        //creates initial 'user' parent
        databaseUsers = FirebaseDatabase.getInstance().getReference("users");

        //input fields
        inputEmail = findViewById(R.id.emailEditText);
        inputPassword = findViewById(R.id.passwordEditText);
        inputName = findViewById(R.id.nameEditText);

        //account type spinner
        inputAccountType = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.account_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        inputAccountType.setAdapter(adapter);
        inputAccountType.setOnItemSelectedListener(this);

        progressBar = findViewById(R.id.progressBar);

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();
        FirebaseDatabase.getInstance();

        // check if user is logged in
        if (mAuth.getCurrentUser() != null) {
            //get account type from firebase of current user, nav to appropriate dash
            String uid = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
            DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
            userDetails.keepSynced(true);

            userDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    currentUserAccount = dataSnapshot.child("account").getValue().toString();
                    //dash nav if/else statements
                    if (currentUserAccount.equals(teacherAccountNav)) {
                        startActivity(new Intent(RegistrationActivity.this, TeacherDashActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(RegistrationActivity.this, StudentDashActivity.class));
                        finish();
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
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onRegisterClicked(View view) {
        final String emailInput = inputEmail.getText().toString().trim();
        String password = inputPassword.getText().toString().trim();
        final String accountType = accountTypeSelection.trim();
        final String name = inputName.getText().toString().trim();

        //email validation check pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(name)) {
            inputName.setError("Enter Name");
            return;
        }

        if (TextUtils.isEmpty(emailInput)) {
            inputEmail.setError("Enter Email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter Password");
            return;
        }


        if (password.length() < 6) {
            inputPassword.setError("Password requires minimum 6 characters");
            return;
        }

        //email validation
        if (!emailInput.matches(emailPattern)){
            inputEmail.setError("Enter Valid Email");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //create user
        mAuth.createUserWithEmailAndPassword(emailInput, password)
                .addOnCompleteListener(RegistrationActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Toast.makeText(RegistrationActivity.this, "Account Created" + task.isSuccessful(), Toast.LENGTH_LONG).show();
                        progressBar.setVisibility(View.GONE);

                        if (!task.isSuccessful()) {
                            Toast.makeText(RegistrationActivity.this, "Authentication Failed" + task.getException(), Toast.LENGTH_LONG).show();
                            Log.e("MyTag", task.getException().toString());
                        } else {

                            //getting created users uid
                            //use it as a primary key for the user
                            String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                            //create custom user from POJO user class
                            User myUser = new User (
                                    id,
                                    name,
                                    emailInput,
                                    accountType
                            );

                            //add new user to database
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(id)
                                    .setValue(myUser);

                            //get account type from firebase of current user, navigate to appropriate dashboard
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                            DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            userDetails.keepSynced(true);

                            userDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    currentUserAccount = dataSnapshot.child("account").getValue().toString();
                                    //dashboard navigation if/else statements
                                    if (currentUserAccount.equals(teacherAccountNav)) {
                                        startActivity(new Intent(RegistrationActivity.this, TeacherDashActivity.class));
                                        finish();
                                    } else{
                                        startActivity(new Intent(RegistrationActivity.this, StudentDashActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });

    }

    //account type spinner selections
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        ((TextView) parent.getChildAt(0)).setTextColor(getResources().getColor(R.color.white));
        accountTypeSelection = inputAccountType.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    /**
     * ---------------------------------NAVIGATION--------------------------------------
     */
    public void onBackClicked (View view) {
        startActivity(new Intent(RegistrationActivity.this, LoginActivity.class));
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

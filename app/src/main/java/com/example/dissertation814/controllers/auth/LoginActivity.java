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
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.dissertation814.R;
import com.example.dissertation814.controllers.student.StudentDashActivity;
import com.example.dissertation814.controllers.teacher.TeacherDashActivity;
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

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private ProgressBar progressBar;

    private FirebaseAuth mAuth;

    public String currentUserAccount;
    public String teacherAccountNav = "Teacher";

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.emailEditText);
        inputPassword = findViewById(R.id.passwordEditText);
        progressBar = findViewById(R.id.progressBar);

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

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
                        startActivity(new Intent(LoginActivity.this, TeacherDashActivity.class));
                        finish();
                    } else {
                        startActivity(new Intent(LoginActivity.this, StudentDashActivity.class));
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
    public void onLogin (View view) {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        //email validation check pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Enter Email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter Password");
            return;
        }

        if (password.length() < 6){
            inputPassword.setError(getString(R.string.minimum_password));
        }

        //email validation
        if (!email.matches(emailPattern)){
            inputEmail.setError("Enter Valid Email");
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);

                        if (task.isSuccessful()) {

                            //get account type from firebase of current user, nav to appropriate dash
                            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
                            String uid = Objects.requireNonNull(firebaseAuth.getCurrentUser()).getUid();
                            DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                            userDetails.keepSynced(true);

                            userDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    currentUserAccount = dataSnapshot.child("account").getValue().toString();
                                    //dash nav if/else statements
                                    if (currentUserAccount.equals(teacherAccountNav)) {
                                        startActivity(new Intent(LoginActivity.this, TeacherDashActivity.class));
                                        finish();
                                    } else {
                                        startActivity(new Intent(LoginActivity.this, StudentDashActivity.class));
                                        finish();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        } else {
                            Toast.makeText(LoginActivity.this, "Login Failed - Try Again", Toast.LENGTH_SHORT).show();
                        }
                    }//onComplete
                });



    }

    /**
     * ---------------------------------NAVIGATION--------------------------------------
     */
    public void onCreateNewAccount (View view) {
        Intent intent = new Intent(this, RegistrationActivity.class);
        startActivity(intent);
        finish();
    }

    public void onPasswordReset (View view) {
        Intent intent = new Intent(this, PasswordReset.class);
        startActivity(intent);
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

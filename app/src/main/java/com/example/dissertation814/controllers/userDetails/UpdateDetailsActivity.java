package com.example.dissertation814.controllers.userDetails;


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
import android.widget.EditText;
import android.widget.Toast;

import com.example.dissertation814.R;
import com.example.dissertation814.models.User;
import com.example.dissertation814.controllers.student.StudentDashActivity;
import com.example.dissertation814.controllers.teacher.TeacherDashActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class UpdateDetailsActivity extends AppCompatActivity {
    //tag
    private static final String TAG = UpdateDetailsActivity.class.getSimpleName();

    //firebase auth
    private FirebaseAuth mAuth;

    //variables
    private EditText inputName, inputEmail;
    private DatabaseReference mFirebaseDatabase;
    private String userId;
    public String currentUserAccount;
    public String teacherAccountNav = "Teacher";

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_details);

        //initialise views
        inputName = findViewById(R.id.nameEditText);
        inputEmail = findViewById(R.id.emailEditText);

        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        //add it only if it is not saved to database
        assert user != null;
        userId = user.getUid();

        addUserChangeListener();
    }

    /**
     * ---------------------------------METHODS--------------------------------------
     */
    //get user details from Firebase
    private void addUserChangeListener(){
        //User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                //check for null
                if (user == null) {
                    Log.e(TAG, "User data is null");
                    return;
                }

                Log.e(TAG, "User data is changed" + user.name + ", " + user.email);

                //clear and display info in edit text
                inputEmail.setText(user.email);
                inputName.setText(user.name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());

            }
        });
    }

    public void onUpdateClicked(View view) {
        final String name = inputName.getText().toString();
        final String email = inputEmail.getText().toString();

        //email validation check pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (TextUtils.isEmpty(name)){
            inputName.setError("Enter Name");
            return;
        }

        if (TextUtils.isEmpty(email)){
            inputEmail.setError("Enter Email");
            return;
        }

        //email validation
        if (!email.matches(emailPattern)){
            inputEmail.setError("Enter Valid Email");
            return;
        }

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        user.updateEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User email address update");
                            mFirebaseDatabase.child(userId).child("email").setValue(email);
                        } else {
                            Toast.makeText(getApplicationContext(), "Enter valid email", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

        //update name in db
        mFirebaseDatabase.child(userId).child("name").setValue(name);

        startActivity(new Intent(this, DetailsActivity.class));
        Toast.makeText(UpdateDetailsActivity.this, "Details Updated", Toast.LENGTH_LONG).show();
        finish();
    }

    /**
     * ---------------------------------NAVIGATION--------------------------------------
     */
    public void onBackClicked(View view) {
        startActivity(new Intent(UpdateDetailsActivity.this, DetailsActivity.class));
        finish();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onHomeClicked (View view) {
        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

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
                    startActivity(new Intent(UpdateDetailsActivity.this, TeacherDashActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(UpdateDetailsActivity.this, StudentDashActivity.class));
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

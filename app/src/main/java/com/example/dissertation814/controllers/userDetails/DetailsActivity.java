package com.example.dissertation814.controllers.userDetails;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dissertation814.R;
import com.example.dissertation814.controllers.MainActivity;
import com.example.dissertation814.controllers.auth.LoginActivity;
import com.example.dissertation814.controllers.auth.PasswordReset;
import com.example.dissertation814.controllers.student.StudentDashActivity;
import com.example.dissertation814.controllers.teacher.TeacherDashActivity;
import com.example.dissertation814.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class DetailsActivity extends AppCompatActivity {
    //tag
    private static final String TAG = DetailsActivity.class.getSimpleName();

    //firebase auth
    public FirebaseAuth mAuth;

    //variables
    private TextView inputName, inputEmail;
    private DatabaseReference mFirebaseDatabase;
    public String userId;
    public String currentUserAccount;
    public String teacherAccountNav = "Teacher";
    public User userDetails;

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        inputName = findViewById(R.id.nameTextView);
        inputEmail = findViewById(R.id.emailTextView);

        FirebaseDatabase mFirebaseInstance = FirebaseDatabase.getInstance();

        //reference to 'users' node
        mFirebaseDatabase = mFirebaseInstance.getReference("users");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        userId = user.getUid();

        addUserChangeListener();

    }

    /**
     * ---------------------------------METHODS--------------------------------------
     */

    //delete user account and user details from db, if account type is teacher - delete their students from db
    public void onDeleteAccount(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.delete_account_dialog, null);
        builder.setView(dialogView);

        final ImageButton buttonNo = dialogView.findViewById(R.id.imageButtonNo2);
        final ImageButton buttonYes = dialogView.findViewById(R.id.imageButtonYes2);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        buttonYes.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                final String uid = Objects.requireNonNull(user.getUid());

                DatabaseReference userDetails = FirebaseDatabase.getInstance().getReference().child("users").child(uid);
                userDetails.keepSynced(true);

                userDetails.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        currentUserAccount = dataSnapshot.child("account").getValue().toString();
                        if(currentUserAccount.equals(teacherAccountNav)){
                            //delete teachers students
                            FirebaseDatabase.getInstance().getReference().child("students").child(uid).removeValue();
                            //delete teachers details from database
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).removeValue();

                            //delete details from Firebase Auth
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(DetailsActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                alertDialog.dismiss();
                                                finish();
                                            }else{
                                                Toast.makeText(DetailsActivity.this, "Deletion failed: try again", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                            }
                                        }
                                    });

                        }else{
                            //delete students details from database
                            FirebaseDatabase.getInstance().getReference().child("users").child(uid).removeValue();

                            //delete details from Firebase Auth
                            user.delete()
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if(task.isSuccessful()){
                                                Toast.makeText(DetailsActivity.this, "Account Deleted", Toast.LENGTH_SHORT).show();
                                                Intent intent = new Intent(DetailsActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                alertDialog.dismiss();
                                                finish();
                                            }else{
                                                Toast.makeText(DetailsActivity.this, "Deletion failed: try again", Toast.LENGTH_SHORT).show();
                                                alertDialog.dismiss();
                                            }
                                        }
                                    });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    //get user data from db for display
     private void addUserChangeListener() {
        //User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                //check for null
                if(user == null) {
                    Log.e(TAG, "User data is null");
                    return;
                }

                Log.e(TAG, "User data is changed" + user.name + ", " + user.email);

                //display info in the text views
                inputName.setText(user.name);
                inputEmail.setText(user.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //Failed ro read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }


    /**
     * ---------------------------------NAVIGATION--------------------------------------
     */

    //home button navigation
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
                    startActivity(new Intent(DetailsActivity.this, TeacherDashActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(DetailsActivity.this, StudentDashActivity.class));
                    finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //logout button navigation
    public void onLogout (View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    //update details navigation
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onUpdateDetails (View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.reauth_dialog, null);

        builder.setView(dialogView);

        final TextView emailInput = dialogView.findViewById(R.id.emailTextView);
        final EditText passwordInput = dialogView.findViewById(R.id.passwordEditText);
        final Button enterBtn = dialogView.findViewById(R.id.enterButton);

        //get and set current users email address
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                //check for null
                if(user == null){
                    Log.e(TAG, "User data is null");
                    return;
                }

                Log.e(TAG, "User data is changed" + user.name + ", " + user.email);

                //display email
                emailInput.setText(user.email);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //failed to read value
                Log.e(TAG, "Failed to read user", databaseError.toException());
            }
        });

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        enterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String email = emailInput.getText().toString();
                final String password = passwordInput.getText().toString();

                if (TextUtils.isEmpty(password)) {
                    passwordInput.setError("Enter Password");
                    return;
                }

                if (password.length() < 6){
                    passwordInput.setError(getString(R.string.minimum_password));
                }

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                AuthCredential credential = EmailAuthProvider.getCredential(email, password);

                assert user != null;
                user.reauthenticate(credential)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Log.d(TAG, "User re-authenticated");
                                    startActivity(new Intent(DetailsActivity.this, UpdateDetailsActivity.class));
                                    finish();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Password Incorrect", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    //password reset navigation
    public void onPasswordReset(View view){
        startActivity(new Intent(this, PasswordReset.class));
    }
}

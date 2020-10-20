package com.example.dissertation814.controllers.emailExport;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dissertation814.R;
import com.example.dissertation814.controllers.student.StudentDashActivity;
import com.example.dissertation814.controllers.teacher.TeacherDashActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class EmailExportActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth mAuth;

    //public variables
    public String currentUserAccount;
    public String teacherAccountNav = "Teacher";

    private EditText mEditTextEmailAddress;
    private EditText mEditTextSubject;
    private EditText mEditTextMessage;
    private Uri pdfUriSel;
    private String emailStr;

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_export);

        //initialise views and button
        mEditTextEmailAddress = findViewById(R.id.edit_text_email_address);
        mEditTextSubject = findViewById(R.id.edit_text_subject);
        final TextView attachmentName = findViewById(R.id.text_attachment);
        mEditTextMessage = findViewById(R.id.edit_text_message);
        Button sendButton = findViewById(R.id.sendButton);

        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

        //get account type from firebase of current user
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

                    Intent intent = getIntent();
                    emailStr = intent.getStringExtra("EMAIL_SELECTION");
                    String pdfNameStr = intent.getStringExtra("PDF_NAME");
                    pdfUriSel = intent.getParcelableExtra("PDF_URI");

                    //set fields to data retrieved from selections in previous activity
                    mEditTextEmailAddress.setText(emailStr);
                    attachmentName.setText(pdfNameStr);

                } else {

                    Intent intent = getIntent();
                    String pdfNameStr = intent.getStringExtra("LESSON_PDF_NAME");
                    pdfUriSel = intent.getParcelableExtra("LESSON_PDF_URI");

                    //set fields to data retrieved from selections in previous activity
                    attachmentName.setText(pdfNameStr);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //send functionality
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });
    }

    /**
     * ---------------------------------METHODS--------------------------------------
     */
    private void sendMail(){

        //putting all fields toString
        String recipient = mEditTextEmailAddress.getText().toString();
        String [] recipientSel = recipient.split(",");
        String subject = mEditTextSubject.getText().toString();
        String message = mEditTextMessage.getText().toString();

        //email validation check pattern
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        //email field empty check
        if (TextUtils.isEmpty(recipient)){
            mEditTextEmailAddress.setError("Enter Email");
            return;
        }

        //email validation
        if (!recipient.matches(emailPattern)){
            mEditTextEmailAddress.setError("Enter Valid Email");
            return;
        }

       Intent emailSelectorIntent = new Intent(Intent.ACTION_SENDTO);
       emailSelectorIntent.setData(Uri.parse("mailto:"));

       final Intent emailIntent = new Intent(Intent.ACTION_SEND);
       emailIntent.putExtra(Intent.EXTRA_EMAIL, recipientSel);
       emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
       emailIntent.putExtra(Intent.EXTRA_TEXT, message);
       emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
       emailIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
       emailIntent.setSelector(emailSelectorIntent);

       Uri attachment = pdfUriSel;
       emailIntent.putExtra(Intent.EXTRA_STREAM, attachment);

       if(emailIntent.resolveActivity(getPackageManager()) != null){
           startActivity(emailIntent);
       }

    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onCancelClicked (View view){
        //Firebase instance
        mAuth = FirebaseAuth.getInstance();

        //get account type from firebase of current user
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
                    startActivity(new Intent(EmailExportActivity.this, TeacherDashActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(EmailExportActivity.this, StudentDashActivity.class));
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

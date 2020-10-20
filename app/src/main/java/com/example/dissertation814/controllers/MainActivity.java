package com.example.dissertation814.controllers;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.dissertation814.R;
import com.example.dissertation814.controllers.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {

    //permissions variables
    private static final int PERMISSION_REQUEST_CODE = 101;
    String[] all_permission={Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * ---------------------------------METHODS--------------------------------------
     */

    public void onGetStarted (View view) {
        if(checkPermission(all_permission)) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else{
            requestPermission(all_permission);
        }
    }

    private boolean checkPermission(String [] permission){
        for(int i=0; i<permission.length; i++){
            int result = ContextCompat.checkSelfPermission(MainActivity.this, permission[i]);
            if(result== PackageManager.PERMISSION_GRANTED){
                continue;
            }else{
                return false;
            }
        }
        return true;
    }

    private void requestPermission(String [] permission){
        ActivityCompat.requestPermissions(MainActivity.this, permission, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case PERMISSION_REQUEST_CODE:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    Toast.makeText(MainActivity.this, "Permissions Granted", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                else{
                    Toast.makeText(MainActivity.this, "Permissions Denied", Toast.LENGTH_SHORT).show();
                }
        }
    }
}

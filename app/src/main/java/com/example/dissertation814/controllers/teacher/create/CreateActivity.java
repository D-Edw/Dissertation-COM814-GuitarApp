package com.example.dissertation814.controllers.teacher.create;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.dissertation814.R;
import com.example.dissertation814.adapters.CreateAdapter;
import com.example.dissertation814.models.Diagram;
import com.example.dissertation814.models.PdfPage;
import com.example.dissertation814.controllers.teacher.TeacherDashActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class CreateActivity extends AppCompatActivity {

    //firebase auth
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    //public variables
    public String currentUserAccount;
    public String teacherAccountNav = "Teacher";
    public String currentUserId;

    ViewPager viewPager;
    CreateAdapter createAdapter;
    List<PdfPage> pdfPageList = new ArrayList<>();

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);

        currentUserId = mAuth.getCurrentUser().getUid();

    }

    /**
     * ---------------------------------onStart--------------------------------------
     */
    @Override
    protected void onStart() {
        super.onStart();

        createAdapter = new CreateAdapter(pdfPageList, this);

        viewPager = findViewById(R.id.viewPager);
        viewPager.setAdapter(createAdapter);
        viewPager.setPadding(130, 0, 130, 0);

        createAdapter.setOnItemClickListener(new CreateAdapter.onItemClickListener() {
            @Override
            public void onEditClick(int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(CreateActivity.this);
                LayoutInflater inflater = getLayoutInflater();
                final View dialogView = inflater.inflate(R.layout.add_text_dialog, null);
                builder.setView(dialogView);

                final EditText enterText = dialogView.findViewById(R.id.addTextEditText);
                final Button addTextButton = dialogView.findViewById(R.id.addNewTextButton);

                final PdfPage pdfPage = pdfPageList.get(position);

                enterText.setText(pdfPage.getPageText());

                final AlertDialog alertDialog = builder.create();
                alertDialog.show();

                addTextButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        pdfPage.setPageText(enterText.getText().toString().trim());

                        Toast.makeText(CreateActivity.this, "Text Page Edited", Toast.LENGTH_SHORT).show();

                        alertDialog.dismiss();
                        onStart();
                    }
                });


            }

            @Override
            public void onDeleteClick(int position) {
                pdfPageList.remove(position);
                Toast.makeText(CreateActivity.this, "Page Deleted", Toast.LENGTH_SHORT).show();
                onStart();
            }
        });
    }

    /**
     * ---------------------------------METHODS--------------------------------------
     */
    public void onAddImageClick(View view){
        showAddImageDialog();
    }

    private void showAddImageDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.image_gallery_selection_dialog, null);
        builder.setView(dialogView);

        final Button buttonChord = dialogView.findViewById(R.id.chordButton);
        final Button buttonScale = dialogView.findViewById(R.id.scaleButton);
        final Button buttonArpeggio = dialogView.findViewById(R.id.arpeggioButton);

        final AlertDialog alertDialog = builder.create();
        alertDialog.show();

        buttonChord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, ChordGalleryActivity.class);
                //startActivity for result
                startActivityForResult(intent, 2);
                alertDialog.dismiss();
            }
        });

        buttonScale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, ScaleGalleryActivity.class);
                //startActivity for result
                startActivityForResult(intent, 2);
                alertDialog.dismiss();

            }
        });

        buttonArpeggio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CreateActivity.this, ArpeggioGalleryActivity.class);
                //startActivity for result
                startActivityForResult(intent, 2);
                alertDialog.dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==2) {
            if (resultCode == 2) {

                //make diagram object
                Diagram diagram = new Diagram();
                diagram.setDiagramName("");
                assert data != null;
                diagram.setDiagramType(data.getStringExtra("DIAGRAM_TYPE"));
                diagram.setDiagramRes(data.getIntExtra("DIAGRAM_RES", 0));

               //make page object
                PdfPage pdfPage = new PdfPage();
                pdfPage.setImageType(diagram.getDiagramType());
                pdfPage.setPageImage(diagram.getDiagramRes());
                pdfPage.setPageType("Image");
                pdfPage.setPageText("");

                //add page to array
                pdfPageList.add(pdfPage);
                Toast.makeText(CreateActivity.this, "Image Page Added", Toast.LENGTH_SHORT).show();

                onStart();
            } else if (resultCode == 1) {
                //result from back button
                onStart();
            }
        }
    }

    public void onClearClick(View view){
        showClearDialog();
    }

    private void showClearDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.clear_work_dialog, null);
        builder.setView(dialogView);

        final ImageButton buttonNo = dialogView.findViewById(R.id.imageButtonNo);
        final ImageButton buttonYes = dialogView.findViewById(R.id.imageButtonYes);

        final AlertDialog alertDialog = builder.create();
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
                pdfPageList.clear();
                alertDialog.dismiss();
                onStart();
            }
        });
    }

    public void onAddTextClicked(View view){
        showAddTextDialog();
    }

    private void showAddTextDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_DialogWhenLarge);

        LayoutInflater inflater = getLayoutInflater();

        //dialog XML
        final View dialogView = inflater.inflate(R.layout.add_text_dialog, null);

        dialogBuilder.setView(dialogView);

        final EditText enterText = dialogView.findViewById(R.id.addTextEditText);
        final Button addTextButton = dialogView.findViewById(R.id.addNewTextButton);

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        addTextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PdfPage pdfPage = new PdfPage();

                final String text = enterText.getText().toString();

                if (TextUtils.isEmpty(text)){
                    enterText.setError("Enter text before adding page");
                    return;
                }

                pdfPage.setPageText(enterText.getText().toString().trim());
                pdfPage.setPageType("Text");

                pdfPageList.add(pdfPage);
                Toast.makeText(CreateActivity.this, "Text Page Added", Toast.LENGTH_SHORT).show();

                alertDialog.dismiss();
                onStart();
            }
        });


    }

    public void onSaveClicked(View view){
        showSaveDialog();
    }

    private void showSaveDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        //dialog XML
        final View dialogView = inflater.inflate(R.layout.save_pdf_dialog,null);

        builder.setView(dialogView);

        final EditText enterFileName = dialogView.findViewById(R.id.pdfNameEditText);
        final ImageButton buttonNo = dialogView.findViewById(R.id.imageButtonNo4);
        final ImageButton buttonYes = dialogView.findViewById(R.id.imageButtonYes4);

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
                String fileName = enterFileName.getText().toString().trim();

                if (TextUtils.isEmpty(fileName)){
                    enterFileName.setError("Enter File Name");
                    return;
                }

                if (pdfPageList.size() > 0) {

                    //PDF Document creation
                    PdfDocument pdfDocument = new PdfDocument();

                    //for loop for arrayList new page
                    int i = 0;
                    for (PdfPage userDoc : pdfPageList) {

                        //write text to page if page type is "Text"
                        if (userDoc.getPageType().equals("Text")) {
                            //create a page info description
                            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1400, 1920, i + 1).create();

                            //start a page
                            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                            //writing to new page
                            TextPaint paint = new TextPaint();

                            //setting font and text size
                            paint.setTextSize(34);
                            paint.setTypeface(Typeface.create("Arial", Typeface.NORMAL));

                            //canvas used to write to page, getting object data from inputted text
                            String myString = userDoc.getPageText();
                            StaticLayout mTextLayout = new StaticLayout(myString, paint, page.getCanvas().getWidth(), Layout.Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
                            page.getCanvas().save();

                            //setting where to start text on the page
                            int x = 30, y = 30;

                            page.getCanvas().translate(x, y);
                            mTextLayout.draw(page.getCanvas());
                            page.getCanvas().restore();

                            //finish page
                            pdfDocument.finishPage(page);

                            //add to page count
                            i++;
                        } else if (userDoc.getPageType().equals("Image")) { //write to page if page type is "Image"
                            //create a page info description
                            PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(1400, 1920, i + 1).create();

                            //start a page
                            PdfDocument.Page page = pdfDocument.startPage(pageInfo);

                            //writing to new page
                            Paint paint = new Paint();

                            //this gets the resource files bitmap
                            int imageFile = userDoc.getPageImage();
                            Bitmap image = BitmapFactory.decodeResource(getResources(), imageFile);

                            //writing image to page
                            if (userDoc.getImageType().equals("Scale")) {
                                page.getCanvas().drawBitmap(image, 0, 250, paint);
                            } else if (userDoc.getImageType().equals("Chord")) {
                                page.getCanvas().drawBitmap(image, 250, 250, paint);
                            } else if (userDoc.getImageType().equals("Arpeggio")) {
                                page.getCanvas().drawBitmap(image, 80, 250, paint);
                            }

                            //finish page
                            pdfDocument.finishPage(page);

                            //add to page count
                            i++;
                        }
                    }

                    String folder_main = "/GuitarAppStorage";

                    //get or create parent folder for app
                    String rootPath = String.valueOf(Environment.getExternalStorageDirectory());
                    File parentFolder = new File(rootPath + folder_main);
                    if (!parentFolder.exists()) {
                        parentFolder.mkdirs();
                    }

                    //get or create child folder with currentUserId as folder name
                    File childFolder = new File(rootPath + folder_main, currentUserId);
                    if (!childFolder.exists()) {
                        childFolder.mkdirs();
                    }

                    //new file with path and filename
                    File file = new File(childFolder, fileName + ".pdf");

                    //write to folder
                    try {
                        pdfDocument.writeTo(new FileOutputStream(file));
                        Toast.makeText(CreateActivity.this, "File Created", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                        Toast.makeText(CreateActivity.this, "Error: File not created", Toast.LENGTH_SHORT).show();
                    }

                    alertDialog.dismiss();
                    onStart();
                } else {
                    alertDialog.dismiss();
                    onStart();
                    Toast.makeText(getApplicationContext(), "Please add material before saving", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * ---------------------------------NAVIGATION--------------------------------------
     */
    //home navigation
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onHomeClicked(View view) {

        showHomeDialog();

    }

    private void showHomeDialog(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();

        //dialog XML
        final View dialogView = inflater.inflate(R.layout.home_save_dialog, null);

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
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                startActivity(new Intent(CreateActivity.this, TeacherDashActivity.class));
                alertDialog.dismiss();
                finish();
            }
        });
    }


}

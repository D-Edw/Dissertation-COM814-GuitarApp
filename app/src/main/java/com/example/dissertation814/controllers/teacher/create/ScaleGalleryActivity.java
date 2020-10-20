package com.example.dissertation814.controllers.teacher.create;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;

import com.example.dissertation814.R;
import com.example.dissertation814.adapters.DiagramAdapter;
import com.example.dissertation814.models.Diagram;

import java.util.ArrayList;
import java.util.List;

public class ScaleGalleryActivity extends AppCompatActivity {

    private List<Diagram> diagramList = new ArrayList<>();
    GridView gridView;
    DiagramAdapter diagramAdapter;
    SearchView searchView;

    /**
     * ---------------------------------onCreate--------------------------------------
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_gallery);

        //add scales to diagram list
        diagramList.add(new Diagram("A Blues", "Scale", R.drawable.a_blues_scale_android));
        diagramList.add(new Diagram("A Major Pentatonic", "Scale", R.drawable.a_major_pentatonic_scale_android));
        diagramList.add(new Diagram("A Major", "Scale", R.drawable.a_major_scale_android));
        diagramList.add(new Diagram("A Minor", "Scale", R.drawable.a_minor_scale_android));
        diagramList.add(new Diagram("A Minor Pentatonic", "Scale", R.drawable.a_minor_pentatonic_android));
        diagramList.add(new Diagram("E Blues", "Scale", R.drawable.e_blues_scale_android));
        diagramList.add(new Diagram("E Minor Pentatonic", "Scale", R.drawable.e_minor_pentatonic_android));
        diagramList.add(new Diagram("G Major Pentatonic", "Scale", R.drawable.g_major_pent_android));
        diagramList.add(new Diagram("G Major", "Scale", R.drawable.g_major_scale_android));
        diagramList.add(new Diagram("G Minor Pentatonic", "Scale", R.drawable.g_minor_pent_android));
        diagramList.add(new Diagram("CAGED 1", "Scale", R.drawable.positionone));
        diagramList.add(new Diagram("CAGED 2", "Scale", R.drawable.position2));
        diagramList.add(new Diagram("CAGED 3", "Scale", R.drawable.position3));
        diagramList.add(new Diagram("CAGED 4", "Scale", R.drawable.position4));
        diagramList.add(new Diagram("CAGED 5", "Scale", R.drawable.position5));

        //initialise views
        gridView = findViewById(R.id.gridView);
        searchView = findViewById(R.id.searchView);

        //set adapter
        diagramAdapter = new DiagramAdapter(diagramList, this);

        //set adapter to gridView
        gridView.setAdapter(diagramAdapter);

        //click listener
        diagramAdapter.setOnItemClickListener(new DiagramAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final Diagram diagram = diagramList.get(position);

                int res = diagram.getDiagramRes();
                String type = diagram.getDiagramType();

                Intent intent = new Intent();
                intent.putExtra("DIAGRAM_RES", res);
                intent.putExtra("DIAGRAM_TYPE", type);
                setResult(2, intent);
                finish();
            }
        });

        if(searchView != null){
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    search(newText);
                    return true;
                }
            });
        }
    }

    /**
     * ---------------------------------METHODS--------------------------------------
     */
    //search bar
    private void search(String str){

        final ArrayList<Diagram> searchedList = new ArrayList<>();
        //adding main 'list' to be filtered into 'searchedList'
        for(Diagram object : diagramList){
            if(object.getDiagramName().toLowerCase().contains(str.toLowerCase())){
                searchedList.add(object);
            }
        }

        //make 'searched' grid view
        gridView = findViewById(R.id.gridView);
        diagramAdapter = new DiagramAdapter(searchedList, this);
        gridView.setAdapter(diagramAdapter);

        //search click listener
        diagramAdapter.setOnItemClickListener(new DiagramAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                final Diagram diagram = searchedList.get(position);

                int res = diagram.getDiagramRes();
                String type = diagram.getDiagramType();

                Intent intent = new Intent();
                intent.putExtra("DIAGRAM_RES", res);
                intent.putExtra("DIAGRAM_TYPE", type);
                setResult(2, intent);
                finish();
            }
        });
    }

    /**
     * ---------------------------------NAVIGATION--------------------------------------
     */
    public void onBackClicked(View view) {
        //go back to create activity while keeping previously inputted data
        setResult(1);
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

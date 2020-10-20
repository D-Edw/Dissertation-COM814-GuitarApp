package com.example.dissertation814.controllers.teacher.create;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.GridView;
import androidx.appcompat.widget.SearchView;

import com.example.dissertation814.R;
import com.example.dissertation814.adapters.DiagramAdapter;
import com.example.dissertation814.models.Diagram;

import java.util.ArrayList;
import java.util.List;

public class ChordGalleryActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_chord_gallery);

        //add chords to diagram list
        diagramList.add(new Diagram("C Major", "Chord", R.drawable.cmajor_android));
        diagramList.add(new Diagram("C Minor", "Chord", R.drawable.cm_android));
        diagramList.add(new Diagram("C# Minor", "Chord", R.drawable.c_sharp_m_android));
        diagramList.add(new Diagram("Db Major", "Chord", R.drawable.db_android));
        diagramList.add(new Diagram("D Major", "Chord", R.drawable.d_android));
        diagramList.add(new Diagram("D Minor", "Chord", R.drawable.dm_android));
        diagramList.add(new Diagram("Eb Major", "Chord", R.drawable.e_flat_android));
        diagramList.add(new Diagram("E Major", "Chord", R.drawable.e_android));
        diagramList.add(new Diagram("E Minor", "Chord", R.drawable.em_android));
        diagramList.add(new Diagram("F Major", "Chord", R.drawable.f_android));
        diagramList.add(new Diagram("F Minor", "Chord", R.drawable.fm_android));
        diagramList.add(new Diagram("F# Major", "Chord", R.drawable.f_sharp__android));
        diagramList.add(new Diagram("F# Minor", "Chord", R.drawable.f_sharp_m_android));
        diagramList.add(new Diagram("G Major", "Chord", R.drawable.g_android));
        diagramList.add(new Diagram("G Minor", "Chord", R.drawable.gm_android));
        diagramList.add(new Diagram("G# Minor", "Chord", R.drawable.g_sharp_m_android));
        diagramList.add(new Diagram("Ab Major", "Chord", R.drawable.a_flat_android));
        diagramList.add(new Diagram("A Major", "Chord", R.drawable.a_android));
        diagramList.add(new Diagram("A Minor", "Chord", R.drawable.am_android));
        diagramList.add(new Diagram("Bb Major", "Chord", R.drawable.b_flat_android));
        diagramList.add(new Diagram("Bb Minor", "Chord", R.drawable.bbm_android));
        diagramList.add(new Diagram("B Major", "Chord", R.drawable.b_android));
        diagramList.add(new Diagram("B Minor", "Chord", R.drawable.bm_android));

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

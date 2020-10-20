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

public class ArpeggioGalleryActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_arpeggio_gallery);

        //add arpeggios to diagram list
        diagramList.add(new Diagram("A Major", "Arpeggio", R.drawable.a_maj_arp));
        diagramList.add(new Diagram("C Position 1", "Arpeggio", R.drawable.c_pos1));
        diagramList.add(new Diagram("C Position 2", "Arpeggio", R.drawable.c_pos2));
        diagramList.add(new Diagram("C Position 3", "Arpeggio", R.drawable.c_pos3));
        diagramList.add(new Diagram("C Position 4", "Arpeggio", R.drawable.c_pos4));
        diagramList.add(new Diagram("C Position 5", "Arpeggio", R.drawable.c_pos5));
        diagramList.add(new Diagram("Cm Position 1", "Arpeggio", R.drawable.cm_pos1));
        diagramList.add(new Diagram("Cm Position 2", "Arpeggio", R.drawable.cm_pos2));
        diagramList.add(new Diagram("Cm Position 3", "Arpeggio", R.drawable.cm_pos3));
        diagramList.add(new Diagram("Cm Position 4", "Arpeggio", R.drawable.cm_pos4));
        diagramList.add(new Diagram("Cm Position 5", "Arpeggio", R.drawable.cm_pos5));

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
        //adding main 'List' to be filtered into 'searchedList
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

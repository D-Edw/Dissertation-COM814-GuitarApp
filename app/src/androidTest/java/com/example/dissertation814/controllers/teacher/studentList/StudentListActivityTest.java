package com.example.dissertation814.controllers.teacher.studentList;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class StudentListActivityTest {
    //rule launches activity
    @Rule
    public ActivityTestRule<StudentListActivity> studentListActivityActivityTestRule = new ActivityTestRule<>(StudentListActivity.class);
    private StudentListActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = studentListActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.studentListTextView));
        assertNotNull(mActivity.findViewById(R.id.imageButton));
        assertNotNull(mActivity.findViewById(R.id.addStudentButton));
        assertNotNull(mActivity.findViewById(R.id.searchView));
        assertNotNull(mActivity.findViewById(R.id.rv));
    }

}
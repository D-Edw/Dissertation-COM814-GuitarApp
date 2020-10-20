package com.example.dissertation814.controllers.teacher.studentList;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class AddStudentActivityTest {
    //rule launches activity
    @Rule
    public ActivityTestRule<AddStudentActivity> addStudentActivityActivityTestRule = new ActivityTestRule<>(AddStudentActivity.class);
    private AddStudentActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = addStudentActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.addStudentTextView));
        assertNotNull(mActivity.findViewById(R.id.homeButton));
        assertNotNull(mActivity.findViewById(R.id.addStudentButton));
        assertNotNull(mActivity.findViewById(R.id.nameEditText));
        assertNotNull(mActivity.findViewById(R.id.emailEditText));
        assertNotNull(mActivity.findViewById(R.id.personImageView));
        assertNotNull(mActivity.findViewById(R.id.emailImageView));
        assertNotNull(mActivity.findViewById(R.id.backButton));
    }

}
package com.example.dissertation814.controllers.student;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class StudentDashActivityTest {

    //rule launches activity
    @Rule
    public ActivityTestRule<StudentDashActivity> studentDashActivityActivityTestRule = new ActivityTestRule<>(StudentDashActivity.class);
    private StudentDashActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = studentDashActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.dashboardTextView));
        assertNotNull(mActivity.findViewById(R.id.accountImageButton));
        assertNotNull(mActivity.findViewById(R.id.lessonsImageButton));
        assertNotNull(mActivity.findViewById(R.id.createImageButton));
    }
}
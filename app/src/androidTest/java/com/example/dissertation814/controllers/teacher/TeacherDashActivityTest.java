package com.example.dissertation814.controllers.teacher;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class TeacherDashActivityTest {
    //rule launches activity
    @Rule
    public ActivityTestRule<TeacherDashActivity> teacherDashActivityActivityTestRule = new ActivityTestRule<>(TeacherDashActivity.class);
    private TeacherDashActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = teacherDashActivityActivityTestRule.getActivity();
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
        assertNotNull(mActivity.findViewById(R.id.studentsImageButton));
        assertNotNull(mActivity.findViewById(R.id.createImageButton));
        assertNotNull(mActivity.findViewById(R.id.filesImageButton));
    }

}
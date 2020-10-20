package com.example.dissertation814.controllers.teacher.create;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class CreateActivityTest {
    //rule launches activity
    @Rule
    public ActivityTestRule<CreateActivity> createActivityActivityTestRule = new ActivityTestRule<>(CreateActivity.class);
    private CreateActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = createActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.createTextView));
        assertNotNull(mActivity.findViewById(R.id.imageButton));
        assertNotNull(mActivity.findViewById(R.id.addTextButton));
        assertNotNull(mActivity.findViewById(R.id.clearButton));
        assertNotNull(mActivity.findViewById(R.id.saveButton));
        assertNotNull(mActivity.findViewById(R.id.addImageButton));
        assertNotNull(mActivity.findViewById(R.id.viewPager));
    }

}
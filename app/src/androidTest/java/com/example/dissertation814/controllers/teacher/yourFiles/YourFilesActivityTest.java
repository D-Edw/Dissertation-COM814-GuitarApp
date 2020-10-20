package com.example.dissertation814.controllers.teacher.yourFiles;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class YourFilesActivityTest {
    //rule launches activity
    @Rule
    public ActivityTestRule<YourFilesActivity> yourFilesActivityActivityTestRule = new ActivityTestRule<>(YourFilesActivity.class);
    private YourFilesActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = yourFilesActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.filesTextView));
        assertNotNull(mActivity.findViewById(R.id.imageButton));
        assertNotNull(mActivity.findViewById(R.id.searchView));
        assertNotNull(mActivity.findViewById(R.id.rv));
    }

}
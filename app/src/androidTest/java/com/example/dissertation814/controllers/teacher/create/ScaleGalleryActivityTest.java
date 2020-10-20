package com.example.dissertation814.controllers.teacher.create;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ScaleGalleryActivityTest {
    //rule launches activity
    @Rule
    public ActivityTestRule<ScaleGalleryActivity> scaleGalleryActivityActivityTestRule = new ActivityTestRule<>(ScaleGalleryActivity.class);
    private ScaleGalleryActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = scaleGalleryActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.scalesTextView));
        assertNotNull(mActivity.findViewById(R.id.searchView));
        assertNotNull(mActivity.findViewById(R.id.backButton));
        assertNotNull(mActivity.findViewById(R.id.gridView));
    }

}
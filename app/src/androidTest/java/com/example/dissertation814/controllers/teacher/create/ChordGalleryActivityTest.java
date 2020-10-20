package com.example.dissertation814.controllers.teacher.create;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class ChordGalleryActivityTest {
    //rule launches activity
    @Rule
    public ActivityTestRule<ChordGalleryActivity> chordGalleryActivityActivityTestRule = new ActivityTestRule<>(ChordGalleryActivity.class);
    private ChordGalleryActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = chordGalleryActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.chordsTextView));
        assertNotNull(mActivity.findViewById(R.id.searchView));
        assertNotNull(mActivity.findViewById(R.id.backButton));
        assertNotNull(mActivity.findViewById(R.id.gridView));
    }

}
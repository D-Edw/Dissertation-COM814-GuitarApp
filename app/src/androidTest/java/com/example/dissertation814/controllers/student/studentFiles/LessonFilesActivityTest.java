package com.example.dissertation814.controllers.student.studentFiles;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class LessonFilesActivityTest {

    //rule launches activity
    @Rule
    public ActivityTestRule<LessonFilesActivity> lessonFilesActivityActivityTestRule = new ActivityTestRule<>(LessonFilesActivity.class);
    private LessonFilesActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = lessonFilesActivityActivityTestRule.getActivity();
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
        assertNotNull(mActivity.findViewById(R.id.searchViewLesson));
        assertNotNull(mActivity.findViewById(R.id.rvLesson));
    }
}
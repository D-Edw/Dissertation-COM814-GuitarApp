package com.example.dissertation814.controllers.userDetails;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class UpdateDetailsActivityTest {
    //rule launches activity
    @Rule
    public ActivityTestRule<UpdateDetailsActivity> updateDetailsActivityActivityTestRule = new ActivityTestRule<>(UpdateDetailsActivity.class);
    private UpdateDetailsActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = updateDetailsActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.accountDetailsTextView));
        assertNotNull(mActivity.findViewById(R.id.updateButton));
        assertNotNull(mActivity.findViewById(R.id.nameEditText));
        assertNotNull(mActivity.findViewById(R.id.emailEditText));
        assertNotNull(mActivity.findViewById(R.id.emailImageView));
        assertNotNull(mActivity.findViewById(R.id.personImageView));
        assertNotNull(mActivity.findViewById(R.id.imageButton2));
        assertNotNull(mActivity.findViewById(R.id.backButton));
    }
}
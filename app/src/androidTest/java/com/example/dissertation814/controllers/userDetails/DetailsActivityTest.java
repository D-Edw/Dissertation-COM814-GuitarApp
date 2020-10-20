package com.example.dissertation814.controllers.userDetails;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class DetailsActivityTest {

    //rule launches activity
    @Rule
    public ActivityTestRule<DetailsActivity> detailsActivityActivityTestRule = new ActivityTestRule<>(DetailsActivity.class);
    private DetailsActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = detailsActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.deleteAccountButton));
        assertNotNull(mActivity.findViewById(R.id.accountDetailsTextView));
        assertNotNull(mActivity.findViewById(R.id.logoutButton));
        assertNotNull(mActivity.findViewById(R.id.nameTextView));
        assertNotNull(mActivity.findViewById(R.id.emailTextView));
        assertNotNull(mActivity.findViewById(R.id.emailImageView));
        assertNotNull(mActivity.findViewById(R.id.personImageView));
        assertNotNull(mActivity.findViewById(R.id.updateDetailsButton));
        assertNotNull(mActivity.findViewById(R.id.passwordResetButton));
        assertNotNull(mActivity.findViewById(R.id.imageButton2));
    }
}
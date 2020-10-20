package com.example.dissertation814.controllers.auth;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class PasswordResetTest {

    //rule launches activity
    @Rule
    public ActivityTestRule<PasswordReset> passwordResetActivityTestRule = new ActivityTestRule<>(PasswordReset.class);
    private PasswordReset mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = passwordResetActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.passwordTextView));
        assertNotNull(mActivity.findViewById(R.id.resetButton));
        assertNotNull(mActivity.findViewById(R.id.emailEditText));
        assertNotNull(mActivity.findViewById(R.id.emailImageView));
        assertNotNull(mActivity.findViewById(R.id.resetTextView));
        assertNotNull(mActivity.findViewById(R.id.backButton));
    }
}
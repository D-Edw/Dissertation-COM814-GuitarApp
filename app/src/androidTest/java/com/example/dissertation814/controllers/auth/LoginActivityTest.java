package com.example.dissertation814.controllers.auth;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class LoginActivityTest {

    //rule launches activity
    @Rule
    public ActivityTestRule<LoginActivity> loginActivityActivityTestRule = new ActivityTestRule<>(LoginActivity.class);
    private LoginActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = loginActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.loginTextView));
        assertNotNull(mActivity.findViewById(R.id.loginButton));
        assertNotNull(mActivity.findViewById(R.id.emailEditText));
        assertNotNull(mActivity.findViewById(R.id.passwordEditText));
        assertNotNull(mActivity.findViewById(R.id.passwordImageView));
        assertNotNull(mActivity.findViewById(R.id.emailImageView));
        assertNotNull(mActivity.findViewById(R.id.newAccountButton));
        assertNotNull(mActivity.findViewById(R.id.passwordResetButton));
        assertNotNull(mActivity.findViewById(R.id.progressBar));
    }
}
package com.example.dissertation814.controllers.auth;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class RegistrationActivityTest {

    //rule launched activity
    @Rule
    public ActivityTestRule<RegistrationActivity> registrationActivityActivityTestRule = new ActivityTestRule<>(RegistrationActivity.class);
    private RegistrationActivity mActivity = null;

    @Before
    public void setUp() {
        mActivity = registrationActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.createAccountTextView));
        assertNotNull(mActivity.findViewById(R.id.createAccountButton));
        assertNotNull(mActivity.findViewById(R.id.nameEditText));
        assertNotNull(mActivity.findViewById(R.id.emailEditText));
        assertNotNull(mActivity.findViewById(R.id.passwordEditText));
        assertNotNull(mActivity.findViewById(R.id.passwordImageView));
        assertNotNull(mActivity.findViewById(R.id.emailImageView));
        assertNotNull(mActivity.findViewById(R.id.personImageView));
        assertNotNull(mActivity.findViewById(R.id.spinner2));
        assertNotNull(mActivity.findViewById(R.id.arrowImageView));
        assertNotNull(mActivity.findViewById(R.id.progressBar));
        assertNotNull(mActivity.findViewById(R.id.backButton));
    }

    @After
    public void tearDown() {
        mActivity = null;
    }

}
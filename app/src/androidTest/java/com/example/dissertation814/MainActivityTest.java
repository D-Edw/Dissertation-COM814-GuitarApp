package com.example.dissertation814;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.controllers.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class MainActivityTest {

    //rule launches activity
    @Rule
    public ActivityTestRule<MainActivity> mainActivityActivityTestRule = new ActivityTestRule<>(MainActivity.class);
    private MainActivity mActivity = null;

    @Before
    public void setUp() {

        mActivity = mainActivityActivityTestRule.getActivity();
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.textWelcome));
        assertNotNull(mActivity.findViewById(R.id.textWelcomeMessage));
        assertNotNull(mActivity.findViewById(R.id.imageView));
        assertNotNull(mActivity.findViewById(R.id.getStartedButton));
    }

    @After
    public void tearDown() {
        mActivity = null;
    }
}
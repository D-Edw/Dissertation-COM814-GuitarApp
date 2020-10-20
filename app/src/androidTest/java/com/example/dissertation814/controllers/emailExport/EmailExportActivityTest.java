package com.example.dissertation814.controllers.emailExport;

import androidx.test.rule.ActivityTestRule;

import com.example.dissertation814.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class EmailExportActivityTest {

    //rule launches activity
    @Rule
    public ActivityTestRule<EmailExportActivity> emailExportActivityActivityTestRule = new ActivityTestRule<>(EmailExportActivity.class);
    private EmailExportActivity mActivity = null;

    @Before
    public void setUp() throws Exception {
        mActivity = emailExportActivityActivityTestRule.getActivity();
    }

    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }

    @Test
    public void testLaunch(){
        //if all the activity components are found, the activity is successfully launched
        assertNotNull(mActivity.findViewById(R.id.sendFileTV));
        assertNotNull(mActivity.findViewById(R.id.to));
        assertNotNull(mActivity.findViewById(R.id.edit_text_email_address));
        assertNotNull(mActivity.findViewById(R.id.subject));
        assertNotNull(mActivity.findViewById(R.id.edit_text_subject));
        assertNotNull(mActivity.findViewById(R.id.attachment));
        assertNotNull(mActivity.findViewById(R.id.text_attachment));
        assertNotNull(mActivity.findViewById(R.id.messageTV));
        assertNotNull(mActivity.findViewById(R.id.edit_text_message));
        assertNotNull(mActivity.findViewById(R.id.cancelButton));
        assertNotNull(mActivity.findViewById(R.id.sendButton));
    }
}
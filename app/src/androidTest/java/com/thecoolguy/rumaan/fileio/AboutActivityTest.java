package com.thecoolguy.rumaan.fileio;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.thecoolguy.rumaan.fileio.ui.AboutActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by rumaankhalander on 18/01/18.
 */

@RunWith(AndroidJUnit4.class)
public class AboutActivityTest {

    @Rule
    public ActivityTestRule<AboutActivity> testRule = new ActivityTestRule<>(AboutActivity.class);


}
package com.thecoolguy.rumaan.fileio;

import static org.junit.Assert.assertEquals;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import com.thecoolguy.rumaan.fileio.ui.MainActivity;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivityTest {

  @Rule
  public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(
      MainActivity.class);

  @Test
  public void useAppContext() throws Exception {
    Context context = InstrumentationRegistry.getTargetContext();
    assertEquals("com.thecoolguy.rumaan.fileio", context.getPackageName());
  }


}

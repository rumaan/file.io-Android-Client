package com.thecoolguy.rumaan.fileio;

import static junit.framework.Assert.assertEquals;

import android.content.Intent;
import com.thecoolguy.rumaan.fileio.ui.activities.MainActivity;
import com.thecoolguy.rumaan.fileio.ui.activities.UploadHistoryActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.shadows.ShadowApplication;

@RunWith(RobolectricTestRunner.class)
public class MainActivityTest {

  @Test
  public void clickChooseFile_shouldStartFileChooser() {
    MainActivity mainActivity = Robolectric.setupActivity(MainActivity.class);

    mainActivity.findViewById(R.id.choose_file).performClick();

    Intent expectedIntent = new Intent(mainActivity, UploadHistoryActivity.class);
    Intent actual = ShadowApplication.getInstance().getNextStartedActivity();

    assertEquals(expectedIntent.getComponent(), actual.getComponent());
  }
}

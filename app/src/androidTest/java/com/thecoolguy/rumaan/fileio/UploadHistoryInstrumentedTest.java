package com.thecoolguy.rumaan.fileio;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.filters.MediumTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.models.FileEntity;
import com.thecoolguy.rumaan.fileio.ui.activities.UploadHistoryActivity;
import com.thecoolguy.rumaan.fileio.utils.ConstantsKt;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@MediumTest
public class UploadHistoryInstrumentedTest {

  @Rule
  public ActivityTestRule<UploadHistoryActivity> activityActivityTestRule = new ActivityTestRule<>(
      UploadHistoryActivity.class);

  private UploadHistoryRoomDatabase database;

  public static Matcher<FileEntity> withFileName(final Matcher fileNameMatcher) {
    return new TypeSafeMatcher<FileEntity>() {

      @Override
      public void describeTo(Description description) {
      }

      @Override
      protected boolean matchesSafely(FileEntity item) {
        return fileNameMatcher.matches(item.getName());
      }
    };
  }

  @Before
  public void init() {
    database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(),
        UploadHistoryRoomDatabase.class).build();
    // add five items into the database
    for (int i = 0; i < 5; i++) {
      FileEntity fileEntity = new FileEntity("test fileEntity name " + i, "test URL "
          + i, Utils.Date.INSTANCE.getCurrentDate(), ConstantsKt.DEFAULT_EXPIRE_WEEKS);
      database.uploadItemDao().insert(fileEntity);
    }
  }

  @Test
  public void check_longPressItemDelete() {

  }
}


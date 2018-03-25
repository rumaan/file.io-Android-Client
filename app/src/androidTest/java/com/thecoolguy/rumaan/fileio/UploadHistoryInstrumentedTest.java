package com.thecoolguy.rumaan.fileio;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.models.FileEntity;
import com.thecoolguy.rumaan.fileio.ui.UploadHistoryActivity;
import com.thecoolguy.rumaan.fileio.utils.Constants;
import com.thecoolguy.rumaan.fileio.utils.Date;

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
    public ActivityTestRule<UploadHistoryActivity> activityActivityTestRule = new ActivityTestRule<>(UploadHistoryActivity.class);

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
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), UploadHistoryRoomDatabase.class).build();
        // add five items into the database
        for (int i = 0; i < 5; i++) {
            FileEntity fileEntity = new FileEntity("test fileEntity name " + i, "test URL "
                    + i, Date.getTimeStamp(), Constants.DEFAULT_EXPIRE_WEEKS);
            database.uploadItemDao().insert(fileEntity);
        }

    }

    @Test
    public void check_longPressItemDelete() {

    }
}


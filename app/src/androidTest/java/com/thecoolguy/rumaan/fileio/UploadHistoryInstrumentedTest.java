package com.thecoolguy.rumaan.fileio;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;
import com.thecoolguy.rumaan.fileio.ui.UploadHistoryActivity;
import com.thecoolguy.rumaan.fileio.utils.Consts;
import com.thecoolguy.rumaan.fileio.utils.DateUtil;

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

    public static Matcher<UploadItem> withFileName(final Matcher fileNameMatcher) {
        return new TypeSafeMatcher<UploadItem>() {

            @Override
            public void describeTo(Description description) {
            }

            @Override
            protected boolean matchesSafely(UploadItem item) {
                return fileNameMatcher.matches(item.getFileName());
            }
        };
    }

    @Before
    public void init() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), UploadHistoryRoomDatabase.class).build();
        // add five items into the database
        for (int i = 0; i < 5; i++) {
            UploadItem uploadItem = new UploadItem("test file name " + i, "test URL "
                    + i, DateUtil.getTimeStamp(), Consts.DEFAULT_EXPIRE_WEEKS);
            database.uploadItemDao().insert(uploadItem);
        }

    }

    @Test
    public void check_longPressItemDelete() {

    }
}


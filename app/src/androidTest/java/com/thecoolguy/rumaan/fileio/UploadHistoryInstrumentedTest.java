package com.thecoolguy.rumaan.fileio;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;
import com.thecoolguy.rumaan.fileio.ui.UploadHistoryActivity;
import com.thecoolguy.rumaan.fileio.utils.Consts;
import com.thecoolguy.rumaan.fileio.utils.DateUtil;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;


/**
 * Created by rumaankhalander on 12/01/18.
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
public class UploadHistoryInstrumentedTest {
    @Rule
    public ActivityTestRule<UploadHistoryActivity> activityActivityTestRule = new ActivityTestRule<>(UploadHistoryActivity.class);

    private UploadHistoryRoomDatabase database;

    @Before
    public void init() {
        database = Room.inMemoryDatabaseBuilder(InstrumentationRegistry.getContext(), UploadHistoryRoomDatabase.class).build();
        for (int i = 0; i < 5; i++) {
            UploadItem uploadItem = new UploadItem("test file name " + i, "test URL " + i, DateUtil.getTimeStamp(), Consts.DEFAULT_EXPIRE_WEEKS);
            database.uploadItemDao().insert(uploadItem);
        }

    }

    @Test
    public void check_longPressItemDelete() {
        onData(is(instanceOf(UploadItem.class)));
        onView(withId(R.id.list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
    }
}

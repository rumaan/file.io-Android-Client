package com.thecoolguy.rumaan.fileio;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.thecoolguy.rumaan.fileio.data.models.UploadItem;
import com.thecoolguy.rumaan.fileio.ui.UploadHistoryActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
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

    private ClipboardManager clipboardManager;

    @Before
    public void setClipboardManager() {
        // this ain't working!
        Context context = InstrumentationRegistry.getTargetContext();
        clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
    }

    @Test
    public void check_clickItemCopiesLinkToClipboard() {
        onData(is(instanceOf(UploadItem.class)));

        onView(withId(R.id.list))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        assertNotNull(clipboardManager);
        assertTrue(clipboardManager.hasPrimaryClip());

        ClipData.Item clipItem = clipboardManager.getPrimaryClip().getItemAt(0);
        assertNotNull(clipItem);

        assertNotNull(clipItem.getText());

        // TODO: match link text with clipboard
    }

    @Test
    public void check_longPressItemDelete() {
        onData(is(instanceOf(UploadItem.class)));

        onView(withId(R.id.list))
                .perform(
                        RecyclerViewActions.actionOnItemAtPosition(0, longClick())
                );
    }
}

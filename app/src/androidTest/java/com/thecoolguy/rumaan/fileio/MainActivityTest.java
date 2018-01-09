package com.thecoolguy.rumaan.fileio;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;

import com.thecoolguy.rumaan.fileio.ui.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


@RunWith(AndroidJUnit4.class)
@MediumTest
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> activityTestRule = new ActivityTestRule<>(MainActivity.class);


    String noNetworkDialogFragment;

    @Before
    public void setNoNetworkDialogTagString() {
        noNetworkDialogFragment = activityTestRule.getActivity().getString(R.string.no_net_dialog_fragment_tag);

    }

    @Test
    public void useAppContext() throws Exception {
        Context context = InstrumentationRegistry.getTargetContext();
        assertEquals("com.thecoolguy.rumaan.fileio", context.getPackageName());
    }


    @Test
    public void check_handleMenus() {

    }

    @Test
    public void check_ClickOpensUploadHistory() {
        // Click overflow button
        onView(withId(R.id.menu))
                .perform(click());

        // Click upload history option
        onView(withText("Upload History"))
                .perform(click());
    }

    @Test
    public void check_ClickOpensAbout() {
        onView(withId(R.id.menu))
                .perform(click());
        onView(withText("About"))
                .perform(click());
    }


    @Test
    public void handle_NoNetwork() {
        // BEFORE: Turn off the network access

        onView(withId(R.id.btn_upload))
                .perform(click());

        Fragment noNetworkDialog = activityTestRule.getActivity().getSupportFragmentManager()
                .findFragmentByTag(noNetworkDialogFragment);

        // check whether its from DialogFragment
        assertTrue(noNetworkDialog instanceof DialogFragment);
        // check whether the dialog is showing
        assertTrue(((DialogFragment) noNetworkDialog).getShowsDialog());
    }
}

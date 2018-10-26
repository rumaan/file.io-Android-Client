package com.thecoolguy.rumaan.fileio

import androidx.test.InstrumentationRegistry
import androidx.test.filters.SmallTest
import androidx.test.rule.ActivityTestRule
import androidx.test.runner.AndroidJUnit4
import com.thecoolguy.rumaan.fileio.ui.activities.MainActivity
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented TEst, which will execute on an Android device.
 *
 * @see [Testing documentation](http://d.android.com/tools/testing)
 */
@RunWith(AndroidJUnit4::class)
@SmallTest
class ExampleInstrumentedTest {

    @Rule
    var activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Test
    fun useAppContext() {
        // Context of the app under TEst.
        val appContext = InstrumentationRegistry.getTargetContext()
        assertEquals("com.thecoolguy.rumaan.fileio", appContext.packageName)
    }
}

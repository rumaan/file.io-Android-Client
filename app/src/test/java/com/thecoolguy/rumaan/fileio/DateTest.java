package com.thecoolguy.rumaan.fileio;

import com.thecoolguy.rumaan.fileio.utils.DateUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Test the utility date functions.
 */

public class DateTest {

    /* Probably the worst test for date ever 🤣*/
    @Test
    public void test_CurrentDate() {
        // change this wtr to current day
        String actualDate = "28 January, 2018";

        String date = DateUtil.getTimeStamp();

        assertNotNull(date);
        assertEquals(actualDate, date);
    }
}

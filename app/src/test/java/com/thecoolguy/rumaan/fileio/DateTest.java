package com.thecoolguy.rumaan.fileio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.thecoolguy.rumaan.fileio.utils.Utils;
import org.junit.Test;

/**
 * Test the utility date functions.
 */

public class DateTest {

  /* Probably the worst test for date ever 🤣*/
  @Test
  public void test_CurrentDate() {
    // change this wtr to current day
    String actualDate = "04 April, 2018";

    String date = Utils.Date.getCurrentDate();

    assertNotNull(date);
    assertEquals(actualDate, date);
  }
}

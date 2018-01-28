package com.thecoolguy.rumaan.fileio.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Date functions helper class
 */

public class DateUtil {
    private static final String TIME_STAMP_FORMAT = "dd MMMM, yyyy";

    public static String getTimeStamp() {
        return new SimpleDateFormat(TIME_STAMP_FORMAT, Locale.US).format(new Date());
    }
}

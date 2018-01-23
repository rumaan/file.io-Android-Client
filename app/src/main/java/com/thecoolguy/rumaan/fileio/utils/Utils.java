package com.thecoolguy.rumaan.fileio.utils;

/**
 * Created by rumaankhalander on 23/01/18.
 */

public class Utils {
    /* Removes the '/d' from the URL */
    public static String parseEncryptUrl(String s) {
        int index = s.lastIndexOf(Consts.POSTFIX);
        return s.substring(0, index);
    }
}

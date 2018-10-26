package com.thecoolguy.rumaan.fileio;

import com.thecoolguy.rumaan.fileio.utils.ConstantsKt;
import com.thecoolguy.rumaan.fileio.utils.Utils.URLParser;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

public class UploadRepositoryTest {

    @Test
    public void check_ExpireUrl() {
        String url = "https://file.io/?expires=2";
        String days = "2";
        String withConstantsUrl = ConstantsKt.BASE_URL + ConstantsKt.QUERY
                + ConstantsKt.EXPIRE_PARAM + days;
        String testUrl = URLParser.INSTANCE.getExpireUrl(days);

        assertNotNull(testUrl);
        assertEquals(url, testUrl);
        assertEquals(url, withConstantsUrl);
    }
}
package com.thecoolguy.rumaan.fileio;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import com.thecoolguy.rumaan.fileio.utils.Constants;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import org.junit.Test;

public class UploadRepositoryTest {

  @Test
  public void check_ExpireUrl() {
    String url = "https://file.io/?expires=2";
    String days = "2";
    String withConstantsUrl = Constants.BASE_URL + Constants.QUERY + Constants.EXPIRE_PARAM + days;
    String testUrl = Utils.URLParser.getExpireUrl(days);

    assertNotNull(testUrl);
    assertEquals(url, testUrl);
    assertEquals(url, withConstantsUrl);
  }
}
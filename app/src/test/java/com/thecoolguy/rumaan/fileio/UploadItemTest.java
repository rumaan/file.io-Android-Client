package com.thecoolguy.rumaan.fileio;

import com.thecoolguy.rumaan.fileio.data.Upload;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by rumaankhalander on 09/01/18.
 */


public class UploadItemTest {
    private String testFileName = "testfilename";
    private String testUrl = "testUrl";

    @Test
    public void check_UploadItemSetValues() {
        UploadItem uploadItem = new UploadItem();

        // nullability of upload item object
        assertNotNull(uploadItem);

        assertNotNull(uploadItem.getId());


        // Test for file name
        uploadItem.setFileName(testFileName);
        assertNotNull(uploadItem.getFileName());
        assertEquals(testFileName, uploadItem.getFileName());


        // Test for url
        uploadItem.setUrl(testUrl);
        assertNotNull(uploadItem.getUrl());
        assertEquals(testUrl, uploadItem.getUrl());
    }
}

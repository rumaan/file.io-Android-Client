package com.thecoolguy.rumaan.fileio;

import com.thecoolguy.rumaan.fileio.data.models.FileEntity;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Created by rumaankhalander on 09/01/18.
 */


public class FileEntityTest {
    private String testFileName = "testfilename";
    private String testUrl = "testUrl";

    @Test
    public void check_UploadItemSetValues() {
        FileEntity fileEntity = new FileEntity();

        // nullability of upload item object
        assertNotNull(fileEntity);

        assertNotNull(fileEntity.getId());


        // Test for fileEntity name
        fileEntity.setName(testFileName);
        assertNotNull(fileEntity.getName());
        assertEquals(testFileName, fileEntity.getName());


        // Test for url
        fileEntity.setUrl(testUrl);
        assertNotNull(fileEntity.getUrl());
        assertEquals(testUrl, fileEntity.getUrl());
    }
}

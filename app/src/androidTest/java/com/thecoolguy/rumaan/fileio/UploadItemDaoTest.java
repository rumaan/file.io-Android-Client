package com.thecoolguy.rumaan.fileio;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

/**
 * Test for RoomDatabase
 * Created by rumaankhalander on 07/01/18.
 */

@RunWith(AndroidJUnit4.class)
public class UploadItemDaoTest {

    UploadHistoryRoomDatabase database;

    @Before
    public void initializeDb() {
        database = Room.inMemoryDatabaseBuilder(
                InstrumentationRegistry.getContext(),
                UploadHistoryRoomDatabase.class)
                .build();
    }

    @After
    public void closeDb() {
        database.close();
    }

    @Test
    public void check_InsertUploadItemSaves() {
        UploadItem uploadItem = new UploadItem("test", "test");

        // check for nulls in the upload item object
        assertNotNull(uploadItem);

        assertEquals("test", uploadItem.getFileName());
        assertEquals("test", uploadItem.getUrl());


        // check for nulls in database object
        assertNotNull(database);

        long id = database.uploadItemDao().insert(uploadItem);

        assertNotNull(id);

        // retrieve from the DB
        assertNotNull(database.uploadItemDao().getItem(id));
    }

}

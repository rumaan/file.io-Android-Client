package com.thecoolguy.rumaan.fileio;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.models.FileEntity;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for RoomDatabase
 * Created by rumaankhalander on 07/01/18.
 */

@RunWith(AndroidJUnit4.class)
public class FileEntityDaoTest {

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
    FileEntity fileEntity = new FileEntity("test", "test", Utils.Date.getCurrentDate(), 2);

    // check for nulls in the upload item object
    assertNotNull(fileEntity);

    assertEquals("test", fileEntity.getName());
    assertEquals("test", fileEntity.getUrl());

    // check for nulls in database object
    assertNotNull(database);

    long id = database.uploadItemDao().insert(fileEntity);

    // retrieve from the DB
    assertNotNull(database.uploadItemDao().getItem(id));
  }

}

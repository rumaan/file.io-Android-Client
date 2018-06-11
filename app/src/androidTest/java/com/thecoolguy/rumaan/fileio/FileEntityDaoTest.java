package com.thecoolguy.rumaan.fileio;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.models.FileEntity;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for RoomDatabase
 * Created by rumaankhalander on 07/01/18.
 */

@RunWith(AndroidJUnit4.class)
@LargeTest
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
  public void check_CountTheRows() {
    int count = database.uploadItemDao().numberOfItems();

    /* Initially the number of rows in database is zero */
    assertEquals(0, count);

    /* Create an empty file entity */
    FileEntity fileEntity = new FileEntity(
        "testItem", "testUrl", Utils.Date.getCurrentDate(), 1);
    /* Insert an item into the database */
    database.uploadItemDao().insert(fileEntity);

    /* Get the total number of rows after insertion */
    count = database.uploadItemDao().numberOfItems();

    assertEquals(1, count);
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

    FileEntity fileEntity1 = fileEntity;
    fileEntity1.setName("test1");

    database.uploadItemDao().insert(fileEntity1);

    long id = database.uploadItemDao().insert(fileEntity);

    // retrieve from the DB
    assertNotNull(database.uploadItemDao().getItem(id));
  }

  @Test
  public void check_saveMultipleItemsRetrieves() {
    List<FileEntity> fileEntities = new ArrayList<>();
    FileEntity fileEntity = new FileEntity("test", "test", Utils.Date.getCurrentDate(), 2);

    int items = 10;

    for (int i = 0; i < items; i++) {
      fileEntity.setName(fileEntity.getName() + i);
      fileEntities.add(fileEntity);
    }

    fileEntities.forEach(new Consumer<FileEntity>() {
      @Override
      public void accept(FileEntity fileEntity) {
        database.uploadItemDao().insert(fileEntity);
      }
    });

    assertEquals(items, database.uploadItemDao().numberOfItems());

    List<FileEntity> list = database.uploadItemDao().getAllUploads().getValue();
    assertNotNull(list);
    assertEquals(fileEntities, list);
  }

}

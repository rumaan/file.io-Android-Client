package com.thecoolguy.rumaan.fileio;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

import android.arch.persistence.room.Room;
import android.support.test.InstrumentationRegistry;
import android.support.test.filters.MediumTest;
import android.support.test.runner.AndroidJUnit4;
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.db.UploadItemDao;
import com.thecoolguy.rumaan.fileio.data.models.FileEntity;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.subscribers.DisposableSubscriber;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Test for RoomDatabase
 * Created by rumaankhalander on 07/01/18.
 */

@RunWith(AndroidJUnit4.class)
@MediumTest
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
    int count = database.uploadItemDao().getTotalRows();

    /* Initially the number of rows in database is zero */
    assertEquals(0, count);


    /* Create an empty file entity */
    FileEntity fileEntity = new FileEntity(
        "testItem", "testUrl", Utils.Date.getCurrentDate(), 1);
    /* Insert an item into the database */
    database.uploadItemDao().insert(fileEntity);

    /* Get the total number of rows after insertion */
    count = database.uploadItemDao().getTotalRows();

    assertEquals(1, count);
  }

  @Test
  public void check_retrieveAllItems() {
    UploadItemDao dao = database.uploadItemDao();

    // create dummy list items
    final List<FileEntity> list = new ArrayList<>();
    for (int i = 0; i < 3; i++) {
      list.add(
          new FileEntity("test name " + i, "test url " + i, Utils.Date.getCurrentDate(), i + 1));
    }

    assertEquals(3, list.size());

    for (FileEntity entity : list) {
      dao.insert(entity);
    }

    final Flowable<List<FileEntity>> allUploads = database.uploadItemDao().getAllUploads();

    // test by blocking
    allUploads
        .subscribeOn(AndroidSchedulers.mainThread())
        .subscribe(new DisposableSubscriber<List<FileEntity>>() {
          @Override
          public void onNext(List<FileEntity> fileEntities) {
            assertNotNull(fileEntities);
            for (FileEntity e : fileEntities) {
              assertTrue(list.contains(e));
            }
          }

          @Override
          public void onError(Throwable t) {
          }

          @Override
          public void onComplete() {

          }
        });

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

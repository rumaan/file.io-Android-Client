package com.thecoolguy.rumaan.fileio.data.repository;

import android.app.Application;
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.db.UploadItemDao;
import com.thecoolguy.rumaan.fileio.utils.Utils;

/**
 * Handles the data part of the application
 * - network
 * - database
 */
public final class Repository {

  private static final String TAG = "Repository";
  private final UploadItemDao mUploadItemDao;

  public Repository(Application application) {
    UploadHistoryRoomDatabase uploadHistoryRoomDatabase = UploadHistoryRoomDatabase
        .getInstance(application);

    mUploadItemDao = uploadHistoryRoomDatabase.uploadItemDao();
  }


  /* Create the upload service and start the upload */
  public static void initiateUpload() {

  }

}

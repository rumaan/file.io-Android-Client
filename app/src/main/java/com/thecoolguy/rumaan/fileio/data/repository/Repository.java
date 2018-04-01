package com.thecoolguy.rumaan.fileio.data.repository;

import android.app.Application;
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.db.UploadItemDao;
import com.thecoolguy.rumaan.fileio.data.models.LocalFile;
import org.jetbrains.annotations.NotNull;

/**
 * Handles the data part of the application
 * - network
 * - database
 */
public final class Repository {

  private static final String TAG = "Repository";
  private final UploadItemDao mUploadItemDao;

  private UploadHistoryRoomDatabase uploadHistoryRoomDatabase;

  public Repository(Application application) {
    uploadHistoryRoomDatabase = UploadHistoryRoomDatabase
        .getInstance(application);

    mUploadItemDao = uploadHistoryRoomDatabase.uploadItemDao();
  }


  /* Create the upload service and start the upload */
  public static void initiateUpload(@NotNull final LocalFile localFile) {
    Uploader.INSTANCE.uploadFile(localFile);
  }

}

package com.thecoolguy.rumaan.fileio.data.repository;

import android.app.Application;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.db.UploadItemDao;
import com.thecoolguy.rumaan.fileio.utils.Utils;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

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
  public static void initiateUpload(final Uri fileUri, final Context context) {
    FileInputStream fileInputStream = Utils.getFileInputStream(fileUri, context);
    if (fileInputStream != null) {
      Uploader.INSTANCE.uploadFile(context, fileUri, fileInputStream);
    } else {
      Log.e(TAG, "initiateUpload: Error Getting the file!", new FileNotFoundException());
    }
  }

}

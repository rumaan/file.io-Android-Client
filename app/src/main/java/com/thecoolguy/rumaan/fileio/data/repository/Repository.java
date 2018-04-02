package com.thecoolguy.rumaan.fileio.data.repository;

import android.app.Application;
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.db.UploadItemDao;
import com.thecoolguy.rumaan.fileio.data.models.FileEntity;
import com.thecoolguy.rumaan.fileio.data.models.LocalFile;
import com.thecoolguy.rumaan.fileio.ui.FileUploadListener;
import org.jetbrains.annotations.NotNull;

/**
 * Handles the data part of the application
 * - network
 * - database
 */
public final class Repository implements FileUploadListener {

  private static final String TAG = "Repository";

  private static Repository sInstance;

  private final UploadItemDao mUploadItemDao;
  private final UploadHistoryRoomDatabase uploadHistoryRoomDatabase;

  private Repository(
      Application application) {
    uploadHistoryRoomDatabase = UploadHistoryRoomDatabase.getInstance(application);
    mUploadItemDao = uploadHistoryRoomDatabase.uploadItemDao();
  }

  public static Repository getInstance() {
    if (sInstance == null) {
      // How the hell did that happen?
      throw new NullPointerException();
    }
    return sInstance;
  }

  public static void setInstance(Application application) {
    if (sInstance == null) {
      sInstance = new Repository(application);
    }
  }

  public void upload(LocalFile localFile) {
    Uploader.INSTANCE.uploadFile(localFile, this);
  }

  private void save(FileEntity fileEntity) {
    // TODO: save to database
  }

  @Override
  public void onFileUpload(@NotNull FileEntity fileEntity) {
    save(fileEntity);
  }

  @Override
  public void onFileUploadError(@NotNull Exception exception) {

  }
}

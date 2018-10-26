package com.thecoolguy.rumaan.fileio.data.db;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import android.content.Context;
import com.thecoolguy.rumaan.fileio.data.models.FileEntity;

@Database(entities = { FileEntity.class }, version = 1)
public abstract class UploadHistoryRoomDatabase extends RoomDatabase {

  private static UploadHistoryRoomDatabase sINSTANCE;

  public static UploadHistoryRoomDatabase getInstance(Context context) {
    if (sINSTANCE == null) {
      synchronized (UploadHistoryRoomDatabase.class) {
        if (sINSTANCE == null) {
          sINSTANCE = Room.databaseBuilder(context.getApplicationContext(),
              UploadHistoryRoomDatabase.class,
              "upload_history")
              .build();
        }
      }
    }
    return sINSTANCE;
  }

  public abstract UploadItemDao uploadItemDao();
}

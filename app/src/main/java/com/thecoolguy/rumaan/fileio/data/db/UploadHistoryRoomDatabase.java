package com.thecoolguy.rumaan.fileio.data.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.thecoolguy.rumaan.fileio.data.models.FileEntity;


@Database(entities = {FileEntity.class}, version = 1)
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

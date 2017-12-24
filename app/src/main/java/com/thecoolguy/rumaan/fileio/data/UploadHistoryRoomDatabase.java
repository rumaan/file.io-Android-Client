package com.thecoolguy.rumaan.fileio.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;


@Database(entities = {UploadItem.class}, version = 1)
public abstract class UploadHistoryRoomDatabase extends RoomDatabase {
    private static UploadHistoryRoomDatabase INSTANCE;

    public static UploadHistoryRoomDatabase getInstance(Context context) {
        if (INSTANCE == null) {
            synchronized (UploadHistoryRoomDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            UploadHistoryRoomDatabase.class,
                            "upload_history")
                            .build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract UploadItemDao uploadItemDao();
}

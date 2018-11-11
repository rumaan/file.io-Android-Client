package com.thecoolguy.rumaan.fileio.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.thecoolguy.rumaan.fileio.data.models.FileEntity

@Database(entities = [FileEntity::class], version = 1)
abstract class UploadHistoryRoomDatabase : RoomDatabase() {

    abstract fun uploadItemDao(): UploadItemDao

    companion object {
        private lateinit var INSTANCE: UploadHistoryRoomDatabase

        fun getInstance(context: Context): UploadHistoryRoomDatabase {
            synchronized(UploadHistoryRoomDatabase::class.java) {
                INSTANCE = Room.databaseBuilder(context.applicationContext,
                        UploadHistoryRoomDatabase::class.java,
                        "upload_history")
                        .allowMainThreadQueries()
                        .build()
            }
            return INSTANCE
        }
    }
}

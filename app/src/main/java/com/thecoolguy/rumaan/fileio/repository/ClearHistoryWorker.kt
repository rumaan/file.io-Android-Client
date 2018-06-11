package com.thecoolguy.rumaan.fileio.repository

import androidx.work.Worker
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase

class ClearHistoryWorker : Worker() {

    override fun doWork(): WorkerResult {
        UploadHistoryRoomDatabase.getInstance(applicationContext)
                .uploadItemDao()
                .clearAll()

        return WorkerResult.SUCCESS
    }
}
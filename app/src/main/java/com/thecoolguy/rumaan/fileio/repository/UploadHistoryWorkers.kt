package com.thecoolguy.rumaan.fileio.repository

import androidx.work.Worker
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase

const val DELETE_TAG = "delete_single_item"
const val ID = "id"

class ClearHistoryWorker : Worker() {

    override fun doWork(): WorkerResult {
        UploadHistoryRoomDatabase.getInstance(applicationContext)
                .uploadItemDao()
                .clearAll()

        return WorkerResult.SUCCESS
    }
}

class DeleteSingleItemWoker : Worker() {
    override fun doWork(): WorkerResult {
        val id = inputData.getLong(ID, -1)
        if (id == -1L) return WorkerResult.FAILURE

        UploadHistoryRoomDatabase.getInstance(applicationContext)
                .uploadItemDao()
                .deleteItemWithId(id)
        return WorkerResult.SUCCESS
    }
}
package com.thecoolguy.rumaan.fileio.repository

import androidx.work.Worker
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase

const val DELETE_TAG = "delete_single_item"
const val ID = "id"

class ClearHistoryWorker : Worker() {

    override fun doWork(): Result {
        UploadHistoryRoomDatabase.getInstance(applicationContext)
                .uploadItemDao()
                .clearAll()

        return Result.SUCCESS
    }
}

class DeleteSingleItemWorker : Worker() {
    override fun doWork(): Result {
        val id = inputData.getLong(ID, -1)
        if (id == -1L) return Result.FAILURE

        UploadHistoryRoomDatabase.getInstance(applicationContext)
                .uploadItemDao()
                .deleteItemWithId(id)
        return Result.SUCCESS
    }
}
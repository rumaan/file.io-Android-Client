package com.thecoolguy.rumaan.fileio.data.db

import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import kotlinx.coroutines.experimental.runBlocking
import org.jetbrains.anko.coroutines.experimental.bg

object DatabaseHelper {

    fun saveToDatabase(fileEntity: FileEntity, mUploadItemDao: UploadItemDao) {
        runBlocking {
            val thread = bg {
                mUploadItemDao.insert(fileEntity)
            }
        }
    }

    fun getAllItems(mUploadItemDao: UploadItemDao): MutableList<FileEntity>? {
        return runBlocking {
            bg {
                mUploadItemDao.allUploads.value
            }.await()
        }
    }

}
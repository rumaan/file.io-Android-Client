package com.thecoolguy.rumaan.fileio.data.db

import com.thecoolguy.rumaan.fileio.data.models.FileEntity

object DatabaseHelper {

    private val TAG = DatabaseHelper.javaClass.simpleName

    fun saveToDatabase(fileEntity: FileEntity, mUploadItemDao: UploadItemDao) =
            mUploadItemDao.insert(fileEntity)

}
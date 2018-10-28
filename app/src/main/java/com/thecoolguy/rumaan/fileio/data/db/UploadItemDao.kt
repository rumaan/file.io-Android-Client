package com.thecoolguy.rumaan.fileio.data.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.thecoolguy.rumaan.fileio.data.models.FileEntity

@Dao
interface UploadItemDao {
    @get:Query("SELECT * FROM " + DatabaseContract.TABLE_NAME)
    val allUploads: LiveData<List<FileEntity>>

    @Insert
    fun insert(fileEntity: FileEntity): Long

    @Query("SELECT DISTINCT * FROM " + DatabaseContract.TABLE_NAME + " where id = :id")
    fun getItem(id: Long): FileEntity

    @Query("DELETE FROM ${DatabaseContract.TABLE_NAME}" + " WHERE id = :id")
    fun deleteItemWithId(id: Long)

    @Delete
    fun delete(vararg fileEntity: FileEntity)

    @Query("DELETE FROM " + DatabaseContract.TABLE_NAME)
    fun clearAll()

    @Query("SELECT DISTINCT count(*) FROM " + DatabaseContract.TABLE_NAME)
    fun numberOfItems(): Int

}

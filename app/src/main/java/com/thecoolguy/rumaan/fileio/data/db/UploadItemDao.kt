package com.thecoolguy.rumaan.fileio.data.db

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.Query
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

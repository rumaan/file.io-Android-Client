package com.thecoolguy.rumaan.fileio.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.thecoolguy.rumaan.fileio.data.models.UploadItem;

import java.util.List;

/**
 * Created by rumaankhalander on 18/12/17.
 */
@Dao
public interface UploadItemDao {

    @Insert
    long insert(UploadItem uploadItem);

    @Query("SELECT DISTINCT * FROM upload_history where id = :id")
    UploadItem getItem(long id);

    @Delete
    void delete(UploadItem... uploadItem);

    @Query("DELETE FROM upload_history")
    void deleteAll();

    @Query("SELECT * FROM upload_history")
    LiveData<List<UploadItem>> getAllUploads();
}

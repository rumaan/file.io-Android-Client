package com.thecoolguy.rumaan.fileio.data.db;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.thecoolguy.rumaan.fileio.data.models.FileEntity;

import java.util.List;

@Dao
public interface UploadItemDao {

    @Insert
    long insert(FileEntity fileEntity);

    @Query("SELECT DISTINCT * FROM File where id = :id")
    FileEntity getItem(long id);

    @Delete
    void delete(FileEntity... fileEntity);

    @Query("DELETE FROM File")
    void deleteAll();

    @Query("SELECT * FROM File")
    LiveData<List<FileEntity>> getAllUploads();
}

package com.thecoolguy.rumaan.fileio.data.db;

import static com.thecoolguy.rumaan.fileio.data.db.DatabaseContract.TABLE_NAME;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import com.thecoolguy.rumaan.fileio.data.models.FileEntity;
import io.reactivex.Flowable;
import io.reactivex.Single;
import java.util.List;

@Dao
public interface UploadItemDao {

  @Insert
  long insert(FileEntity fileEntity);

  @Query("SELECT DISTINCT * FROM " + TABLE_NAME + " where id = :id")
  Single<FileEntity> getItem(long id);

  @Delete
  void delete(FileEntity... fileEntity);

  @Query("DELETE FROM " + TABLE_NAME)
  void deleteAll();

  @Query("SELECT DISTINCT count(*) FROM " + TABLE_NAME)
  int getTotalRows();

  @Query("SELECT * FROM " + TABLE_NAME)
  Flowable<List<FileEntity>> getAllUploads();
}

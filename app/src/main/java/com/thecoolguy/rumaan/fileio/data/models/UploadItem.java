package com.thecoolguy.rumaan.fileio.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.thecoolguy.rumaan.fileio.data.db.DatabaseContract;

@Entity(tableName = DatabaseContract.TABLE_NAME)
public class UploadItem {

  @ColumnInfo(name = DatabaseContract.COLUMN_DATE_UPLOAD)
  private String date;
  @PrimaryKey(autoGenerate = true)
  @NonNull
  private int id;
  @NonNull
  @ColumnInfo(name = DatabaseContract.COLUMN_DATE_FILE_NAME)
  private String fileName;
  @NonNull
  @ColumnInfo(name = DatabaseContract.COLUMN_UPLOAD_URL)
  private String url;
  /* Default Days the link will expire is 14 Days */
  @ColumnInfo(name = DatabaseContract.COLUMN_DAYS_TO_EXPIRE)
  private int daysToExpire = 14;

  @Ignore
  public UploadItem() {
  }

  public UploadItem(@NonNull String fileName, @NonNull String url, String date, int daysToExpire) {
    this.fileName = fileName;
    this.url = url;
    this.daysToExpire = daysToExpire;
    this.date = date;
  }

  public String getDate() {
    return date;
  }

  public void setDate(String date) {
    this.date = date;
  }

  public int getDaysToExpire() {
    return daysToExpire;
  }

  public void setDaysToExpire(int daysToExpire) {
    this.daysToExpire = daysToExpire;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  @NonNull
  public String getFileName() {
    return fileName;
  }

  public void setFileName(@NonNull String fileName) {
    this.fileName = fileName;
  }

  @NonNull
  public String getUrl() {
    return url;
  }

  public void setUrl(@NonNull String url) {
    this.url = url;
  }
}

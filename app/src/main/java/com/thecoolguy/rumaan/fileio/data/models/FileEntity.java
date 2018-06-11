package com.thecoolguy.rumaan.fileio.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import com.thecoolguy.rumaan.fileio.data.db.DatabaseContract;

/**
 * File Entity object which will be saved in the DB.
 * This FileEntity object will have the upload url from the server as well as all the attributes
 * of the local file.
 *
 *
 * Use this after the File Upload is successful and link is obtained.
 */
@Entity(tableName = DatabaseContract.TABLE_NAME)
public final class FileEntity {

  @ColumnInfo(name = DatabaseContract.COLUMN_DATE_UPLOAD)
  private String date;

  @PrimaryKey(autoGenerate = true)
  private long id;

  @ColumnInfo(name = DatabaseContract.COLUMN_DATE_FILE_NAME)
  private String name;

  @ColumnInfo(name = DatabaseContract.COLUMN_UPLOAD_URL)
  private String url;

  /* Default Days the link will expire is 14 Days */
  @ColumnInfo(name = DatabaseContract.COLUMN_DAYS_TO_EXPIRE)
  private int daysToExpire = 14;

  @Ignore
  public FileEntity() {
  }

  public FileEntity(@NonNull String name,
      @NonNull String url, String date, int daysToExpire) {
    this.name = name;
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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  @NonNull
  public String getName() {
    return name;
  }

  public void setName(@NonNull String name) {
    this.name = name;
  }

  @NonNull
  public String getUrl() {
    return url;
  }

  public void setUrl(@NonNull String url) {
    this.url = url;
  }

  @Override
  public String toString() {
    return "FileEntity{" +
        "date='" + date + '\'' +
        ", id=" + id +
        ", name='" + name + '\'' +
        ", url='" + url + '\'' +
        ", daysToExpire=" + daysToExpire +
        '}';
  }

}

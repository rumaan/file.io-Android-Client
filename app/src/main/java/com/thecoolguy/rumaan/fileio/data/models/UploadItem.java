package com.thecoolguy.rumaan.fileio.data.models;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "upload_history")
public class UploadItem {
    @PrimaryKey(autoGenerate = true)
    @NonNull
    private int id;
    @NonNull
    @ColumnInfo(name = "file_name")
    private String fileName;
    @NonNull
    @ColumnInfo(name = "url")
    private String url;

    @Ignore
    public UploadItem() {
    }

    public UploadItem(@NonNull String fileName, @NonNull String url) {
        this.fileName = fileName;
        this.url = url;
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

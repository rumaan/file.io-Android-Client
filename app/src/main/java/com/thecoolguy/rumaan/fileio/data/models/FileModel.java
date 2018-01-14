package com.thecoolguy.rumaan.fileio.data.models;

import java.io.File;

/**
 * Created by rumaankhalander on 14/01/18.
 */

public class FileModel {
    private File file;
    private String daysToExpire;

    public FileModel() {
    }

    public FileModel(File file, String daysToExpire) {
        this.file = file;
        this.daysToExpire = daysToExpire;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }

    public String getDaysToExpire() {
        return daysToExpire;
    }

    public void setDaysToExpire(String daysToExpire) {
        this.daysToExpire = daysToExpire;
    }
}

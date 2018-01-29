package com.thecoolguy.rumaan.fileio.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.thecoolguy.rumaan.fileio.data.models.FileModel;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;

import java.io.File;
import java.util.List;

/**
 * Abstraction for Repository with Lifecycle aware stuffs
 */

public class UploadItemViewModel extends AndroidViewModel {
    private UploadRepository mRepository;
    private LiveData<List<UploadItem>> mUploadHistory;
    private FileModel fileModel;

    public UploadItemViewModel(Application application) {
        super(application);
        mRepository = new UploadRepository(application);
        mUploadHistory = mRepository.getUploadHistoryList();
    }

    public void deleteAll() {
        mRepository.deleteAll();
    }

    public FileModel getFileModel() {
        return fileModel;
    }

    public void setFileModel(FileModel fileModel) {
        this.fileModel = fileModel;
    }

    public void uploadFile(Upload callback) {
        mRepository.uploadFile(fileModel, callback);
    }

    public LiveData<List<UploadItem>> getUploadHistoryList() {
        return mUploadHistory;
    }

    public void insert(UploadItem uploadItem) {
        mRepository.insert(uploadItem);
    }

    public void delete(UploadItem uploadItem, Upload callback) {
        mRepository.delete(uploadItem, callback);
    }

}

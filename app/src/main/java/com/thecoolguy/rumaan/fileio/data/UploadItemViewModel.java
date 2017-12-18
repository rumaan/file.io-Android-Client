package com.thecoolguy.rumaan.fileio.data;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import java.util.List;

/**
 * Created by rumaankhalander on 18/12/17.
 */

public class UploadItemViewModel extends AndroidViewModel {
    private UploadRepository mRepository;
    private LiveData<List<UploadItem>> mUploadHistory;

    public UploadItemViewModel(Application application) {
        super(application);
        mRepository = new UploadRepository(application);
        mUploadHistory = mRepository.getUploadHistoryList();
    }

    public LiveData<List<UploadItem>> getUploadHistoryList() {
        return mUploadHistory;
    }

    public void insert(UploadItem uploadItem) {
        mRepository.insert(uploadItem);
    }
}

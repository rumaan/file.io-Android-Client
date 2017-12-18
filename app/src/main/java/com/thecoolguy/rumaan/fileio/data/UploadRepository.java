package com.thecoolguy.rumaan.fileio.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

/**
 * Created by rumaankhalander on 18/12/17.
 */

public class UploadRepository {
    private UploadItemDao mUploadDao;
    private LiveData<List<UploadItem>> mUploadHistoryList;

    public UploadRepository(Application application) {
        UploadHistoryRoomDatabase uploadHistoryRoomDatabase = UploadHistoryRoomDatabase.getInstance(application);
        mUploadDao = uploadHistoryRoomDatabase.uploadItemDao();
        mUploadHistoryList = mUploadDao.getAllUploads();
    }

    LiveData<List<UploadItem>> getUploadHistoryList() {
        return mUploadHistoryList;
    }

    public void insert(UploadItem uploadItem) {
        new insertAsyncUploadItem(mUploadDao).execute(uploadItem);
    }

    private static class insertAsyncUploadItem extends AsyncTask<UploadItem, Void, Void> {
        private UploadItemDao mUploadDao;

        insertAsyncUploadItem(UploadItemDao uploadItemDao) {
            mUploadDao = uploadItemDao;
        }

        @Override
        protected Void doInBackground(UploadItem... uploadItems) {
            mUploadDao.insert(uploadItems[0]);
            return null;
        }


    }
}

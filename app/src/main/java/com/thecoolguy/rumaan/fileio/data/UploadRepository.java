package com.thecoolguy.rumaan.fileio.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.db.UploadItemDao;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;

import java.io.File;
import java.net.URL;
import java.util.List;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;

/**
 * Handles the data part of the application
 * - network
 * - database
 */

public class UploadRepository {
    private static final String TAG = "UploadRepository";
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

    void deleteAllItems() {
        // Delete Items Async
        new deleteAllAsyncUploadItems(mUploadDao).execute();
    }

    public void insert(UploadItem uploadItem) {
        new insertAsyncUploadItem(mUploadDao).execute(uploadItem);
    }

    public void uploadFile(final File file) {
        Fuel.upload("https://file.io/")
                .source(new Function2<Request, URL, File>() {
                    @Override
                    public File invoke(Request request, URL url) {
                        return file;
                    }
                })
                .name(new Function0<String>() {
                    @Override
                    public String invoke() {
                        return "file";
                    }
                })
                .responseString(new Handler<String>() {
                    @Override
                    public void success(Request request, Response response, String s) {
                        Log.d(TAG, "success: " + response.getResponseMessage() + "\n" + s);
                    }

                    @Override
                    public void failure(Request request, Response response, FuelError fuelError) {
                        Log.e(TAG, "failure: " + fuelError.getMessage(), fuelError.getException());
                    }
                });
    }

    private static class deleteAllAsyncUploadItems extends AsyncTask<Void, Void, Void> {

        private UploadItemDao mItemDao;

        public deleteAllAsyncUploadItems(UploadItemDao mItemDao) {
            this.mItemDao = mItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mItemDao.deleteAll();
            return null;
        }
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

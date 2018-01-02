package com.thecoolguy.rumaan.fileio.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.db.UploadItemDao;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.List;

import kotlin.Unit;
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

    public void uploadFile(final File file, final Upload resultCallback) {
        final UploadItem uploadItem = new UploadItem();
        uploadItem.setFileName(file.getName());

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
                .progress(new Function2<Long, Long, Unit>() {
                    @Override
                    public Unit invoke(Long bytesUploaded, Long totalBytes) {
                        int p = (int) (((float) bytesUploaded / totalBytes) * 100);
                        resultCallback.progress(p);
                        return null;
                    }
                })
                .responseString(new Handler<String>() {
                    @Override
                    public void success(Request request, Response response, String s) {
                        String url = deserializeJSON(s);
                        if (url != null) {
                            uploadItem.setUrl(url);
                            insert(uploadItem);
                            resultCallback.onUpload(url);
                        } else {
                            failure(request, response, new FuelError(new NullPointerException("URL formed from JSON was null."), null, response));
                        }
                    }

                    @Override
                    public void failure(Request request, Response response, FuelError fuelError) {
                        Crashlytics.logException(fuelError);
                        Log.e(TAG, "failure: " + fuelError.getMessage(), fuelError.getException());
                        resultCallback.onError(fuelError);
                    }
                });
    }

    private String deserializeJSON(String json) {
        try {
            JSONObject jsonObject = new JSONObject(json);
            return jsonObject.getString("link");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
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

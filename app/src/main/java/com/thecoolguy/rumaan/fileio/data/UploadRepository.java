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
import com.thecoolguy.rumaan.fileio.data.models.FileModel;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URL;
import java.util.List;

import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function2;

/**
 * Handles the data part of the application
 * - network
 * - database
 */
class UploadRepository {

    private static final String TAG = "UploadRepository";
    private UploadItemDao mUploadItemDao;
    private LiveData<List<UploadItem>> mUploadHistoryList;


    UploadRepository(Application application) {
        UploadHistoryRoomDatabase uploadHistoryRoomDatabase = UploadHistoryRoomDatabase.getInstance(application);
        mUploadItemDao = uploadHistoryRoomDatabase.uploadItemDao();
        mUploadHistoryList = mUploadItemDao.getAllUploads();
    }

    LiveData<List<UploadItem>> getUploadHistoryList() {
        return mUploadHistoryList;
    }


    void deleteAllItems() {
        // Delete Items Async
        new deleteAllAsyncUploadItems(mUploadItemDao).execute();
    }


    void insert(UploadItem uploadItem) {
        new insertAsyncUploadItems(mUploadItemDao).execute(uploadItem);
    }

    void uploadFile(final FileModel fileModel, final Upload resultCallback) {
        final File file = fileModel.getFile();

        // create an upload item from the file model
        final UploadItem uploadItem = new UploadItem();
        uploadItem.setFileName(file.getName());

        // set up query parameters
        // NOTE: here getDaysToExpire() actually returns the number of weeks selected by the user.
        String link = "https://file.io/?expires=" + fileModel.getDaysToExpire();

        Log.i(TAG, "Request Link: " + link);

        //TODO: change the file object to InputStream
        Fuel.upload(link)
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
                    public void success(Request request, Response response, String res) {
                        // Parse the JSON from the response.
                        Pair<String, Integer> parsedResults = getParsedResults(res);
                        if (parsedResults != null) {
                            // Set the URL
                            uploadItem.setUrl(parsedResults.getFirst());
                            // Set the Days after which the link will expire
                            uploadItem.setDaysToExpire(parsedResults.getSecond());
                            // Insert the Object intro the Database
                            insert(uploadItem);
                            resultCallback.onUpload(parsedResults.getFirst());
                        } else {
                            failure(request, response, new FuelError(new NullPointerException("Data formed from JSON maybe was null."), null, response));
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

    private Pair<String, Integer> getParsedResults(String response) {
        if (response != null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                String link = jsonObject.getString("link");
                String expiry = jsonObject.getString("expiry");
                Integer days = getDays(expiry);
                return new Pair<>(link, days);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private Integer getDays(String expiry) {
        // FIXME: look out for months and years, if you seek to implement them in the future.
        // Split on spaces
        return Integer.parseInt(expiry.split(" ")[0]);
    }



    void delete(UploadItem uploadItem, Upload callback) {
        new deleteAsyncUploadItem(mUploadItemDao, callback).execute(uploadItem);
    }

    private static class deleteAsyncUploadItem extends AsyncTask<UploadItem, Void, Void> {
        private UploadItemDao uploadItemDao;
        private Upload callback;

        deleteAsyncUploadItem(UploadItemDao uploadItemDao, Upload callback) {
            this.uploadItemDao = uploadItemDao;
            this.callback = callback;
        }

        @Override
        protected Void doInBackground(UploadItem... uploadItems) {
            uploadItemDao.delete(uploadItems);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            callback.onDelete();
        }
    }


    private static class insertAsyncUploadItems extends AsyncTask<UploadItem, Void, Void> {
        private UploadItemDao mDao;

        insertAsyncUploadItems(UploadItemDao dao) {
            this.mDao = dao;
        }

        @Override
        protected Void doInBackground(UploadItem... uploadItems) {
            mDao.insert(uploadItems[0]);

            return null;
        }
    }

    private static class deleteAllAsyncUploadItems extends AsyncTask<Void, Void, Void> {

        private UploadItemDao mItemDao;

        deleteAllAsyncUploadItems(UploadItemDao mItemDao) {
            this.mItemDao = mItemDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            mItemDao.deleteAll();
            return null;
        }
    }

}

package com.thecoolguy.rumaan.fileio.data;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;
import android.util.Log;

import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase;
import com.thecoolguy.rumaan.fileio.data.db.UploadItemDao;
import com.thecoolguy.rumaan.fileio.data.models.UploadItem;
import com.thecoolguy.rumaan.fileio.data.network.UploadService;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by rumaankhalander on 18/12/17.
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

    public void uploadFile(File file) {

        // TODO: move this code somewhere else

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://file.io/")
                .build();

        MultipartBody.Part part = MultipartBody.Part.createFormData(
                "file",
                file.getName(),
                RequestBody.create(MediaType.parse("*/*"), file)
        );

        UploadService uploadService = retrofit.create(UploadService.class);
        Call<ResponseBody> call = uploadService.uploadFile(part);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.body());
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
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

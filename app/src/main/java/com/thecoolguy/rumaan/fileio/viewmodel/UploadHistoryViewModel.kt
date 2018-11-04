package com.thecoolguy.rumaan.fileio.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase
import com.thecoolguy.rumaan.fileio.data.models.FileEntity

class UploadHistoryViewModel(application: Application) : AndroidViewModel(application) {
    var uploadList: LiveData<List<FileEntity>> = MutableLiveData<List<FileEntity>>()

    init {
        uploadList = UploadHistoryRoomDatabase.getInstance(application)
                .uploadItemDao()
                .allUploads
    }
}
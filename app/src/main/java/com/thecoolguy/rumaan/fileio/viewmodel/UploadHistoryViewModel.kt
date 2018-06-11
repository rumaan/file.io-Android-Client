package com.thecoolguy.rumaan.fileio.viewmodel

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase
import com.thecoolguy.rumaan.fileio.data.models.FileEntity

class UploadHistoryViewModel(application: Application) : AndroidViewModel(application) {
    var uploadList: LiveData<List<FileEntity>> = MutableLiveData<List<FileEntity>>()

    init {
        uploadList = UploadHistoryRoomDatabase.getInstance(application).uploadItemDao().allUploads
    }
}
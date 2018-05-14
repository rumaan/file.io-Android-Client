package com.thecoolguy.rumaan.fileio.ui

import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.listeners.FileUploadProgressListener
import com.thecoolguy.rumaan.fileio.repository.Repository

class UploadService : Service(), FileUploadProgressListener {

    private val mBinder = LocalBinder()

    override fun uploadProgress(progress: Int) {

    }

    override fun onComplete(fileEntity: FileEntity) {
        NotificationHelper().create(applicationContext, fileEntity)
    }

    override fun onBind(intent: Intent): IBinder {
        Repository.getInstance().addUploadProgressListener(this)
        return mBinder
    }

    class LocalBinder : Binder() {
        fun getService() = this
    }


}
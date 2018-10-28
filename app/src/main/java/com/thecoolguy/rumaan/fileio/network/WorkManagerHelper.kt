package com.thecoolguy.rumaan.fileio.network

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import com.thecoolguy.rumaan.fileio.repository.UploadWorker

const val UPLOAD_WORK_TAG = "UploadWork"

fun createWorkRequest(uri: String): OneTimeWorkRequest {

    val data = Data.Builder()
            .putString(UploadWorker.KEY_URI, uri)
            .build()
    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    return OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .addTag(UPLOAD_WORK_TAG)
            .build()
}

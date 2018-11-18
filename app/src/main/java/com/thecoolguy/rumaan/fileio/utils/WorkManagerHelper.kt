package com.thecoolguy.rumaan.fileio.utils

import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.workDataOf
import com.thecoolguy.rumaan.fileio.repository.UploadWorker

const val UPLOAD_WORK_TAG = "UploadWork"

fun createUploadWork(uri: String): OneTimeWorkRequest {

    val data = workDataOf(UploadWorker.KEY_URI to uri)

    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    return OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .addTag(UPLOAD_WORK_TAG)
            .build()
}

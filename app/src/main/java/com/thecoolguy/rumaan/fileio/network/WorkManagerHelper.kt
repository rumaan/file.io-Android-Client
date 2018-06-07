package com.thecoolguy.rumaan.fileio.network

import androidx.work.*
import com.thecoolguy.rumaan.fileio.repository.UploadWorker


fun createWorkRequest(uri: String): OneTimeWorkRequest {
    val data = Data.Builder()
            .putString(UploadWorker.KEY_URI, uri)
            .build()
    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    return OneTimeWorkRequestBuilder<UploadWorker>()
            .apply {
                setConstraints(constraints)
                setInputData(data)
            }.build()
}

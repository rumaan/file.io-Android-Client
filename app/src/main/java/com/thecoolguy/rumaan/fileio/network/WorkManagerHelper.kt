package com.thecoolguy.rumaan.fileio.network

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import com.thecoolguy.rumaan.fileio.data.models.LocalFile
import com.thecoolguy.rumaan.fileio.repository.UploadWorker


fun createWorkRequest(localFile: LocalFile): OneTimeWorkRequest {
    val data = Data.Builder()
            .putString(UploadWorker.KEY_URI, localFile.uri.toString())
            .build()
    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    return OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(data)
            .build()
}

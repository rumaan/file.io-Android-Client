package com.thecoolguy.rumaan.fileio.network

// TODO: Update the view
// TODO: add upload the chosen file view
/*
if (isConnectedToNetwork(this))
{
    viewModel.uploadFile(this)
}
else
{
    // Schedule a Work to upload and post it as notification after completion

    // Pass in the file URI
    // FIXME: redundant calls for getLocalFile()

    val fileData = Data.Builder()
            .putString(UploadWorker.KEY_URI, localFile.uri.toString())
            .build()

    val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

    val oneTimeWorkRequest = OneTimeWorkRequest.Builder(UploadWorker::class.java)
            .setConstraints(constraints)
            .setInputData(fileData)
            .build()

    WorkManager.getInstance().enqueue(oneTimeWorkRequest)
}*/

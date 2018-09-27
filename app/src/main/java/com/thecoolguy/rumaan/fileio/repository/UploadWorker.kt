package com.thecoolguy.rumaan.fileio.repository

import android.net.Uri
import android.util.Log
import androidx.work.Data
import androidx.work.Worker
import androidx.work.toWorkData
import androidx.work.workDataOf
import com.thecoolguy.rumaan.fileio.data.db.DatabaseHelper
import com.thecoolguy.rumaan.fileio.data.db.UploadHistoryRoomDatabase
import com.thecoolguy.rumaan.fileio.data.models.FileEntity
import com.thecoolguy.rumaan.fileio.data.models.Response
import com.thecoolguy.rumaan.fileio.network.Uploader
import com.thecoolguy.rumaan.fileio.network.composeIntoFileEntity
import com.thecoolguy.rumaan.fileio.ui.NotificationHelper
import com.thecoolguy.rumaan.fileio.utils.Utils

class UploadWorker : Worker() {

    companion object {
        const val KEY_URI = "file_uri"
        const val KEY_RESULT = "file_url"
        private val TAG = UploadWorker::class.simpleName

    }

    private fun save(fileEntity: FileEntity) {
        val id = DatabaseHelper.saveToDatabase(
                fileEntity, UploadHistoryRoomDatabase.getInstance(applicationContext).uploadItemDao()
        )
        Log.d(TAG, "Insert Id: $id")
    }

    override fun doWork(): Result {
        val fileUri = inputData.getString(KEY_URI)
        fileUri?.let { uri ->
            // get the local file object from the backing storage
            Utils.getLocalFile(applicationContext, Uri.parse(uri))
                    ?.let { localFile ->
                        // Upload the file
                        val (_, response, _) = Uploader.upload(localFile)

                        val fileEntity =
                                composeIntoFileEntity(Response.Deserializer().deserialize(response), localFile)

                        /* Save the uploaded file details into the LocalDb */
                        save(fileEntity)

                        // post a notification
                        NotificationHelper().create(applicationContext, fileEntity)
                        val output: Data = workDataOf(KEY_RESULT to fileEntity.url)
                        outputData = output

                        return Result.SUCCESS
                    }
        }
        return Result.FAILURE
    }

}
package com.thecoolguy.rumaan.fileio.repository

import android.net.Uri
import androidx.work.Worker
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
        private val TAG = UploadWorker::class.simpleName
    }

    private fun save(fileEntity: FileEntity) {
        DatabaseHelper.saveToDatabase(fileEntity, UploadHistoryRoomDatabase.getInstance(applicationContext).uploadItemDao())
    }

    override fun doWork(): WorkerResult {
        val fileUri = inputData.getString(KEY_URI, null)
        fileUri?.let { it ->
            // get the local file object from the backing storage
            val localFile = Utils.getLocalFile(applicationContext, Uri.parse(it))

            // Upload the file
            val (_, response, _) = Uploader.upload(localFile)

            val fileEntity: FileEntity = composeIntoFileEntity(Response.Deserializer().deserialize(response), localFile)

            /* Save the uploaded file details into the LocalDb */
            save(fileEntity)

            // post a notification
            NotificationHelper().create(applicationContext, fileEntity)

            return WorkerResult.SUCCESS
        }
        return WorkerResult.FAILURE
    }

}